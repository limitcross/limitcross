package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.InvoiceSequenceAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.InvoiceSequence;
import com.limitcross.facility.repository.InvoiceSequenceRepository;
import com.limitcross.facility.service.dto.InvoiceSequenceDTO;
import com.limitcross.facility.service.mapper.InvoiceSequenceMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link InvoiceSequenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceSequenceResourceIT {

    private static final String DEFAULT_SERIES = "AAAAAAAAAA";
    private static final String UPDATED_SERIES = "BBBBBBBBBB";

    private static final String DEFAULT_FISCAL_YEAR = "AAAAAAAAA";
    private static final String UPDATED_FISCAL_YEAR = "BBBBBBBBB";

    private static final Long DEFAULT_LAST_NUMBER = 0L;
    private static final Long UPDATED_LAST_NUMBER = 1L;

    private static final String ENTITY_API_URL = "/api/invoice-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceSequenceRepository invoiceSequenceRepository;

    @Autowired
    private InvoiceSequenceMapper invoiceSequenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceSequenceMockMvc;

    private InvoiceSequence invoiceSequence;

    private InvoiceSequence insertedInvoiceSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceSequence createEntity() {
        return new InvoiceSequence().series(DEFAULT_SERIES).fiscalYear(DEFAULT_FISCAL_YEAR).lastNumber(DEFAULT_LAST_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceSequence createUpdatedEntity() {
        return new InvoiceSequence().series(UPDATED_SERIES).fiscalYear(UPDATED_FISCAL_YEAR).lastNumber(UPDATED_LAST_NUMBER);
    }

    @BeforeEach
    void initTest() {
        invoiceSequence = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceSequence != null) {
            invoiceSequenceRepository.delete(insertedInvoiceSequence);
            insertedInvoiceSequence = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceSequence() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);
        var returnedInvoiceSequenceDTO = om.readValue(
            restInvoiceSequenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceSequenceDTO.class
        );

        // Validate the InvoiceSequence in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceSequence = invoiceSequenceMapper.toEntity(returnedInvoiceSequenceDTO);
        assertInvoiceSequenceUpdatableFieldsEquals(returnedInvoiceSequence, getPersistedInvoiceSequence(returnedInvoiceSequence));

        insertedInvoiceSequence = returnedInvoiceSequence;
    }

    @Test
    @Transactional
    void createInvoiceSequenceWithExistingId() throws Exception {
        // Create the InvoiceSequence with an existing ID
        invoiceSequence.setId(1L);
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSeriesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSequence.setSeries(null);

        // Create the InvoiceSequence, which fails.
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        restInvoiceSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiscalYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSequence.setFiscalYear(null);

        // Create the InvoiceSequence, which fails.
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        restInvoiceSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceSequence.setLastNumber(null);

        // Create the InvoiceSequence, which fails.
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        restInvoiceSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceSequences() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        // Get all the invoiceSequenceList
        restInvoiceSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)))
            .andExpect(jsonPath("$.[*].fiscalYear").value(hasItem(DEFAULT_FISCAL_YEAR)))
            .andExpect(jsonPath("$.[*].lastNumber").value(hasItem(DEFAULT_LAST_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getInvoiceSequence() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        // Get the invoiceSequence
        restInvoiceSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceSequence.getId().intValue()))
            .andExpect(jsonPath("$.series").value(DEFAULT_SERIES))
            .andExpect(jsonPath("$.fiscalYear").value(DEFAULT_FISCAL_YEAR))
            .andExpect(jsonPath("$.lastNumber").value(DEFAULT_LAST_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceSequence() throws Exception {
        // Get the invoiceSequence
        restInvoiceSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceSequence() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSequence
        InvoiceSequence updatedInvoiceSequence = invoiceSequenceRepository.findById(invoiceSequence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceSequence are not directly saved in db
        em.detach(updatedInvoiceSequence);
        updatedInvoiceSequence.series(UPDATED_SERIES).fiscalYear(UPDATED_FISCAL_YEAR).lastNumber(UPDATED_LAST_NUMBER);
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(updatedInvoiceSequence);

        restInvoiceSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSequenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceSequenceToMatchAllProperties(updatedInvoiceSequence);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceSequenceWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSequence using partial update
        InvoiceSequence partialUpdatedInvoiceSequence = new InvoiceSequence();
        partialUpdatedInvoiceSequence.setId(invoiceSequence.getId());

        restInvoiceSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceSequence))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSequence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceSequenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceSequence, invoiceSequence),
            getPersistedInvoiceSequence(invoiceSequence)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceSequenceWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceSequence using partial update
        InvoiceSequence partialUpdatedInvoiceSequence = new InvoiceSequence();
        partialUpdatedInvoiceSequence.setId(invoiceSequence.getId());

        partialUpdatedInvoiceSequence.series(UPDATED_SERIES).fiscalYear(UPDATED_FISCAL_YEAR).lastNumber(UPDATED_LAST_NUMBER);

        restInvoiceSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceSequence))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSequence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceSequenceUpdatableFieldsEquals(
            partialUpdatedInvoiceSequence,
            getPersistedInvoiceSequence(partialUpdatedInvoiceSequence)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceSequenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceSequence.setId(longCount.incrementAndGet());

        // Create the InvoiceSequence
        InvoiceSequenceDTO invoiceSequenceDTO = invoiceSequenceMapper.toDto(invoiceSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSequenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceSequenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceSequence() throws Exception {
        // Initialize the database
        insertedInvoiceSequence = invoiceSequenceRepository.saveAndFlush(invoiceSequence);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceSequence
        restInvoiceSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceSequenceRepository.count();
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

    protected InvoiceSequence getPersistedInvoiceSequence(InvoiceSequence invoiceSequence) {
        return invoiceSequenceRepository.findById(invoiceSequence.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceSequenceToMatchAllProperties(InvoiceSequence expectedInvoiceSequence) {
        assertInvoiceSequenceAllPropertiesEquals(expectedInvoiceSequence, getPersistedInvoiceSequence(expectedInvoiceSequence));
    }

    protected void assertPersistedInvoiceSequenceToMatchUpdatableProperties(InvoiceSequence expectedInvoiceSequence) {
        assertInvoiceSequenceAllUpdatablePropertiesEquals(expectedInvoiceSequence, getPersistedInvoiceSequence(expectedInvoiceSequence));
    }
}
