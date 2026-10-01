package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ReferralRewardAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.ReferralReward;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.ReferralStatus;
import com.limitcross.facility.repository.ReferralRewardRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ReferralRewardService;
import com.limitcross.facility.service.dto.ReferralRewardDTO;
import com.limitcross.facility.service.mapper.ReferralRewardMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ReferralRewardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReferralRewardResourceIT {

    private static final BigDecimal DEFAULT_REWARD_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_REWARD_AMOUNT = new BigDecimal(1);

    private static final ReferralStatus DEFAULT_STATUS = ReferralStatus.PENDING;
    private static final ReferralStatus UPDATED_STATUS = ReferralStatus.GRANTED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/referral-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferralRewardRepository referralRewardRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ReferralRewardRepository referralRewardRepositoryMock;

    @Autowired
    private ReferralRewardMapper referralRewardMapper;

    @Mock
    private ReferralRewardService referralRewardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferralRewardMockMvc;

    private ReferralReward referralReward;

    private ReferralReward insertedReferralReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralReward createEntity(EntityManager em) {
        ReferralReward referralReward = new ReferralReward()
            .rewardAmount(DEFAULT_REWARD_AMOUNT)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        referralReward.setReferrer(user);
        // Add required entity
        referralReward.setReferee(user);
        return referralReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferralReward createUpdatedEntity(EntityManager em) {
        ReferralReward updatedReferralReward = new ReferralReward()
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedReferralReward.setReferrer(user);
        // Add required entity
        updatedReferralReward.setReferee(user);
        return updatedReferralReward;
    }

    @BeforeEach
    void initTest() {
        referralReward = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReferralReward != null) {
            referralRewardRepository.delete(insertedReferralReward);
            insertedReferralReward = null;
        }
    }

    @Test
    @Transactional
    void createReferralReward() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);
        var returnedReferralRewardDTO = om.readValue(
            restReferralRewardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralRewardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReferralRewardDTO.class
        );

        // Validate the ReferralReward in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReferralReward = referralRewardMapper.toEntity(returnedReferralRewardDTO);
        assertReferralRewardUpdatableFieldsEquals(returnedReferralReward, getPersistedReferralReward(returnedReferralReward));

        insertedReferralReward = returnedReferralReward;
    }

    @Test
    @Transactional
    void createReferralRewardWithExistingId() throws Exception {
        // Create the ReferralReward with an existing ID
        referralReward.setId(1L);
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferralRewardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralRewardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRewardAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralReward.setRewardAmount(null);

        // Create the ReferralReward, which fails.
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        restReferralRewardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralRewardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referralReward.setStatus(null);

        // Create the ReferralReward, which fails.
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        restReferralRewardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralRewardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferralRewards() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        // Get all the referralRewardList
        restReferralRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referralReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].rewardAmount").value(hasItem(sameNumber(DEFAULT_REWARD_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReferralRewardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(referralRewardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReferralRewardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(referralRewardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReferralRewardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(referralRewardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReferralRewardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(referralRewardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReferralReward() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        // Get the referralReward
        restReferralRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, referralReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referralReward.getId().intValue()))
            .andExpect(jsonPath("$.rewardAmount").value(sameNumber(DEFAULT_REWARD_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReferralReward() throws Exception {
        // Get the referralReward
        restReferralRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferralReward() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralReward
        ReferralReward updatedReferralReward = referralRewardRepository.findById(referralReward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReferralReward are not directly saved in db
        em.detach(updatedReferralReward);
        updatedReferralReward.rewardAmount(UPDATED_REWARD_AMOUNT).status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(updatedReferralReward);

        restReferralRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referralRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferralRewardToMatchAllProperties(updatedReferralReward);
    }

    @Test
    @Transactional
    void putNonExistingReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referralRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referralRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referralRewardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferralRewardWithPatch() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralReward using partial update
        ReferralReward partialUpdatedReferralReward = new ReferralReward();
        partialUpdatedReferralReward.setId(referralReward.getId());

        partialUpdatedReferralReward.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);

        restReferralRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralReward))
            )
            .andExpect(status().isOk());

        // Validate the ReferralReward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralRewardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReferralReward, referralReward),
            getPersistedReferralReward(referralReward)
        );
    }

    @Test
    @Transactional
    void fullUpdateReferralRewardWithPatch() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referralReward using partial update
        ReferralReward partialUpdatedReferralReward = new ReferralReward();
        partialUpdatedReferralReward.setId(referralReward.getId());

        partialUpdatedReferralReward.rewardAmount(UPDATED_REWARD_AMOUNT).status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);

        restReferralRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferralReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferralReward))
            )
            .andExpect(status().isOk());

        // Validate the ReferralReward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferralRewardUpdatableFieldsEquals(partialUpdatedReferralReward, getPersistedReferralReward(partialUpdatedReferralReward));
    }

    @Test
    @Transactional
    void patchNonExistingReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referralRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referralRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferralReward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referralReward.setId(longCount.incrementAndGet());

        // Create the ReferralReward
        ReferralRewardDTO referralRewardDTO = referralRewardMapper.toDto(referralReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferralRewardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(referralRewardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferralReward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferralReward() throws Exception {
        // Initialize the database
        insertedReferralReward = referralRewardRepository.saveAndFlush(referralReward);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the referralReward
        restReferralRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, referralReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return referralRewardRepository.count();
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

    protected ReferralReward getPersistedReferralReward(ReferralReward referralReward) {
        return referralRewardRepository.findById(referralReward.getId()).orElseThrow();
    }

    protected void assertPersistedReferralRewardToMatchAllProperties(ReferralReward expectedReferralReward) {
        assertReferralRewardAllPropertiesEquals(expectedReferralReward, getPersistedReferralReward(expectedReferralReward));
    }

    protected void assertPersistedReferralRewardToMatchUpdatableProperties(ReferralReward expectedReferralReward) {
        assertReferralRewardAllUpdatablePropertiesEquals(expectedReferralReward, getPersistedReferralReward(expectedReferralReward));
    }
}
