package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CancellationPolicyAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.CancellationPolicy;
import com.limitcross.facility.domain.enumeration.PolicyAudience;
import com.limitcross.facility.repository.CancellationPolicyRepository;
import com.limitcross.facility.service.CancellationPolicyService;
import com.limitcross.facility.service.dto.CancellationPolicyDTO;
import com.limitcross.facility.service.mapper.CancellationPolicyMapper;
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
 * Integration tests for the {@link CancellationPolicyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CancellationPolicyResourceIT {

    private static final Integer DEFAULT_HOURS_BEFORE_START = 0;
    private static final Integer UPDATED_HOURS_BEFORE_START = 1;

    private static final BigDecimal DEFAULT_FEE_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_FEE_PERCENT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MIN_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MIN_FEE = new BigDecimal(1);

    private static final PolicyAudience DEFAULT_APPLIES_TO = PolicyAudience.CUSTOMER;
    private static final PolicyAudience UPDATED_APPLIES_TO = PolicyAudience.PRO;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/cancellation-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CancellationPolicyRepository cancellationPolicyRepository;

    @Mock
    private CancellationPolicyRepository cancellationPolicyRepositoryMock;

    @Autowired
    private CancellationPolicyMapper cancellationPolicyMapper;

    @Mock
    private CancellationPolicyService cancellationPolicyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCancellationPolicyMockMvc;

    private CancellationPolicy cancellationPolicy;

    private CancellationPolicy insertedCancellationPolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CancellationPolicy createEntity() {
        return new CancellationPolicy()
            .hoursBeforeStart(DEFAULT_HOURS_BEFORE_START)
            .feePercent(DEFAULT_FEE_PERCENT)
            .minFee(DEFAULT_MIN_FEE)
            .appliesTo(DEFAULT_APPLIES_TO)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CancellationPolicy createUpdatedEntity() {
        return new CancellationPolicy()
            .hoursBeforeStart(UPDATED_HOURS_BEFORE_START)
            .feePercent(UPDATED_FEE_PERCENT)
            .minFee(UPDATED_MIN_FEE)
            .appliesTo(UPDATED_APPLIES_TO)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        cancellationPolicy = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCancellationPolicy != null) {
            cancellationPolicyRepository.delete(insertedCancellationPolicy);
            insertedCancellationPolicy = null;
        }
    }

    @Test
    @Transactional
    void createCancellationPolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);
        var returnedCancellationPolicyDTO = om.readValue(
            restCancellationPolicyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CancellationPolicyDTO.class
        );

        // Validate the CancellationPolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCancellationPolicy = cancellationPolicyMapper.toEntity(returnedCancellationPolicyDTO);
        assertCancellationPolicyUpdatableFieldsEquals(
            returnedCancellationPolicy,
            getPersistedCancellationPolicy(returnedCancellationPolicy)
        );

        insertedCancellationPolicy = returnedCancellationPolicy;
    }

    @Test
    @Transactional
    void createCancellationPolicyWithExistingId() throws Exception {
        // Create the CancellationPolicy with an existing ID
        cancellationPolicy.setId(1L);
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCancellationPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHoursBeforeStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cancellationPolicy.setHoursBeforeStart(null);

        // Create the CancellationPolicy, which fails.
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        restCancellationPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeePercentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cancellationPolicy.setFeePercent(null);

        // Create the CancellationPolicy, which fails.
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        restCancellationPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAppliesToIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cancellationPolicy.setAppliesTo(null);

        // Create the CancellationPolicy, which fails.
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        restCancellationPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cancellationPolicy.setActive(null);

        // Create the CancellationPolicy, which fails.
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        restCancellationPolicyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCancellationPolicies() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        // Get all the cancellationPolicyList
        restCancellationPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancellationPolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].hoursBeforeStart").value(hasItem(DEFAULT_HOURS_BEFORE_START)))
            .andExpect(jsonPath("$.[*].feePercent").value(hasItem(sameNumber(DEFAULT_FEE_PERCENT))))
            .andExpect(jsonPath("$.[*].minFee").value(hasItem(sameNumber(DEFAULT_MIN_FEE))))
            .andExpect(jsonPath("$.[*].appliesTo").value(hasItem(DEFAULT_APPLIES_TO.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCancellationPoliciesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cancellationPolicyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCancellationPolicyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cancellationPolicyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCancellationPoliciesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cancellationPolicyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCancellationPolicyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cancellationPolicyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCancellationPolicy() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        // Get the cancellationPolicy
        restCancellationPolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, cancellationPolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cancellationPolicy.getId().intValue()))
            .andExpect(jsonPath("$.hoursBeforeStart").value(DEFAULT_HOURS_BEFORE_START))
            .andExpect(jsonPath("$.feePercent").value(sameNumber(DEFAULT_FEE_PERCENT)))
            .andExpect(jsonPath("$.minFee").value(sameNumber(DEFAULT_MIN_FEE)))
            .andExpect(jsonPath("$.appliesTo").value(DEFAULT_APPLIES_TO.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingCancellationPolicy() throws Exception {
        // Get the cancellationPolicy
        restCancellationPolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCancellationPolicy() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cancellationPolicy
        CancellationPolicy updatedCancellationPolicy = cancellationPolicyRepository.findById(cancellationPolicy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCancellationPolicy are not directly saved in db
        em.detach(updatedCancellationPolicy);
        updatedCancellationPolicy
            .hoursBeforeStart(UPDATED_HOURS_BEFORE_START)
            .feePercent(UPDATED_FEE_PERCENT)
            .minFee(UPDATED_MIN_FEE)
            .appliesTo(UPDATED_APPLIES_TO)
            .active(UPDATED_ACTIVE);
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(updatedCancellationPolicy);

        restCancellationPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cancellationPolicyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cancellationPolicyDTO))
            )
            .andExpect(status().isOk());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCancellationPolicyToMatchAllProperties(updatedCancellationPolicy);
    }

    @Test
    @Transactional
    void putNonExistingCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cancellationPolicyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cancellationPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cancellationPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCancellationPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cancellationPolicy using partial update
        CancellationPolicy partialUpdatedCancellationPolicy = new CancellationPolicy();
        partialUpdatedCancellationPolicy.setId(cancellationPolicy.getId());

        partialUpdatedCancellationPolicy.feePercent(UPDATED_FEE_PERCENT).minFee(UPDATED_MIN_FEE);

        restCancellationPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCancellationPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCancellationPolicy))
            )
            .andExpect(status().isOk());

        // Validate the CancellationPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCancellationPolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCancellationPolicy, cancellationPolicy),
            getPersistedCancellationPolicy(cancellationPolicy)
        );
    }

    @Test
    @Transactional
    void fullUpdateCancellationPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cancellationPolicy using partial update
        CancellationPolicy partialUpdatedCancellationPolicy = new CancellationPolicy();
        partialUpdatedCancellationPolicy.setId(cancellationPolicy.getId());

        partialUpdatedCancellationPolicy
            .hoursBeforeStart(UPDATED_HOURS_BEFORE_START)
            .feePercent(UPDATED_FEE_PERCENT)
            .minFee(UPDATED_MIN_FEE)
            .appliesTo(UPDATED_APPLIES_TO)
            .active(UPDATED_ACTIVE);

        restCancellationPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCancellationPolicy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCancellationPolicy))
            )
            .andExpect(status().isOk());

        // Validate the CancellationPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCancellationPolicyUpdatableFieldsEquals(
            partialUpdatedCancellationPolicy,
            getPersistedCancellationPolicy(partialUpdatedCancellationPolicy)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cancellationPolicyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cancellationPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cancellationPolicyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCancellationPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cancellationPolicy.setId(longCount.incrementAndGet());

        // Create the CancellationPolicy
        CancellationPolicyDTO cancellationPolicyDTO = cancellationPolicyMapper.toDto(cancellationPolicy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCancellationPolicyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cancellationPolicyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CancellationPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCancellationPolicy() throws Exception {
        // Initialize the database
        insertedCancellationPolicy = cancellationPolicyRepository.saveAndFlush(cancellationPolicy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cancellationPolicy
        restCancellationPolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, cancellationPolicy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cancellationPolicyRepository.count();
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

    protected CancellationPolicy getPersistedCancellationPolicy(CancellationPolicy cancellationPolicy) {
        return cancellationPolicyRepository.findById(cancellationPolicy.getId()).orElseThrow();
    }

    protected void assertPersistedCancellationPolicyToMatchAllProperties(CancellationPolicy expectedCancellationPolicy) {
        assertCancellationPolicyAllPropertiesEquals(expectedCancellationPolicy, getPersistedCancellationPolicy(expectedCancellationPolicy));
    }

    protected void assertPersistedCancellationPolicyToMatchUpdatableProperties(CancellationPolicy expectedCancellationPolicy) {
        assertCancellationPolicyAllUpdatablePropertiesEquals(
            expectedCancellationPolicy,
            getPersistedCancellationPolicy(expectedCancellationPolicy)
        );
    }
}
