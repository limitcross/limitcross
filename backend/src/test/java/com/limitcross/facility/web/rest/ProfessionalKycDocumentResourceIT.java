package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalKycDocumentAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalKycDocument;
import com.limitcross.facility.domain.enumeration.KycDocType;
import com.limitcross.facility.domain.enumeration.VerificationStatus;
import com.limitcross.facility.repository.ProfessionalKycDocumentRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ProfessionalKycDocumentService;
import com.limitcross.facility.service.dto.ProfessionalKycDocumentDTO;
import com.limitcross.facility.service.mapper.ProfessionalKycDocumentMapper;
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
 * Integration tests for the {@link ProfessionalKycDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalKycDocumentResourceIT {

    private static final KycDocType DEFAULT_DOC_TYPE = KycDocType.AADHAAR;
    private static final KycDocType UPDATED_DOC_TYPE = KycDocType.PAN;

    private static final String DEFAULT_DOC_NUMBER_ENC = "AAAAAAAAAA";
    private static final String UPDATED_DOC_NUMBER_ENC = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final VerificationStatus DEFAULT_STATUS = VerificationStatus.PENDING;
    private static final VerificationStatus UPDATED_STATUS = VerificationStatus.VERIFIED;

    private static final Instant DEFAULT_REVIEWED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEWED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REJECT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECT_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professional-kyc-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalKycDocumentRepository professionalKycDocumentRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ProfessionalKycDocumentRepository professionalKycDocumentRepositoryMock;

    @Autowired
    private ProfessionalKycDocumentMapper professionalKycDocumentMapper;

    @Mock
    private ProfessionalKycDocumentService professionalKycDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalKycDocumentMockMvc;

    private ProfessionalKycDocument professionalKycDocument;

    private ProfessionalKycDocument insertedProfessionalKycDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalKycDocument createEntity(EntityManager em) {
        ProfessionalKycDocument professionalKycDocument = new ProfessionalKycDocument()
            .docType(DEFAULT_DOC_TYPE)
            .docNumberEnc(DEFAULT_DOC_NUMBER_ENC)
            .fileUrl(DEFAULT_FILE_URL)
            .status(DEFAULT_STATUS)
            .reviewedAt(DEFAULT_REVIEWED_AT)
            .rejectReason(DEFAULT_REJECT_REASON)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalKycDocument.setProfessional(professional);
        return professionalKycDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalKycDocument createUpdatedEntity(EntityManager em) {
        ProfessionalKycDocument updatedProfessionalKycDocument = new ProfessionalKycDocument()
            .docType(UPDATED_DOC_TYPE)
            .docNumberEnc(UPDATED_DOC_NUMBER_ENC)
            .fileUrl(UPDATED_FILE_URL)
            .status(UPDATED_STATUS)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .rejectReason(UPDATED_REJECT_REASON)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalKycDocument.setProfessional(professional);
        return updatedProfessionalKycDocument;
    }

    @BeforeEach
    void initTest() {
        professionalKycDocument = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalKycDocument != null) {
            professionalKycDocumentRepository.delete(insertedProfessionalKycDocument);
            insertedProfessionalKycDocument = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);
        var returnedProfessionalKycDocumentDTO = om.readValue(
            restProfessionalKycDocumentMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalKycDocumentDTO.class
        );

        // Validate the ProfessionalKycDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalKycDocument = professionalKycDocumentMapper.toEntity(returnedProfessionalKycDocumentDTO);
        assertProfessionalKycDocumentUpdatableFieldsEquals(
            returnedProfessionalKycDocument,
            getPersistedProfessionalKycDocument(returnedProfessionalKycDocument)
        );

        insertedProfessionalKycDocument = returnedProfessionalKycDocument;
    }

    @Test
    @Transactional
    void createProfessionalKycDocumentWithExistingId() throws Exception {
        // Create the ProfessionalKycDocument with an existing ID
        professionalKycDocument.setId(1L);
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalKycDocument.setDocType(null);

        // Create the ProfessionalKycDocument, which fails.
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        restProfessionalKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalKycDocument.setFileUrl(null);

        // Create the ProfessionalKycDocument, which fails.
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        restProfessionalKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalKycDocument.setStatus(null);

        // Create the ProfessionalKycDocument, which fails.
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        restProfessionalKycDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalKycDocuments() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        // Get all the professionalKycDocumentList
        restProfessionalKycDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalKycDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].docNumberEnc").value(hasItem(DEFAULT_DOC_NUMBER_ENC)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(DEFAULT_REVIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectReason").value(hasItem(DEFAULT_REJECT_REASON)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalKycDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalKycDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalKycDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalKycDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalKycDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalKycDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalKycDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalKycDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalKycDocument() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        // Get the professionalKycDocument
        restProfessionalKycDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalKycDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalKycDocument.getId().intValue()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.docNumberEnc").value(DEFAULT_DOC_NUMBER_ENC))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.reviewedAt").value(DEFAULT_REVIEWED_AT.toString()))
            .andExpect(jsonPath("$.rejectReason").value(DEFAULT_REJECT_REASON))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalKycDocument() throws Exception {
        // Get the professionalKycDocument
        restProfessionalKycDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalKycDocument() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalKycDocument
        ProfessionalKycDocument updatedProfessionalKycDocument = professionalKycDocumentRepository
            .findById(professionalKycDocument.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalKycDocument are not directly saved in db
        em.detach(updatedProfessionalKycDocument);
        updatedProfessionalKycDocument
            .docType(UPDATED_DOC_TYPE)
            .docNumberEnc(UPDATED_DOC_NUMBER_ENC)
            .fileUrl(UPDATED_FILE_URL)
            .status(UPDATED_STATUS)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .rejectReason(UPDATED_REJECT_REASON)
            .createdAt(UPDATED_CREATED_AT);
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(updatedProfessionalKycDocument);

        restProfessionalKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalKycDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalKycDocumentToMatchAllProperties(updatedProfessionalKycDocument);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalKycDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalKycDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalKycDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalKycDocument using partial update
        ProfessionalKycDocument partialUpdatedProfessionalKycDocument = new ProfessionalKycDocument();
        partialUpdatedProfessionalKycDocument.setId(professionalKycDocument.getId());

        partialUpdatedProfessionalKycDocument
            .docType(UPDATED_DOC_TYPE)
            .docNumberEnc(UPDATED_DOC_NUMBER_ENC)
            .status(UPDATED_STATUS)
            .rejectReason(UPDATED_REJECT_REASON);

        restProfessionalKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalKycDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalKycDocument))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalKycDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalKycDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalKycDocument, professionalKycDocument),
            getPersistedProfessionalKycDocument(professionalKycDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalKycDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalKycDocument using partial update
        ProfessionalKycDocument partialUpdatedProfessionalKycDocument = new ProfessionalKycDocument();
        partialUpdatedProfessionalKycDocument.setId(professionalKycDocument.getId());

        partialUpdatedProfessionalKycDocument
            .docType(UPDATED_DOC_TYPE)
            .docNumberEnc(UPDATED_DOC_NUMBER_ENC)
            .fileUrl(UPDATED_FILE_URL)
            .status(UPDATED_STATUS)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .rejectReason(UPDATED_REJECT_REASON)
            .createdAt(UPDATED_CREATED_AT);

        restProfessionalKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalKycDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalKycDocument))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalKycDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalKycDocumentUpdatableFieldsEquals(
            partialUpdatedProfessionalKycDocument,
            getPersistedProfessionalKycDocument(partialUpdatedProfessionalKycDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalKycDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalKycDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalKycDocument.setId(longCount.incrementAndGet());

        // Create the ProfessionalKycDocument
        ProfessionalKycDocumentDTO professionalKycDocumentDTO = professionalKycDocumentMapper.toDto(professionalKycDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalKycDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalKycDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalKycDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalKycDocument() throws Exception {
        // Initialize the database
        insertedProfessionalKycDocument = professionalKycDocumentRepository.saveAndFlush(professionalKycDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalKycDocument
        restProfessionalKycDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalKycDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalKycDocumentRepository.count();
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

    protected ProfessionalKycDocument getPersistedProfessionalKycDocument(ProfessionalKycDocument professionalKycDocument) {
        return professionalKycDocumentRepository.findById(professionalKycDocument.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalKycDocumentToMatchAllProperties(ProfessionalKycDocument expectedProfessionalKycDocument) {
        assertProfessionalKycDocumentAllPropertiesEquals(
            expectedProfessionalKycDocument,
            getPersistedProfessionalKycDocument(expectedProfessionalKycDocument)
        );
    }

    protected void assertPersistedProfessionalKycDocumentToMatchUpdatableProperties(
        ProfessionalKycDocument expectedProfessionalKycDocument
    ) {
        assertProfessionalKycDocumentAllUpdatablePropertiesEquals(
            expectedProfessionalKycDocument,
            getPersistedProfessionalKycDocument(expectedProfessionalKycDocument)
        );
    }
}
