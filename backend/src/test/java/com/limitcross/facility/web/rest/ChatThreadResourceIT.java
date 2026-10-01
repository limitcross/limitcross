package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ChatThreadAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.ChatThread;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.ChatThreadStatus;
import com.limitcross.facility.repository.ChatThreadRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ChatThreadService;
import com.limitcross.facility.service.dto.ChatThreadDTO;
import com.limitcross.facility.service.mapper.ChatThreadMapper;
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
 * Integration tests for the {@link ChatThreadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChatThreadResourceIT {

    private static final ChatThreadStatus DEFAULT_STATUS = ChatThreadStatus.OPEN;
    private static final ChatThreadStatus UPDATED_STATUS = ChatThreadStatus.CLOSED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chat-threads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatThreadRepository chatThreadRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ChatThreadRepository chatThreadRepositoryMock;

    @Autowired
    private ChatThreadMapper chatThreadMapper;

    @Mock
    private ChatThreadService chatThreadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatThreadMockMvc;

    private ChatThread chatThread;

    private ChatThread insertedChatThread;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatThread createEntity(EntityManager em) {
        ChatThread chatThread = new ChatThread().status(DEFAULT_STATUS).createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        chatThread.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        chatThread.setCustomer(user);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        chatThread.setProfessional(professional);
        return chatThread;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatThread createUpdatedEntity(EntityManager em) {
        ChatThread updatedChatThread = new ChatThread().status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedChatThread.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedChatThread.setCustomer(user);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedChatThread.setProfessional(professional);
        return updatedChatThread;
    }

    @BeforeEach
    void initTest() {
        chatThread = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedChatThread != null) {
            chatThreadRepository.delete(insertedChatThread);
            insertedChatThread = null;
        }
    }

    @Test
    @Transactional
    void createChatThread() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);
        var returnedChatThreadDTO = om.readValue(
            restChatThreadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatThreadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatThreadDTO.class
        );

        // Validate the ChatThread in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatThread = chatThreadMapper.toEntity(returnedChatThreadDTO);
        assertChatThreadUpdatableFieldsEquals(returnedChatThread, getPersistedChatThread(returnedChatThread));

        insertedChatThread = returnedChatThread;
    }

    @Test
    @Transactional
    void createChatThreadWithExistingId() throws Exception {
        // Create the ChatThread with an existing ID
        chatThread.setId(1L);
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatThreadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatThreadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chatThread.setStatus(null);

        // Create the ChatThread, which fails.
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        restChatThreadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatThreadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChatThreads() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        // Get all the chatThreadList
        restChatThreadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatThread.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChatThreadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(chatThreadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatThreadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(chatThreadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChatThreadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(chatThreadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChatThreadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(chatThreadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getChatThread() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        // Get the chatThread
        restChatThreadMockMvc
            .perform(get(ENTITY_API_URL_ID, chatThread.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatThread.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChatThread() throws Exception {
        // Get the chatThread
        restChatThreadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatThread() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatThread
        ChatThread updatedChatThread = chatThreadRepository.findById(chatThread.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatThread are not directly saved in db
        em.detach(updatedChatThread);
        updatedChatThread.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(updatedChatThread);

        restChatThreadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatThreadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatThreadDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatThreadToMatchAllProperties(updatedChatThread);
    }

    @Test
    @Transactional
    void putNonExistingChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatThreadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatThreadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatThreadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatThreadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatThreadWithPatch() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatThread using partial update
        ChatThread partialUpdatedChatThread = new ChatThread();
        partialUpdatedChatThread.setId(chatThread.getId());

        restChatThreadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatThread.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatThread))
            )
            .andExpect(status().isOk());

        // Validate the ChatThread in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatThreadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChatThread, chatThread),
            getPersistedChatThread(chatThread)
        );
    }

    @Test
    @Transactional
    void fullUpdateChatThreadWithPatch() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatThread using partial update
        ChatThread partialUpdatedChatThread = new ChatThread();
        partialUpdatedChatThread.setId(chatThread.getId());

        partialUpdatedChatThread.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);

        restChatThreadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatThread.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatThread))
            )
            .andExpect(status().isOk());

        // Validate the ChatThread in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatThreadUpdatableFieldsEquals(partialUpdatedChatThread, getPersistedChatThread(partialUpdatedChatThread));
    }

    @Test
    @Transactional
    void patchNonExistingChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatThreadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatThreadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatThreadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatThread() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatThread.setId(longCount.incrementAndGet());

        // Create the ChatThread
        ChatThreadDTO chatThreadDTO = chatThreadMapper.toDto(chatThread);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatThreadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatThreadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatThread in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatThread() throws Exception {
        // Initialize the database
        insertedChatThread = chatThreadRepository.saveAndFlush(chatThread);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatThread
        restChatThreadMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatThread.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatThreadRepository.count();
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

    protected ChatThread getPersistedChatThread(ChatThread chatThread) {
        return chatThreadRepository.findById(chatThread.getId()).orElseThrow();
    }

    protected void assertPersistedChatThreadToMatchAllProperties(ChatThread expectedChatThread) {
        assertChatThreadAllPropertiesEquals(expectedChatThread, getPersistedChatThread(expectedChatThread));
    }

    protected void assertPersistedChatThreadToMatchUpdatableProperties(ChatThread expectedChatThread) {
        assertChatThreadAllUpdatablePropertiesEquals(expectedChatThread, getPersistedChatThread(expectedChatThread));
    }
}
