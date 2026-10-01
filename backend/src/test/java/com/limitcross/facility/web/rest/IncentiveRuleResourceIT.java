package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.IncentiveRuleAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.IncentiveRule;
import com.limitcross.facility.domain.enumeration.IncentiveMetric;
import com.limitcross.facility.domain.enumeration.IncentivePeriod;
import com.limitcross.facility.repository.IncentiveRuleRepository;
import com.limitcross.facility.service.IncentiveRuleService;
import com.limitcross.facility.service.dto.IncentiveRuleDTO;
import com.limitcross.facility.service.mapper.IncentiveRuleMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link IncentiveRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IncentiveRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final IncentiveMetric DEFAULT_METRIC = IncentiveMetric.JOBS_COMPLETED;
    private static final IncentiveMetric UPDATED_METRIC = IncentiveMetric.RATING;

    private static final BigDecimal DEFAULT_THRESHOLD = new BigDecimal(1);
    private static final BigDecimal UPDATED_THRESHOLD = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REWARD_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_REWARD_AMOUNT = new BigDecimal(1);

    private static final IncentivePeriod DEFAULT_PERIOD = IncentivePeriod.DAILY;
    private static final IncentivePeriod UPDATED_PERIOD = IncentivePeriod.WEEKLY;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/incentive-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncentiveRuleRepository incentiveRuleRepository;

    @Mock
    private IncentiveRuleRepository incentiveRuleRepositoryMock;

    @Autowired
    private IncentiveRuleMapper incentiveRuleMapper;

    @Mock
    private IncentiveRuleService incentiveRuleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncentiveRuleMockMvc;

    private IncentiveRule incentiveRule;

    private IncentiveRule insertedIncentiveRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncentiveRule createEntity() {
        return new IncentiveRule()
            .name(DEFAULT_NAME)
            .metric(DEFAULT_METRIC)
            .threshold(DEFAULT_THRESHOLD)
            .rewardAmount(DEFAULT_REWARD_AMOUNT)
            .period(DEFAULT_PERIOD)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncentiveRule createUpdatedEntity() {
        return new IncentiveRule()
            .name(UPDATED_NAME)
            .metric(UPDATED_METRIC)
            .threshold(UPDATED_THRESHOLD)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .period(UPDATED_PERIOD)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        incentiveRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIncentiveRule != null) {
            incentiveRuleRepository.delete(insertedIncentiveRule);
            insertedIncentiveRule = null;
        }
    }

    @Test
    @Transactional
    void createIncentiveRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);
        var returnedIncentiveRuleDTO = om.readValue(
            restIncentiveRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncentiveRuleDTO.class
        );

        // Validate the IncentiveRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIncentiveRule = incentiveRuleMapper.toEntity(returnedIncentiveRuleDTO);
        assertIncentiveRuleUpdatableFieldsEquals(returnedIncentiveRule, getPersistedIncentiveRule(returnedIncentiveRule));

        insertedIncentiveRule = returnedIncentiveRule;
    }

    @Test
    @Transactional
    void createIncentiveRuleWithExistingId() throws Exception {
        // Create the IncentiveRule with an existing ID
        incentiveRule.setId(1L);
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setName(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetricIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setMetric(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkThresholdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setThreshold(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRewardAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setRewardAmount(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setPeriod(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incentiveRule.setActive(null);

        // Create the IncentiveRule, which fails.
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        restIncentiveRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncentiveRules() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        // Get all the incentiveRuleList
        restIncentiveRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incentiveRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].metric").value(hasItem(DEFAULT_METRIC.toString())))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(sameNumber(DEFAULT_THRESHOLD))))
            .andExpect(jsonPath("$.[*].rewardAmount").value(hasItem(sameNumber(DEFAULT_REWARD_AMOUNT))))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncentiveRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(incentiveRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncentiveRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(incentiveRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncentiveRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(incentiveRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncentiveRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(incentiveRuleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIncentiveRule() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        // Get the incentiveRule
        restIncentiveRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, incentiveRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incentiveRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.metric").value(DEFAULT_METRIC.toString()))
            .andExpect(jsonPath("$.threshold").value(sameNumber(DEFAULT_THRESHOLD)))
            .andExpect(jsonPath("$.rewardAmount").value(sameNumber(DEFAULT_REWARD_AMOUNT)))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingIncentiveRule() throws Exception {
        // Get the incentiveRule
        restIncentiveRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncentiveRule() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incentiveRule
        IncentiveRule updatedIncentiveRule = incentiveRuleRepository.findById(incentiveRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIncentiveRule are not directly saved in db
        em.detach(updatedIncentiveRule);
        updatedIncentiveRule
            .name(UPDATED_NAME)
            .metric(UPDATED_METRIC)
            .threshold(UPDATED_THRESHOLD)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .period(UPDATED_PERIOD)
            .active(UPDATED_ACTIVE);
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(updatedIncentiveRule);

        restIncentiveRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incentiveRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incentiveRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncentiveRuleToMatchAllProperties(updatedIncentiveRule);
    }

    @Test
    @Transactional
    void putNonExistingIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incentiveRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incentiveRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incentiveRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncentiveRuleWithPatch() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incentiveRule using partial update
        IncentiveRule partialUpdatedIncentiveRule = new IncentiveRule();
        partialUpdatedIncentiveRule.setId(incentiveRule.getId());

        partialUpdatedIncentiveRule.name(UPDATED_NAME).metric(UPDATED_METRIC).threshold(UPDATED_THRESHOLD);

        restIncentiveRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncentiveRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncentiveRule))
            )
            .andExpect(status().isOk());

        // Validate the IncentiveRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncentiveRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncentiveRule, incentiveRule),
            getPersistedIncentiveRule(incentiveRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncentiveRuleWithPatch() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incentiveRule using partial update
        IncentiveRule partialUpdatedIncentiveRule = new IncentiveRule();
        partialUpdatedIncentiveRule.setId(incentiveRule.getId());

        partialUpdatedIncentiveRule
            .name(UPDATED_NAME)
            .metric(UPDATED_METRIC)
            .threshold(UPDATED_THRESHOLD)
            .rewardAmount(UPDATED_REWARD_AMOUNT)
            .period(UPDATED_PERIOD)
            .active(UPDATED_ACTIVE);

        restIncentiveRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncentiveRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncentiveRule))
            )
            .andExpect(status().isOk());

        // Validate the IncentiveRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncentiveRuleUpdatableFieldsEquals(partialUpdatedIncentiveRule, getPersistedIncentiveRule(partialUpdatedIncentiveRule));
    }

    @Test
    @Transactional
    void patchNonExistingIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incentiveRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incentiveRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incentiveRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncentiveRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incentiveRule.setId(longCount.incrementAndGet());

        // Create the IncentiveRule
        IncentiveRuleDTO incentiveRuleDTO = incentiveRuleMapper.toDto(incentiveRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncentiveRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(incentiveRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncentiveRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncentiveRule() throws Exception {
        // Initialize the database
        insertedIncentiveRule = incentiveRuleRepository.saveAndFlush(incentiveRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the incentiveRule
        restIncentiveRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, incentiveRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return incentiveRuleRepository.count();
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

    protected IncentiveRule getPersistedIncentiveRule(IncentiveRule incentiveRule) {
        return incentiveRuleRepository.findById(incentiveRule.getId()).orElseThrow();
    }

    protected void assertPersistedIncentiveRuleToMatchAllProperties(IncentiveRule expectedIncentiveRule) {
        assertIncentiveRuleAllPropertiesEquals(expectedIncentiveRule, getPersistedIncentiveRule(expectedIncentiveRule));
    }

    protected void assertPersistedIncentiveRuleToMatchUpdatableProperties(IncentiveRule expectedIncentiveRule) {
        assertIncentiveRuleAllUpdatablePropertiesEquals(expectedIncentiveRule, getPersistedIncentiveRule(expectedIncentiveRule));
    }
}
