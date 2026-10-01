package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingStatusHistoryAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingStatusHistory;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.repository.BookingStatusHistoryRepository;
import com.limitcross.facility.service.BookingStatusHistoryService;
import com.limitcross.facility.service.dto.BookingStatusHistoryDTO;
import com.limitcross.facility.service.mapper.BookingStatusHistoryMapper;
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
 * Integration tests for the {@link BookingStatusHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingStatusHistoryResourceIT {

    private static final BookingStatus DEFAULT_FROM_STATUS = BookingStatus.REQUESTED;
    private static final BookingStatus UPDATED_FROM_STATUS = BookingStatus.PENDING_ASSIGNMENT;

    private static final BookingStatus DEFAULT_TO_STATUS = BookingStatus.REQUESTED;
    private static final BookingStatus UPDATED_TO_STATUS = BookingStatus.PENDING_ASSIGNMENT;

    private static final String DEFAULT_CHANGED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CHANGED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/booking-status-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingStatusHistoryRepository bookingStatusHistoryRepository;

    @Mock
    private BookingStatusHistoryRepository bookingStatusHistoryRepositoryMock;

    @Autowired
    private BookingStatusHistoryMapper bookingStatusHistoryMapper;

    @Mock
    private BookingStatusHistoryService bookingStatusHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingStatusHistoryMockMvc;

    private BookingStatusHistory bookingStatusHistory;

    private BookingStatusHistory insertedBookingStatusHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingStatusHistory createEntity(EntityManager em) {
        BookingStatusHistory bookingStatusHistory = new BookingStatusHistory()
            .fromStatus(DEFAULT_FROM_STATUS)
            .toStatus(DEFAULT_TO_STATUS)
            .changedBy(DEFAULT_CHANGED_BY)
            .note(DEFAULT_NOTE)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        bookingStatusHistory.setBooking(booking);
        return bookingStatusHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingStatusHistory createUpdatedEntity(EntityManager em) {
        BookingStatusHistory updatedBookingStatusHistory = new BookingStatusHistory()
            .fromStatus(UPDATED_FROM_STATUS)
            .toStatus(UPDATED_TO_STATUS)
            .changedBy(UPDATED_CHANGED_BY)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedBookingStatusHistory.setBooking(booking);
        return updatedBookingStatusHistory;
    }

    @BeforeEach
    void initTest() {
        bookingStatusHistory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingStatusHistory != null) {
            bookingStatusHistoryRepository.delete(insertedBookingStatusHistory);
            insertedBookingStatusHistory = null;
        }
    }

    @Test
    @Transactional
    void createBookingStatusHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);
        var returnedBookingStatusHistoryDTO = om.readValue(
            restBookingStatusHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingStatusHistoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingStatusHistoryDTO.class
        );

        // Validate the BookingStatusHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingStatusHistory = bookingStatusHistoryMapper.toEntity(returnedBookingStatusHistoryDTO);
        assertBookingStatusHistoryUpdatableFieldsEquals(
            returnedBookingStatusHistory,
            getPersistedBookingStatusHistory(returnedBookingStatusHistory)
        );

        insertedBookingStatusHistory = returnedBookingStatusHistory;
    }

    @Test
    @Transactional
    void createBookingStatusHistoryWithExistingId() throws Exception {
        // Create the BookingStatusHistory with an existing ID
        bookingStatusHistory.setId(1L);
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingStatusHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingStatusHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkToStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingStatusHistory.setToStatus(null);

        // Create the BookingStatusHistory, which fails.
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        restBookingStatusHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingStatusHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingStatusHistory.setCreatedAt(null);

        // Create the BookingStatusHistory, which fails.
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        restBookingStatusHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingStatusHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingStatusHistories() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        // Get all the bookingStatusHistoryList
        restBookingStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingStatusHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromStatus").value(hasItem(DEFAULT_FROM_STATUS.toString())))
            .andExpect(jsonPath("$.[*].toStatus").value(hasItem(DEFAULT_TO_STATUS.toString())))
            .andExpect(jsonPath("$.[*].changedBy").value(hasItem(DEFAULT_CHANGED_BY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingStatusHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingStatusHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingStatusHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingStatusHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingStatusHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingStatusHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingStatusHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingStatusHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingStatusHistory() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        // Get the bookingStatusHistory
        restBookingStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingStatusHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingStatusHistory.getId().intValue()))
            .andExpect(jsonPath("$.fromStatus").value(DEFAULT_FROM_STATUS.toString()))
            .andExpect(jsonPath("$.toStatus").value(DEFAULT_TO_STATUS.toString()))
            .andExpect(jsonPath("$.changedBy").value(DEFAULT_CHANGED_BY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookingStatusHistory() throws Exception {
        // Get the bookingStatusHistory
        restBookingStatusHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingStatusHistory() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingStatusHistory
        BookingStatusHistory updatedBookingStatusHistory = bookingStatusHistoryRepository
            .findById(bookingStatusHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedBookingStatusHistory are not directly saved in db
        em.detach(updatedBookingStatusHistory);
        updatedBookingStatusHistory
            .fromStatus(UPDATED_FROM_STATUS)
            .toStatus(UPDATED_TO_STATUS)
            .changedBy(UPDATED_CHANGED_BY)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(updatedBookingStatusHistory);

        restBookingStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingStatusHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingStatusHistoryToMatchAllProperties(updatedBookingStatusHistory);
    }

    @Test
    @Transactional
    void putNonExistingBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingStatusHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingStatusHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingStatusHistory using partial update
        BookingStatusHistory partialUpdatedBookingStatusHistory = new BookingStatusHistory();
        partialUpdatedBookingStatusHistory.setId(bookingStatusHistory.getId());

        partialUpdatedBookingStatusHistory.fromStatus(UPDATED_FROM_STATUS).createdAt(UPDATED_CREATED_AT);

        restBookingStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingStatusHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the BookingStatusHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingStatusHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingStatusHistory, bookingStatusHistory),
            getPersistedBookingStatusHistory(bookingStatusHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingStatusHistory using partial update
        BookingStatusHistory partialUpdatedBookingStatusHistory = new BookingStatusHistory();
        partialUpdatedBookingStatusHistory.setId(bookingStatusHistory.getId());

        partialUpdatedBookingStatusHistory
            .fromStatus(UPDATED_FROM_STATUS)
            .toStatus(UPDATED_TO_STATUS)
            .changedBy(UPDATED_CHANGED_BY)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);

        restBookingStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingStatusHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingStatusHistory))
            )
            .andExpect(status().isOk());

        // Validate the BookingStatusHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingStatusHistoryUpdatableFieldsEquals(
            partialUpdatedBookingStatusHistory,
            getPersistedBookingStatusHistory(partialUpdatedBookingStatusHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingStatusHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingStatusHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingStatusHistory.setId(longCount.incrementAndGet());

        // Create the BookingStatusHistory
        BookingStatusHistoryDTO bookingStatusHistoryDTO = bookingStatusHistoryMapper.toDto(bookingStatusHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingStatusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingStatusHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingStatusHistory() throws Exception {
        // Initialize the database
        insertedBookingStatusHistory = bookingStatusHistoryRepository.saveAndFlush(bookingStatusHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingStatusHistory
        restBookingStatusHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingStatusHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingStatusHistoryRepository.count();
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

    protected BookingStatusHistory getPersistedBookingStatusHistory(BookingStatusHistory bookingStatusHistory) {
        return bookingStatusHistoryRepository.findById(bookingStatusHistory.getId()).orElseThrow();
    }

    protected void assertPersistedBookingStatusHistoryToMatchAllProperties(BookingStatusHistory expectedBookingStatusHistory) {
        assertBookingStatusHistoryAllPropertiesEquals(
            expectedBookingStatusHistory,
            getPersistedBookingStatusHistory(expectedBookingStatusHistory)
        );
    }

    protected void assertPersistedBookingStatusHistoryToMatchUpdatableProperties(BookingStatusHistory expectedBookingStatusHistory) {
        assertBookingStatusHistoryAllUpdatablePropertiesEquals(
            expectedBookingStatusHistory,
            getPersistedBookingStatusHistory(expectedBookingStatusHistory)
        );
    }
}
