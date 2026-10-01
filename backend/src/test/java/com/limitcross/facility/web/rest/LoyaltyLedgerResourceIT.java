package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.LoyaltyLedgerAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.LoyaltyLedger;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.LoyaltyReason;
import com.limitcross.facility.repository.LoyaltyLedgerRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.LoyaltyLedgerService;
import com.limitcross.facility.service.dto.LoyaltyLedgerDTO;
import com.limitcross.facility.service.mapper.LoyaltyLedgerMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LoyaltyLedgerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LoyaltyLedgerResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final LoyaltyReason DEFAULT_REASON = LoyaltyReason.EARN_BOOKING;
    private static final LoyaltyReason UPDATED_REASON = LoyaltyReason.REDEEM;

    private static final LocalDate DEFAULT_EXPIRES_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRES_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/loyalty-ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoyaltyLedgerRepository loyaltyLedgerRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private LoyaltyLedgerRepository loyaltyLedgerRepositoryMock;

    @Autowired
    private LoyaltyLedgerMapper loyaltyLedgerMapper;

    @Mock
    private LoyaltyLedgerService loyaltyLedgerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoyaltyLedgerMockMvc;

    private LoyaltyLedger loyaltyLedger;

    private LoyaltyLedger insertedLoyaltyLedger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyLedger createEntity(EntityManager em) {
        LoyaltyLedger loyaltyLedger = new LoyaltyLedger()
            .points(DEFAULT_POINTS)
            .reason(DEFAULT_REASON)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        loyaltyLedger.setUser(user);
        return loyaltyLedger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoyaltyLedger createUpdatedEntity(EntityManager em) {
        LoyaltyLedger updatedLoyaltyLedger = new LoyaltyLedger()
            .points(UPDATED_POINTS)
            .reason(UPDATED_REASON)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedLoyaltyLedger.setUser(user);
        return updatedLoyaltyLedger;
    }

    @BeforeEach
    void initTest() {
        loyaltyLedger = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLoyaltyLedger != null) {
            loyaltyLedgerRepository.delete(insertedLoyaltyLedger);
            insertedLoyaltyLedger = null;
        }
    }

    @Test
    @Transactional
    void createLoyaltyLedger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);
        var returnedLoyaltyLedgerDTO = om.readValue(
            restLoyaltyLedgerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyLedgerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoyaltyLedgerDTO.class
        );

        // Validate the LoyaltyLedger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoyaltyLedger = loyaltyLedgerMapper.toEntity(returnedLoyaltyLedgerDTO);
        assertLoyaltyLedgerUpdatableFieldsEquals(returnedLoyaltyLedger, getPersistedLoyaltyLedger(returnedLoyaltyLedger));

        insertedLoyaltyLedger = returnedLoyaltyLedger;
    }

    @Test
    @Transactional
    void createLoyaltyLedgerWithExistingId() throws Exception {
        // Create the LoyaltyLedger with an existing ID
        loyaltyLedger.setId(1L);
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoyaltyLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyLedgerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyLedger.setPoints(null);

        // Create the LoyaltyLedger, which fails.
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        restLoyaltyLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyLedgerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loyaltyLedger.setReason(null);

        // Create the LoyaltyLedger, which fails.
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        restLoyaltyLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyLedgerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoyaltyLedgers() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        // Get all the loyaltyLedgerList
        restLoyaltyLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loyaltyLedger.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLoyaltyLedgersWithEagerRelationshipsIsEnabled() throws Exception {
        when(loyaltyLedgerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLoyaltyLedgerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(loyaltyLedgerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLoyaltyLedgersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(loyaltyLedgerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLoyaltyLedgerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(loyaltyLedgerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLoyaltyLedger() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        // Get the loyaltyLedger
        restLoyaltyLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, loyaltyLedger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loyaltyLedger.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLoyaltyLedger() throws Exception {
        // Get the loyaltyLedger
        restLoyaltyLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoyaltyLedger() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyLedger
        LoyaltyLedger updatedLoyaltyLedger = loyaltyLedgerRepository.findById(loyaltyLedger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoyaltyLedger are not directly saved in db
        em.detach(updatedLoyaltyLedger);
        updatedLoyaltyLedger.points(UPDATED_POINTS).reason(UPDATED_REASON).expiresAt(UPDATED_EXPIRES_AT).createdAt(UPDATED_CREATED_AT);
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(updatedLoyaltyLedger);

        restLoyaltyLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyLedgerDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoyaltyLedgerToMatchAllProperties(updatedLoyaltyLedger);
    }

    @Test
    @Transactional
    void putNonExistingLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loyaltyLedgerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loyaltyLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loyaltyLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoyaltyLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyLedger using partial update
        LoyaltyLedger partialUpdatedLoyaltyLedger = new LoyaltyLedger();
        partialUpdatedLoyaltyLedger.setId(loyaltyLedger.getId());

        partialUpdatedLoyaltyLedger.points(UPDATED_POINTS).expiresAt(UPDATED_EXPIRES_AT).createdAt(UPDATED_CREATED_AT);

        restLoyaltyLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyLedger))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyLedgerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLoyaltyLedger, loyaltyLedger),
            getPersistedLoyaltyLedger(loyaltyLedger)
        );
    }

    @Test
    @Transactional
    void fullUpdateLoyaltyLedgerWithPatch() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loyaltyLedger using partial update
        LoyaltyLedger partialUpdatedLoyaltyLedger = new LoyaltyLedger();
        partialUpdatedLoyaltyLedger.setId(loyaltyLedger.getId());

        partialUpdatedLoyaltyLedger
            .points(UPDATED_POINTS)
            .reason(UPDATED_REASON)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);

        restLoyaltyLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoyaltyLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoyaltyLedger))
            )
            .andExpect(status().isOk());

        // Validate the LoyaltyLedger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoyaltyLedgerUpdatableFieldsEquals(partialUpdatedLoyaltyLedger, getPersistedLoyaltyLedger(partialUpdatedLoyaltyLedger));
    }

    @Test
    @Transactional
    void patchNonExistingLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loyaltyLedgerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loyaltyLedgerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoyaltyLedger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loyaltyLedger.setId(longCount.incrementAndGet());

        // Create the LoyaltyLedger
        LoyaltyLedgerDTO loyaltyLedgerDTO = loyaltyLedgerMapper.toDto(loyaltyLedger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoyaltyLedgerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loyaltyLedgerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoyaltyLedger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoyaltyLedger() throws Exception {
        // Initialize the database
        insertedLoyaltyLedger = loyaltyLedgerRepository.saveAndFlush(loyaltyLedger);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loyaltyLedger
        restLoyaltyLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, loyaltyLedger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loyaltyLedgerRepository.count();
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

    protected LoyaltyLedger getPersistedLoyaltyLedger(LoyaltyLedger loyaltyLedger) {
        return loyaltyLedgerRepository.findById(loyaltyLedger.getId()).orElseThrow();
    }

    protected void assertPersistedLoyaltyLedgerToMatchAllProperties(LoyaltyLedger expectedLoyaltyLedger) {
        assertLoyaltyLedgerAllPropertiesEquals(expectedLoyaltyLedger, getPersistedLoyaltyLedger(expectedLoyaltyLedger));
    }

    protected void assertPersistedLoyaltyLedgerToMatchUpdatableProperties(LoyaltyLedger expectedLoyaltyLedger) {
        assertLoyaltyLedgerAllUpdatablePropertiesEquals(expectedLoyaltyLedger, getPersistedLoyaltyLedger(expectedLoyaltyLedger));
    }
}
