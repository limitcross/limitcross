package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalTierAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.repository.ProfessionalTierRepository;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import com.limitcross.facility.service.mapper.ProfessionalTierMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProfessionalTierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalTierResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_MIN_RATING = 0D;
    private static final Double UPDATED_MIN_RATING = 1D;

    private static final Integer DEFAULT_MIN_JOBS = 0;
    private static final Integer UPDATED_MIN_JOBS = 1;

    private static final BigDecimal DEFAULT_COMMISSION_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_COMMISSION_PERCENT = new BigDecimal(1);

    private static final Integer DEFAULT_DISPATCH_PRIORITY = 1;
    private static final Integer UPDATED_DISPATCH_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/professional-tiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalTierRepository professionalTierRepository;

    @Autowired
    private ProfessionalTierMapper professionalTierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalTierMockMvc;

    private ProfessionalTier professionalTier;

    private ProfessionalTier insertedProfessionalTier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTier createEntity() {
        return new ProfessionalTier()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .minRating(DEFAULT_MIN_RATING)
            .minJobs(DEFAULT_MIN_JOBS)
            .commissionPercent(DEFAULT_COMMISSION_PERCENT)
            .dispatchPriority(DEFAULT_DISPATCH_PRIORITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTier createUpdatedEntity() {
        return new ProfessionalTier()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .minRating(UPDATED_MIN_RATING)
            .minJobs(UPDATED_MIN_JOBS)
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .dispatchPriority(UPDATED_DISPATCH_PRIORITY);
    }

    @BeforeEach
    void initTest() {
        professionalTier = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalTier != null) {
            professionalTierRepository.delete(insertedProfessionalTier);
            insertedProfessionalTier = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalTier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);
        var returnedProfessionalTierDTO = om.readValue(
            restProfessionalTierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalTierDTO.class
        );

        // Validate the ProfessionalTier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalTier = professionalTierMapper.toEntity(returnedProfessionalTierDTO);
        assertProfessionalTierUpdatableFieldsEquals(returnedProfessionalTier, getPersistedProfessionalTier(returnedProfessionalTier));

        insertedProfessionalTier = returnedProfessionalTier;
    }

    @Test
    @Transactional
    void createProfessionalTierWithExistingId() throws Exception {
        // Create the ProfessionalTier with an existing ID
        professionalTier.setId(1L);
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTier.setCode(null);

        // Create the ProfessionalTier, which fails.
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        restProfessionalTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTier.setName(null);

        // Create the ProfessionalTier, which fails.
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        restProfessionalTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommissionPercentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTier.setCommissionPercent(null);

        // Create the ProfessionalTier, which fails.
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        restProfessionalTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalTiers() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        // Get all the professionalTierList
        restProfessionalTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalTier.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].minRating").value(hasItem(DEFAULT_MIN_RATING)))
            .andExpect(jsonPath("$.[*].minJobs").value(hasItem(DEFAULT_MIN_JOBS)))
            .andExpect(jsonPath("$.[*].commissionPercent").value(hasItem(sameNumber(DEFAULT_COMMISSION_PERCENT))))
            .andExpect(jsonPath("$.[*].dispatchPriority").value(hasItem(DEFAULT_DISPATCH_PRIORITY)));
    }

    @Test
    @Transactional
    void getProfessionalTier() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        // Get the professionalTier
        restProfessionalTierMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalTier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalTier.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.minRating").value(DEFAULT_MIN_RATING))
            .andExpect(jsonPath("$.minJobs").value(DEFAULT_MIN_JOBS))
            .andExpect(jsonPath("$.commissionPercent").value(sameNumber(DEFAULT_COMMISSION_PERCENT)))
            .andExpect(jsonPath("$.dispatchPriority").value(DEFAULT_DISPATCH_PRIORITY));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalTier() throws Exception {
        // Get the professionalTier
        restProfessionalTierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalTier() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTier
        ProfessionalTier updatedProfessionalTier = professionalTierRepository.findById(professionalTier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalTier are not directly saved in db
        em.detach(updatedProfessionalTier);
        updatedProfessionalTier
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .minRating(UPDATED_MIN_RATING)
            .minJobs(UPDATED_MIN_JOBS)
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .dispatchPriority(UPDATED_DISPATCH_PRIORITY);
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(updatedProfessionalTier);

        restProfessionalTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalTierToMatchAllProperties(updatedProfessionalTier);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalTierWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTier using partial update
        ProfessionalTier partialUpdatedProfessionalTier = new ProfessionalTier();
        partialUpdatedProfessionalTier.setId(professionalTier.getId());

        partialUpdatedProfessionalTier.minJobs(UPDATED_MIN_JOBS).commissionPercent(UPDATED_COMMISSION_PERCENT);

        restProfessionalTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTier))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalTier, professionalTier),
            getPersistedProfessionalTier(professionalTier)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalTierWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTier using partial update
        ProfessionalTier partialUpdatedProfessionalTier = new ProfessionalTier();
        partialUpdatedProfessionalTier.setId(professionalTier.getId());

        partialUpdatedProfessionalTier
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .minRating(UPDATED_MIN_RATING)
            .minJobs(UPDATED_MIN_JOBS)
            .commissionPercent(UPDATED_COMMISSION_PERCENT)
            .dispatchPriority(UPDATED_DISPATCH_PRIORITY);

        restProfessionalTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTier))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTierUpdatableFieldsEquals(
            partialUpdatedProfessionalTier,
            getPersistedProfessionalTier(partialUpdatedProfessionalTier)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalTierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalTier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTier.setId(longCount.incrementAndGet());

        // Create the ProfessionalTier
        ProfessionalTierDTO professionalTierDTO = professionalTierMapper.toDto(professionalTier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalTierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalTier() throws Exception {
        // Initialize the database
        insertedProfessionalTier = professionalTierRepository.saveAndFlush(professionalTier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalTier
        restProfessionalTierMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalTier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalTierRepository.count();
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

    protected ProfessionalTier getPersistedProfessionalTier(ProfessionalTier professionalTier) {
        return professionalTierRepository.findById(professionalTier.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalTierToMatchAllProperties(ProfessionalTier expectedProfessionalTier) {
        assertProfessionalTierAllPropertiesEquals(expectedProfessionalTier, getPersistedProfessionalTier(expectedProfessionalTier));
    }

    protected void assertPersistedProfessionalTierToMatchUpdatableProperties(ProfessionalTier expectedProfessionalTier) {
        assertProfessionalTierAllUpdatablePropertiesEquals(
            expectedProfessionalTier,
            getPersistedProfessionalTier(expectedProfessionalTier)
        );
    }
}
