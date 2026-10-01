package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalDailyMetricsAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalDailyMetrics;
import com.limitcross.facility.repository.ProfessionalDailyMetricsRepository;
import com.limitcross.facility.service.ProfessionalDailyMetricsService;
import com.limitcross.facility.service.dto.ProfessionalDailyMetricsDTO;
import com.limitcross.facility.service.mapper.ProfessionalDailyMetricsMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ProfessionalDailyMetricsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalDailyMetricsResourceIT {

    private static final LocalDate DEFAULT_METRIC_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_METRIC_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_JOBS_OFFERED = 0;
    private static final Integer UPDATED_JOBS_OFFERED = 1;

    private static final Integer DEFAULT_JOBS_ACCEPTED = 0;
    private static final Integer UPDATED_JOBS_ACCEPTED = 1;

    private static final Integer DEFAULT_JOBS_COMPLETED = 0;
    private static final Integer UPDATED_JOBS_COMPLETED = 1;

    private static final Integer DEFAULT_JOBS_CANCELLED = 0;
    private static final Integer UPDATED_JOBS_CANCELLED = 1;

    private static final Integer DEFAULT_ONLINE_MINUTES = 0;
    private static final Integer UPDATED_ONLINE_MINUTES = 1;

    private static final BigDecimal DEFAULT_EARNINGS = new BigDecimal(1);
    private static final BigDecimal UPDATED_EARNINGS = new BigDecimal(2);

    private static final Double DEFAULT_AVG_RATING = 1D;
    private static final Double UPDATED_AVG_RATING = 2D;

    private static final String ENTITY_API_URL = "/api/professional-daily-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalDailyMetricsRepository professionalDailyMetricsRepository;

    @Mock
    private ProfessionalDailyMetricsRepository professionalDailyMetricsRepositoryMock;

    @Autowired
    private ProfessionalDailyMetricsMapper professionalDailyMetricsMapper;

    @Mock
    private ProfessionalDailyMetricsService professionalDailyMetricsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalDailyMetricsMockMvc;

    private ProfessionalDailyMetrics professionalDailyMetrics;

    private ProfessionalDailyMetrics insertedProfessionalDailyMetrics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalDailyMetrics createEntity(EntityManager em) {
        ProfessionalDailyMetrics professionalDailyMetrics = new ProfessionalDailyMetrics()
            .metricDate(DEFAULT_METRIC_DATE)
            .jobsOffered(DEFAULT_JOBS_OFFERED)
            .jobsAccepted(DEFAULT_JOBS_ACCEPTED)
            .jobsCompleted(DEFAULT_JOBS_COMPLETED)
            .jobsCancelled(DEFAULT_JOBS_CANCELLED)
            .onlineMinutes(DEFAULT_ONLINE_MINUTES)
            .earnings(DEFAULT_EARNINGS)
            .avgRating(DEFAULT_AVG_RATING);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalDailyMetrics.setProfessional(professional);
        return professionalDailyMetrics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalDailyMetrics createUpdatedEntity(EntityManager em) {
        ProfessionalDailyMetrics updatedProfessionalDailyMetrics = new ProfessionalDailyMetrics()
            .metricDate(UPDATED_METRIC_DATE)
            .jobsOffered(UPDATED_JOBS_OFFERED)
            .jobsAccepted(UPDATED_JOBS_ACCEPTED)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .jobsCancelled(UPDATED_JOBS_CANCELLED)
            .onlineMinutes(UPDATED_ONLINE_MINUTES)
            .earnings(UPDATED_EARNINGS)
            .avgRating(UPDATED_AVG_RATING);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalDailyMetrics.setProfessional(professional);
        return updatedProfessionalDailyMetrics;
    }

    @BeforeEach
    void initTest() {
        professionalDailyMetrics = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalDailyMetrics != null) {
            professionalDailyMetricsRepository.delete(insertedProfessionalDailyMetrics);
            insertedProfessionalDailyMetrics = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);
        var returnedProfessionalDailyMetricsDTO = om.readValue(
            restProfessionalDailyMetricsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDailyMetricsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalDailyMetricsDTO.class
        );

        // Validate the ProfessionalDailyMetrics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalDailyMetrics = professionalDailyMetricsMapper.toEntity(returnedProfessionalDailyMetricsDTO);
        assertProfessionalDailyMetricsUpdatableFieldsEquals(
            returnedProfessionalDailyMetrics,
            getPersistedProfessionalDailyMetrics(returnedProfessionalDailyMetrics)
        );

        insertedProfessionalDailyMetrics = returnedProfessionalDailyMetrics;
    }

    @Test
    @Transactional
    void createProfessionalDailyMetricsWithExistingId() throws Exception {
        // Create the ProfessionalDailyMetrics with an existing ID
        professionalDailyMetrics.setId(1L);
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalDailyMetricsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalDailyMetrics.setMetricDate(null);

        // Create the ProfessionalDailyMetrics, which fails.
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        restProfessionalDailyMetricsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalDailyMetrics() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        // Get all the professionalDailyMetricsList
        restProfessionalDailyMetricsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDailyMetrics.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricDate").value(hasItem(DEFAULT_METRIC_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobsOffered").value(hasItem(DEFAULT_JOBS_OFFERED)))
            .andExpect(jsonPath("$.[*].jobsAccepted").value(hasItem(DEFAULT_JOBS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].jobsCompleted").value(hasItem(DEFAULT_JOBS_COMPLETED)))
            .andExpect(jsonPath("$.[*].jobsCancelled").value(hasItem(DEFAULT_JOBS_CANCELLED)))
            .andExpect(jsonPath("$.[*].onlineMinutes").value(hasItem(DEFAULT_ONLINE_MINUTES)))
            .andExpect(jsonPath("$.[*].earnings").value(hasItem(sameNumber(DEFAULT_EARNINGS))))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalDailyMetricsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalDailyMetricsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalDailyMetricsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalDailyMetricsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalDailyMetricsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalDailyMetricsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalDailyMetricsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalDailyMetricsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalDailyMetrics() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        // Get the professionalDailyMetrics
        restProfessionalDailyMetricsMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalDailyMetrics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalDailyMetrics.getId().intValue()))
            .andExpect(jsonPath("$.metricDate").value(DEFAULT_METRIC_DATE.toString()))
            .andExpect(jsonPath("$.jobsOffered").value(DEFAULT_JOBS_OFFERED))
            .andExpect(jsonPath("$.jobsAccepted").value(DEFAULT_JOBS_ACCEPTED))
            .andExpect(jsonPath("$.jobsCompleted").value(DEFAULT_JOBS_COMPLETED))
            .andExpect(jsonPath("$.jobsCancelled").value(DEFAULT_JOBS_CANCELLED))
            .andExpect(jsonPath("$.onlineMinutes").value(DEFAULT_ONLINE_MINUTES))
            .andExpect(jsonPath("$.earnings").value(sameNumber(DEFAULT_EARNINGS)))
            .andExpect(jsonPath("$.avgRating").value(DEFAULT_AVG_RATING));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalDailyMetrics() throws Exception {
        // Get the professionalDailyMetrics
        restProfessionalDailyMetricsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalDailyMetrics() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalDailyMetrics
        ProfessionalDailyMetrics updatedProfessionalDailyMetrics = professionalDailyMetricsRepository
            .findById(professionalDailyMetrics.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalDailyMetrics are not directly saved in db
        em.detach(updatedProfessionalDailyMetrics);
        updatedProfessionalDailyMetrics
            .metricDate(UPDATED_METRIC_DATE)
            .jobsOffered(UPDATED_JOBS_OFFERED)
            .jobsAccepted(UPDATED_JOBS_ACCEPTED)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .jobsCancelled(UPDATED_JOBS_CANCELLED)
            .onlineMinutes(UPDATED_ONLINE_MINUTES)
            .earnings(UPDATED_EARNINGS)
            .avgRating(UPDATED_AVG_RATING);
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(updatedProfessionalDailyMetrics);

        restProfessionalDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalDailyMetricsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalDailyMetricsToMatchAllProperties(updatedProfessionalDailyMetrics);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalDailyMetricsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDailyMetricsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalDailyMetricsWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalDailyMetrics using partial update
        ProfessionalDailyMetrics partialUpdatedProfessionalDailyMetrics = new ProfessionalDailyMetrics();
        partialUpdatedProfessionalDailyMetrics.setId(professionalDailyMetrics.getId());

        partialUpdatedProfessionalDailyMetrics
            .metricDate(UPDATED_METRIC_DATE)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .earnings(UPDATED_EARNINGS)
            .avgRating(UPDATED_AVG_RATING);

        restProfessionalDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalDailyMetrics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalDailyMetrics))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalDailyMetrics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalDailyMetricsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalDailyMetrics, professionalDailyMetrics),
            getPersistedProfessionalDailyMetrics(professionalDailyMetrics)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalDailyMetricsWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalDailyMetrics using partial update
        ProfessionalDailyMetrics partialUpdatedProfessionalDailyMetrics = new ProfessionalDailyMetrics();
        partialUpdatedProfessionalDailyMetrics.setId(professionalDailyMetrics.getId());

        partialUpdatedProfessionalDailyMetrics
            .metricDate(UPDATED_METRIC_DATE)
            .jobsOffered(UPDATED_JOBS_OFFERED)
            .jobsAccepted(UPDATED_JOBS_ACCEPTED)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .jobsCancelled(UPDATED_JOBS_CANCELLED)
            .onlineMinutes(UPDATED_ONLINE_MINUTES)
            .earnings(UPDATED_EARNINGS)
            .avgRating(UPDATED_AVG_RATING);

        restProfessionalDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalDailyMetrics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalDailyMetrics))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalDailyMetrics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalDailyMetricsUpdatableFieldsEquals(
            partialUpdatedProfessionalDailyMetrics,
            getPersistedProfessionalDailyMetrics(partialUpdatedProfessionalDailyMetrics)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalDailyMetricsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalDailyMetrics.setId(longCount.incrementAndGet());

        // Create the ProfessionalDailyMetrics
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = professionalDailyMetricsMapper.toDto(professionalDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalDailyMetricsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalDailyMetrics() throws Exception {
        // Initialize the database
        insertedProfessionalDailyMetrics = professionalDailyMetricsRepository.saveAndFlush(professionalDailyMetrics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalDailyMetrics
        restProfessionalDailyMetricsMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalDailyMetrics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalDailyMetricsRepository.count();
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

    protected ProfessionalDailyMetrics getPersistedProfessionalDailyMetrics(ProfessionalDailyMetrics professionalDailyMetrics) {
        return professionalDailyMetricsRepository.findById(professionalDailyMetrics.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalDailyMetricsToMatchAllProperties(ProfessionalDailyMetrics expectedProfessionalDailyMetrics) {
        assertProfessionalDailyMetricsAllPropertiesEquals(
            expectedProfessionalDailyMetrics,
            getPersistedProfessionalDailyMetrics(expectedProfessionalDailyMetrics)
        );
    }

    protected void assertPersistedProfessionalDailyMetricsToMatchUpdatableProperties(
        ProfessionalDailyMetrics expectedProfessionalDailyMetrics
    ) {
        assertProfessionalDailyMetricsAllUpdatablePropertiesEquals(
            expectedProfessionalDailyMetrics,
            getPersistedProfessionalDailyMetrics(expectedProfessionalDailyMetrics)
        );
    }
}
