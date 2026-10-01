package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ServiceZoneAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.repository.ServiceZoneRepository;
import com.limitcross.facility.service.ServiceZoneService;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
import com.limitcross.facility.service.mapper.ServiceZoneMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ServiceZoneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceZoneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/service-zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceZoneRepository serviceZoneRepository;

    @Mock
    private ServiceZoneRepository serviceZoneRepositoryMock;

    @Autowired
    private ServiceZoneMapper serviceZoneMapper;

    @Mock
    private ServiceZoneService serviceZoneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceZoneMockMvc;

    private ServiceZone serviceZone;

    private ServiceZone insertedServiceZone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceZone createEntity(EntityManager em) {
        ServiceZone serviceZone = new ServiceZone().name(DEFAULT_NAME).pincode(DEFAULT_PINCODE).active(DEFAULT_ACTIVE);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        serviceZone.setCity(city);
        return serviceZone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceZone createUpdatedEntity(EntityManager em) {
        ServiceZone updatedServiceZone = new ServiceZone().name(UPDATED_NAME).pincode(UPDATED_PINCODE).active(UPDATED_ACTIVE);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedServiceZone.setCity(city);
        return updatedServiceZone;
    }

    @BeforeEach
    void initTest() {
        serviceZone = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceZone != null) {
            serviceZoneRepository.delete(insertedServiceZone);
            insertedServiceZone = null;
        }
    }

    @Test
    @Transactional
    void createServiceZone() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);
        var returnedServiceZoneDTO = om.readValue(
            restServiceZoneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceZoneDTO.class
        );

        // Validate the ServiceZone in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceZone = serviceZoneMapper.toEntity(returnedServiceZoneDTO);
        assertServiceZoneUpdatableFieldsEquals(returnedServiceZone, getPersistedServiceZone(returnedServiceZone));

        insertedServiceZone = returnedServiceZone;
    }

    @Test
    @Transactional
    void createServiceZoneWithExistingId() throws Exception {
        // Create the ServiceZone with an existing ID
        serviceZone.setId(1L);
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceZone.setName(null);

        // Create the ServiceZone, which fails.
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        restServiceZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPincodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceZone.setPincode(null);

        // Create the ServiceZone, which fails.
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        restServiceZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceZone.setActive(null);

        // Create the ServiceZone, which fails.
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        restServiceZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceZones() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        // Get all the serviceZoneList
        restServiceZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceZone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceZonesWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceZoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceZoneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceZonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceZoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceZoneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceZone() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        // Get the serviceZone
        restServiceZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceZone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceZone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingServiceZone() throws Exception {
        // Get the serviceZone
        restServiceZoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceZone() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceZone
        ServiceZone updatedServiceZone = serviceZoneRepository.findById(serviceZone.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceZone are not directly saved in db
        em.detach(updatedServiceZone);
        updatedServiceZone.name(UPDATED_NAME).pincode(UPDATED_PINCODE).active(UPDATED_ACTIVE);
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(updatedServiceZone);

        restServiceZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceZoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceZoneToMatchAllProperties(updatedServiceZone);
    }

    @Test
    @Transactional
    void putNonExistingServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceZoneWithPatch() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceZone using partial update
        ServiceZone partialUpdatedServiceZone = new ServiceZone();
        partialUpdatedServiceZone.setId(serviceZone.getId());

        restServiceZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceZone))
            )
            .andExpect(status().isOk());

        // Validate the ServiceZone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceZoneUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceZone, serviceZone),
            getPersistedServiceZone(serviceZone)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceZoneWithPatch() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceZone using partial update
        ServiceZone partialUpdatedServiceZone = new ServiceZone();
        partialUpdatedServiceZone.setId(serviceZone.getId());

        partialUpdatedServiceZone.name(UPDATED_NAME).pincode(UPDATED_PINCODE).active(UPDATED_ACTIVE);

        restServiceZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceZone))
            )
            .andExpect(status().isOk());

        // Validate the ServiceZone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceZoneUpdatableFieldsEquals(partialUpdatedServiceZone, getPersistedServiceZone(partialUpdatedServiceZone));
    }

    @Test
    @Transactional
    void patchNonExistingServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceZoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceZone.setId(longCount.incrementAndGet());

        // Create the ServiceZone
        ServiceZoneDTO serviceZoneDTO = serviceZoneMapper.toDto(serviceZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceZoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceZoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceZone() throws Exception {
        // Initialize the database
        insertedServiceZone = serviceZoneRepository.saveAndFlush(serviceZone);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceZone
        restServiceZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceZone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceZoneRepository.count();
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

    protected ServiceZone getPersistedServiceZone(ServiceZone serviceZone) {
        return serviceZoneRepository.findById(serviceZone.getId()).orElseThrow();
    }

    protected void assertPersistedServiceZoneToMatchAllProperties(ServiceZone expectedServiceZone) {
        assertServiceZoneAllPropertiesEquals(expectedServiceZone, getPersistedServiceZone(expectedServiceZone));
    }

    protected void assertPersistedServiceZoneToMatchUpdatableProperties(ServiceZone expectedServiceZone) {
        assertServiceZoneAllUpdatablePropertiesEquals(expectedServiceZone, getPersistedServiceZone(expectedServiceZone));
    }
}
