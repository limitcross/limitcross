package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalTrainingAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTraining;
import com.limitcross.facility.domain.TrainingModule;
import com.limitcross.facility.domain.enumeration.TrainingStatus;
import com.limitcross.facility.repository.ProfessionalTrainingRepository;
import com.limitcross.facility.service.ProfessionalTrainingService;
import com.limitcross.facility.service.dto.ProfessionalTrainingDTO;
import com.limitcross.facility.service.mapper.ProfessionalTrainingMapper;
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
 * Integration tests for the {@link ProfessionalTrainingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalTrainingResourceIT {

    private static final Integer DEFAULT_SCORE = 0;
    private static final Integer UPDATED_SCORE = 1;

    private static final TrainingStatus DEFAULT_STATUS = TrainingStatus.ASSIGNED;
    private static final TrainingStatus UPDATED_STATUS = TrainingStatus.PASSED;

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professional-trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalTrainingRepository professionalTrainingRepository;

    @Mock
    private ProfessionalTrainingRepository professionalTrainingRepositoryMock;

    @Autowired
    private ProfessionalTrainingMapper professionalTrainingMapper;

    @Mock
    private ProfessionalTrainingService professionalTrainingServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalTrainingMockMvc;

    private ProfessionalTraining professionalTraining;

    private ProfessionalTraining insertedProfessionalTraining;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTraining createEntity(EntityManager em) {
        ProfessionalTraining professionalTraining = new ProfessionalTraining()
            .score(DEFAULT_SCORE)
            .status(DEFAULT_STATUS)
            .completedAt(DEFAULT_COMPLETED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalTraining.setProfessional(professional);
        // Add required entity
        TrainingModule trainingModule;
        if (TestUtil.findAll(em, TrainingModule.class).isEmpty()) {
            trainingModule = TrainingModuleResourceIT.createEntity();
            em.persist(trainingModule);
            em.flush();
        } else {
            trainingModule = TestUtil.findAll(em, TrainingModule.class).get(0);
        }
        professionalTraining.setModule(trainingModule);
        return professionalTraining;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTraining createUpdatedEntity(EntityManager em) {
        ProfessionalTraining updatedProfessionalTraining = new ProfessionalTraining()
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .completedAt(UPDATED_COMPLETED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalTraining.setProfessional(professional);
        // Add required entity
        TrainingModule trainingModule;
        if (TestUtil.findAll(em, TrainingModule.class).isEmpty()) {
            trainingModule = TrainingModuleResourceIT.createUpdatedEntity();
            em.persist(trainingModule);
            em.flush();
        } else {
            trainingModule = TestUtil.findAll(em, TrainingModule.class).get(0);
        }
        updatedProfessionalTraining.setModule(trainingModule);
        return updatedProfessionalTraining;
    }

    @BeforeEach
    void initTest() {
        professionalTraining = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalTraining != null) {
            professionalTrainingRepository.delete(insertedProfessionalTraining);
            insertedProfessionalTraining = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalTraining() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);
        var returnedProfessionalTrainingDTO = om.readValue(
            restProfessionalTrainingMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTrainingDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalTrainingDTO.class
        );

        // Validate the ProfessionalTraining in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalTraining = professionalTrainingMapper.toEntity(returnedProfessionalTrainingDTO);
        assertProfessionalTrainingUpdatableFieldsEquals(
            returnedProfessionalTraining,
            getPersistedProfessionalTraining(returnedProfessionalTraining)
        );

        insertedProfessionalTraining = returnedProfessionalTraining;
    }

    @Test
    @Transactional
    void createProfessionalTrainingWithExistingId() throws Exception {
        // Create the ProfessionalTraining with an existing ID
        professionalTraining.setId(1L);
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTrainingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTraining.setStatus(null);

        // Create the ProfessionalTraining, which fails.
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        restProfessionalTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTrainingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalTrainings() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        // Get all the professionalTrainingList
        restProfessionalTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalTraining.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTrainingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalTrainingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTrainingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalTrainingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTrainingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalTrainingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTrainingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalTrainingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalTraining() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        // Get the professionalTraining
        restProfessionalTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalTraining.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalTraining.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalTraining() throws Exception {
        // Get the professionalTraining
        restProfessionalTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalTraining() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTraining
        ProfessionalTraining updatedProfessionalTraining = professionalTrainingRepository
            .findById(professionalTraining.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalTraining are not directly saved in db
        em.detach(updatedProfessionalTraining);
        updatedProfessionalTraining.score(UPDATED_SCORE).status(UPDATED_STATUS).completedAt(UPDATED_COMPLETED_AT);
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(updatedProfessionalTraining);

        restProfessionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTrainingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalTrainingToMatchAllProperties(updatedProfessionalTraining);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTrainingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTrainingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTraining using partial update
        ProfessionalTraining partialUpdatedProfessionalTraining = new ProfessionalTraining();
        partialUpdatedProfessionalTraining.setId(professionalTraining.getId());

        partialUpdatedProfessionalTraining.status(UPDATED_STATUS).completedAt(UPDATED_COMPLETED_AT);

        restProfessionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTraining))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTrainingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalTraining, professionalTraining),
            getPersistedProfessionalTraining(professionalTraining)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalTrainingWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTraining using partial update
        ProfessionalTraining partialUpdatedProfessionalTraining = new ProfessionalTraining();
        partialUpdatedProfessionalTraining.setId(professionalTraining.getId());

        partialUpdatedProfessionalTraining.score(UPDATED_SCORE).status(UPDATED_STATUS).completedAt(UPDATED_COMPLETED_AT);

        restProfessionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTraining))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTraining in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTrainingUpdatableFieldsEquals(
            partialUpdatedProfessionalTraining,
            getPersistedProfessionalTraining(partialUpdatedProfessionalTraining)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalTrainingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTraining.setId(longCount.incrementAndGet());

        // Create the ProfessionalTraining
        ProfessionalTrainingDTO professionalTrainingDTO = professionalTrainingMapper.toDto(professionalTraining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalTrainingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTraining in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalTraining() throws Exception {
        // Initialize the database
        insertedProfessionalTraining = professionalTrainingRepository.saveAndFlush(professionalTraining);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalTraining
        restProfessionalTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalTraining.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalTrainingRepository.count();
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

    protected ProfessionalTraining getPersistedProfessionalTraining(ProfessionalTraining professionalTraining) {
        return professionalTrainingRepository.findById(professionalTraining.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalTrainingToMatchAllProperties(ProfessionalTraining expectedProfessionalTraining) {
        assertProfessionalTrainingAllPropertiesEquals(
            expectedProfessionalTraining,
            getPersistedProfessionalTraining(expectedProfessionalTraining)
        );
    }

    protected void assertPersistedProfessionalTrainingToMatchUpdatableProperties(ProfessionalTraining expectedProfessionalTraining) {
        assertProfessionalTrainingAllUpdatablePropertiesEquals(
            expectedProfessionalTraining,
            getPersistedProfessionalTraining(expectedProfessionalTraining)
        );
    }
}
