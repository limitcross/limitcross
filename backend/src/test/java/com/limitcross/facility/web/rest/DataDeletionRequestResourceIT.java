package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.DataDeletionRequestAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.DataDeletionRequest;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.DeletionStatus;
import com.limitcross.facility.repository.DataDeletionRequestRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.DataDeletionRequestService;
import com.limitcross.facility.service.dto.DataDeletionRequestDTO;
import com.limitcross.facility.service.mapper.DataDeletionRequestMapper;
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
 * Integration tests for the {@link DataDeletionRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DataDeletionRequestResourceIT {

    private static final DeletionStatus DEFAULT_STATUS = DeletionStatus.REQUESTED;
    private static final DeletionStatus UPDATED_STATUS = DeletionStatus.IN_PROGRESS;

    private static final Instant DEFAULT_REQUESTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-deletion-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DataDeletionRequestRepository dataDeletionRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DataDeletionRequestRepository dataDeletionRequestRepositoryMock;

    @Autowired
    private DataDeletionRequestMapper dataDeletionRequestMapper;

    @Mock
    private DataDeletionRequestService dataDeletionRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataDeletionRequestMockMvc;

    private DataDeletionRequest dataDeletionRequest;

    private DataDeletionRequest insertedDataDeletionRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDeletionRequest createEntity(EntityManager em) {
        DataDeletionRequest dataDeletionRequest = new DataDeletionRequest()
            .status(DEFAULT_STATUS)
            .requestedAt(DEFAULT_REQUESTED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .note(DEFAULT_NOTE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        dataDeletionRequest.setUser(user);
        return dataDeletionRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDeletionRequest createUpdatedEntity(EntityManager em) {
        DataDeletionRequest updatedDataDeletionRequest = new DataDeletionRequest()
            .status(UPDATED_STATUS)
            .requestedAt(UPDATED_REQUESTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .note(UPDATED_NOTE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDataDeletionRequest.setUser(user);
        return updatedDataDeletionRequest;
    }

    @BeforeEach
    void initTest() {
        dataDeletionRequest = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDataDeletionRequest != null) {
            dataDeletionRequestRepository.delete(insertedDataDeletionRequest);
            insertedDataDeletionRequest = null;
        }
    }

    @Test
    @Transactional
    void createDataDeletionRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);
        var returnedDataDeletionRequestDTO = om.readValue(
            restDataDeletionRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataDeletionRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DataDeletionRequestDTO.class
        );

        // Validate the DataDeletionRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDataDeletionRequest = dataDeletionRequestMapper.toEntity(returnedDataDeletionRequestDTO);
        assertDataDeletionRequestUpdatableFieldsEquals(
            returnedDataDeletionRequest,
            getPersistedDataDeletionRequest(returnedDataDeletionRequest)
        );

        insertedDataDeletionRequest = returnedDataDeletionRequest;
    }

    @Test
    @Transactional
    void createDataDeletionRequestWithExistingId() throws Exception {
        // Create the DataDeletionRequest with an existing ID
        dataDeletionRequest.setId(1L);
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataDeletionRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataDeletionRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataDeletionRequest.setStatus(null);

        // Create the DataDeletionRequest, which fails.
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        restDataDeletionRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataDeletionRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataDeletionRequests() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        // Get all the dataDeletionRequestList
        restDataDeletionRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataDeletionRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestedAt").value(hasItem(DEFAULT_REQUESTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataDeletionRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(dataDeletionRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataDeletionRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dataDeletionRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDataDeletionRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dataDeletionRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDataDeletionRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dataDeletionRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDataDeletionRequest() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        // Get the dataDeletionRequest
        restDataDeletionRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, dataDeletionRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataDeletionRequest.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.requestedAt").value(DEFAULT_REQUESTED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingDataDeletionRequest() throws Exception {
        // Get the dataDeletionRequest
        restDataDeletionRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDataDeletionRequest() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataDeletionRequest
        DataDeletionRequest updatedDataDeletionRequest = dataDeletionRequestRepository.findById(dataDeletionRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDataDeletionRequest are not directly saved in db
        em.detach(updatedDataDeletionRequest);
        updatedDataDeletionRequest
            .status(UPDATED_STATUS)
            .requestedAt(UPDATED_REQUESTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .note(UPDATED_NOTE);
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(updatedDataDeletionRequest);

        restDataDeletionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDeletionRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDataDeletionRequestToMatchAllProperties(updatedDataDeletionRequest);
    }

    @Test
    @Transactional
    void putNonExistingDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDeletionRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataDeletionRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataDeletionRequestWithPatch() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataDeletionRequest using partial update
        DataDeletionRequest partialUpdatedDataDeletionRequest = new DataDeletionRequest();
        partialUpdatedDataDeletionRequest.setId(dataDeletionRequest.getId());

        partialUpdatedDataDeletionRequest.status(UPDATED_STATUS).note(UPDATED_NOTE);

        restDataDeletionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDeletionRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataDeletionRequest))
            )
            .andExpect(status().isOk());

        // Validate the DataDeletionRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataDeletionRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDataDeletionRequest, dataDeletionRequest),
            getPersistedDataDeletionRequest(dataDeletionRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateDataDeletionRequestWithPatch() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataDeletionRequest using partial update
        DataDeletionRequest partialUpdatedDataDeletionRequest = new DataDeletionRequest();
        partialUpdatedDataDeletionRequest.setId(dataDeletionRequest.getId());

        partialUpdatedDataDeletionRequest
            .status(UPDATED_STATUS)
            .requestedAt(UPDATED_REQUESTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .note(UPDATED_NOTE);

        restDataDeletionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDeletionRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataDeletionRequest))
            )
            .andExpect(status().isOk());

        // Validate the DataDeletionRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataDeletionRequestUpdatableFieldsEquals(
            partialUpdatedDataDeletionRequest,
            getPersistedDataDeletionRequest(partialUpdatedDataDeletionRequest)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataDeletionRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataDeletionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataDeletionRequest.setId(longCount.incrementAndGet());

        // Create the DataDeletionRequest
        DataDeletionRequestDTO dataDeletionRequestDTO = dataDeletionRequestMapper.toDto(dataDeletionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDeletionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dataDeletionRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDeletionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataDeletionRequest() throws Exception {
        // Initialize the database
        insertedDataDeletionRequest = dataDeletionRequestRepository.saveAndFlush(dataDeletionRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dataDeletionRequest
        restDataDeletionRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataDeletionRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dataDeletionRequestRepository.count();
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

    protected DataDeletionRequest getPersistedDataDeletionRequest(DataDeletionRequest dataDeletionRequest) {
        return dataDeletionRequestRepository.findById(dataDeletionRequest.getId()).orElseThrow();
    }

    protected void assertPersistedDataDeletionRequestToMatchAllProperties(DataDeletionRequest expectedDataDeletionRequest) {
        assertDataDeletionRequestAllPropertiesEquals(
            expectedDataDeletionRequest,
            getPersistedDataDeletionRequest(expectedDataDeletionRequest)
        );
    }

    protected void assertPersistedDataDeletionRequestToMatchUpdatableProperties(DataDeletionRequest expectedDataDeletionRequest) {
        assertDataDeletionRequestAllUpdatablePropertiesEquals(
            expectedDataDeletionRequest,
            getPersistedDataDeletionRequest(expectedDataDeletionRequest)
        );
    }
}
