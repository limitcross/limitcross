package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.SlotCapacityAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.repository.SlotCapacityRepository;
import com.limitcross.facility.service.SlotCapacityService;
import com.limitcross.facility.service.dto.SlotCapacityDTO;
import com.limitcross.facility.service.mapper.SlotCapacityMapper;
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
 * Integration tests for the {@link SlotCapacityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SlotCapacityResourceIT {

    private static final Instant DEFAULT_SLOT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SLOT_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SLOT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SLOT_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CAPACITY = 0;
    private static final Integer UPDATED_CAPACITY = 1;

    private static final Integer DEFAULT_BOOKED_COUNT = 0;
    private static final Integer UPDATED_BOOKED_COUNT = 1;

    private static final Boolean DEFAULT_BLOCKED = false;
    private static final Boolean UPDATED_BLOCKED = true;

    private static final BigDecimal DEFAULT_PRICE_MULTIPLIER = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_MULTIPLIER = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/slot-capacities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SlotCapacityRepository slotCapacityRepository;

    @Mock
    private SlotCapacityRepository slotCapacityRepositoryMock;

    @Autowired
    private SlotCapacityMapper slotCapacityMapper;

    @Mock
    private SlotCapacityService slotCapacityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotCapacityMockMvc;

    private SlotCapacity slotCapacity;

    private SlotCapacity insertedSlotCapacity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotCapacity createEntity(EntityManager em) {
        SlotCapacity slotCapacity = new SlotCapacity()
            .slotStart(DEFAULT_SLOT_START)
            .slotEnd(DEFAULT_SLOT_END)
            .capacity(DEFAULT_CAPACITY)
            .bookedCount(DEFAULT_BOOKED_COUNT)
            .blocked(DEFAULT_BLOCKED)
            .priceMultiplier(DEFAULT_PRICE_MULTIPLIER);
        // Add required entity
        ServiceZone serviceZone;
        if (TestUtil.findAll(em, ServiceZone.class).isEmpty()) {
            serviceZone = ServiceZoneResourceIT.createEntity(em);
            em.persist(serviceZone);
            em.flush();
        } else {
            serviceZone = TestUtil.findAll(em, ServiceZone.class).get(0);
        }
        slotCapacity.setZone(serviceZone);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        slotCapacity.setCategory(serviceCategory);
        return slotCapacity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotCapacity createUpdatedEntity(EntityManager em) {
        SlotCapacity updatedSlotCapacity = new SlotCapacity()
            .slotStart(UPDATED_SLOT_START)
            .slotEnd(UPDATED_SLOT_END)
            .capacity(UPDATED_CAPACITY)
            .bookedCount(UPDATED_BOOKED_COUNT)
            .blocked(UPDATED_BLOCKED)
            .priceMultiplier(UPDATED_PRICE_MULTIPLIER);
        // Add required entity
        ServiceZone serviceZone;
        if (TestUtil.findAll(em, ServiceZone.class).isEmpty()) {
            serviceZone = ServiceZoneResourceIT.createUpdatedEntity(em);
            em.persist(serviceZone);
            em.flush();
        } else {
            serviceZone = TestUtil.findAll(em, ServiceZone.class).get(0);
        }
        updatedSlotCapacity.setZone(serviceZone);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createUpdatedEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        updatedSlotCapacity.setCategory(serviceCategory);
        return updatedSlotCapacity;
    }

    @BeforeEach
    void initTest() {
        slotCapacity = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSlotCapacity != null) {
            slotCapacityRepository.delete(insertedSlotCapacity);
            insertedSlotCapacity = null;
        }
    }

    @Test
    @Transactional
    void createSlotCapacity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);
        var returnedSlotCapacityDTO = om.readValue(
            restSlotCapacityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SlotCapacityDTO.class
        );

        // Validate the SlotCapacity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSlotCapacity = slotCapacityMapper.toEntity(returnedSlotCapacityDTO);
        assertSlotCapacityUpdatableFieldsEquals(returnedSlotCapacity, getPersistedSlotCapacity(returnedSlotCapacity));

        insertedSlotCapacity = returnedSlotCapacity;
    }

    @Test
    @Transactional
    void createSlotCapacityWithExistingId() throws Exception {
        // Create the SlotCapacity with an existing ID
        slotCapacity.setId(1L);
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSlotStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotCapacity.setSlotStart(null);

        // Create the SlotCapacity, which fails.
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        restSlotCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlotEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotCapacity.setSlotEnd(null);

        // Create the SlotCapacity, which fails.
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        restSlotCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotCapacity.setCapacity(null);

        // Create the SlotCapacity, which fails.
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        restSlotCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBookedCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slotCapacity.setBookedCount(null);

        // Create the SlotCapacity, which fails.
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        restSlotCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSlotCapacities() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        // Get all the slotCapacityList
        restSlotCapacityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slotCapacity.getId().intValue())))
            .andExpect(jsonPath("$.[*].slotStart").value(hasItem(DEFAULT_SLOT_START.toString())))
            .andExpect(jsonPath("$.[*].slotEnd").value(hasItem(DEFAULT_SLOT_END.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].bookedCount").value(hasItem(DEFAULT_BOOKED_COUNT)))
            .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED)))
            .andExpect(jsonPath("$.[*].priceMultiplier").value(hasItem(sameNumber(DEFAULT_PRICE_MULTIPLIER))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotCapacitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(slotCapacityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotCapacityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(slotCapacityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotCapacitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(slotCapacityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotCapacityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(slotCapacityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSlotCapacity() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        // Get the slotCapacity
        restSlotCapacityMockMvc
            .perform(get(ENTITY_API_URL_ID, slotCapacity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slotCapacity.getId().intValue()))
            .andExpect(jsonPath("$.slotStart").value(DEFAULT_SLOT_START.toString()))
            .andExpect(jsonPath("$.slotEnd").value(DEFAULT_SLOT_END.toString()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.bookedCount").value(DEFAULT_BOOKED_COUNT))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED))
            .andExpect(jsonPath("$.priceMultiplier").value(sameNumber(DEFAULT_PRICE_MULTIPLIER)));
    }

    @Test
    @Transactional
    void getNonExistingSlotCapacity() throws Exception {
        // Get the slotCapacity
        restSlotCapacityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSlotCapacity() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotCapacity
        SlotCapacity updatedSlotCapacity = slotCapacityRepository.findById(slotCapacity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSlotCapacity are not directly saved in db
        em.detach(updatedSlotCapacity);
        updatedSlotCapacity
            .slotStart(UPDATED_SLOT_START)
            .slotEnd(UPDATED_SLOT_END)
            .capacity(UPDATED_CAPACITY)
            .bookedCount(UPDATED_BOOKED_COUNT)
            .blocked(UPDATED_BLOCKED)
            .priceMultiplier(UPDATED_PRICE_MULTIPLIER);
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(updatedSlotCapacity);

        restSlotCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotCapacityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotCapacityDTO))
            )
            .andExpect(status().isOk());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSlotCapacityToMatchAllProperties(updatedSlotCapacity);
    }

    @Test
    @Transactional
    void putNonExistingSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slotCapacityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotCapacityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotCapacityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSlotCapacityWithPatch() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotCapacity using partial update
        SlotCapacity partialUpdatedSlotCapacity = new SlotCapacity();
        partialUpdatedSlotCapacity.setId(slotCapacity.getId());

        partialUpdatedSlotCapacity.slotStart(UPDATED_SLOT_START).slotEnd(UPDATED_SLOT_END).blocked(UPDATED_BLOCKED);

        restSlotCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotCapacity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotCapacity))
            )
            .andExpect(status().isOk());

        // Validate the SlotCapacity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotCapacityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSlotCapacity, slotCapacity),
            getPersistedSlotCapacity(slotCapacity)
        );
    }

    @Test
    @Transactional
    void fullUpdateSlotCapacityWithPatch() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slotCapacity using partial update
        SlotCapacity partialUpdatedSlotCapacity = new SlotCapacity();
        partialUpdatedSlotCapacity.setId(slotCapacity.getId());

        partialUpdatedSlotCapacity
            .slotStart(UPDATED_SLOT_START)
            .slotEnd(UPDATED_SLOT_END)
            .capacity(UPDATED_CAPACITY)
            .bookedCount(UPDATED_BOOKED_COUNT)
            .blocked(UPDATED_BLOCKED)
            .priceMultiplier(UPDATED_PRICE_MULTIPLIER);

        restSlotCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlotCapacity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlotCapacity))
            )
            .andExpect(status().isOk());

        // Validate the SlotCapacity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotCapacityUpdatableFieldsEquals(partialUpdatedSlotCapacity, getPersistedSlotCapacity(partialUpdatedSlotCapacity));
    }

    @Test
    @Transactional
    void patchNonExistingSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, slotCapacityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotCapacityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotCapacityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSlotCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slotCapacity.setId(longCount.incrementAndGet());

        // Create the SlotCapacity
        SlotCapacityDTO slotCapacityDTO = slotCapacityMapper.toDto(slotCapacity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotCapacityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(slotCapacityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SlotCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSlotCapacity() throws Exception {
        // Initialize the database
        insertedSlotCapacity = slotCapacityRepository.saveAndFlush(slotCapacity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the slotCapacity
        restSlotCapacityMockMvc
            .perform(delete(ENTITY_API_URL_ID, slotCapacity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return slotCapacityRepository.count();
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

    protected SlotCapacity getPersistedSlotCapacity(SlotCapacity slotCapacity) {
        return slotCapacityRepository.findById(slotCapacity.getId()).orElseThrow();
    }

    protected void assertPersistedSlotCapacityToMatchAllProperties(SlotCapacity expectedSlotCapacity) {
        assertSlotCapacityAllPropertiesEquals(expectedSlotCapacity, getPersistedSlotCapacity(expectedSlotCapacity));
    }

    protected void assertPersistedSlotCapacityToMatchUpdatableProperties(SlotCapacity expectedSlotCapacity) {
        assertSlotCapacityAllUpdatablePropertiesEquals(expectedSlotCapacity, getPersistedSlotCapacity(expectedSlotCapacity));
    }
}
