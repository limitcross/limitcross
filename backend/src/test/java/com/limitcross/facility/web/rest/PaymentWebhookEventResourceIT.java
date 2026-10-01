package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.PaymentWebhookEventAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.PaymentWebhookEvent;
import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.WebhookStatus;
import com.limitcross.facility.repository.PaymentWebhookEventRepository;
import com.limitcross.facility.service.dto.PaymentWebhookEventDTO;
import com.limitcross.facility.service.mapper.PaymentWebhookEventMapper;
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
 * Integration tests for the {@link PaymentWebhookEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentWebhookEventResourceIT {

    private static final GatewayProvider DEFAULT_GATEWAY = GatewayProvider.RAZORPAY;
    private static final GatewayProvider UPDATED_GATEWAY = GatewayProvider.CASHFREE;

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SIGNATURE_OK = false;
    private static final Boolean UPDATED_SIGNATURE_OK = true;

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final WebhookStatus DEFAULT_STATUS = WebhookStatus.RECEIVED;
    private static final WebhookStatus UPDATED_STATUS = WebhookStatus.PROCESSED;

    private static final Instant DEFAULT_RECEIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payment-webhook-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentWebhookEventRepository paymentWebhookEventRepository;

    @Autowired
    private PaymentWebhookEventMapper paymentWebhookEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentWebhookEventMockMvc;

    private PaymentWebhookEvent paymentWebhookEvent;

    private PaymentWebhookEvent insertedPaymentWebhookEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentWebhookEvent createEntity() {
        return new PaymentWebhookEvent()
            .gateway(DEFAULT_GATEWAY)
            .eventId(DEFAULT_EVENT_ID)
            .eventType(DEFAULT_EVENT_TYPE)
            .signatureOk(DEFAULT_SIGNATURE_OK)
            .payload(DEFAULT_PAYLOAD)
            .status(DEFAULT_STATUS)
            .receivedAt(DEFAULT_RECEIVED_AT)
            .processedAt(DEFAULT_PROCESSED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentWebhookEvent createUpdatedEntity() {
        return new PaymentWebhookEvent()
            .gateway(UPDATED_GATEWAY)
            .eventId(UPDATED_EVENT_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .signatureOk(UPDATED_SIGNATURE_OK)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT);
    }

    @BeforeEach
    void initTest() {
        paymentWebhookEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaymentWebhookEvent != null) {
            paymentWebhookEventRepository.delete(insertedPaymentWebhookEvent);
            insertedPaymentWebhookEvent = null;
        }
    }

    @Test
    @Transactional
    void createPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);
        var returnedPaymentWebhookEventDTO = om.readValue(
            restPaymentWebhookEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentWebhookEventDTO.class
        );

        // Validate the PaymentWebhookEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentWebhookEvent = paymentWebhookEventMapper.toEntity(returnedPaymentWebhookEventDTO);
        assertPaymentWebhookEventUpdatableFieldsEquals(
            returnedPaymentWebhookEvent,
            getPersistedPaymentWebhookEvent(returnedPaymentWebhookEvent)
        );

        insertedPaymentWebhookEvent = returnedPaymentWebhookEvent;
    }

    @Test
    @Transactional
    void createPaymentWebhookEventWithExistingId() throws Exception {
        // Create the PaymentWebhookEvent with an existing ID
        paymentWebhookEvent.setId(1L);
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGatewayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentWebhookEvent.setGateway(null);

        // Create the PaymentWebhookEvent, which fails.
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentWebhookEvent.setEventId(null);

        // Create the PaymentWebhookEvent, which fails.
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentWebhookEvent.setEventType(null);

        // Create the PaymentWebhookEvent, which fails.
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSignatureOkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentWebhookEvent.setSignatureOk(null);

        // Create the PaymentWebhookEvent, which fails.
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentWebhookEvent.setStatus(null);

        // Create the PaymentWebhookEvent, which fails.
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentWebhookEvents() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        // Get all the paymentWebhookEventList
        restPaymentWebhookEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentWebhookEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].gateway").value(hasItem(DEFAULT_GATEWAY.toString())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].signatureOk").value(hasItem(DEFAULT_SIGNATURE_OK)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(DEFAULT_PROCESSED_AT.toString())));
    }

    @Test
    @Transactional
    void getPaymentWebhookEvent() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        // Get the paymentWebhookEvent
        restPaymentWebhookEventMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentWebhookEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentWebhookEvent.getId().intValue()))
            .andExpect(jsonPath("$.gateway").value(DEFAULT_GATEWAY.toString()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.signatureOk").value(DEFAULT_SIGNATURE_OK))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.receivedAt").value(DEFAULT_RECEIVED_AT.toString()))
            .andExpect(jsonPath("$.processedAt").value(DEFAULT_PROCESSED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentWebhookEvent() throws Exception {
        // Get the paymentWebhookEvent
        restPaymentWebhookEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentWebhookEvent() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentWebhookEvent
        PaymentWebhookEvent updatedPaymentWebhookEvent = paymentWebhookEventRepository.findById(paymentWebhookEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentWebhookEvent are not directly saved in db
        em.detach(updatedPaymentWebhookEvent);
        updatedPaymentWebhookEvent
            .gateway(UPDATED_GATEWAY)
            .eventId(UPDATED_EVENT_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .signatureOk(UPDATED_SIGNATURE_OK)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT);
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(updatedPaymentWebhookEvent);

        restPaymentWebhookEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentWebhookEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentWebhookEventToMatchAllProperties(updatedPaymentWebhookEvent);
    }

    @Test
    @Transactional
    void putNonExistingPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentWebhookEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentWebhookEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWebhookEventWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentWebhookEvent using partial update
        PaymentWebhookEvent partialUpdatedPaymentWebhookEvent = new PaymentWebhookEvent();
        partialUpdatedPaymentWebhookEvent.setId(paymentWebhookEvent.getId());

        partialUpdatedPaymentWebhookEvent.signatureOk(UPDATED_SIGNATURE_OK).status(UPDATED_STATUS).receivedAt(UPDATED_RECEIVED_AT);

        restPaymentWebhookEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentWebhookEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentWebhookEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentWebhookEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentWebhookEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentWebhookEvent, paymentWebhookEvent),
            getPersistedPaymentWebhookEvent(paymentWebhookEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentWebhookEventWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentWebhookEvent using partial update
        PaymentWebhookEvent partialUpdatedPaymentWebhookEvent = new PaymentWebhookEvent();
        partialUpdatedPaymentWebhookEvent.setId(paymentWebhookEvent.getId());

        partialUpdatedPaymentWebhookEvent
            .gateway(UPDATED_GATEWAY)
            .eventId(UPDATED_EVENT_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .signatureOk(UPDATED_SIGNATURE_OK)
            .payload(UPDATED_PAYLOAD)
            .status(UPDATED_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .processedAt(UPDATED_PROCESSED_AT);

        restPaymentWebhookEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentWebhookEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentWebhookEvent))
            )
            .andExpect(status().isOk());

        // Validate the PaymentWebhookEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentWebhookEventUpdatableFieldsEquals(
            partialUpdatedPaymentWebhookEvent,
            getPersistedPaymentWebhookEvent(partialUpdatedPaymentWebhookEvent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentWebhookEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentWebhookEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentWebhookEvent.setId(longCount.incrementAndGet());

        // Create the PaymentWebhookEvent
        PaymentWebhookEventDTO paymentWebhookEventDTO = paymentWebhookEventMapper.toDto(paymentWebhookEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentWebhookEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentWebhookEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentWebhookEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentWebhookEvent() throws Exception {
        // Initialize the database
        insertedPaymentWebhookEvent = paymentWebhookEventRepository.saveAndFlush(paymentWebhookEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentWebhookEvent
        restPaymentWebhookEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentWebhookEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentWebhookEventRepository.count();
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

    protected PaymentWebhookEvent getPersistedPaymentWebhookEvent(PaymentWebhookEvent paymentWebhookEvent) {
        return paymentWebhookEventRepository.findById(paymentWebhookEvent.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentWebhookEventToMatchAllProperties(PaymentWebhookEvent expectedPaymentWebhookEvent) {
        assertPaymentWebhookEventAllPropertiesEquals(
            expectedPaymentWebhookEvent,
            getPersistedPaymentWebhookEvent(expectedPaymentWebhookEvent)
        );
    }

    protected void assertPersistedPaymentWebhookEventToMatchUpdatableProperties(PaymentWebhookEvent expectedPaymentWebhookEvent) {
        assertPaymentWebhookEventAllUpdatablePropertiesEquals(
            expectedPaymentWebhookEvent,
            getPersistedPaymentWebhookEvent(expectedPaymentWebhookEvent)
        );
    }
}
