package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalSkillAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalSkill;
import com.limitcross.facility.repository.ProfessionalSkillRepository;
import com.limitcross.facility.service.ProfessionalSkillService;
import com.limitcross.facility.service.dto.ProfessionalSkillDTO;
import com.limitcross.facility.service.mapper.ProfessionalSkillMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ProfessionalSkillResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalSkillResourceIT {

    private static final Integer DEFAULT_SKILL_LEVEL = 1;
    private static final Integer UPDATED_SKILL_LEVEL = 2;

    private static final LocalDate DEFAULT_CERTIFIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CERTIFIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/professional-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalSkillRepository professionalSkillRepository;

    @Mock
    private ProfessionalSkillRepository professionalSkillRepositoryMock;

    @Autowired
    private ProfessionalSkillMapper professionalSkillMapper;

    @Mock
    private ProfessionalSkillService professionalSkillServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalSkillMockMvc;

    private ProfessionalSkill professionalSkill;

    private ProfessionalSkill insertedProfessionalSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalSkill createEntity(EntityManager em) {
        ProfessionalSkill professionalSkill = new ProfessionalSkill().skillLevel(DEFAULT_SKILL_LEVEL).certifiedAt(DEFAULT_CERTIFIED_AT);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        professionalSkill.setService(facilityService);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalSkill.setProfessional(professional);
        return professionalSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalSkill createUpdatedEntity(EntityManager em) {
        ProfessionalSkill updatedProfessionalSkill = new ProfessionalSkill()
            .skillLevel(UPDATED_SKILL_LEVEL)
            .certifiedAt(UPDATED_CERTIFIED_AT);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedProfessionalSkill.setService(facilityService);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalSkill.setProfessional(professional);
        return updatedProfessionalSkill;
    }

    @BeforeEach
    void initTest() {
        professionalSkill = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalSkill != null) {
            professionalSkillRepository.delete(insertedProfessionalSkill);
            insertedProfessionalSkill = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalSkill() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);
        var returnedProfessionalSkillDTO = om.readValue(
            restProfessionalSkillMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalSkillDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalSkillDTO.class
        );

        // Validate the ProfessionalSkill in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalSkill = professionalSkillMapper.toEntity(returnedProfessionalSkillDTO);
        assertProfessionalSkillUpdatableFieldsEquals(returnedProfessionalSkill, getPersistedProfessionalSkill(returnedProfessionalSkill));

        insertedProfessionalSkill = returnedProfessionalSkill;
    }

    @Test
    @Transactional
    void createProfessionalSkillWithExistingId() throws Exception {
        // Create the ProfessionalSkill with an existing ID
        professionalSkill.setId(1L);
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalSkillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfessionalSkills() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        // Get all the professionalSkillList
        restProfessionalSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillLevel").value(hasItem(DEFAULT_SKILL_LEVEL)))
            .andExpect(jsonPath("$.[*].certifiedAt").value(hasItem(DEFAULT_CERTIFIED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalSkillsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalSkillServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalSkillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalSkillServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalSkillsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalSkillServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalSkillMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalSkillRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalSkill() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        // Get the professionalSkill
        restProfessionalSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalSkill.getId().intValue()))
            .andExpect(jsonPath("$.skillLevel").value(DEFAULT_SKILL_LEVEL))
            .andExpect(jsonPath("$.certifiedAt").value(DEFAULT_CERTIFIED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalSkill() throws Exception {
        // Get the professionalSkill
        restProfessionalSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalSkill() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalSkill
        ProfessionalSkill updatedProfessionalSkill = professionalSkillRepository.findById(professionalSkill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalSkill are not directly saved in db
        em.detach(updatedProfessionalSkill);
        updatedProfessionalSkill.skillLevel(UPDATED_SKILL_LEVEL).certifiedAt(UPDATED_CERTIFIED_AT);
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(updatedProfessionalSkill);

        restProfessionalSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalSkillDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalSkillToMatchAllProperties(updatedProfessionalSkill);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalSkillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalSkillWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalSkill using partial update
        ProfessionalSkill partialUpdatedProfessionalSkill = new ProfessionalSkill();
        partialUpdatedProfessionalSkill.setId(professionalSkill.getId());

        partialUpdatedProfessionalSkill.skillLevel(UPDATED_SKILL_LEVEL);

        restProfessionalSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalSkill))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalSkillUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalSkill, professionalSkill),
            getPersistedProfessionalSkill(professionalSkill)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalSkillWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalSkill using partial update
        ProfessionalSkill partialUpdatedProfessionalSkill = new ProfessionalSkill();
        partialUpdatedProfessionalSkill.setId(professionalSkill.getId());

        partialUpdatedProfessionalSkill.skillLevel(UPDATED_SKILL_LEVEL).certifiedAt(UPDATED_CERTIFIED_AT);

        restProfessionalSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalSkill))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalSkill in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalSkillUpdatableFieldsEquals(
            partialUpdatedProfessionalSkill,
            getPersistedProfessionalSkill(partialUpdatedProfessionalSkill)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalSkillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalSkillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalSkill() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalSkill.setId(longCount.incrementAndGet());

        // Create the ProfessionalSkill
        ProfessionalSkillDTO professionalSkillDTO = professionalSkillMapper.toDto(professionalSkill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalSkillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalSkill in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalSkill() throws Exception {
        // Initialize the database
        insertedProfessionalSkill = professionalSkillRepository.saveAndFlush(professionalSkill);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalSkill
        restProfessionalSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalSkill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalSkillRepository.count();
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

    protected ProfessionalSkill getPersistedProfessionalSkill(ProfessionalSkill professionalSkill) {
        return professionalSkillRepository.findById(professionalSkill.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalSkillToMatchAllProperties(ProfessionalSkill expectedProfessionalSkill) {
        assertProfessionalSkillAllPropertiesEquals(expectedProfessionalSkill, getPersistedProfessionalSkill(expectedProfessionalSkill));
    }

    protected void assertPersistedProfessionalSkillToMatchUpdatableProperties(ProfessionalSkill expectedProfessionalSkill) {
        assertProfessionalSkillAllUpdatablePropertiesEquals(
            expectedProfessionalSkill,
            getPersistedProfessionalSkill(expectedProfessionalSkill)
        );
    }
}
