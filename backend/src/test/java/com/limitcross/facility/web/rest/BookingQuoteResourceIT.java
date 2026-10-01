package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingQuoteAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingQuote;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.enumeration.QuoteStatus;
import com.limitcross.facility.repository.BookingQuoteRepository;
import com.limitcross.facility.service.BookingQuoteService;
import com.limitcross.facility.service.dto.BookingQuoteDTO;
import com.limitcross.facility.service.mapper.BookingQuoteMapper;
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
 * Integration tests for the {@link BookingQuoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingQuoteResourceIT {

    private static final QuoteStatus DEFAULT_STATUS = QuoteStatus.PROPOSED;
    private static final QuoteStatus UPDATED_STATUS = QuoteStatus.ACCEPTED;

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALID_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESPONDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/booking-quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingQuoteRepository bookingQuoteRepository;

    @Mock
    private BookingQuoteRepository bookingQuoteRepositoryMock;

    @Autowired
    private BookingQuoteMapper bookingQuoteMapper;

    @Mock
    private BookingQuoteService bookingQuoteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingQuoteMockMvc;

    private BookingQuote bookingQuote;

    private BookingQuote insertedBookingQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingQuote createEntity(EntityManager em) {
        BookingQuote bookingQuote = new BookingQuote()
            .status(DEFAULT_STATUS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .notes(DEFAULT_NOTES)
            .validUntil(DEFAULT_VALID_UNTIL)
            .respondedAt(DEFAULT_RESPONDED_AT)
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
        bookingQuote.setProfessional(professional);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        bookingQuote.setBooking(booking);
        return bookingQuote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingQuote createUpdatedEntity(EntityManager em) {
        BookingQuote updatedBookingQuote = new BookingQuote()
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .notes(UPDATED_NOTES)
            .validUntil(UPDATED_VALID_UNTIL)
            .respondedAt(UPDATED_RESPONDED_AT)
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
        updatedBookingQuote.setProfessional(professional);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedBookingQuote.setBooking(booking);
        return updatedBookingQuote;
    }

    @BeforeEach
    void initTest() {
        bookingQuote = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingQuote != null) {
            bookingQuoteRepository.delete(insertedBookingQuote);
            insertedBookingQuote = null;
        }
    }

    @Test
    @Transactional
    void createBookingQuote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);
        var returnedBookingQuoteDTO = om.readValue(
            restBookingQuoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingQuoteDTO.class
        );

        // Validate the BookingQuote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingQuote = bookingQuoteMapper.toEntity(returnedBookingQuoteDTO);
        assertBookingQuoteUpdatableFieldsEquals(returnedBookingQuote, getPersistedBookingQuote(returnedBookingQuote));

        insertedBookingQuote = returnedBookingQuote;
    }

    @Test
    @Transactional
    void createBookingQuoteWithExistingId() throws Exception {
        // Create the BookingQuote with an existing ID
        bookingQuote.setId(1L);
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuote.setStatus(null);

        // Create the BookingQuote, which fails.
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        restBookingQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuote.setTotalAmount(null);

        // Create the BookingQuote, which fails.
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        restBookingQuoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingQuotes() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        // Get all the bookingQuoteList
        restBookingQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingQuote.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].respondedAt").value(hasItem(DEFAULT_RESPONDED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingQuotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingQuoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingQuoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingQuoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingQuotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingQuoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingQuoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingQuoteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingQuote() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        // Get the bookingQuote
        restBookingQuoteMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingQuote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingQuote.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.respondedAt").value(DEFAULT_RESPONDED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookingQuote() throws Exception {
        // Get the bookingQuote
        restBookingQuoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingQuote() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuote
        BookingQuote updatedBookingQuote = bookingQuoteRepository.findById(bookingQuote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingQuote are not directly saved in db
        em.detach(updatedBookingQuote);
        updatedBookingQuote
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .notes(UPDATED_NOTES)
            .validUntil(UPDATED_VALID_UNTIL)
            .respondedAt(UPDATED_RESPONDED_AT)
            .createdAt(UPDATED_CREATED_AT);
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(updatedBookingQuote);

        restBookingQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingQuoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingQuoteToMatchAllProperties(updatedBookingQuote);
    }

    @Test
    @Transactional
    void putNonExistingBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingQuoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuote using partial update
        BookingQuote partialUpdatedBookingQuote = new BookingQuote();
        partialUpdatedBookingQuote.setId(bookingQuote.getId());

        partialUpdatedBookingQuote
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .notes(UPDATED_NOTES)
            .respondedAt(UPDATED_RESPONDED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restBookingQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingQuote))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingQuoteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingQuote, bookingQuote),
            getPersistedBookingQuote(bookingQuote)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuote using partial update
        BookingQuote partialUpdatedBookingQuote = new BookingQuote();
        partialUpdatedBookingQuote.setId(bookingQuote.getId());

        partialUpdatedBookingQuote
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .notes(UPDATED_NOTES)
            .validUntil(UPDATED_VALID_UNTIL)
            .respondedAt(UPDATED_RESPONDED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restBookingQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingQuote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingQuote))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingQuoteUpdatableFieldsEquals(partialUpdatedBookingQuote, getPersistedBookingQuote(partialUpdatedBookingQuote));
    }

    @Test
    @Transactional
    void patchNonExistingBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingQuoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingQuoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuote.setId(longCount.incrementAndGet());

        // Create the BookingQuote
        BookingQuoteDTO bookingQuoteDTO = bookingQuoteMapper.toDto(bookingQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingQuoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingQuote() throws Exception {
        // Initialize the database
        insertedBookingQuote = bookingQuoteRepository.saveAndFlush(bookingQuote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingQuote
        restBookingQuoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingQuote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingQuoteRepository.count();
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

    protected BookingQuote getPersistedBookingQuote(BookingQuote bookingQuote) {
        return bookingQuoteRepository.findById(bookingQuote.getId()).orElseThrow();
    }

    protected void assertPersistedBookingQuoteToMatchAllProperties(BookingQuote expectedBookingQuote) {
        assertBookingQuoteAllPropertiesEquals(expectedBookingQuote, getPersistedBookingQuote(expectedBookingQuote));
    }

    protected void assertPersistedBookingQuoteToMatchUpdatableProperties(BookingQuote expectedBookingQuote) {
        assertBookingQuoteAllUpdatablePropertiesEquals(expectedBookingQuote, getPersistedBookingQuote(expectedBookingQuote));
    }
}
