package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CallSessionAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.CallSession;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.CallStatus;
import com.limitcross.facility.repository.CallSessionRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.CallSessionService;
import com.limitcross.facility.service.dto.CallSessionDTO;
import com.limitcross.facility.service.mapper.CallSessionMapper;
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
 * Integration tests for the {@link CallSessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CallSessionResourceIT {

    private static final String DEFAULT_VIRTUAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VIRTUAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER_CALL_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_CALL_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION_SEC = 0;
    private static final Integer UPDATED_DURATION_SEC = 1;

    private static final CallStatus DEFAULT_STATUS = CallStatus.INITIATED;
    private static final CallStatus UPDATED_STATUS = CallStatus.CONNECTED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/call-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CallSessionRepository callSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CallSessionRepository callSessionRepositoryMock;

    @Autowired
    private CallSessionMapper callSessionMapper;

    @Mock
    private CallSessionService callSessionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCallSessionMockMvc;

    private CallSession callSession;

    private CallSession insertedCallSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CallSession createEntity(EntityManager em) {
        CallSession callSession = new CallSession()
            .virtualNumber(DEFAULT_VIRTUAL_NUMBER)
            .providerCallId(DEFAULT_PROVIDER_CALL_ID)
            .durationSec(DEFAULT_DURATION_SEC)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        callSession.setCaller(user);
        // Add required entity
        callSession.setCallee(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        callSession.setBooking(booking);
        return callSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CallSession createUpdatedEntity(EntityManager em) {
        CallSession updatedCallSession = new CallSession()
            .virtualNumber(UPDATED_VIRTUAL_NUMBER)
            .providerCallId(UPDATED_PROVIDER_CALL_ID)
            .durationSec(UPDATED_DURATION_SEC)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCallSession.setCaller(user);
        // Add required entity
        updatedCallSession.setCallee(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedCallSession.setBooking(booking);
        return updatedCallSession;
    }

    @BeforeEach
    void initTest() {
        callSession = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCallSession != null) {
            callSessionRepository.delete(insertedCallSession);
            insertedCallSession = null;
        }
    }

    @Test
    @Transactional
    void createCallSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);
        var returnedCallSessionDTO = om.readValue(
            restCallSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(callSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CallSessionDTO.class
        );

        // Validate the CallSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCallSession = callSessionMapper.toEntity(returnedCallSessionDTO);
        assertCallSessionUpdatableFieldsEquals(returnedCallSession, getPersistedCallSession(returnedCallSession));

        insertedCallSession = returnedCallSession;
    }

    @Test
    @Transactional
    void createCallSessionWithExistingId() throws Exception {
        // Create the CallSession with an existing ID
        callSession.setId(1L);
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCallSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(callSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVirtualNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        callSession.setVirtualNumber(null);

        // Create the CallSession, which fails.
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        restCallSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(callSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        callSession.setStatus(null);

        // Create the CallSession, which fails.
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        restCallSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(callSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCallSessions() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        // Get all the callSessionList
        restCallSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(callSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].virtualNumber").value(hasItem(DEFAULT_VIRTUAL_NUMBER)))
            .andExpect(jsonPath("$.[*].providerCallId").value(hasItem(DEFAULT_PROVIDER_CALL_ID)))
            .andExpect(jsonPath("$.[*].durationSec").value(hasItem(DEFAULT_DURATION_SEC)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCallSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(callSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCallSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(callSessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCallSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(callSessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCallSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(callSessionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCallSession() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        // Get the callSession
        restCallSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, callSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(callSession.getId().intValue()))
            .andExpect(jsonPath("$.virtualNumber").value(DEFAULT_VIRTUAL_NUMBER))
            .andExpect(jsonPath("$.providerCallId").value(DEFAULT_PROVIDER_CALL_ID))
            .andExpect(jsonPath("$.durationSec").value(DEFAULT_DURATION_SEC))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCallSession() throws Exception {
        // Get the callSession
        restCallSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCallSession() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the callSession
        CallSession updatedCallSession = callSessionRepository.findById(callSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCallSession are not directly saved in db
        em.detach(updatedCallSession);
        updatedCallSession
            .virtualNumber(UPDATED_VIRTUAL_NUMBER)
            .providerCallId(UPDATED_PROVIDER_CALL_ID)
            .durationSec(UPDATED_DURATION_SEC)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(updatedCallSession);

        restCallSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, callSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(callSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCallSessionToMatchAllProperties(updatedCallSession);
    }

    @Test
    @Transactional
    void putNonExistingCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, callSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(callSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(callSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(callSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCallSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the callSession using partial update
        CallSession partialUpdatedCallSession = new CallSession();
        partialUpdatedCallSession.setId(callSession.getId());

        partialUpdatedCallSession.virtualNumber(UPDATED_VIRTUAL_NUMBER).status(UPDATED_STATUS);

        restCallSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCallSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCallSession))
            )
            .andExpect(status().isOk());

        // Validate the CallSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCallSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCallSession, callSession),
            getPersistedCallSession(callSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateCallSessionWithPatch() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the callSession using partial update
        CallSession partialUpdatedCallSession = new CallSession();
        partialUpdatedCallSession.setId(callSession.getId());

        partialUpdatedCallSession
            .virtualNumber(UPDATED_VIRTUAL_NUMBER)
            .providerCallId(UPDATED_PROVIDER_CALL_ID)
            .durationSec(UPDATED_DURATION_SEC)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);

        restCallSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCallSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCallSession))
            )
            .andExpect(status().isOk());

        // Validate the CallSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCallSessionUpdatableFieldsEquals(partialUpdatedCallSession, getPersistedCallSession(partialUpdatedCallSession));
    }

    @Test
    @Transactional
    void patchNonExistingCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, callSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(callSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(callSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCallSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        callSession.setId(longCount.incrementAndGet());

        // Create the CallSession
        CallSessionDTO callSessionDTO = callSessionMapper.toDto(callSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(callSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CallSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCallSession() throws Exception {
        // Initialize the database
        insertedCallSession = callSessionRepository.saveAndFlush(callSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the callSession
        restCallSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, callSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return callSessionRepository.count();
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

    protected CallSession getPersistedCallSession(CallSession callSession) {
        return callSessionRepository.findById(callSession.getId()).orElseThrow();
    }

    protected void assertPersistedCallSessionToMatchAllProperties(CallSession expectedCallSession) {
        assertCallSessionAllPropertiesEquals(expectedCallSession, getPersistedCallSession(expectedCallSession));
    }

    protected void assertPersistedCallSessionToMatchUpdatableProperties(CallSession expectedCallSession) {
        assertCallSessionAllUpdatablePropertiesEquals(expectedCallSession, getPersistedCallSession(expectedCallSession));
    }
}
