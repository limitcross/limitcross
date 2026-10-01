package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.TrainingModuleAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.TrainingModule;
import com.limitcross.facility.repository.TrainingModuleRepository;
import com.limitcross.facility.service.TrainingModuleService;
import com.limitcross.facility.service.dto.TrainingModuleDTO;
import com.limitcross.facility.service.mapper.TrainingModuleMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TrainingModuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrainingModuleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final Integer DEFAULT_PASS_SCORE = 0;
    private static final Integer UPDATED_PASS_SCORE = 1;

    private static final String ENTITY_API_URL = "/api/training-modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainingModuleRepository trainingModuleRepository;

    @Mock
    private TrainingModuleRepository trainingModuleRepositoryMock;

    @Autowired
    private TrainingModuleMapper trainingModuleMapper;

    @Mock
    private TrainingModuleService trainingModuleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingModuleMockMvc;

    private TrainingModule trainingModule;

    private TrainingModule insertedTrainingModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingModule createEntity() {
        return new TrainingModule()
            .title(DEFAULT_TITLE)
            .contentUrl(DEFAULT_CONTENT_URL)
            .mandatory(DEFAULT_MANDATORY)
            .passScore(DEFAULT_PASS_SCORE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingModule createUpdatedEntity() {
        return new TrainingModule()
            .title(UPDATED_TITLE)
            .contentUrl(UPDATED_CONTENT_URL)
            .mandatory(UPDATED_MANDATORY)
            .passScore(UPDATED_PASS_SCORE);
    }

    @BeforeEach
    void initTest() {
        trainingModule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrainingModule != null) {
            trainingModuleRepository.delete(insertedTrainingModule);
            insertedTrainingModule = null;
        }
    }

    @Test
    @Transactional
    void createTrainingModule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);
        var returnedTrainingModuleDTO = om.readValue(
            restTrainingModuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingModuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainingModuleDTO.class
        );

        // Validate the TrainingModule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrainingModule = trainingModuleMapper.toEntity(returnedTrainingModuleDTO);
        assertTrainingModuleUpdatableFieldsEquals(returnedTrainingModule, getPersistedTrainingModule(returnedTrainingModule));

        insertedTrainingModule = returnedTrainingModule;
    }

    @Test
    @Transactional
    void createTrainingModuleWithExistingId() throws Exception {
        // Create the TrainingModule with an existing ID
        trainingModule.setId(1L);
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingModuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trainingModule.setTitle(null);

        // Create the TrainingModule, which fails.
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        restTrainingModuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingModuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainingModules() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        // Get all the trainingModuleList
        restTrainingModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contentUrl").value(hasItem(DEFAULT_CONTENT_URL)))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY)))
            .andExpect(jsonPath("$.[*].passScore").value(hasItem(DEFAULT_PASS_SCORE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingModulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(trainingModuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingModuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trainingModuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrainingModulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trainingModuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrainingModuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trainingModuleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrainingModule() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        // Get the trainingModule
        restTrainingModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingModule.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.contentUrl").value(DEFAULT_CONTENT_URL))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY))
            .andExpect(jsonPath("$.passScore").value(DEFAULT_PASS_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingTrainingModule() throws Exception {
        // Get the trainingModule
        restTrainingModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainingModule() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingModule
        TrainingModule updatedTrainingModule = trainingModuleRepository.findById(trainingModule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrainingModule are not directly saved in db
        em.detach(updatedTrainingModule);
        updatedTrainingModule
            .title(UPDATED_TITLE)
            .contentUrl(UPDATED_CONTENT_URL)
            .mandatory(UPDATED_MANDATORY)
            .passScore(UPDATED_PASS_SCORE);
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(updatedTrainingModule);

        restTrainingModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingModuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainingModuleToMatchAllProperties(updatedTrainingModule);
    }

    @Test
    @Transactional
    void putNonExistingTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingModuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingModuleWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingModule using partial update
        TrainingModule partialUpdatedTrainingModule = new TrainingModule();
        partialUpdatedTrainingModule.setId(trainingModule.getId());

        partialUpdatedTrainingModule.title(UPDATED_TITLE);

        restTrainingModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingModule))
            )
            .andExpect(status().isOk());

        // Validate the TrainingModule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingModuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrainingModule, trainingModule),
            getPersistedTrainingModule(trainingModule)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrainingModuleWithPatch() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trainingModule using partial update
        TrainingModule partialUpdatedTrainingModule = new TrainingModule();
        partialUpdatedTrainingModule.setId(trainingModule.getId());

        partialUpdatedTrainingModule
            .title(UPDATED_TITLE)
            .contentUrl(UPDATED_CONTENT_URL)
            .mandatory(UPDATED_MANDATORY)
            .passScore(UPDATED_PASS_SCORE);

        restTrainingModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrainingModule))
            )
            .andExpect(status().isOk());

        // Validate the TrainingModule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingModuleUpdatableFieldsEquals(partialUpdatedTrainingModule, getPersistedTrainingModule(partialUpdatedTrainingModule));
    }

    @Test
    @Transactional
    void patchNonExistingTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingModuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trainingModule.setId(longCount.incrementAndGet());

        // Create the TrainingModule
        TrainingModuleDTO trainingModuleDTO = trainingModuleMapper.toDto(trainingModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingModuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainingModuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingModule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingModule() throws Exception {
        // Initialize the database
        insertedTrainingModule = trainingModuleRepository.saveAndFlush(trainingModule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trainingModule
        restTrainingModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingModule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainingModuleRepository.count();
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

    protected TrainingModule getPersistedTrainingModule(TrainingModule trainingModule) {
        return trainingModuleRepository.findById(trainingModule.getId()).orElseThrow();
    }

    protected void assertPersistedTrainingModuleToMatchAllProperties(TrainingModule expectedTrainingModule) {
        assertTrainingModuleAllPropertiesEquals(expectedTrainingModule, getPersistedTrainingModule(expectedTrainingModule));
    }

    protected void assertPersistedTrainingModuleToMatchUpdatableProperties(TrainingModule expectedTrainingModule) {
        assertTrainingModuleAllUpdatablePropertiesEquals(expectedTrainingModule, getPersistedTrainingModule(expectedTrainingModule));
    }
}
