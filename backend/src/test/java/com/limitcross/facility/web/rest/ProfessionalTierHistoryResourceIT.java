package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalTierHistoryAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.domain.ProfessionalTierHistory;
import com.limitcross.facility.repository.ProfessionalTierHistoryRepository;
import com.limitcross.facility.service.ProfessionalTierHistoryService;
import com.limitcross.facility.service.dto.ProfessionalTierHistoryDTO;
import com.limitcross.facility.service.mapper.ProfessionalTierHistoryMapper;
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
 * Integration tests for the {@link ProfessionalTierHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalTierHistoryResourceIT {

    private static final Instant DEFAULT_EFFECTIVE_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFFECTIVE_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professional-tier-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalTierHistoryRepository professionalTierHistoryRepository;

    @Mock
    private ProfessionalTierHistoryRepository professionalTierHistoryRepositoryMock;

    @Autowired
    private ProfessionalTierHistoryMapper professionalTierHistoryMapper;

    @Mock
    private ProfessionalTierHistoryService professionalTierHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalTierHistoryMockMvc;

    private ProfessionalTierHistory professionalTierHistory;

    private ProfessionalTierHistory insertedProfessionalTierHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTierHistory createEntity(EntityManager em) {
        ProfessionalTierHistory professionalTierHistory = new ProfessionalTierHistory()
            .effectiveFrom(DEFAULT_EFFECTIVE_FROM)
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
        professionalTierHistory.setProfessional(professional);
        // Add required entity
        ProfessionalTier professionalTier;
        if (TestUtil.findAll(em, ProfessionalTier.class).isEmpty()) {
            professionalTier = ProfessionalTierResourceIT.createEntity();
            em.persist(professionalTier);
            em.flush();
        } else {
            professionalTier = TestUtil.findAll(em, ProfessionalTier.class).get(0);
        }
        professionalTierHistory.setTier(professionalTier);
        return professionalTierHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalTierHistory createUpdatedEntity(EntityManager em) {
        ProfessionalTierHistory updatedProfessionalTierHistory = new ProfessionalTierHistory()
            .effectiveFrom(UPDATED_EFFECTIVE_FROM)
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
        updatedProfessionalTierHistory.setProfessional(professional);
        // Add required entity
        ProfessionalTier professionalTier;
        if (TestUtil.findAll(em, ProfessionalTier.class).isEmpty()) {
            professionalTier = ProfessionalTierResourceIT.createUpdatedEntity();
            em.persist(professionalTier);
            em.flush();
        } else {
            professionalTier = TestUtil.findAll(em, ProfessionalTier.class).get(0);
        }
        updatedProfessionalTierHistory.setTier(professionalTier);
        return updatedProfessionalTierHistory;
    }

    @BeforeEach
    void initTest() {
        professionalTierHistory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalTierHistory != null) {
            professionalTierHistoryRepository.delete(insertedProfessionalTierHistory);
            insertedProfessionalTierHistory = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);
        var returnedProfessionalTierHistoryDTO = om.readValue(
            restProfessionalTierHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierHistoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalTierHistoryDTO.class
        );

        // Validate the ProfessionalTierHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalTierHistory = professionalTierHistoryMapper.toEntity(returnedProfessionalTierHistoryDTO);
        assertProfessionalTierHistoryUpdatableFieldsEquals(
            returnedProfessionalTierHistory,
            getPersistedProfessionalTierHistory(returnedProfessionalTierHistory)
        );

        insertedProfessionalTierHistory = returnedProfessionalTierHistory;
    }

    @Test
    @Transactional
    void createProfessionalTierHistoryWithExistingId() throws Exception {
        // Create the ProfessionalTierHistory with an existing ID
        professionalTierHistory.setId(1L);
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalTierHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEffectiveFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalTierHistory.setEffectiveFrom(null);

        // Create the ProfessionalTierHistory, which fails.
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        restProfessionalTierHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalTierHistories() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        // Get all the professionalTierHistoryList
        restProfessionalTierHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalTierHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectiveFrom").value(hasItem(DEFAULT_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTierHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalTierHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTierHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalTierHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalTierHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalTierHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalTierHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalTierHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalTierHistory() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        // Get the professionalTierHistory
        restProfessionalTierHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalTierHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalTierHistory.getId().intValue()))
            .andExpect(jsonPath("$.effectiveFrom").value(DEFAULT_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalTierHistory() throws Exception {
        // Get the professionalTierHistory
        restProfessionalTierHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalTierHistory() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTierHistory
        ProfessionalTierHistory updatedProfessionalTierHistory = professionalTierHistoryRepository
            .findById(professionalTierHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalTierHistory are not directly saved in db
        em.detach(updatedProfessionalTierHistory);
        updatedProfessionalTierHistory.effectiveFrom(UPDATED_EFFECTIVE_FROM).reason(UPDATED_REASON);
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(updatedProfessionalTierHistory);

        restProfessionalTierHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTierHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalTierHistoryToMatchAllProperties(updatedProfessionalTierHistory);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalTierHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalTierHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalTierHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTierHistory using partial update
        ProfessionalTierHistory partialUpdatedProfessionalTierHistory = new ProfessionalTierHistory();
        partialUpdatedProfessionalTierHistory.setId(professionalTierHistory.getId());

        partialUpdatedProfessionalTierHistory.reason(UPDATED_REASON);

        restProfessionalTierHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTierHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTierHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTierHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTierHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalTierHistory, professionalTierHistory),
            getPersistedProfessionalTierHistory(professionalTierHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalTierHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalTierHistory using partial update
        ProfessionalTierHistory partialUpdatedProfessionalTierHistory = new ProfessionalTierHistory();
        partialUpdatedProfessionalTierHistory.setId(professionalTierHistory.getId());

        partialUpdatedProfessionalTierHistory.effectiveFrom(UPDATED_EFFECTIVE_FROM).reason(UPDATED_REASON);

        restProfessionalTierHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalTierHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalTierHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalTierHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalTierHistoryUpdatableFieldsEquals(
            partialUpdatedProfessionalTierHistory,
            getPersistedProfessionalTierHistory(partialUpdatedProfessionalTierHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalTierHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalTierHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalTierHistory.setId(longCount.incrementAndGet());

        // Create the ProfessionalTierHistory
        ProfessionalTierHistoryDTO professionalTierHistoryDTO = professionalTierHistoryMapper.toDto(professionalTierHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalTierHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalTierHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalTierHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalTierHistory() throws Exception {
        // Initialize the database
        insertedProfessionalTierHistory = professionalTierHistoryRepository.saveAndFlush(professionalTierHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalTierHistory
        restProfessionalTierHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalTierHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalTierHistoryRepository.count();
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

    protected ProfessionalTierHistory getPersistedProfessionalTierHistory(ProfessionalTierHistory professionalTierHistory) {
        return professionalTierHistoryRepository.findById(professionalTierHistory.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalTierHistoryToMatchAllProperties(ProfessionalTierHistory expectedProfessionalTierHistory) {
        assertProfessionalTierHistoryAllPropertiesEquals(
            expectedProfessionalTierHistory,
            getPersistedProfessionalTierHistory(expectedProfessionalTierHistory)
        );
    }

    protected void assertPersistedProfessionalTierHistoryToMatchUpdatableProperties(
        ProfessionalTierHistory expectedProfessionalTierHistory
    ) {
        assertProfessionalTierHistoryAllUpdatablePropertiesEquals(
            expectedProfessionalTierHistory,
            getPersistedProfessionalTierHistory(expectedProfessionalTierHistory)
        );
    }
}
