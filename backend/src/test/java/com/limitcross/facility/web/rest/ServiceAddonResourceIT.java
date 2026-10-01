package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ServiceAddonAsserts.*;
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
import com.limitcross.facility.domain.ServiceAddon;
import com.limitcross.facility.repository.ServiceAddonRepository;
import com.limitcross.facility.service.ServiceAddonService;
import com.limitcross.facility.service.dto.ServiceAddonDTO;
import com.limitcross.facility.service.mapper.ServiceAddonMapper;
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
 * Integration tests for the {@link ServiceAddonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceAddonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_DURATION_MINUTES = 0;
    private static final Integer UPDATED_DURATION_MINUTES = 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/service-addons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceAddonRepository serviceAddonRepository;

    @Mock
    private ServiceAddonRepository serviceAddonRepositoryMock;

    @Autowired
    private ServiceAddonMapper serviceAddonMapper;

    @Mock
    private ServiceAddonService serviceAddonServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceAddonMockMvc;

    private ServiceAddon serviceAddon;

    private ServiceAddon insertedServiceAddon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceAddon createEntity(EntityManager em) {
        ServiceAddon serviceAddon = new ServiceAddon()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .durationMinutes(DEFAULT_DURATION_MINUTES)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        serviceAddon.setService(facilityService);
        return serviceAddon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceAddon createUpdatedEntity(EntityManager em) {
        ServiceAddon updatedServiceAddon = new ServiceAddon()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .active(UPDATED_ACTIVE);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedServiceAddon.setService(facilityService);
        return updatedServiceAddon;
    }

    @BeforeEach
    void initTest() {
        serviceAddon = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceAddon != null) {
            serviceAddonRepository.delete(insertedServiceAddon);
            insertedServiceAddon = null;
        }
    }

    @Test
    @Transactional
    void createServiceAddon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);
        var returnedServiceAddonDTO = om.readValue(
            restServiceAddonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceAddonDTO.class
        );

        // Validate the ServiceAddon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceAddon = serviceAddonMapper.toEntity(returnedServiceAddonDTO);
        assertServiceAddonUpdatableFieldsEquals(returnedServiceAddon, getPersistedServiceAddon(returnedServiceAddon));

        insertedServiceAddon = returnedServiceAddon;
    }

    @Test
    @Transactional
    void createServiceAddonWithExistingId() throws Exception {
        // Create the ServiceAddon with an existing ID
        serviceAddon.setId(1L);
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceAddonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceAddon.setName(null);

        // Create the ServiceAddon, which fails.
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        restServiceAddonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceAddon.setPrice(null);

        // Create the ServiceAddon, which fails.
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        restServiceAddonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceAddon.setActive(null);

        // Create the ServiceAddon, which fails.
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        restServiceAddonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceAddons() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        // Get all the serviceAddonList
        restServiceAddonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceAddon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceAddonsWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceAddonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceAddonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceAddonServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceAddonsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceAddonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceAddonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceAddonRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceAddon() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        // Get the serviceAddon
        restServiceAddonMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceAddon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceAddon.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.durationMinutes").value(DEFAULT_DURATION_MINUTES))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingServiceAddon() throws Exception {
        // Get the serviceAddon
        restServiceAddonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceAddon() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceAddon
        ServiceAddon updatedServiceAddon = serviceAddonRepository.findById(serviceAddon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceAddon are not directly saved in db
        em.detach(updatedServiceAddon);
        updatedServiceAddon.name(UPDATED_NAME).price(UPDATED_PRICE).durationMinutes(UPDATED_DURATION_MINUTES).active(UPDATED_ACTIVE);
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(updatedServiceAddon);

        restServiceAddonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceAddonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceAddonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceAddonToMatchAllProperties(updatedServiceAddon);
    }

    @Test
    @Transactional
    void putNonExistingServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceAddonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceAddonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceAddonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceAddonWithPatch() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceAddon using partial update
        ServiceAddon partialUpdatedServiceAddon = new ServiceAddon();
        partialUpdatedServiceAddon.setId(serviceAddon.getId());

        partialUpdatedServiceAddon.name(UPDATED_NAME).durationMinutes(UPDATED_DURATION_MINUTES);

        restServiceAddonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceAddon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceAddon))
            )
            .andExpect(status().isOk());

        // Validate the ServiceAddon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceAddonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceAddon, serviceAddon),
            getPersistedServiceAddon(serviceAddon)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceAddonWithPatch() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceAddon using partial update
        ServiceAddon partialUpdatedServiceAddon = new ServiceAddon();
        partialUpdatedServiceAddon.setId(serviceAddon.getId());

        partialUpdatedServiceAddon.name(UPDATED_NAME).price(UPDATED_PRICE).durationMinutes(UPDATED_DURATION_MINUTES).active(UPDATED_ACTIVE);

        restServiceAddonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceAddon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceAddon))
            )
            .andExpect(status().isOk());

        // Validate the ServiceAddon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceAddonUpdatableFieldsEquals(partialUpdatedServiceAddon, getPersistedServiceAddon(partialUpdatedServiceAddon));
    }

    @Test
    @Transactional
    void patchNonExistingServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceAddonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceAddonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceAddonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceAddon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceAddon.setId(longCount.incrementAndGet());

        // Create the ServiceAddon
        ServiceAddonDTO serviceAddonDTO = serviceAddonMapper.toDto(serviceAddon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceAddonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceAddonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceAddon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceAddon() throws Exception {
        // Initialize the database
        insertedServiceAddon = serviceAddonRepository.saveAndFlush(serviceAddon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceAddon
        restServiceAddonMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceAddon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceAddonRepository.count();
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

    protected ServiceAddon getPersistedServiceAddon(ServiceAddon serviceAddon) {
        return serviceAddonRepository.findById(serviceAddon.getId()).orElseThrow();
    }

    protected void assertPersistedServiceAddonToMatchAllProperties(ServiceAddon expectedServiceAddon) {
        assertServiceAddonAllPropertiesEquals(expectedServiceAddon, getPersistedServiceAddon(expectedServiceAddon));
    }

    protected void assertPersistedServiceAddonToMatchUpdatableProperties(ServiceAddon expectedServiceAddon) {
        assertServiceAddonAllUpdatablePropertiesEquals(expectedServiceAddon, getPersistedServiceAddon(expectedServiceAddon));
    }
}
