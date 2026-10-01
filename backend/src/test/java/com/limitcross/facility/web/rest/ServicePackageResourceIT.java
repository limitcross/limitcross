package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ServicePackageAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.repository.ServicePackageRepository;
import com.limitcross.facility.service.ServicePackageService;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import com.limitcross.facility.service.mapper.ServicePackageMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ServicePackageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicePackageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MRP = new BigDecimal(0);
    private static final BigDecimal UPDATED_MRP = new BigDecimal(1);

    private static final Integer DEFAULT_DURATION_MINUTES = 1;
    private static final Integer UPDATED_DURATION_MINUTES = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/service-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Mock
    private ServicePackageRepository servicePackageRepositoryMock;

    @Autowired
    private ServicePackageMapper servicePackageMapper;

    @Mock
    private ServicePackageService servicePackageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicePackageMockMvc;

    private ServicePackage servicePackage;

    private ServicePackage insertedServicePackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackage createEntity(EntityManager em) {
        ServicePackage servicePackage = new ServicePackage()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .basePrice(DEFAULT_BASE_PRICE)
            .mrp(DEFAULT_MRP)
            .durationMinutes(DEFAULT_DURATION_MINUTES)
            .active(DEFAULT_ACTIVE)
            .sortOrder(DEFAULT_SORT_ORDER);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        servicePackage.setService(facilityService);
        return servicePackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicePackage createUpdatedEntity(EntityManager em) {
        ServicePackage updatedServicePackage = new ServicePackage()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .mrp(UPDATED_MRP)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedServicePackage.setService(facilityService);
        return updatedServicePackage;
    }

    @BeforeEach
    void initTest() {
        servicePackage = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicePackage != null) {
            servicePackageRepository.delete(insertedServicePackage);
            insertedServicePackage = null;
        }
    }

    @Test
    @Transactional
    void createServicePackage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);
        var returnedServicePackageDTO = om.readValue(
            restServicePackageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicePackageDTO.class
        );

        // Validate the ServicePackage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicePackage = servicePackageMapper.toEntity(returnedServicePackageDTO);
        assertServicePackageUpdatableFieldsEquals(returnedServicePackage, getPersistedServicePackage(returnedServicePackage));

        insertedServicePackage = returnedServicePackage;
    }

    @Test
    @Transactional
    void createServicePackageWithExistingId() throws Exception {
        // Create the ServicePackage with an existing ID
        servicePackage.setId(1L);
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicePackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicePackage.setName(null);

        // Create the ServicePackage, which fails.
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        restServicePackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicePackage.setBasePrice(null);

        // Create the ServicePackage, which fails.
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        restServicePackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationMinutesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicePackage.setDurationMinutes(null);

        // Create the ServicePackage, which fails.
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        restServicePackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicePackage.setActive(null);

        // Create the ServicePackage, which fails.
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        restServicePackageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicePackages() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        // Get all the servicePackageList
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicePackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(sameNumber(DEFAULT_BASE_PRICE))))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(sameNumber(DEFAULT_MRP))))
            .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicePackagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicePackageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicePackageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicePackageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicePackagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicePackageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicePackageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicePackageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicePackage() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        // Get the servicePackage
        restServicePackageMockMvc
            .perform(get(ENTITY_API_URL_ID, servicePackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicePackage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.basePrice").value(sameNumber(DEFAULT_BASE_PRICE)))
            .andExpect(jsonPath("$.mrp").value(sameNumber(DEFAULT_MRP)))
            .andExpect(jsonPath("$.durationMinutes").value(DEFAULT_DURATION_MINUTES))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingServicePackage() throws Exception {
        // Get the servicePackage
        restServicePackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicePackage() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicePackage
        ServicePackage updatedServicePackage = servicePackageRepository.findById(servicePackage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicePackage are not directly saved in db
        em.detach(updatedServicePackage);
        updatedServicePackage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .mrp(UPDATED_MRP)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(updatedServicePackage);

        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicePackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicePackageToMatchAllProperties(updatedServicePackage);
    }

    @Test
    @Transactional
    void putNonExistingServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicePackageWithPatch() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicePackage using partial update
        ServicePackage partialUpdatedServicePackage = new ServicePackage();
        partialUpdatedServicePackage.setId(servicePackage.getId());

        partialUpdatedServicePackage
            .description(UPDATED_DESCRIPTION)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicePackage))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicePackageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServicePackage, servicePackage),
            getPersistedServicePackage(servicePackage)
        );
    }

    @Test
    @Transactional
    void fullUpdateServicePackageWithPatch() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicePackage using partial update
        ServicePackage partialUpdatedServicePackage = new ServicePackage();
        partialUpdatedServicePackage.setId(servicePackage.getId());

        partialUpdatedServicePackage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .mrp(UPDATED_MRP)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE)
            .sortOrder(UPDATED_SORT_ORDER);

        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicePackage))
            )
            .andExpect(status().isOk());

        // Validate the ServicePackage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicePackageUpdatableFieldsEquals(partialUpdatedServicePackage, getPersistedServicePackage(partialUpdatedServicePackage));
    }

    @Test
    @Transactional
    void patchNonExistingServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicePackageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicePackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicePackage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicePackage.setId(longCount.incrementAndGet());

        // Create the ServicePackage
        ServicePackageDTO servicePackageDTO = servicePackageMapper.toDto(servicePackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicePackageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicePackageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicePackage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicePackage() throws Exception {
        // Initialize the database
        insertedServicePackage = servicePackageRepository.saveAndFlush(servicePackage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicePackage
        restServicePackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicePackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicePackageRepository.count();
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

    protected ServicePackage getPersistedServicePackage(ServicePackage servicePackage) {
        return servicePackageRepository.findById(servicePackage.getId()).orElseThrow();
    }

    protected void assertPersistedServicePackageToMatchAllProperties(ServicePackage expectedServicePackage) {
        assertServicePackageAllPropertiesEquals(expectedServicePackage, getPersistedServicePackage(expectedServicePackage));
    }

    protected void assertPersistedServicePackageToMatchUpdatableProperties(ServicePackage expectedServicePackage) {
        assertServicePackageAllUpdatablePropertiesEquals(expectedServicePackage, getPersistedServicePackage(expectedServicePackage));
    }
}
