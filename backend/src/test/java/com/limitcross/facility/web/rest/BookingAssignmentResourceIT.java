package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingAssignmentAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingAssignment;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.enumeration.AssignmentStatus;
import com.limitcross.facility.repository.BookingAssignmentRepository;
import com.limitcross.facility.service.BookingAssignmentService;
import com.limitcross.facility.service.dto.BookingAssignmentDTO;
import com.limitcross.facility.service.mapper.BookingAssignmentMapper;
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
 * Integration tests for the {@link BookingAssignmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingAssignmentResourceIT {

    private static final AssignmentStatus DEFAULT_STATUS = AssignmentStatus.OFFERED;
    private static final AssignmentStatus UPDATED_STATUS = AssignmentStatus.ACCEPTED;

    private static final Instant DEFAULT_OFFERED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OFFERED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESPONDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REJECT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECT_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/booking-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingAssignmentRepository bookingAssignmentRepository;

    @Mock
    private BookingAssignmentRepository bookingAssignmentRepositoryMock;

    @Autowired
    private BookingAssignmentMapper bookingAssignmentMapper;

    @Mock
    private BookingAssignmentService bookingAssignmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingAssignmentMockMvc;

    private BookingAssignment bookingAssignment;

    private BookingAssignment insertedBookingAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingAssignment createEntity(EntityManager em) {
        BookingAssignment bookingAssignment = new BookingAssignment()
            .status(DEFAULT_STATUS)
            .offeredAt(DEFAULT_OFFERED_AT)
            .respondedAt(DEFAULT_RESPONDED_AT)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .rejectReason(DEFAULT_REJECT_REASON);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        bookingAssignment.setProfessional(professional);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        bookingAssignment.setBooking(booking);
        return bookingAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingAssignment createUpdatedEntity(EntityManager em) {
        BookingAssignment updatedBookingAssignment = new BookingAssignment()
            .status(UPDATED_STATUS)
            .offeredAt(UPDATED_OFFERED_AT)
            .respondedAt(UPDATED_RESPONDED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .rejectReason(UPDATED_REJECT_REASON);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedBookingAssignment.setProfessional(professional);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedBookingAssignment.setBooking(booking);
        return updatedBookingAssignment;
    }

    @BeforeEach
    void initTest() {
        bookingAssignment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingAssignment != null) {
            bookingAssignmentRepository.delete(insertedBookingAssignment);
            insertedBookingAssignment = null;
        }
    }

    @Test
    @Transactional
    void createBookingAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);
        var returnedBookingAssignmentDTO = om.readValue(
            restBookingAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingAssignmentDTO.class
        );

        // Validate the BookingAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingAssignment = bookingAssignmentMapper.toEntity(returnedBookingAssignmentDTO);
        assertBookingAssignmentUpdatableFieldsEquals(returnedBookingAssignment, getPersistedBookingAssignment(returnedBookingAssignment));

        insertedBookingAssignment = returnedBookingAssignment;
    }

    @Test
    @Transactional
    void createBookingAssignmentWithExistingId() throws Exception {
        // Create the BookingAssignment with an existing ID
        bookingAssignment.setId(1L);
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingAssignment.setStatus(null);

        // Create the BookingAssignment, which fails.
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        restBookingAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingAssignment.setExpiresAt(null);

        // Create the BookingAssignment, which fails.
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        restBookingAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingAssignments() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        // Get all the bookingAssignmentList
        restBookingAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].offeredAt").value(hasItem(DEFAULT_OFFERED_AT.toString())))
            .andExpect(jsonPath("$.[*].respondedAt").value(hasItem(DEFAULT_RESPONDED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectReason").value(hasItem(DEFAULT_REJECT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingAssignmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingAssignmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingAssignmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingAssignmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingAssignment() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        // Get the bookingAssignment
        restBookingAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingAssignment.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.offeredAt").value(DEFAULT_OFFERED_AT.toString()))
            .andExpect(jsonPath("$.respondedAt").value(DEFAULT_RESPONDED_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.rejectReason").value(DEFAULT_REJECT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingBookingAssignment() throws Exception {
        // Get the bookingAssignment
        restBookingAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingAssignment() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingAssignment
        BookingAssignment updatedBookingAssignment = bookingAssignmentRepository.findById(bookingAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingAssignment are not directly saved in db
        em.detach(updatedBookingAssignment);
        updatedBookingAssignment
            .status(UPDATED_STATUS)
            .offeredAt(UPDATED_OFFERED_AT)
            .respondedAt(UPDATED_RESPONDED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .rejectReason(UPDATED_REJECT_REASON);
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(updatedBookingAssignment);

        restBookingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingAssignmentToMatchAllProperties(updatedBookingAssignment);
    }

    @Test
    @Transactional
    void putNonExistingBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingAssignment using partial update
        BookingAssignment partialUpdatedBookingAssignment = new BookingAssignment();
        partialUpdatedBookingAssignment.setId(bookingAssignment.getId());

        partialUpdatedBookingAssignment.offeredAt(UPDATED_OFFERED_AT).expiresAt(UPDATED_EXPIRES_AT).rejectReason(UPDATED_REJECT_REASON);

        restBookingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingAssignment))
            )
            .andExpect(status().isOk());

        // Validate the BookingAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingAssignment, bookingAssignment),
            getPersistedBookingAssignment(bookingAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingAssignment using partial update
        BookingAssignment partialUpdatedBookingAssignment = new BookingAssignment();
        partialUpdatedBookingAssignment.setId(bookingAssignment.getId());

        partialUpdatedBookingAssignment
            .status(UPDATED_STATUS)
            .offeredAt(UPDATED_OFFERED_AT)
            .respondedAt(UPDATED_RESPONDED_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .rejectReason(UPDATED_REJECT_REASON);

        restBookingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingAssignment))
            )
            .andExpect(status().isOk());

        // Validate the BookingAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingAssignmentUpdatableFieldsEquals(
            partialUpdatedBookingAssignment,
            getPersistedBookingAssignment(partialUpdatedBookingAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingAssignment.setId(longCount.incrementAndGet());

        // Create the BookingAssignment
        BookingAssignmentDTO bookingAssignmentDTO = bookingAssignmentMapper.toDto(bookingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingAssignment() throws Exception {
        // Initialize the database
        insertedBookingAssignment = bookingAssignmentRepository.saveAndFlush(bookingAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingAssignment
        restBookingAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingAssignmentRepository.count();
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

    protected BookingAssignment getPersistedBookingAssignment(BookingAssignment bookingAssignment) {
        return bookingAssignmentRepository.findById(bookingAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedBookingAssignmentToMatchAllProperties(BookingAssignment expectedBookingAssignment) {
        assertBookingAssignmentAllPropertiesEquals(expectedBookingAssignment, getPersistedBookingAssignment(expectedBookingAssignment));
    }

    protected void assertPersistedBookingAssignmentToMatchUpdatableProperties(BookingAssignment expectedBookingAssignment) {
        assertBookingAssignmentAllUpdatablePropertiesEquals(
            expectedBookingAssignment,
            getPersistedBookingAssignment(expectedBookingAssignment)
        );
    }
}
