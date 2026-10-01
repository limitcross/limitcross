package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.FeatureFlagAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FeatureFlag;
import com.limitcross.facility.repository.FeatureFlagRepository;
import com.limitcross.facility.service.dto.FeatureFlagDTO;
import com.limitcross.facility.service.mapper.FeatureFlagMapper;
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
 * Integration tests for the {@link FeatureFlagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeatureFlagResourceIT {

    private static final String DEFAULT_FLAG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FLAG_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Integer DEFAULT_ROLLOUT_PERCENT = 0;
    private static final Integer UPDATED_ROLLOUT_PERCENT = 1;

    private static final String DEFAULT_CITY_IDS = "AAAAAAAAAA";
    private static final String UPDATED_CITY_IDS = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feature-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private FeatureFlagMapper featureFlagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeatureFlagMockMvc;

    private FeatureFlag featureFlag;

    private FeatureFlag insertedFeatureFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureFlag createEntity() {
        return new FeatureFlag()
            .flagKey(DEFAULT_FLAG_KEY)
            .enabled(DEFAULT_ENABLED)
            .rolloutPercent(DEFAULT_ROLLOUT_PERCENT)
            .cityIds(DEFAULT_CITY_IDS)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureFlag createUpdatedEntity() {
        return new FeatureFlag()
            .flagKey(UPDATED_FLAG_KEY)
            .enabled(UPDATED_ENABLED)
            .rolloutPercent(UPDATED_ROLLOUT_PERCENT)
            .cityIds(UPDATED_CITY_IDS)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        featureFlag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFeatureFlag != null) {
            featureFlagRepository.delete(insertedFeatureFlag);
            insertedFeatureFlag = null;
        }
    }

    @Test
    @Transactional
    void createFeatureFlag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);
        var returnedFeatureFlagDTO = om.readValue(
            restFeatureFlagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeatureFlagDTO.class
        );

        // Validate the FeatureFlag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeatureFlag = featureFlagMapper.toEntity(returnedFeatureFlagDTO);
        assertFeatureFlagUpdatableFieldsEquals(returnedFeatureFlag, getPersistedFeatureFlag(returnedFeatureFlag));

        insertedFeatureFlag = returnedFeatureFlag;
    }

    @Test
    @Transactional
    void createFeatureFlagWithExistingId() throws Exception {
        // Create the FeatureFlag with an existing ID
        featureFlag.setId(1L);
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeatureFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFlagKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        featureFlag.setFlagKey(null);

        // Create the FeatureFlag, which fails.
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        restFeatureFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        featureFlag.setEnabled(null);

        // Create the FeatureFlag, which fails.
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        restFeatureFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeatureFlags() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(featureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].flagKey").value(hasItem(DEFAULT_FLAG_KEY)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].rolloutPercent").value(hasItem(DEFAULT_ROLLOUT_PERCENT)))
            .andExpect(jsonPath("$.[*].cityIds").value(hasItem(DEFAULT_CITY_IDS)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get the featureFlag
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, featureFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(featureFlag.getId().intValue()))
            .andExpect(jsonPath("$.flagKey").value(DEFAULT_FLAG_KEY))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED))
            .andExpect(jsonPath("$.rolloutPercent").value(DEFAULT_ROLLOUT_PERCENT))
            .andExpect(jsonPath("$.cityIds").value(DEFAULT_CITY_IDS))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeatureFlag() throws Exception {
        // Get the featureFlag
        restFeatureFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag
        FeatureFlag updatedFeatureFlag = featureFlagRepository.findById(featureFlag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeatureFlag are not directly saved in db
        em.detach(updatedFeatureFlag);
        updatedFeatureFlag
            .flagKey(UPDATED_FLAG_KEY)
            .enabled(UPDATED_ENABLED)
            .rolloutPercent(UPDATED_ROLLOUT_PERCENT)
            .cityIds(UPDATED_CITY_IDS)
            .updatedAt(UPDATED_UPDATED_AT);
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(updatedFeatureFlag);

        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featureFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeatureFlagToMatchAllProperties(updatedFeatureFlag);
    }

    @Test
    @Transactional
    void putNonExistingFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featureFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeatureFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag using partial update
        FeatureFlag partialUpdatedFeatureFlag = new FeatureFlag();
        partialUpdatedFeatureFlag.setId(featureFlag.getId());

        partialUpdatedFeatureFlag.enabled(UPDATED_ENABLED).rolloutPercent(UPDATED_ROLLOUT_PERCENT).cityIds(UPDATED_CITY_IDS);

        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureFlag))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureFlagUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeatureFlag, featureFlag),
            getPersistedFeatureFlag(featureFlag)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeatureFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag using partial update
        FeatureFlag partialUpdatedFeatureFlag = new FeatureFlag();
        partialUpdatedFeatureFlag.setId(featureFlag.getId());

        partialUpdatedFeatureFlag
            .flagKey(UPDATED_FLAG_KEY)
            .enabled(UPDATED_ENABLED)
            .rolloutPercent(UPDATED_ROLLOUT_PERCENT)
            .cityIds(UPDATED_CITY_IDS)
            .updatedAt(UPDATED_UPDATED_AT);

        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureFlag))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureFlagUpdatableFieldsEquals(partialUpdatedFeatureFlag, getPersistedFeatureFlag(partialUpdatedFeatureFlag));
    }

    @Test
    @Transactional
    void patchNonExistingFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, featureFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // Create the FeatureFlag
        FeatureFlagDTO featureFlagDTO = featureFlagMapper.toDto(featureFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(featureFlagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the featureFlag
        restFeatureFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, featureFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return featureFlagRepository.count();
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

    protected FeatureFlag getPersistedFeatureFlag(FeatureFlag featureFlag) {
        return featureFlagRepository.findById(featureFlag.getId()).orElseThrow();
    }

    protected void assertPersistedFeatureFlagToMatchAllProperties(FeatureFlag expectedFeatureFlag) {
        assertFeatureFlagAllPropertiesEquals(expectedFeatureFlag, getPersistedFeatureFlag(expectedFeatureFlag));
    }

    protected void assertPersistedFeatureFlagToMatchUpdatableProperties(FeatureFlag expectedFeatureFlag) {
        assertFeatureFlagAllUpdatablePropertiesEquals(expectedFeatureFlag, getPersistedFeatureFlag(expectedFeatureFlag));
    }
}
