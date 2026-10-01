package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalAvailabilityAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalAvailability;
import com.limitcross.facility.repository.ProfessionalAvailabilityRepository;
import com.limitcross.facility.service.ProfessionalAvailabilityService;
import com.limitcross.facility.service.dto.ProfessionalAvailabilityDTO;
import com.limitcross.facility.service.mapper.ProfessionalAvailabilityMapper;
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
 * Integration tests for the {@link ProfessionalAvailabilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalAvailabilityResourceIT {

    private static final Integer DEFAULT_DAY_OF_WEEK = 1;
    private static final Integer UPDATED_DAY_OF_WEEK = 2;

    private static final String DEFAULT_START_TIME = "23:26";
    private static final String UPDATED_START_TIME = "22:18";

    private static final String DEFAULT_END_TIME = "12:38";
    private static final String UPDATED_END_TIME = "01:13";

    private static final String ENTITY_API_URL = "/api/professional-availabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalAvailabilityRepository professionalAvailabilityRepository;

    @Mock
    private ProfessionalAvailabilityRepository professionalAvailabilityRepositoryMock;

    @Autowired
    private ProfessionalAvailabilityMapper professionalAvailabilityMapper;

    @Mock
    private ProfessionalAvailabilityService professionalAvailabilityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalAvailabilityMockMvc;

    private ProfessionalAvailability professionalAvailability;

    private ProfessionalAvailability insertedProfessionalAvailability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalAvailability createEntity(EntityManager em) {
        ProfessionalAvailability professionalAvailability = new ProfessionalAvailability()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalAvailability.setProfessional(professional);
        return professionalAvailability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalAvailability createUpdatedEntity(EntityManager em) {
        ProfessionalAvailability updatedProfessionalAvailability = new ProfessionalAvailability()
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalAvailability.setProfessional(professional);
        return updatedProfessionalAvailability;
    }

    @BeforeEach
    void initTest() {
        professionalAvailability = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalAvailability != null) {
            professionalAvailabilityRepository.delete(insertedProfessionalAvailability);
            insertedProfessionalAvailability = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalAvailability() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);
        var returnedProfessionalAvailabilityDTO = om.readValue(
            restProfessionalAvailabilityMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalAvailabilityDTO.class
        );

        // Validate the ProfessionalAvailability in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalAvailability = professionalAvailabilityMapper.toEntity(returnedProfessionalAvailabilityDTO);
        assertProfessionalAvailabilityUpdatableFieldsEquals(
            returnedProfessionalAvailability,
            getPersistedProfessionalAvailability(returnedProfessionalAvailability)
        );

        insertedProfessionalAvailability = returnedProfessionalAvailability;
    }

    @Test
    @Transactional
    void createProfessionalAvailabilityWithExistingId() throws Exception {
        // Create the ProfessionalAvailability with an existing ID
        professionalAvailability.setId(1L);
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDayOfWeekIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalAvailability.setDayOfWeek(null);

        // Create the ProfessionalAvailability, which fails.
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        restProfessionalAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalAvailability.setStartTime(null);

        // Create the ProfessionalAvailability, which fails.
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        restProfessionalAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalAvailability.setEndTime(null);

        // Create the ProfessionalAvailability, which fails.
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        restProfessionalAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalAvailabilities() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        // Get all the professionalAvailabilityList
        restProfessionalAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalAvailability.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalAvailabilitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalAvailabilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalAvailabilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalAvailabilityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalAvailabilitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalAvailabilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalAvailabilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalAvailabilityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalAvailability() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        // Get the professionalAvailability
        restProfessionalAvailabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalAvailability.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalAvailability() throws Exception {
        // Get the professionalAvailability
        restProfessionalAvailabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalAvailability() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalAvailability
        ProfessionalAvailability updatedProfessionalAvailability = professionalAvailabilityRepository
            .findById(professionalAvailability.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalAvailability are not directly saved in db
        em.detach(updatedProfessionalAvailability);
        updatedProfessionalAvailability.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(updatedProfessionalAvailability);

        restProfessionalAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalAvailabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalAvailabilityToMatchAllProperties(updatedProfessionalAvailability);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalAvailabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalAvailabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalAvailabilityWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalAvailability using partial update
        ProfessionalAvailability partialUpdatedProfessionalAvailability = new ProfessionalAvailability();
        partialUpdatedProfessionalAvailability.setId(professionalAvailability.getId());

        restProfessionalAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalAvailability))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalAvailability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalAvailabilityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalAvailability, professionalAvailability),
            getPersistedProfessionalAvailability(professionalAvailability)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalAvailabilityWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalAvailability using partial update
        ProfessionalAvailability partialUpdatedProfessionalAvailability = new ProfessionalAvailability();
        partialUpdatedProfessionalAvailability.setId(professionalAvailability.getId());

        partialUpdatedProfessionalAvailability.dayOfWeek(UPDATED_DAY_OF_WEEK).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restProfessionalAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalAvailability))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalAvailability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalAvailabilityUpdatableFieldsEquals(
            partialUpdatedProfessionalAvailability,
            getPersistedProfessionalAvailability(partialUpdatedProfessionalAvailability)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalAvailabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalAvailability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalAvailability.setId(longCount.incrementAndGet());

        // Create the ProfessionalAvailability
        ProfessionalAvailabilityDTO professionalAvailabilityDTO = professionalAvailabilityMapper.toDto(professionalAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalAvailabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalAvailability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalAvailability() throws Exception {
        // Initialize the database
        insertedProfessionalAvailability = professionalAvailabilityRepository.saveAndFlush(professionalAvailability);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalAvailability
        restProfessionalAvailabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalAvailability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalAvailabilityRepository.count();
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

    protected ProfessionalAvailability getPersistedProfessionalAvailability(ProfessionalAvailability professionalAvailability) {
        return professionalAvailabilityRepository.findById(professionalAvailability.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalAvailabilityToMatchAllProperties(ProfessionalAvailability expectedProfessionalAvailability) {
        assertProfessionalAvailabilityAllPropertiesEquals(
            expectedProfessionalAvailability,
            getPersistedProfessionalAvailability(expectedProfessionalAvailability)
        );
    }

    protected void assertPersistedProfessionalAvailabilityToMatchUpdatableProperties(
        ProfessionalAvailability expectedProfessionalAvailability
    ) {
        assertProfessionalAvailabilityAllUpdatablePropertiesEquals(
            expectedProfessionalAvailability,
            getPersistedProfessionalAvailability(expectedProfessionalAvailability)
        );
    }
}
