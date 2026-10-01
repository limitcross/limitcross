package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.SlotHoldAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.SlotHold;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.HoldStatus;
import com.limitcross.facility.repository.SlotHoldRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.SlotHoldService;
import com.limitcross.facility.service.dto.SlotHoldDTO;
import com.limitcross.facility.service.mapper.SlotHoldMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SlotHoldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SlotHoldResourceIT {

    private static final HoldStatus DEFAULT_STATUS = HoldStatus.HELD;
    private static final HoldStatus UPDATED_STATUS = HoldStatus.CONVERTED;

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/slot-holds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SlotHoldRepository slotHoldRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SlotHoldRepository slotHoldRepositoryMock;

    @Autowired
    private SlotHoldMapper slotHoldMapper;

    @Mock
    private SlotHoldService slotHoldServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotHoldMockMvc;

    private SlotHold slotHold;

    private SlotHold insertedSlotHold;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotHold createEntity(EntityManager em) {
        SlotHold slotHold = new SlotHold().status(DEFAULT_STATUS).expiresAt(DEFAULT_EXPIRES_AT).createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        slotHold.setUser(user);
        return slotHold;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotHold createUpdatedEntity(EntityManager em) {
        SlotHold updatedSlotHold = new SlotHold().status(UPDATED_STATUS).expiresAt(UPDATED_EXPIRES_AT).createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSlotHold.setUser(user);
        return updatedSlotHold;
    }

    @BeforeEach
    void initTest() {
        slotHold = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSlotHold != null) {
            slotHoldRepository.delete(insertedSlotHold);
            insertedSlotHold = null;
        }
    }

    @Test
    @Transactional
    void createSlotHold() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);
        var returnedSlotHoldDTO = om.readValue(
            restSlotHoldMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotHoldDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SlotHoldDTO.class
        );

        // Validate the SlotHold in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSlotHold = slotHoldMapper.toEntity(returnedSlotHoldDTO);
        assertSlotHoldUpdatableFieldsEquals(returnedSlotHold, getPersistedSlotHold(returnedSlotHold));

        insertedSlotHold = returnedSlotHold;
    }

    @Test
    @Transactional
    void createSlotHoldWithExistingId() throws Exception {
        // Create the SlotHold with an existing ID
        slotHold.setId(1L);
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotHoldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotHold.setStatus(null);

        // Create the SlotHold, which fails.
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        restSlotHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotHoldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotHold.setExpiresAt(null);

        // Create the SlotHold, which fails.
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        restSlotHoldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotHoldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSlotHolds() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        // Get all the slotHoldList
        restSlotHoldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slotHold.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotHoldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(slotHoldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotHoldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(slotHoldServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotHoldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(slotHoldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotHoldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(slotHoldRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSlotHold() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        // Get the slotHold
        restSlotHoldMockMvc
            .perform(get(ENTITY_API_URL_ID, slotHold.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slotHold.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSlotHold() throws Exception {
        // Get the slotHold
        restSlotHoldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSlotHold() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotHold
        SlotHold updatedSlotHold = slotHoldRepository.findById(slotHold.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSlotHold are not directly saved in db
        em.detach(updatedSlotHold);
        updatedSlotHold.status(UPDATED_STATUS).expiresAt(UPDATED_EXPIRES_AT).createdAt(UPDATED_CREATED_AT);
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(updatedSlotHold);

        restSlotHoldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotHoldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotHoldDTO))
            )
            .andExpect(status().isOk());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSlotHoldToMatchAllProperties(updatedSlotHold);
    }

    @Test
    @Transactional
    void putNonExistingSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotHoldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotHoldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotHoldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotHoldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSlotHoldWithPatch() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotHold using partial update
        SlotHold partialUpdatedSlotHold = new SlotHold();
        partialUpdatedSlotHold.setId(slotHold.getId());

        partialUpdatedSlotHold.status(UPDATED_STATUS);

        restSlotHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotHold.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotHold))
            )
            .andExpect(status().isOk());

        // Validate the SlotHold in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotHoldUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSlotHold, slotHold), getPersistedSlotHold(slotHold));
    }

    @Test
    @Transactional
    void fullUpdateSlotHoldWithPatch() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotHold using partial update
        SlotHold partialUpdatedSlotHold = new SlotHold();
        partialUpdatedSlotHold.setId(slotHold.getId());

        partialUpdatedSlotHold.status(UPDATED_STATUS).expiresAt(UPDATED_EXPIRES_AT).createdAt(UPDATED_CREATED_AT);

        restSlotHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotHold.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotHold))
            )
            .andExpect(status().isOk());

        // Validate the SlotHold in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotHoldUpdatableFieldsEquals(partialUpdatedSlotHold, getPersistedSlotHold(partialUpdatedSlotHold));
    }

    @Test
    @Transactional
    void patchNonExistingSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, slotHoldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotHoldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotHoldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSlotHold() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotHold.setId(longCount.incrementAndGet());

        // Create the SlotHold
        SlotHoldDTO slotHoldDTO = slotHoldMapper.toDto(slotHold);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotHoldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(slotHoldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotHold in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSlotHold() throws Exception {
        // Initialize the database
        insertedSlotHold = slotHoldRepository.saveAndFlush(slotHold);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the slotHold
        restSlotHoldMockMvc
            .perform(delete(ENTITY_API_URL_ID, slotHold.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return slotHoldRepository.count();
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

    protected SlotHold getPersistedSlotHold(SlotHold slotHold) {
        return slotHoldRepository.findById(slotHold.getId()).orElseThrow();
    }

    protected void assertPersistedSlotHoldToMatchAllProperties(SlotHold expectedSlotHold) {
        assertSlotHoldAllPropertiesEquals(expectedSlotHold, getPersistedSlotHold(expectedSlotHold));
    }

    protected void assertPersistedSlotHoldToMatchUpdatableProperties(SlotHold expectedSlotHold) {
        assertSlotHoldAllUpdatablePropertiesEquals(expectedSlotHold, getPersistedSlotHold(expectedSlotHold));
    }
}
