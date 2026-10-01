package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CityPackagePriceAsserts.*;
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
import com.limitcross.facility.domain.CityPackagePrice;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.repository.CityPackagePriceRepository;
import com.limitcross.facility.service.CityPackagePriceService;
import com.limitcross.facility.service.dto.CityPackagePriceDTO;
import com.limitcross.facility.service.mapper.CityPackagePriceMapper;
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
 * Integration tests for the {@link CityPackagePriceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CityPackagePriceResourceIT {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_SURGE_MULTIPLIER = new BigDecimal(0);
    private static final BigDecimal UPDATED_SURGE_MULTIPLIER = new BigDecimal(1);

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/city-package-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityPackagePriceRepository cityPackagePriceRepository;

    @Mock
    private CityPackagePriceRepository cityPackagePriceRepositoryMock;

    @Autowired
    private CityPackagePriceMapper cityPackagePriceMapper;

    @Mock
    private CityPackagePriceService cityPackagePriceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityPackagePriceMockMvc;

    private CityPackagePrice cityPackagePrice;

    private CityPackagePrice insertedCityPackagePrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityPackagePrice createEntity(EntityManager em) {
        CityPackagePrice cityPackagePrice = new CityPackagePrice()
            .price(DEFAULT_PRICE)
            .surgeMultiplier(DEFAULT_SURGE_MULTIPLIER)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO);
        // Add required entity
        ServicePackage servicePackage;
        if (TestUtil.findAll(em, ServicePackage.class).isEmpty()) {
            servicePackage = ServicePackageResourceIT.createEntity(em);
            em.persist(servicePackage);
            em.flush();
        } else {
            servicePackage = TestUtil.findAll(em, ServicePackage.class).get(0);
        }
        cityPackagePrice.setServicePackage(servicePackage);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        cityPackagePrice.setCity(city);
        return cityPackagePrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityPackagePrice createUpdatedEntity(EntityManager em) {
        CityPackagePrice updatedCityPackagePrice = new CityPackagePrice()
            .price(UPDATED_PRICE)
            .surgeMultiplier(UPDATED_SURGE_MULTIPLIER)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        // Add required entity
        ServicePackage servicePackage;
        if (TestUtil.findAll(em, ServicePackage.class).isEmpty()) {
            servicePackage = ServicePackageResourceIT.createUpdatedEntity(em);
            em.persist(servicePackage);
            em.flush();
        } else {
            servicePackage = TestUtil.findAll(em, ServicePackage.class).get(0);
        }
        updatedCityPackagePrice.setServicePackage(servicePackage);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedCityPackagePrice.setCity(city);
        return updatedCityPackagePrice;
    }

    @BeforeEach
    void initTest() {
        cityPackagePrice = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCityPackagePrice != null) {
            cityPackagePriceRepository.delete(insertedCityPackagePrice);
            insertedCityPackagePrice = null;
        }
    }

    @Test
    @Transactional
    void createCityPackagePrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);
        var returnedCityPackagePriceDTO = om.readValue(
            restCityPackagePriceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityPackagePriceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CityPackagePriceDTO.class
        );

        // Validate the CityPackagePrice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCityPackagePrice = cityPackagePriceMapper.toEntity(returnedCityPackagePriceDTO);
        assertCityPackagePriceUpdatableFieldsEquals(returnedCityPackagePrice, getPersistedCityPackagePrice(returnedCityPackagePrice));

        insertedCityPackagePrice = returnedCityPackagePrice;
    }

    @Test
    @Transactional
    void createCityPackagePriceWithExistingId() throws Exception {
        // Create the CityPackagePrice with an existing ID
        cityPackagePrice.setId(1L);
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityPackagePriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityPackagePriceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cityPackagePrice.setPrice(null);

        // Create the CityPackagePrice, which fails.
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        restCityPackagePriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityPackagePriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cityPackagePrice.setValidFrom(null);

        // Create the CityPackagePrice, which fails.
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        restCityPackagePriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityPackagePriceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCityPackagePrices() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        // Get all the cityPackagePriceList
        restCityPackagePriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cityPackagePrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].surgeMultiplier").value(hasItem(sameNumber(DEFAULT_SURGE_MULTIPLIER))))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityPackagePricesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cityPackagePriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityPackagePriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cityPackagePriceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityPackagePricesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cityPackagePriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityPackagePriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cityPackagePriceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCityPackagePrice() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        // Get the cityPackagePrice
        restCityPackagePriceMockMvc
            .perform(get(ENTITY_API_URL_ID, cityPackagePrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cityPackagePrice.getId().intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.surgeMultiplier").value(sameNumber(DEFAULT_SURGE_MULTIPLIER)))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCityPackagePrice() throws Exception {
        // Get the cityPackagePrice
        restCityPackagePriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCityPackagePrice() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityPackagePrice
        CityPackagePrice updatedCityPackagePrice = cityPackagePriceRepository.findById(cityPackagePrice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCityPackagePrice are not directly saved in db
        em.detach(updatedCityPackagePrice);
        updatedCityPackagePrice
            .price(UPDATED_PRICE)
            .surgeMultiplier(UPDATED_SURGE_MULTIPLIER)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(updatedCityPackagePrice);

        restCityPackagePriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityPackagePriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityPackagePriceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityPackagePriceToMatchAllProperties(updatedCityPackagePrice);
    }

    @Test
    @Transactional
    void putNonExistingCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityPackagePriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityPackagePriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityPackagePriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityPackagePriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityPackagePriceWithPatch() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityPackagePrice using partial update
        CityPackagePrice partialUpdatedCityPackagePrice = new CityPackagePrice();
        partialUpdatedCityPackagePrice.setId(cityPackagePrice.getId());

        partialUpdatedCityPackagePrice.surgeMultiplier(UPDATED_SURGE_MULTIPLIER);

        restCityPackagePriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityPackagePrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityPackagePrice))
            )
            .andExpect(status().isOk());

        // Validate the CityPackagePrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityPackagePriceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCityPackagePrice, cityPackagePrice),
            getPersistedCityPackagePrice(cityPackagePrice)
        );
    }

    @Test
    @Transactional
    void fullUpdateCityPackagePriceWithPatch() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityPackagePrice using partial update
        CityPackagePrice partialUpdatedCityPackagePrice = new CityPackagePrice();
        partialUpdatedCityPackagePrice.setId(cityPackagePrice.getId());

        partialUpdatedCityPackagePrice
            .price(UPDATED_PRICE)
            .surgeMultiplier(UPDATED_SURGE_MULTIPLIER)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restCityPackagePriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityPackagePrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityPackagePrice))
            )
            .andExpect(status().isOk());

        // Validate the CityPackagePrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityPackagePriceUpdatableFieldsEquals(
            partialUpdatedCityPackagePrice,
            getPersistedCityPackagePrice(partialUpdatedCityPackagePrice)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityPackagePriceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityPackagePriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityPackagePriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCityPackagePrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityPackagePrice.setId(longCount.incrementAndGet());

        // Create the CityPackagePrice
        CityPackagePriceDTO cityPackagePriceDTO = cityPackagePriceMapper.toDto(cityPackagePrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityPackagePriceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityPackagePriceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityPackagePrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCityPackagePrice() throws Exception {
        // Initialize the database
        insertedCityPackagePrice = cityPackagePriceRepository.saveAndFlush(cityPackagePrice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cityPackagePrice
        restCityPackagePriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, cityPackagePrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cityPackagePriceRepository.count();
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

    protected CityPackagePrice getPersistedCityPackagePrice(CityPackagePrice cityPackagePrice) {
        return cityPackagePriceRepository.findById(cityPackagePrice.getId()).orElseThrow();
    }

    protected void assertPersistedCityPackagePriceToMatchAllProperties(CityPackagePrice expectedCityPackagePrice) {
        assertCityPackagePriceAllPropertiesEquals(expectedCityPackagePrice, getPersistedCityPackagePrice(expectedCityPackagePrice));
    }

    protected void assertPersistedCityPackagePriceToMatchUpdatableProperties(CityPackagePrice expectedCityPackagePrice) {
        assertCityPackagePriceAllUpdatablePropertiesEquals(
            expectedCityPackagePrice,
            getPersistedCityPackagePrice(expectedCityPackagePrice)
        );
    }
}
