package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalTimeOffAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTimeOff;
import com.limitcross.facility.domain.enumeration.TimeOffReason;
import com.limitcross.facility.repository.ProfessionalTimeOffRepository;
import com.limitcross.facility.service.ProfessionalTimeOffService;
import com.limitcross.facility.service.dto.ProfessionalTimeOffDTO;
import com.limitcross.facility.service.mapper.ProfessionalTimeOffMapper;
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
 * Integration tests for the {@link ProfessionalTimeOffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalTimeOffResourceIT {

    private static final Instant DEFAULT_STARTS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TimeOffReason DEFAULT_REASON = TimeOffReason.LEAVE;
    private static final TimeOffReason UPDATED_REASON = TimeOffReason.BOOKING;

    private static final String ENTITY_API_URL = "/api/professional-time-offs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalTimeOffRepository professionalTimeOffRepository;

    @Mock
    private ProfessionalTimeOffRepository professionalTimeOffRepositoryMock;

    @Autowired
    private ProfessionalTimeOffMapper professionalTimeOffMapper;

    @Mock
    private ProfessionalTimeOffService professionalTimeOffServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalTimeOffMockMvc;

    private ProfessionalTimeOff professionalTimeOff;

    private ProfessionalTimeOff insertedProfessionalTimeOff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTimeOff createEntity(EntityManager em) {
        ProfessionalTimeOff professionalTimeOff = new ProfessionalTimeOff()
            .startsAt(DEFAULT_STARTS_AT)
            .endsAt(DEFAULT_ENDS_AT)
            .reason(DEFAULT_REASON);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalTimeOff.setProfessional(professional);
        return professionalTimeOff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTimeOff createUpdatedEntity(EntityManager em) {
        ProfessionalTimeOff updatedProfessionalTimeOff = new ProfessionalTimeOff()
            .startsAt(UPDATED_STARTS_AT)
            .endsAt(UPDATED_ENDS_AT)
            .reason(UPDATED_REASON);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalTimeOff.setProfessional(professional);
        return updatedProfessionalTimeOff;
    }

    @BeforeEach
    void initTest() {
        professionalTimeOff = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalTimeOff != null) {
            professionalTimeOffRepository.delete(insertedProfessionalTimeOff);
            insertedProfessionalTimeOff = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);
        var returnedProfessionalTimeOffDTO = om.readValue(
            restProfessionalTimeOffMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalTimeOffDTO.class
        );

        // Validate the ProfessionalTimeOff in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalTimeOff = professionalTimeOffMapper.toEntity(returnedProfessionalTimeOffDTO);
        assertProfessionalTimeOffUpdatableFieldsEquals(
            returnedProfessionalTimeOff,
            getPersistedProfessionalTimeOff(returnedProfessionalTimeOff)
        );

        insertedProfessionalTimeOff = returnedProfessionalTimeOff;
    }

    @Test
    @Transactional
    void createProfessionalTimeOffWithExistingId() throws Exception {
        // Create the ProfessionalTimeOff with an existing ID
        professionalTimeOff.setId(1L);
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalTimeOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartsAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTimeOff.setStartsAt(null);

        // Create the ProfessionalTimeOff, which fails.
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        restProfessionalTimeOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndsAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTimeOff.setEndsAt(null);

        // Create the ProfessionalTimeOff, which fails.
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        restProfessionalTimeOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTimeOff.setReason(null);

        // Create the ProfessionalTimeOff, which fails.
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        restProfessionalTimeOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalTimeOffs() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        // Get all the professionalTimeOffList
        restProfessionalTimeOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalTimeOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].startsAt").value(hasItem(DEFAULT_STARTS_AT.toString())))
            .andExpect(jsonPath("$.[*].endsAt").value(hasItem(DEFAULT_ENDS_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTimeOffsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalTimeOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTimeOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalTimeOffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTimeOffsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalTimeOffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTimeOffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalTimeOffRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalTimeOff() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        // Get the professionalTimeOff
        restProfessionalTimeOffMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalTimeOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalTimeOff.getId().intValue()))
            .andExpect(jsonPath("$.startsAt").value(DEFAULT_STARTS_AT.toString()))
            .andExpect(jsonPath("$.endsAt").value(DEFAULT_ENDS_AT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalTimeOff() throws Exception {
        // Get the professionalTimeOff
        restProfessionalTimeOffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalTimeOff() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTimeOff
        ProfessionalTimeOff updatedProfessionalTimeOff = professionalTimeOffRepository.findById(professionalTimeOff.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalTimeOff are not directly saved in db
        em.detach(updatedProfessionalTimeOff);
        updatedProfessionalTimeOff.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT).reason(UPDATED_REASON);
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(updatedProfessionalTimeOff);

        restProfessionalTimeOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTimeOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalTimeOffToMatchAllProperties(updatedProfessionalTimeOff);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTimeOffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTimeOffDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalTimeOffWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTimeOff using partial update
        ProfessionalTimeOff partialUpdatedProfessionalTimeOff = new ProfessionalTimeOff();
        partialUpdatedProfessionalTimeOff.setId(professionalTimeOff.getId());

        partialUpdatedProfessionalTimeOff.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT).reason(UPDATED_REASON);

        restProfessionalTimeOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTimeOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTimeOff))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTimeOff in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTimeOffUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalTimeOff, professionalTimeOff),
            getPersistedProfessionalTimeOff(professionalTimeOff)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalTimeOffWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTimeOff using partial update
        ProfessionalTimeOff partialUpdatedProfessionalTimeOff = new ProfessionalTimeOff();
        partialUpdatedProfessionalTimeOff.setId(professionalTimeOff.getId());

        partialUpdatedProfessionalTimeOff.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT).reason(UPDATED_REASON);

        restProfessionalTimeOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTimeOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTimeOff))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTimeOff in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTimeOffUpdatableFieldsEquals(
            partialUpdatedProfessionalTimeOff,
            getPersistedProfessionalTimeOff(partialUpdatedProfessionalTimeOff)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalTimeOffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalTimeOff() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTimeOff.setId(longCount.incrementAndGet());

        // Create the ProfessionalTimeOff
        ProfessionalTimeOffDTO professionalTimeOffDTO = professionalTimeOffMapper.toDto(professionalTimeOff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTimeOffMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalTimeOffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTimeOff in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalTimeOff() throws Exception {
        // Initialize the database
        insertedProfessionalTimeOff = professionalTimeOffRepository.saveAndFlush(professionalTimeOff);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalTimeOff
        restProfessionalTimeOffMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalTimeOff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalTimeOffRepository.count();
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

    protected ProfessionalTimeOff getPersistedProfessionalTimeOff(ProfessionalTimeOff professionalTimeOff) {
        return professionalTimeOffRepository.findById(professionalTimeOff.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalTimeOffToMatchAllProperties(ProfessionalTimeOff expectedProfessionalTimeOff) {
        assertProfessionalTimeOffAllPropertiesEquals(
            expectedProfessionalTimeOff,
            getPersistedProfessionalTimeOff(expectedProfessionalTimeOff)
        );
    }

    protected void assertPersistedProfessionalTimeOffToMatchUpdatableProperties(ProfessionalTimeOff expectedProfessionalTimeOff) {
        assertProfessionalTimeOffAllUpdatablePropertiesEquals(
            expectedProfessionalTimeOff,
            getPersistedProfessionalTimeOff(expectedProfessionalTimeOff)
        );
    }
}
