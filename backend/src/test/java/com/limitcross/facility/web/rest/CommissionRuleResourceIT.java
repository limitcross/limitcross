package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CommissionRuleAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.CommissionRule;
import com.limitcross.facility.repository.CommissionRuleRepository;
import com.limitcross.facility.service.CommissionRuleService;
import com.limitcross.facility.service.dto.CommissionRuleDTO;
import com.limitcross.facility.service.mapper.CommissionRuleMapper;
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
 * Integration tests for the {@link CommissionRuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommissionRuleResourceIT {

    private static final BigDecimal DEFAULT_COMMISSION_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_COMMISSION_PERCENT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_FLAT_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_FLAT_FEE = new BigDecimal(1);

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commission-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommissionRuleRepository commissionRuleRepository;

    @Mock
    private CommissionRuleRepository commissionRuleRepositoryMock;

    @Autowired
    private CommissionRuleMapper commissionRuleMapper;

    @Mock
    private CommissionRuleService commissionRuleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommissionRuleMockMvc;

    private CommissionRule commissionRule;

    private CommissionRule insertedCommissionRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRule createEntity() {
        return new CommissionRule()
            .commissionPercent(DEFAULT_COMMISSION_PERCENT)
            .flatFee(DEFAULT_FLAT_FEE)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRule createUpdatedEntity() {
        return new CommissionRule()
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .flatFee(UPDATED_FLAT_FEE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
    }

    @BeforeEach
    void initTest() {
        commissionRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommissionRule != null) {
            commissionRuleRepository.delete(insertedCommissionRule);
            insertedCommissionRule = null;
        }
    }

    @Test
    @Transactional
    void createCommissionRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);
        var returnedCommissionRuleDTO = om.readValue(
            restCommissionRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommissionRuleDTO.class
        );

        // Validate the CommissionRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommissionRule = commissionRuleMapper.toEntity(returnedCommissionRuleDTO);
        assertCommissionRuleUpdatableFieldsEquals(returnedCommissionRule, getPersistedCommissionRule(returnedCommissionRule));

        insertedCommissionRule = returnedCommissionRule;
    }

    @Test
    @Transactional
    void createCommissionRuleWithExistingId() throws Exception {
        // Create the CommissionRule with an existing ID
        commissionRule.setId(1L);
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommissionRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommissionPercentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setCommissionPercent(null);

        // Create the CommissionRule, which fails.
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        restCommissionRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setValidFrom(null);

        // Create the CommissionRule, which fails.
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        restCommissionRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommissionRules() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        // Get all the commissionRuleList
        restCommissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commissionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].commissionPercent").value(hasItem(sameNumber(DEFAULT_COMMISSION_PERCENT))))
            .andExpect(jsonPath("$.[*].flatFee").value(hasItem(sameNumber(DEFAULT_FLAT_FEE))))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommissionRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(commissionRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommissionRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commissionRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommissionRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(commissionRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommissionRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(commissionRuleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        // Get the commissionRule
        restCommissionRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, commissionRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commissionRule.getId().intValue()))
            .andExpect(jsonPath("$.commissionPercent").value(sameNumber(DEFAULT_COMMISSION_PERCENT)))
            .andExpect(jsonPath("$.flatFee").value(sameNumber(DEFAULT_FLAT_FEE)))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommissionRule() throws Exception {
        // Get the commissionRule
        restCommissionRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule
        CommissionRule updatedCommissionRule = commissionRuleRepository.findById(commissionRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommissionRule are not directly saved in db
        em.detach(updatedCommissionRule);
        updatedCommissionRule
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .flatFee(UPDATED_FLAT_FEE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(updatedCommissionRule);

        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commissionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommissionRuleToMatchAllProperties(updatedCommissionRule);
    }

    @Test
    @Transactional
    void putNonExistingCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commissionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommissionRuleWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule using partial update
        CommissionRule partialUpdatedCommissionRule = new CommissionRule();
        partialUpdatedCommissionRule.setId(commissionRule.getId());

        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRule))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommissionRule, commissionRule),
            getPersistedCommissionRule(commissionRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommissionRuleWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule using partial update
        CommissionRule partialUpdatedCommissionRule = new CommissionRule();
        partialUpdatedCommissionRule.setId(commissionRule.getId());

        partialUpdatedCommissionRule
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .flatFee(UPDATED_FLAT_FEE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRule))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleUpdatableFieldsEquals(partialUpdatedCommissionRule, getPersistedCommissionRule(partialUpdatedCommissionRule));
    }

    @Test
    @Transactional
    void patchNonExistingCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commissionRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // Create the CommissionRule
        CommissionRuleDTO commissionRuleDTO = commissionRuleMapper.toDto(commissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commissionRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commissionRule
        restCommissionRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, commissionRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commissionRuleRepository.count();
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

    protected CommissionRule getPersistedCommissionRule(CommissionRule commissionRule) {
        return commissionRuleRepository.findById(commissionRule.getId()).orElseThrow();
    }

    protected void assertPersistedCommissionRuleToMatchAllProperties(CommissionRule expectedCommissionRule) {
        assertCommissionRuleAllPropertiesEquals(expectedCommissionRule, getPersistedCommissionRule(expectedCommissionRule));
    }

    protected void assertPersistedCommissionRuleToMatchUpdatableProperties(CommissionRule expectedCommissionRule) {
        assertCommissionRuleAllUpdatablePropertiesEquals(expectedCommissionRule, getPersistedCommissionRule(expectedCommissionRule));
    }
}
