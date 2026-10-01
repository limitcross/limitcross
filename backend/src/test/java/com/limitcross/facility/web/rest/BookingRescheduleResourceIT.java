package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingRescheduleAsserts.*;
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
import com.limitcross.facility.domain.BookingReschedule;
import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.repository.BookingRescheduleRepository;
import com.limitcross.facility.service.BookingRescheduleService;
import com.limitcross.facility.service.dto.BookingRescheduleDTO;
import com.limitcross.facility.service.mapper.BookingRescheduleMapper;
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
 * Integration tests for the {@link BookingRescheduleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingRescheduleResourceIT {

    private static final Instant DEFAULT_OLD_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OLD_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OLD_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OLD_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEW_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEW_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEW_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEW_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ActorType DEFAULT_REQUESTED_BY = ActorType.CUSTOMER;
    private static final ActorType UPDATED_REQUESTED_BY = ActorType.PRO;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FEE_CHARGED = new BigDecimal(0);
    private static final BigDecimal UPDATED_FEE_CHARGED = new BigDecimal(1);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/booking-reschedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingRescheduleRepository bookingRescheduleRepository;

    @Mock
    private BookingRescheduleRepository bookingRescheduleRepositoryMock;

    @Autowired
    private BookingRescheduleMapper bookingRescheduleMapper;

    @Mock
    private BookingRescheduleService bookingRescheduleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingRescheduleMockMvc;

    private BookingReschedule bookingReschedule;

    private BookingReschedule insertedBookingReschedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingReschedule createEntity(EntityManager em) {
        BookingReschedule bookingReschedule = new BookingReschedule()
            .oldStart(DEFAULT_OLD_START)
            .oldEnd(DEFAULT_OLD_END)
            .newStart(DEFAULT_NEW_START)
            .newEnd(DEFAULT_NEW_END)
            .requestedBy(DEFAULT_REQUESTED_BY)
            .reason(DEFAULT_REASON)
            .feeCharged(DEFAULT_FEE_CHARGED)
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
        bookingReschedule.setBooking(booking);
        return bookingReschedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingReschedule createUpdatedEntity(EntityManager em) {
        BookingReschedule updatedBookingReschedule = new BookingReschedule()
            .oldStart(UPDATED_OLD_START)
            .oldEnd(UPDATED_OLD_END)
            .newStart(UPDATED_NEW_START)
            .newEnd(UPDATED_NEW_END)
            .requestedBy(UPDATED_REQUESTED_BY)
            .reason(UPDATED_REASON)
            .feeCharged(UPDATED_FEE_CHARGED)
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
        updatedBookingReschedule.setBooking(booking);
        return updatedBookingReschedule;
    }

    @BeforeEach
    void initTest() {
        bookingReschedule = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingReschedule != null) {
            bookingRescheduleRepository.delete(insertedBookingReschedule);
            insertedBookingReschedule = null;
        }
    }

    @Test
    @Transactional
    void createBookingReschedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);
        var returnedBookingRescheduleDTO = om.readValue(
            restBookingRescheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingRescheduleDTO.class
        );

        // Validate the BookingReschedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingReschedule = bookingRescheduleMapper.toEntity(returnedBookingRescheduleDTO);
        assertBookingRescheduleUpdatableFieldsEquals(returnedBookingReschedule, getPersistedBookingReschedule(returnedBookingReschedule));

        insertedBookingReschedule = returnedBookingReschedule;
    }

    @Test
    @Transactional
    void createBookingRescheduleWithExistingId() throws Exception {
        // Create the BookingReschedule with an existing ID
        bookingReschedule.setId(1L);
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOldStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingReschedule.setOldStart(null);

        // Create the BookingReschedule, which fails.
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOldEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingReschedule.setOldEnd(null);

        // Create the BookingReschedule, which fails.
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingReschedule.setNewStart(null);

        // Create the BookingReschedule, which fails.
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingReschedule.setNewEnd(null);

        // Create the BookingReschedule, which fails.
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingReschedule.setRequestedBy(null);

        // Create the BookingReschedule, which fails.
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        restBookingRescheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingReschedules() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        // Get all the bookingRescheduleList
        restBookingRescheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingReschedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].oldStart").value(hasItem(DEFAULT_OLD_START.toString())))
            .andExpect(jsonPath("$.[*].oldEnd").value(hasItem(DEFAULT_OLD_END.toString())))
            .andExpect(jsonPath("$.[*].newStart").value(hasItem(DEFAULT_NEW_START.toString())))
            .andExpect(jsonPath("$.[*].newEnd").value(hasItem(DEFAULT_NEW_END.toString())))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].feeCharged").value(hasItem(sameNumber(DEFAULT_FEE_CHARGED))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingReschedulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingRescheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingRescheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingRescheduleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingReschedulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingRescheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingRescheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingRescheduleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingReschedule() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        // Get the bookingReschedule
        restBookingRescheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingReschedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingReschedule.getId().intValue()))
            .andExpect(jsonPath("$.oldStart").value(DEFAULT_OLD_START.toString()))
            .andExpect(jsonPath("$.oldEnd").value(DEFAULT_OLD_END.toString()))
            .andExpect(jsonPath("$.newStart").value(DEFAULT_NEW_START.toString()))
            .andExpect(jsonPath("$.newEnd").value(DEFAULT_NEW_END.toString()))
            .andExpect(jsonPath("$.requestedBy").value(DEFAULT_REQUESTED_BY.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.feeCharged").value(sameNumber(DEFAULT_FEE_CHARGED)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookingReschedule() throws Exception {
        // Get the bookingReschedule
        restBookingRescheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingReschedule() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingReschedule
        BookingReschedule updatedBookingReschedule = bookingRescheduleRepository.findById(bookingReschedule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingReschedule are not directly saved in db
        em.detach(updatedBookingReschedule);
        updatedBookingReschedule
            .oldStart(UPDATED_OLD_START)
            .oldEnd(UPDATED_OLD_END)
            .newStart(UPDATED_NEW_START)
            .newEnd(UPDATED_NEW_END)
            .requestedBy(UPDATED_REQUESTED_BY)
            .reason(UPDATED_REASON)
            .feeCharged(UPDATED_FEE_CHARGED)
            .createdAt(UPDATED_CREATED_AT);
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(updatedBookingReschedule);

        restBookingRescheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingRescheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingRescheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingRescheduleToMatchAllProperties(updatedBookingReschedule);
    }

    @Test
    @Transactional
    void putNonExistingBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingRescheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingRescheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingRescheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingRescheduleWithPatch() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingReschedule using partial update
        BookingReschedule partialUpdatedBookingReschedule = new BookingReschedule();
        partialUpdatedBookingReschedule.setId(bookingReschedule.getId());

        partialUpdatedBookingReschedule.oldEnd(UPDATED_OLD_END).reason(UPDATED_REASON);

        restBookingRescheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingReschedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingReschedule))
            )
            .andExpect(status().isOk());

        // Validate the BookingReschedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingRescheduleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingReschedule, bookingReschedule),
            getPersistedBookingReschedule(bookingReschedule)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingRescheduleWithPatch() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingReschedule using partial update
        BookingReschedule partialUpdatedBookingReschedule = new BookingReschedule();
        partialUpdatedBookingReschedule.setId(bookingReschedule.getId());

        partialUpdatedBookingReschedule
            .oldStart(UPDATED_OLD_START)
            .oldEnd(UPDATED_OLD_END)
            .newStart(UPDATED_NEW_START)
            .newEnd(UPDATED_NEW_END)
            .requestedBy(UPDATED_REQUESTED_BY)
            .reason(UPDATED_REASON)
            .feeCharged(UPDATED_FEE_CHARGED)
            .createdAt(UPDATED_CREATED_AT);

        restBookingRescheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingReschedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingReschedule))
            )
            .andExpect(status().isOk());

        // Validate the BookingReschedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingRescheduleUpdatableFieldsEquals(
            partialUpdatedBookingReschedule,
            getPersistedBookingReschedule(partialUpdatedBookingReschedule)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingRescheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingRescheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingRescheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingReschedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingReschedule.setId(longCount.incrementAndGet());

        // Create the BookingReschedule
        BookingRescheduleDTO bookingRescheduleDTO = bookingRescheduleMapper.toDto(bookingReschedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingRescheduleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingRescheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingReschedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingReschedule() throws Exception {
        // Initialize the database
        insertedBookingReschedule = bookingRescheduleRepository.saveAndFlush(bookingReschedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingReschedule
        restBookingRescheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingReschedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingRescheduleRepository.count();
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

    protected BookingReschedule getPersistedBookingReschedule(BookingReschedule bookingReschedule) {
        return bookingRescheduleRepository.findById(bookingReschedule.getId()).orElseThrow();
    }

    protected void assertPersistedBookingRescheduleToMatchAllProperties(BookingReschedule expectedBookingReschedule) {
        assertBookingRescheduleAllPropertiesEquals(expectedBookingReschedule, getPersistedBookingReschedule(expectedBookingReschedule));
    }

    protected void assertPersistedBookingRescheduleToMatchUpdatableProperties(BookingReschedule expectedBookingReschedule) {
        assertBookingRescheduleAllUpdatablePropertiesEquals(
            expectedBookingReschedule,
            getPersistedBookingReschedule(expectedBookingReschedule)
        );
    }
}
