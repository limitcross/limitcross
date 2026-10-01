package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingMediaAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingMedia;
import com.limitcross.facility.repository.BookingMediaRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.BookingMediaService;
import com.limitcross.facility.service.dto.BookingMediaDTO;
import com.limitcross.facility.service.mapper.BookingMediaMapper;
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
 * Integration tests for the {@link BookingMediaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingMediaResourceIT {

    private static final com.limitcross.facility.domain.enumeration.MediaType DEFAULT_MEDIA_TYPE =
        com.limitcross.facility.domain.enumeration.MediaType.BEFORE;
    private static final com.limitcross.facility.domain.enumeration.MediaType UPDATED_MEDIA_TYPE =
        com.limitcross.facility.domain.enumeration.MediaType.AFTER;

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/booking-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingMediaRepository bookingMediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BookingMediaRepository bookingMediaRepositoryMock;

    @Autowired
    private BookingMediaMapper bookingMediaMapper;

    @Mock
    private BookingMediaService bookingMediaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMediaMockMvc;

    private BookingMedia bookingMedia;

    private BookingMedia insertedBookingMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingMedia createEntity(EntityManager em) {
        BookingMedia bookingMedia = new BookingMedia()
            .mediaType(DEFAULT_MEDIA_TYPE)
            .fileUrl(DEFAULT_FILE_URL)
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
        bookingMedia.setBooking(booking);
        return bookingMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingMedia createUpdatedEntity(EntityManager em) {
        BookingMedia updatedBookingMedia = new BookingMedia()
            .mediaType(UPDATED_MEDIA_TYPE)
            .fileUrl(UPDATED_FILE_URL)
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
        updatedBookingMedia.setBooking(booking);
        return updatedBookingMedia;
    }

    @BeforeEach
    void initTest() {
        bookingMedia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingMedia != null) {
            bookingMediaRepository.delete(insertedBookingMedia);
            insertedBookingMedia = null;
        }
    }

    @Test
    @Transactional
    void createBookingMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);
        var returnedBookingMediaDTO = om.readValue(
            restBookingMediaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingMediaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingMediaDTO.class
        );

        // Validate the BookingMedia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingMedia = bookingMediaMapper.toEntity(returnedBookingMediaDTO);
        assertBookingMediaUpdatableFieldsEquals(returnedBookingMedia, getPersistedBookingMedia(returnedBookingMedia));

        insertedBookingMedia = returnedBookingMedia;
    }

    @Test
    @Transactional
    void createBookingMediaWithExistingId() throws Exception {
        // Create the BookingMedia with an existing ID
        bookingMedia.setId(1L);
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingMediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMediaTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingMedia.setMediaType(null);

        // Create the BookingMedia, which fails.
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        restBookingMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingMediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingMedia.setFileUrl(null);

        // Create the BookingMedia, which fails.
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        restBookingMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingMediaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingMedias() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        // Get all the bookingMediaList
        restBookingMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingMediasWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingMediaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingMediasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingMediaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingMedia() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        // Get the bookingMedia
        restBookingMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingMedia.getId().intValue()))
            .andExpect(jsonPath("$.mediaType").value(DEFAULT_MEDIA_TYPE.toString()))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookingMedia() throws Exception {
        // Get the bookingMedia
        restBookingMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingMedia() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingMedia
        BookingMedia updatedBookingMedia = bookingMediaRepository.findById(bookingMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingMedia are not directly saved in db
        em.detach(updatedBookingMedia);
        updatedBookingMedia.mediaType(UPDATED_MEDIA_TYPE).fileUrl(UPDATED_FILE_URL).createdAt(UPDATED_CREATED_AT);
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(updatedBookingMedia);

        restBookingMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingMediaToMatchAllProperties(updatedBookingMedia);
    }

    @Test
    @Transactional
    void putNonExistingBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingMediaWithPatch() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingMedia using partial update
        BookingMedia partialUpdatedBookingMedia = new BookingMedia();
        partialUpdatedBookingMedia.setId(bookingMedia.getId());

        partialUpdatedBookingMedia.mediaType(UPDATED_MEDIA_TYPE).fileUrl(UPDATED_FILE_URL);

        restBookingMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingMedia))
            )
            .andExpect(status().isOk());

        // Validate the BookingMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingMediaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingMedia, bookingMedia),
            getPersistedBookingMedia(bookingMedia)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingMediaWithPatch() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingMedia using partial update
        BookingMedia partialUpdatedBookingMedia = new BookingMedia();
        partialUpdatedBookingMedia.setId(bookingMedia.getId());

        partialUpdatedBookingMedia.mediaType(UPDATED_MEDIA_TYPE).fileUrl(UPDATED_FILE_URL).createdAt(UPDATED_CREATED_AT);

        restBookingMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingMedia))
            )
            .andExpect(status().isOk());

        // Validate the BookingMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingMediaUpdatableFieldsEquals(partialUpdatedBookingMedia, getPersistedBookingMedia(partialUpdatedBookingMedia));
    }

    @Test
    @Transactional
    void patchNonExistingBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingMedia.setId(longCount.incrementAndGet());

        // Create the BookingMedia
        BookingMediaDTO bookingMediaDTO = bookingMediaMapper.toDto(bookingMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingMedia() throws Exception {
        // Initialize the database
        insertedBookingMedia = bookingMediaRepository.saveAndFlush(bookingMedia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingMedia
        restBookingMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingMediaRepository.count();
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

    protected BookingMedia getPersistedBookingMedia(BookingMedia bookingMedia) {
        return bookingMediaRepository.findById(bookingMedia.getId()).orElseThrow();
    }

    protected void assertPersistedBookingMediaToMatchAllProperties(BookingMedia expectedBookingMedia) {
        assertBookingMediaAllPropertiesEquals(expectedBookingMedia, getPersistedBookingMedia(expectedBookingMedia));
    }

    protected void assertPersistedBookingMediaToMatchUpdatableProperties(BookingMedia expectedBookingMedia) {
        assertBookingMediaAllUpdatablePropertiesEquals(expectedBookingMedia, getPersistedBookingMedia(expectedBookingMedia));
    }
}
