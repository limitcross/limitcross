package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CityDailyMetricsAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CityDailyMetrics;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.repository.CityDailyMetricsRepository;
import com.limitcross.facility.service.CityDailyMetricsService;
import com.limitcross.facility.service.dto.CityDailyMetricsDTO;
import com.limitcross.facility.service.mapper.CityDailyMetricsMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CityDailyMetricsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CityDailyMetricsResourceIT {

    private static final LocalDate DEFAULT_METRIC_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_METRIC_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_BOOKINGS_CREATED = 0;
    private static final Integer UPDATED_BOOKINGS_CREATED = 1;

    private static final Integer DEFAULT_BOOKINGS_COMPLETED = 0;
    private static final Integer UPDATED_BOOKINGS_COMPLETED = 1;

    private static final Integer DEFAULT_BOOKINGS_CANCELLED = 0;
    private static final Integer UPDATED_BOOKINGS_CANCELLED = 1;

    private static final BigDecimal DEFAULT_GMV = new BigDecimal(1);
    private static final BigDecimal UPDATED_GMV = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PLATFORM_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLATFORM_REVENUE = new BigDecimal(2);

    private static final Double DEFAULT_AVG_RATING = 1D;
    private static final Double UPDATED_AVG_RATING = 2D;

    private static final Integer DEFAULT_AVG_ASSIGNMENT_SEC = 1;
    private static final Integer UPDATED_AVG_ASSIGNMENT_SEC = 2;

    private static final String ENTITY_API_URL = "/api/city-daily-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityDailyMetricsRepository cityDailyMetricsRepository;

    @Mock
    private CityDailyMetricsRepository cityDailyMetricsRepositoryMock;

    @Autowired
    private CityDailyMetricsMapper cityDailyMetricsMapper;

    @Mock
    private CityDailyMetricsService cityDailyMetricsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityDailyMetricsMockMvc;

    private CityDailyMetrics cityDailyMetrics;

    private CityDailyMetrics insertedCityDailyMetrics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityDailyMetrics createEntity(EntityManager em) {
        CityDailyMetrics cityDailyMetrics = new CityDailyMetrics()
            .metricDate(DEFAULT_METRIC_DATE)
            .bookingsCreated(DEFAULT_BOOKINGS_CREATED)
            .bookingsCompleted(DEFAULT_BOOKINGS_COMPLETED)
            .bookingsCancelled(DEFAULT_BOOKINGS_CANCELLED)
            .gmv(DEFAULT_GMV)
            .platformRevenue(DEFAULT_PLATFORM_REVENUE)
            .avgRating(DEFAULT_AVG_RATING)
            .avgAssignmentSec(DEFAULT_AVG_ASSIGNMENT_SEC);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        cityDailyMetrics.setCity(city);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        cityDailyMetrics.setCategory(serviceCategory);
        return cityDailyMetrics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityDailyMetrics createUpdatedEntity(EntityManager em) {
        CityDailyMetrics updatedCityDailyMetrics = new CityDailyMetrics()
            .metricDate(UPDATED_METRIC_DATE)
            .bookingsCreated(UPDATED_BOOKINGS_CREATED)
            .bookingsCompleted(UPDATED_BOOKINGS_COMPLETED)
            .bookingsCancelled(UPDATED_BOOKINGS_CANCELLED)
            .gmv(UPDATED_GMV)
            .platformRevenue(UPDATED_PLATFORM_REVENUE)
            .avgRating(UPDATED_AVG_RATING)
            .avgAssignmentSec(UPDATED_AVG_ASSIGNMENT_SEC);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedCityDailyMetrics.setCity(city);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createUpdatedEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        updatedCityDailyMetrics.setCategory(serviceCategory);
        return updatedCityDailyMetrics;
    }

    @BeforeEach
    void initTest() {
        cityDailyMetrics = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCityDailyMetrics != null) {
            cityDailyMetricsRepository.delete(insertedCityDailyMetrics);
            insertedCityDailyMetrics = null;
        }
    }

    @Test
    @Transactional
    void createCityDailyMetrics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);
        var returnedCityDailyMetricsDTO = om.readValue(
            restCityDailyMetricsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDailyMetricsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CityDailyMetricsDTO.class
        );

        // Validate the CityDailyMetrics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCityDailyMetrics = cityDailyMetricsMapper.toEntity(returnedCityDailyMetricsDTO);
        assertCityDailyMetricsUpdatableFieldsEquals(returnedCityDailyMetrics, getPersistedCityDailyMetrics(returnedCityDailyMetrics));

        insertedCityDailyMetrics = returnedCityDailyMetrics;
    }

    @Test
    @Transactional
    void createCityDailyMetricsWithExistingId() throws Exception {
        // Create the CityDailyMetrics with an existing ID
        cityDailyMetrics.setId(1L);
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityDailyMetricsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDailyMetricsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cityDailyMetrics.setMetricDate(null);

        // Create the CityDailyMetrics, which fails.
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        restCityDailyMetricsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDailyMetricsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCityDailyMetrics() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        // Get all the cityDailyMetricsList
        restCityDailyMetricsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cityDailyMetrics.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricDate").value(hasItem(DEFAULT_METRIC_DATE.toString())))
            .andExpect(jsonPath("$.[*].bookingsCreated").value(hasItem(DEFAULT_BOOKINGS_CREATED)))
            .andExpect(jsonPath("$.[*].bookingsCompleted").value(hasItem(DEFAULT_BOOKINGS_COMPLETED)))
            .andExpect(jsonPath("$.[*].bookingsCancelled").value(hasItem(DEFAULT_BOOKINGS_CANCELLED)))
            .andExpect(jsonPath("$.[*].gmv").value(hasItem(sameNumber(DEFAULT_GMV))))
            .andExpect(jsonPath("$.[*].platformRevenue").value(hasItem(sameNumber(DEFAULT_PLATFORM_REVENUE))))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)))
            .andExpect(jsonPath("$.[*].avgAssignmentSec").value(hasItem(DEFAULT_AVG_ASSIGNMENT_SEC)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityDailyMetricsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cityDailyMetricsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityDailyMetricsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cityDailyMetricsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityDailyMetricsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cityDailyMetricsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityDailyMetricsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cityDailyMetricsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCityDailyMetrics() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        // Get the cityDailyMetrics
        restCityDailyMetricsMockMvc
            .perform(get(ENTITY_API_URL_ID, cityDailyMetrics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cityDailyMetrics.getId().intValue()))
            .andExpect(jsonPath("$.metricDate").value(DEFAULT_METRIC_DATE.toString()))
            .andExpect(jsonPath("$.bookingsCreated").value(DEFAULT_BOOKINGS_CREATED))
            .andExpect(jsonPath("$.bookingsCompleted").value(DEFAULT_BOOKINGS_COMPLETED))
            .andExpect(jsonPath("$.bookingsCancelled").value(DEFAULT_BOOKINGS_CANCELLED))
            .andExpect(jsonPath("$.gmv").value(sameNumber(DEFAULT_GMV)))
            .andExpect(jsonPath("$.platformRevenue").value(sameNumber(DEFAULT_PLATFORM_REVENUE)))
            .andExpect(jsonPath("$.avgRating").value(DEFAULT_AVG_RATING))
            .andExpect(jsonPath("$.avgAssignmentSec").value(DEFAULT_AVG_ASSIGNMENT_SEC));
    }

    @Test
    @Transactional
    void getNonExistingCityDailyMetrics() throws Exception {
        // Get the cityDailyMetrics
        restCityDailyMetricsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCityDailyMetrics() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityDailyMetrics
        CityDailyMetrics updatedCityDailyMetrics = cityDailyMetricsRepository.findById(cityDailyMetrics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCityDailyMetrics are not directly saved in db
        em.detach(updatedCityDailyMetrics);
        updatedCityDailyMetrics
            .metricDate(UPDATED_METRIC_DATE)
            .bookingsCreated(UPDATED_BOOKINGS_CREATED)
            .bookingsCompleted(UPDATED_BOOKINGS_COMPLETED)
            .bookingsCancelled(UPDATED_BOOKINGS_CANCELLED)
            .gmv(UPDATED_GMV)
            .platformRevenue(UPDATED_PLATFORM_REVENUE)
            .avgRating(UPDATED_AVG_RATING)
            .avgAssignmentSec(UPDATED_AVG_ASSIGNMENT_SEC);
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(updatedCityDailyMetrics);

        restCityDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDailyMetricsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDailyMetricsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityDailyMetricsToMatchAllProperties(updatedCityDailyMetrics);
    }

    @Test
    @Transactional
    void putNonExistingCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDailyMetricsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDailyMetricsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityDailyMetricsWithPatch() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityDailyMetrics using partial update
        CityDailyMetrics partialUpdatedCityDailyMetrics = new CityDailyMetrics();
        partialUpdatedCityDailyMetrics.setId(cityDailyMetrics.getId());

        partialUpdatedCityDailyMetrics
            .bookingsCreated(UPDATED_BOOKINGS_CREATED)
            .gmv(UPDATED_GMV)
            .avgAssignmentSec(UPDATED_AVG_ASSIGNMENT_SEC);

        restCityDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityDailyMetrics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityDailyMetrics))
            )
            .andExpect(status().isOk());

        // Validate the CityDailyMetrics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityDailyMetricsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCityDailyMetrics, cityDailyMetrics),
            getPersistedCityDailyMetrics(cityDailyMetrics)
        );
    }

    @Test
    @Transactional
    void fullUpdateCityDailyMetricsWithPatch() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityDailyMetrics using partial update
        CityDailyMetrics partialUpdatedCityDailyMetrics = new CityDailyMetrics();
        partialUpdatedCityDailyMetrics.setId(cityDailyMetrics.getId());

        partialUpdatedCityDailyMetrics
            .metricDate(UPDATED_METRIC_DATE)
            .bookingsCreated(UPDATED_BOOKINGS_CREATED)
            .bookingsCompleted(UPDATED_BOOKINGS_COMPLETED)
            .bookingsCancelled(UPDATED_BOOKINGS_CANCELLED)
            .gmv(UPDATED_GMV)
            .platformRevenue(UPDATED_PLATFORM_REVENUE)
            .avgRating(UPDATED_AVG_RATING)
            .avgAssignmentSec(UPDATED_AVG_ASSIGNMENT_SEC);

        restCityDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityDailyMetrics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityDailyMetrics))
            )
            .andExpect(status().isOk());

        // Validate the CityDailyMetrics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityDailyMetricsUpdatableFieldsEquals(
            partialUpdatedCityDailyMetrics,
            getPersistedCityDailyMetrics(partialUpdatedCityDailyMetrics)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityDailyMetricsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityDailyMetricsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCityDailyMetrics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityDailyMetrics.setId(longCount.incrementAndGet());

        // Create the CityDailyMetrics
        CityDailyMetricsDTO cityDailyMetricsDTO = cityDailyMetricsMapper.toDto(cityDailyMetrics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityDailyMetricsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityDailyMetricsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityDailyMetrics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCityDailyMetrics() throws Exception {
        // Initialize the database
        insertedCityDailyMetrics = cityDailyMetricsRepository.saveAndFlush(cityDailyMetrics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cityDailyMetrics
        restCityDailyMetricsMockMvc
            .perform(delete(ENTITY_API_URL_ID, cityDailyMetrics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cityDailyMetricsRepository.count();
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

    protected CityDailyMetrics getPersistedCityDailyMetrics(CityDailyMetrics cityDailyMetrics) {
        return cityDailyMetricsRepository.findById(cityDailyMetrics.getId()).orElseThrow();
    }

    protected void assertPersistedCityDailyMetricsToMatchAllProperties(CityDailyMetrics expectedCityDailyMetrics) {
        assertCityDailyMetricsAllPropertiesEquals(expectedCityDailyMetrics, getPersistedCityDailyMetrics(expectedCityDailyMetrics));
    }

    protected void assertPersistedCityDailyMetricsToMatchUpdatableProperties(CityDailyMetrics expectedCityDailyMetrics) {
        assertCityDailyMetricsAllUpdatablePropertiesEquals(
            expectedCityDailyMetrics,
            getPersistedCityDailyMetrics(expectedCityDailyMetrics)
        );
    }
}
