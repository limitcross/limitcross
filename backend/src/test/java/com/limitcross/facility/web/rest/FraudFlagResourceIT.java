package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.FraudFlagAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FraudFlag;
import com.limitcross.facility.domain.enumeration.FraudEntityType;
import com.limitcross.facility.domain.enumeration.FraudStatus;
import com.limitcross.facility.repository.FraudFlagRepository;
import com.limitcross.facility.service.dto.FraudFlagDTO;
import com.limitcross.facility.service.mapper.FraudFlagMapper;
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
 * Integration tests for the {@link FraudFlagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FraudFlagResourceIT {

    private static final FraudEntityType DEFAULT_ENTITY_TYPE = FraudEntityType.USER;
    private static final FraudEntityType UPDATED_ENTITY_TYPE = FraudEntityType.PRO;

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RULE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RULE_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RISK_SCORE = 0;
    private static final Integer UPDATED_RISK_SCORE = 1;

    private static final FraudStatus DEFAULT_STATUS = FraudStatus.OPEN;
    private static final FraudStatus UPDATED_STATUS = FraudStatus.CONFIRMED;

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fraud-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FraudFlagRepository fraudFlagRepository;

    @Autowired
    private FraudFlagMapper fraudFlagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraudFlagMockMvc;

    private FraudFlag fraudFlag;

    private FraudFlag insertedFraudFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudFlag createEntity() {
        return new FraudFlag()
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .ruleCode(DEFAULT_RULE_CODE)
            .riskScore(DEFAULT_RISK_SCORE)
            .status(DEFAULT_STATUS)
            .details(DEFAULT_DETAILS)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FraudFlag createUpdatedEntity() {
        return new FraudFlag()
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .ruleCode(UPDATED_RULE_CODE)
            .riskScore(UPDATED_RISK_SCORE)
            .status(UPDATED_STATUS)
            .details(UPDATED_DETAILS)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        fraudFlag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFraudFlag != null) {
            fraudFlagRepository.delete(insertedFraudFlag);
            insertedFraudFlag = null;
        }
    }

    @Test
    @Transactional
    void createFraudFlag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);
        var returnedFraudFlagDTO = om.readValue(
            restFraudFlagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FraudFlagDTO.class
        );

        // Validate the FraudFlag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFraudFlag = fraudFlagMapper.toEntity(returnedFraudFlagDTO);
        assertFraudFlagUpdatableFieldsEquals(returnedFraudFlag, getPersistedFraudFlag(returnedFraudFlag));

        insertedFraudFlag = returnedFraudFlag;
    }

    @Test
    @Transactional
    void createFraudFlagWithExistingId() throws Exception {
        // Create the FraudFlag with an existing ID
        fraudFlag.setId(1L);
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraudFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fraudFlag.setEntityType(null);

        // Create the FraudFlag, which fails.
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        restFraudFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fraudFlag.setEntityId(null);

        // Create the FraudFlag, which fails.
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        restFraudFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRuleCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fraudFlag.setRuleCode(null);

        // Create the FraudFlag, which fails.
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        restFraudFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fraudFlag.setStatus(null);

        // Create the FraudFlag, which fails.
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        restFraudFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFraudFlags() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        // Get all the fraudFlagList
        restFraudFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fraudFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].ruleCode").value(hasItem(DEFAULT_RULE_CODE)))
            .andExpect(jsonPath("$.[*].riskScore").value(hasItem(DEFAULT_RISK_SCORE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getFraudFlag() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        // Get the fraudFlag
        restFraudFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, fraudFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fraudFlag.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.ruleCode").value(DEFAULT_RULE_CODE))
            .andExpect(jsonPath("$.riskScore").value(DEFAULT_RISK_SCORE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFraudFlag() throws Exception {
        // Get the fraudFlag
        restFraudFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFraudFlag() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fraudFlag
        FraudFlag updatedFraudFlag = fraudFlagRepository.findById(fraudFlag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFraudFlag are not directly saved in db
        em.detach(updatedFraudFlag);
        updatedFraudFlag
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .ruleCode(UPDATED_RULE_CODE)
            .riskScore(UPDATED_RISK_SCORE)
            .status(UPDATED_STATUS)
            .details(UPDATED_DETAILS)
            .createdAt(UPDATED_CREATED_AT);
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(updatedFraudFlag);

        restFraudFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fraudFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFraudFlagToMatchAllProperties(updatedFraudFlag);
    }

    @Test
    @Transactional
    void putNonExistingFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraudFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fraudFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fraudFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFraudFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fraudFlag using partial update
        FraudFlag partialUpdatedFraudFlag = new FraudFlag();
        partialUpdatedFraudFlag.setId(fraudFlag.getId());

        partialUpdatedFraudFlag
            .entityId(UPDATED_ENTITY_ID)
            .riskScore(UPDATED_RISK_SCORE)
            .details(UPDATED_DETAILS)
            .createdAt(UPDATED_CREATED_AT);

        restFraudFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFraudFlag))
            )
            .andExpect(status().isOk());

        // Validate the FraudFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFraudFlagUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFraudFlag, fraudFlag),
            getPersistedFraudFlag(fraudFlag)
        );
    }

    @Test
    @Transactional
    void fullUpdateFraudFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fraudFlag using partial update
        FraudFlag partialUpdatedFraudFlag = new FraudFlag();
        partialUpdatedFraudFlag.setId(fraudFlag.getId());

        partialUpdatedFraudFlag
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .ruleCode(UPDATED_RULE_CODE)
            .riskScore(UPDATED_RISK_SCORE)
            .status(UPDATED_STATUS)
            .details(UPDATED_DETAILS)
            .createdAt(UPDATED_CREATED_AT);

        restFraudFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFraudFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFraudFlag))
            )
            .andExpect(status().isOk());

        // Validate the FraudFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFraudFlagUpdatableFieldsEquals(partialUpdatedFraudFlag, getPersistedFraudFlag(partialUpdatedFraudFlag));
    }

    @Test
    @Transactional
    void patchNonExistingFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraudFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fraudFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fraudFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFraudFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fraudFlag.setId(longCount.incrementAndGet());

        // Create the FraudFlag
        FraudFlagDTO fraudFlagDTO = fraudFlagMapper.toDto(fraudFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraudFlagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fraudFlagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FraudFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFraudFlag() throws Exception {
        // Initialize the database
        insertedFraudFlag = fraudFlagRepository.saveAndFlush(fraudFlag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fraudFlag
        restFraudFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, fraudFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fraudFlagRepository.count();
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

    protected FraudFlag getPersistedFraudFlag(FraudFlag fraudFlag) {
        return fraudFlagRepository.findById(fraudFlag.getId()).orElseThrow();
    }

    protected void assertPersistedFraudFlagToMatchAllProperties(FraudFlag expectedFraudFlag) {
        assertFraudFlagAllPropertiesEquals(expectedFraudFlag, getPersistedFraudFlag(expectedFraudFlag));
    }

    protected void assertPersistedFraudFlagToMatchUpdatableProperties(FraudFlag expectedFraudFlag) {
        assertFraudFlagAllUpdatablePropertiesEquals(expectedFraudFlag, getPersistedFraudFlag(expectedFraudFlag));
    }
}
