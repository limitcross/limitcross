package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalWalletAsserts.*;
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
import com.limitcross.facility.domain.ProfessionalWallet;
import com.limitcross.facility.repository.ProfessionalWalletRepository;
import com.limitcross.facility.service.ProfessionalWalletService;
import com.limitcross.facility.service.dto.ProfessionalWalletDTO;
import com.limitcross.facility.service.mapper.ProfessionalWalletMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProfessionalWalletResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalWalletResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professional-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalWalletRepository professionalWalletRepository;

    @Mock
    private ProfessionalWalletRepository professionalWalletRepositoryMock;

    @Autowired
    private ProfessionalWalletMapper professionalWalletMapper;

    @Mock
    private ProfessionalWalletService professionalWalletServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalWalletMockMvc;

    private ProfessionalWallet professionalWallet;

    private ProfessionalWallet insertedProfessionalWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalWallet createEntity(EntityManager em) {
        ProfessionalWallet professionalWallet = new ProfessionalWallet().balance(DEFAULT_BALANCE).updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalWallet.setProfessional(professional);
        return professionalWallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalWallet createUpdatedEntity(EntityManager em) {
        ProfessionalWallet updatedProfessionalWallet = new ProfessionalWallet().balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalWallet.setProfessional(professional);
        return updatedProfessionalWallet;
    }

    @BeforeEach
    void initTest() {
        professionalWallet = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalWallet != null) {
            professionalWalletRepository.delete(insertedProfessionalWallet);
            insertedProfessionalWallet = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);
        var returnedProfessionalWalletDTO = om.readValue(
            restProfessionalWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalWalletDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalWalletDTO.class
        );

        // Validate the ProfessionalWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalWallet = professionalWalletMapper.toEntity(returnedProfessionalWalletDTO);
        assertProfessionalWalletUpdatableFieldsEquals(
            returnedProfessionalWallet,
            getPersistedProfessionalWallet(returnedProfessionalWallet)
        );

        insertedProfessionalWallet = returnedProfessionalWallet;
    }

    @Test
    @Transactional
    void createProfessionalWalletWithExistingId() throws Exception {
        // Create the ProfessionalWallet with an existing ID
        professionalWallet.setId(1L);
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalWalletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalWallet.setBalance(null);

        // Create the ProfessionalWallet, which fails.
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        restProfessionalWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalWallets() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        // Get all the professionalWalletList
        restProfessionalWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalWalletsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalWalletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalWalletServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalWalletsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalWalletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalWalletRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalWallet() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        // Get the professionalWallet
        restProfessionalWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalWallet.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalWallet() throws Exception {
        // Get the professionalWallet
        restProfessionalWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalWallet() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalWallet
        ProfessionalWallet updatedProfessionalWallet = professionalWalletRepository.findById(professionalWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalWallet are not directly saved in db
        em.detach(updatedProfessionalWallet);
        updatedProfessionalWallet.balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(updatedProfessionalWallet);

        restProfessionalWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalWalletDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalWalletToMatchAllProperties(updatedProfessionalWallet);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalWalletWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalWallet using partial update
        ProfessionalWallet partialUpdatedProfessionalWallet = new ProfessionalWallet();
        partialUpdatedProfessionalWallet.setId(professionalWallet.getId());

        restProfessionalWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalWallet))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalWallet, professionalWallet),
            getPersistedProfessionalWallet(professionalWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalWalletWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalWallet using partial update
        ProfessionalWallet partialUpdatedProfessionalWallet = new ProfessionalWallet();
        partialUpdatedProfessionalWallet.setId(professionalWallet.getId());

        partialUpdatedProfessionalWallet.balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);

        restProfessionalWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalWallet))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalWalletUpdatableFieldsEquals(
            partialUpdatedProfessionalWallet,
            getPersistedProfessionalWallet(partialUpdatedProfessionalWallet)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalWalletDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalWallet.setId(longCount.incrementAndGet());

        // Create the ProfessionalWallet
        ProfessionalWalletDTO professionalWalletDTO = professionalWalletMapper.toDto(professionalWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalWallet() throws Exception {
        // Initialize the database
        insertedProfessionalWallet = professionalWalletRepository.saveAndFlush(professionalWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalWallet
        restProfessionalWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalWalletRepository.count();
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

    protected ProfessionalWallet getPersistedProfessionalWallet(ProfessionalWallet professionalWallet) {
        return professionalWalletRepository.findById(professionalWallet.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalWalletToMatchAllProperties(ProfessionalWallet expectedProfessionalWallet) {
        assertProfessionalWalletAllPropertiesEquals(expectedProfessionalWallet, getPersistedProfessionalWallet(expectedProfessionalWallet));
    }

    protected void assertPersistedProfessionalWalletToMatchUpdatableProperties(ProfessionalWallet expectedProfessionalWallet) {
        assertProfessionalWalletAllUpdatablePropertiesEquals(
            expectedProfessionalWallet,
            getPersistedProfessionalWallet(expectedProfessionalWallet)
        );
    }
}
