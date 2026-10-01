package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.SosAlertAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.SosAlert;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.SosStatus;
import com.limitcross.facility.repository.SosAlertRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.SosAlertService;
import com.limitcross.facility.service.dto.SosAlertDTO;
import com.limitcross.facility.service.mapper.SosAlertMapper;
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
 * Integration tests for the {@link SosAlertResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SosAlertResourceIT {

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final SosStatus DEFAULT_STATUS = SosStatus.OPEN;
    private static final SosStatus UPDATED_STATUS = SosStatus.ACKNOWLEDGED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESOLVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sos-alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SosAlertRepository sosAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SosAlertRepository sosAlertRepositoryMock;

    @Autowired
    private SosAlertMapper sosAlertMapper;

    @Mock
    private SosAlertService sosAlertServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSosAlertMockMvc;

    private SosAlert sosAlert;

    private SosAlert insertedSosAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosAlert createEntity(EntityManager em) {
        SosAlert sosAlert = new SosAlert()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .resolvedAt(DEFAULT_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        sosAlert.setRaisedBy(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        sosAlert.setBooking(booking);
        return sosAlert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SosAlert createUpdatedEntity(EntityManager em) {
        SosAlert updatedSosAlert = new SosAlert()
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSosAlert.setRaisedBy(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedSosAlert.setBooking(booking);
        return updatedSosAlert;
    }

    @BeforeEach
    void initTest() {
        sosAlert = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSosAlert != null) {
            sosAlertRepository.delete(insertedSosAlert);
            insertedSosAlert = null;
        }
    }

    @Test
    @Transactional
    void createSosAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);
        var returnedSosAlertDTO = om.readValue(
            restSosAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sosAlertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SosAlertDTO.class
        );

        // Validate the SosAlert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSosAlert = sosAlertMapper.toEntity(returnedSosAlertDTO);
        assertSosAlertUpdatableFieldsEquals(returnedSosAlert, getPersistedSosAlert(returnedSosAlert));

        insertedSosAlert = returnedSosAlert;
    }

    @Test
    @Transactional
    void createSosAlertWithExistingId() throws Exception {
        // Create the SosAlert with an existing ID
        sosAlert.setId(1L);
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSosAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sosAlertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sosAlert.setStatus(null);

        // Create the SosAlert, which fails.
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        restSosAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sosAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSosAlerts() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        // Get all the sosAlertList
        restSosAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sosAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(DEFAULT_RESOLVED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSosAlertsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sosAlertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSosAlertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sosAlertServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSosAlertsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sosAlertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSosAlertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sosAlertRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSosAlert() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        // Get the sosAlert
        restSosAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, sosAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sosAlert.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.resolvedAt").value(DEFAULT_RESOLVED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSosAlert() throws Exception {
        // Get the sosAlert
        restSosAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSosAlert() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sosAlert
        SosAlert updatedSosAlert = sosAlertRepository.findById(sosAlert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSosAlert are not directly saved in db
        em.detach(updatedSosAlert);
        updatedSosAlert
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(updatedSosAlert);

        restSosAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sosAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sosAlertDTO))
            )
            .andExpect(status().isOk());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSosAlertToMatchAllProperties(updatedSosAlert);
    }

    @Test
    @Transactional
    void putNonExistingSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sosAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sosAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sosAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sosAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSosAlertWithPatch() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sosAlert using partial update
        SosAlert partialUpdatedSosAlert = new SosAlert();
        partialUpdatedSosAlert.setId(sosAlert.getId());

        partialUpdatedSosAlert.latitude(UPDATED_LATITUDE).resolvedAt(UPDATED_RESOLVED_AT);

        restSosAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSosAlert))
            )
            .andExpect(status().isOk());

        // Validate the SosAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSosAlertUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSosAlert, sosAlert), getPersistedSosAlert(sosAlert));
    }

    @Test
    @Transactional
    void fullUpdateSosAlertWithPatch() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sosAlert using partial update
        SosAlert partialUpdatedSosAlert = new SosAlert();
        partialUpdatedSosAlert.setId(sosAlert.getId());

        partialUpdatedSosAlert
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);

        restSosAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSosAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSosAlert))
            )
            .andExpect(status().isOk());

        // Validate the SosAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSosAlertUpdatableFieldsEquals(partialUpdatedSosAlert, getPersistedSosAlert(partialUpdatedSosAlert));
    }

    @Test
    @Transactional
    void patchNonExistingSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sosAlertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sosAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sosAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSosAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sosAlert.setId(longCount.incrementAndGet());

        // Create the SosAlert
        SosAlertDTO sosAlertDTO = sosAlertMapper.toDto(sosAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSosAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sosAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SosAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSosAlert() throws Exception {
        // Initialize the database
        insertedSosAlert = sosAlertRepository.saveAndFlush(sosAlert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sosAlert
        restSosAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, sosAlert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sosAlertRepository.count();
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

    protected SosAlert getPersistedSosAlert(SosAlert sosAlert) {
        return sosAlertRepository.findById(sosAlert.getId()).orElseThrow();
    }

    protected void assertPersistedSosAlertToMatchAllProperties(SosAlert expectedSosAlert) {
        assertSosAlertAllPropertiesEquals(expectedSosAlert, getPersistedSosAlert(expectedSosAlert));
    }

    protected void assertPersistedSosAlertToMatchUpdatableProperties(SosAlert expectedSosAlert) {
        assertSosAlertAllUpdatablePropertiesEquals(expectedSosAlert, getPersistedSosAlert(expectedSosAlert));
    }
}
