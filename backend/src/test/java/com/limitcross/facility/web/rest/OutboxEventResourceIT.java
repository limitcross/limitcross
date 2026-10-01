package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.OutboxEventAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.OutboxEvent;
import com.limitcross.facility.domain.enumeration.OutboxStatus;
import com.limitcross.facility.repository.OutboxEventRepository;
import com.limitcross.facility.service.dto.OutboxEventDTO;
import com.limitcross.facility.service.mapper.OutboxEventMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OutboxEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OutboxEventResourceIT {

    private static final String DEFAULT_AGGREGATE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_AGGREGATE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_AGGREGATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_AGGREGATE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final OutboxStatus DEFAULT_STATUS = OutboxStatus.NEW;
    private static final OutboxStatus UPDATED_STATUS = OutboxStatus.SENT;

    private static final Integer DEFAULT_ATTEMPTS = 0;
    private static final Integer UPDATED_ATTEMPTS = 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/outbox-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private OutboxEventMapper outboxEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutboxEventMockMvc;

    private OutboxEvent outboxEvent;

    private OutboxEvent insertedOutboxEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutboxEvent createEntity() {
        return new OutboxEvent()
            .aggregateType(DEFAULT_AGGREGATE_TYPE)
            .aggregateId(DEFAULT_AGGREGATE_ID)
            .eventType(DEFAULT_EVENT_TYPE)
            .payload(DEFAULT_PAYLOAD)
            .status(DEFAULT_STATUS)
            .attempts(DEFAULT_ATTEMPTS)
            .createdAt(DEFAULT_CREATED_AT)
            .processedAt(DEFAULT_PROCESSED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutboxEvent createUpdatedEntity() {
        return new OutboxEvent()
            .aggregateType(UPDATED_AGGREGATE_TYPE)
            .aggregateId(UPDATED_AGGREGATE_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .attempts(UPDATED_ATTEMPTS)
            .createdAt(UPDATED_CREATED_AT)
            .processedAt(UPDATED_PROCESSED_AT);
    }

    @BeforeEach
    void initTest() {
        outboxEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOutboxEvent != null) {
            outboxEventRepository.delete(insertedOutboxEvent);
            insertedOutboxEvent = null;
        }
    }

    @Test
    @Transactional
    void createOutboxEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);
        var returnedOutboxEventDTO = om.readValue(
            restOutboxEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OutboxEventDTO.class
        );

        // Validate the OutboxEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOutboxEvent = outboxEventMapper.toEntity(returnedOutboxEventDTO);
        assertOutboxEventUpdatableFieldsEquals(returnedOutboxEvent, getPersistedOutboxEvent(returnedOutboxEvent));

        insertedOutboxEvent = returnedOutboxEvent;
    }

    @Test
    @Transactional
    void createOutboxEventWithExistingId() throws Exception {
        // Create the OutboxEvent with an existing ID
        outboxEvent.setId(1L);
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutboxEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAggregateTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        outboxEvent.setAggregateType(null);

        // Create the OutboxEvent, which fails.
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        restOutboxEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAggregateIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        outboxEvent.setAggregateId(null);

        // Create the OutboxEvent, which fails.
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        restOutboxEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        outboxEvent.setEventType(null);

        // Create the OutboxEvent, which fails.
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        restOutboxEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        outboxEvent.setStatus(null);

        // Create the OutboxEvent, which fails.
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        restOutboxEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOutboxEvents() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        // Get all the outboxEventList
        restOutboxEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outboxEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].aggregateType").value(hasItem(DEFAULT_AGGREGATE_TYPE)))
            .andExpect(jsonPath("$.[*].aggregateId").value(hasItem(DEFAULT_AGGREGATE_ID)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].attempts").value(hasItem(DEFAULT_ATTEMPTS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(DEFAULT_PROCESSED_AT.toString())));
    }

    @Test
    @Transactional
    void getOutboxEvent() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        // Get the outboxEvent
        restOutboxEventMockMvc
            .perform(get(ENTITY_API_URL_ID, outboxEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outboxEvent.getId().intValue()))
            .andExpect(jsonPath("$.aggregateType").value(DEFAULT_AGGREGATE_TYPE))
            .andExpect(jsonPath("$.aggregateId").value(DEFAULT_AGGREGATE_ID))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.attempts").value(DEFAULT_ATTEMPTS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.processedAt").value(DEFAULT_PROCESSED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOutboxEvent() throws Exception {
        // Get the outboxEvent
        restOutboxEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOutboxEvent() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the outboxEvent
        OutboxEvent updatedOutboxEvent = outboxEventRepository.findById(outboxEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOutboxEvent are not directly saved in db
        em.detach(updatedOutboxEvent);
        updatedOutboxEvent
            .aggregateType(UPDATED_AGGREGATE_TYPE)
            .aggregateId(UPDATED_AGGREGATE_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .attempts(UPDATED_ATTEMPTS)
            .createdAt(UPDATED_CREATED_AT)
            .processedAt(UPDATED_PROCESSED_AT);
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(updatedOutboxEvent);

        restOutboxEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outboxEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(outboxEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOutboxEventToMatchAllProperties(updatedOutboxEvent);
    }

    @Test
    @Transactional
    void putNonExistingOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outboxEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(outboxEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(outboxEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOutboxEventWithPatch() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the outboxEvent using partial update
        OutboxEvent partialUpdatedOutboxEvent = new OutboxEvent();
        partialUpdatedOutboxEvent.setId(outboxEvent.getId());

        partialUpdatedOutboxEvent
            .aggregateId(UPDATED_AGGREGATE_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .processedAt(UPDATED_PROCESSED_AT);

        restOutboxEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutboxEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOutboxEvent))
            )
            .andExpect(status().isOk());

        // Validate the OutboxEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOutboxEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOutboxEvent, outboxEvent),
            getPersistedOutboxEvent(outboxEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateOutboxEventWithPatch() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the outboxEvent using partial update
        OutboxEvent partialUpdatedOutboxEvent = new OutboxEvent();
        partialUpdatedOutboxEvent.setId(outboxEvent.getId());

        partialUpdatedOutboxEvent
            .aggregateType(UPDATED_AGGREGATE_TYPE)
            .aggregateId(UPDATED_AGGREGATE_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .attempts(UPDATED_ATTEMPTS)
            .createdAt(UPDATED_CREATED_AT)
            .processedAt(UPDATED_PROCESSED_AT);

        restOutboxEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutboxEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOutboxEvent))
            )
            .andExpect(status().isOk());

        // Validate the OutboxEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOutboxEventUpdatableFieldsEquals(partialUpdatedOutboxEvent, getPersistedOutboxEvent(partialUpdatedOutboxEvent));
    }

    @Test
    @Transactional
    void patchNonExistingOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outboxEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(outboxEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(outboxEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutboxEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        outboxEvent.setId(longCount.incrementAndGet());

        // Create the OutboxEvent
        OutboxEventDTO outboxEventDTO = outboxEventMapper.toDto(outboxEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutboxEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(outboxEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutboxEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOutboxEvent() throws Exception {
        // Initialize the database
        insertedOutboxEvent = outboxEventRepository.saveAndFlush(outboxEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the outboxEvent
        restOutboxEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, outboxEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return outboxEventRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OutboxEvent getPersistedOutboxEvent(OutboxEvent outboxEvent) {
        return outboxEventRepository.findById(outboxEvent.getId()).orElseThrow();
    }

    protected void assertPersistedOutboxEventToMatchAllProperties(OutboxEvent expectedOutboxEvent) {
        assertOutboxEventAllPropertiesEquals(expectedOutboxEvent, getPersistedOutboxEvent(expectedOutboxEvent));
    }

    protected void assertPersistedOutboxEventToMatchUpdatableProperties(OutboxEvent expectedOutboxEvent) {
        assertOutboxEventAllUpdatablePropertiesEquals(expectedOutboxEvent, getPersistedOutboxEvent(expectedOutboxEvent));
    }
}
