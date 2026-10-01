package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ServiceTranslationAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceTranslation;
import com.limitcross.facility.repository.ServiceTranslationRepository;
import com.limitcross.facility.service.ServiceTranslationService;
import com.limitcross.facility.service.dto.ServiceTranslationDTO;
import com.limitcross.facility.service.mapper.ServiceTranslationMapper;
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
 * Integration tests for the {@link ServiceTranslationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceTranslationResourceIT {

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceTranslationRepository serviceTranslationRepository;

    @Mock
    private ServiceTranslationRepository serviceTranslationRepositoryMock;

    @Autowired
    private ServiceTranslationMapper serviceTranslationMapper;

    @Mock
    private ServiceTranslationService serviceTranslationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceTranslationMockMvc;

    private ServiceTranslation serviceTranslation;

    private ServiceTranslation insertedServiceTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceTranslation createEntity(EntityManager em) {
        ServiceTranslation serviceTranslation = new ServiceTranslation()
            .langKey(DEFAULT_LANG_KEY)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        serviceTranslation.setService(facilityService);
        return serviceTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceTranslation createUpdatedEntity(EntityManager em) {
        ServiceTranslation updatedServiceTranslation = new ServiceTranslation()
            .langKey(UPDATED_LANG_KEY)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedServiceTranslation.setService(facilityService);
        return updatedServiceTranslation;
    }

    @BeforeEach
    void initTest() {
        serviceTranslation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceTranslation != null) {
            serviceTranslationRepository.delete(insertedServiceTranslation);
            insertedServiceTranslation = null;
        }
    }

    @Test
    @Transactional
    void createServiceTranslation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);
        var returnedServiceTranslationDTO = om.readValue(
            restServiceTranslationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceTranslationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceTranslationDTO.class
        );

        // Validate the ServiceTranslation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceTranslation = serviceTranslationMapper.toEntity(returnedServiceTranslationDTO);
        assertServiceTranslationUpdatableFieldsEquals(
            returnedServiceTranslation,
            getPersistedServiceTranslation(returnedServiceTranslation)
        );

        insertedServiceTranslation = returnedServiceTranslation;
    }

    @Test
    @Transactional
    void createServiceTranslationWithExistingId() throws Exception {
        // Create the ServiceTranslation with an existing ID
        serviceTranslation.setId(1L);
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceTranslationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceTranslationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLangKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceTranslation.setLangKey(null);

        // Create the ServiceTranslation, which fails.
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        restServiceTranslationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceTranslationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceTranslation.setTitle(null);

        // Create the ServiceTranslation, which fails.
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        restServiceTranslationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceTranslationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceTranslations() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        // Get all the serviceTranslationList
        restServiceTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceTranslationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceTranslationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceTranslationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceTranslationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceTranslationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceTranslationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceTranslationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceTranslationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceTranslation() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        // Get the serviceTranslation
        restServiceTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceTranslation.getId().intValue()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingServiceTranslation() throws Exception {
        // Get the serviceTranslation
        restServiceTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceTranslation() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceTranslation
        ServiceTranslation updatedServiceTranslation = serviceTranslationRepository.findById(serviceTranslation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceTranslation are not directly saved in db
        em.detach(updatedServiceTranslation);
        updatedServiceTranslation.langKey(UPDATED_LANG_KEY).title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(updatedServiceTranslation);

        restServiceTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceTranslationToMatchAllProperties(updatedServiceTranslation);
    }

    @Test
    @Transactional
    void putNonExistingServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceTranslationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceTranslationWithPatch() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceTranslation using partial update
        ServiceTranslation partialUpdatedServiceTranslation = new ServiceTranslation();
        partialUpdatedServiceTranslation.setId(serviceTranslation.getId());

        partialUpdatedServiceTranslation.langKey(UPDATED_LANG_KEY);

        restServiceTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ServiceTranslation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceTranslationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceTranslation, serviceTranslation),
            getPersistedServiceTranslation(serviceTranslation)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceTranslationWithPatch() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceTranslation using partial update
        ServiceTranslation partialUpdatedServiceTranslation = new ServiceTranslation();
        partialUpdatedServiceTranslation.setId(serviceTranslation.getId());

        partialUpdatedServiceTranslation.langKey(UPDATED_LANG_KEY).title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restServiceTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ServiceTranslation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceTranslationUpdatableFieldsEquals(
            partialUpdatedServiceTranslation,
            getPersistedServiceTranslation(partialUpdatedServiceTranslation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceTranslation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceTranslation.setId(longCount.incrementAndGet());

        // Create the ServiceTranslation
        ServiceTranslationDTO serviceTranslationDTO = serviceTranslationMapper.toDto(serviceTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceTranslationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceTranslationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceTranslation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceTranslation() throws Exception {
        // Initialize the database
        insertedServiceTranslation = serviceTranslationRepository.saveAndFlush(serviceTranslation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceTranslation
        restServiceTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceTranslationRepository.count();
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

    protected ServiceTranslation getPersistedServiceTranslation(ServiceTranslation serviceTranslation) {
        return serviceTranslationRepository.findById(serviceTranslation.getId()).orElseThrow();
    }

    protected void assertPersistedServiceTranslationToMatchAllProperties(ServiceTranslation expectedServiceTranslation) {
        assertServiceTranslationAllPropertiesEquals(expectedServiceTranslation, getPersistedServiceTranslation(expectedServiceTranslation));
    }

    protected void assertPersistedServiceTranslationToMatchUpdatableProperties(ServiceTranslation expectedServiceTranslation) {
        assertServiceTranslationAllUpdatablePropertiesEquals(
            expectedServiceTranslation,
            getPersistedServiceTranslation(expectedServiceTranslation)
        );
    }
}
