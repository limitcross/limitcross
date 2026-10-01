package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CouponRedemptionAsserts.*;
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
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.CouponRedemption;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.repository.CouponRedemptionRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.CouponRedemptionService;
import com.limitcross.facility.service.dto.CouponRedemptionDTO;
import com.limitcross.facility.service.mapper.CouponRedemptionMapper;
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
 * Integration tests for the {@link CouponRedemptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CouponRedemptionResourceIT {

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(1);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/coupon-redemptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CouponRedemptionRepository couponRedemptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CouponRedemptionRepository couponRedemptionRepositoryMock;

    @Autowired
    private CouponRedemptionMapper couponRedemptionMapper;

    @Mock
    private CouponRedemptionService couponRedemptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponRedemptionMockMvc;

    private CouponRedemption couponRedemption;

    private CouponRedemption insertedCouponRedemption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponRedemption createEntity(EntityManager em) {
        CouponRedemption couponRedemption = new CouponRedemption().discountAmount(DEFAULT_DISCOUNT_AMOUNT).createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        couponRedemption.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        couponRedemption.setUser(user);
        // Add required entity
        Coupon coupon;
        if (TestUtil.findAll(em, Coupon.class).isEmpty()) {
            coupon = CouponResourceIT.createEntity();
            em.persist(coupon);
            em.flush();
        } else {
            coupon = TestUtil.findAll(em, Coupon.class).get(0);
        }
        couponRedemption.setCoupon(coupon);
        return couponRedemption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouponRedemption createUpdatedEntity(EntityManager em) {
        CouponRedemption updatedCouponRedemption = new CouponRedemption()
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
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
        updatedCouponRedemption.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCouponRedemption.setUser(user);
        // Add required entity
        Coupon coupon;
        if (TestUtil.findAll(em, Coupon.class).isEmpty()) {
            coupon = CouponResourceIT.createUpdatedEntity();
            em.persist(coupon);
            em.flush();
        } else {
            coupon = TestUtil.findAll(em, Coupon.class).get(0);
        }
        updatedCouponRedemption.setCoupon(coupon);
        return updatedCouponRedemption;
    }

    @BeforeEach
    void initTest() {
        couponRedemption = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCouponRedemption != null) {
            couponRedemptionRepository.delete(insertedCouponRedemption);
            insertedCouponRedemption = null;
        }
    }

    @Test
    @Transactional
    void createCouponRedemption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);
        var returnedCouponRedemptionDTO = om.readValue(
            restCouponRedemptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponRedemptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CouponRedemptionDTO.class
        );

        // Validate the CouponRedemption in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCouponRedemption = couponRedemptionMapper.toEntity(returnedCouponRedemptionDTO);
        assertCouponRedemptionUpdatableFieldsEquals(returnedCouponRedemption, getPersistedCouponRedemption(returnedCouponRedemption));

        insertedCouponRedemption = returnedCouponRedemption;
    }

    @Test
    @Transactional
    void createCouponRedemptionWithExistingId() throws Exception {
        // Create the CouponRedemption with an existing ID
        couponRedemption.setId(1L);
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponRedemptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponRedemptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDiscountAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        couponRedemption.setDiscountAmount(null);

        // Create the CouponRedemption, which fails.
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        restCouponRedemptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponRedemptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCouponRedemptions() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        // Get all the couponRedemptionList
        restCouponRedemptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couponRedemption.getId().intValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouponRedemptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(couponRedemptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCouponRedemptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(couponRedemptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouponRedemptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(couponRedemptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCouponRedemptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(couponRedemptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCouponRedemption() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        // Get the couponRedemption
        restCouponRedemptionMockMvc
            .perform(get(ENTITY_API_URL_ID, couponRedemption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(couponRedemption.getId().intValue()))
            .andExpect(jsonPath("$.discountAmount").value(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCouponRedemption() throws Exception {
        // Get the couponRedemption
        restCouponRedemptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCouponRedemption() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couponRedemption
        CouponRedemption updatedCouponRedemption = couponRedemptionRepository.findById(couponRedemption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCouponRedemption are not directly saved in db
        em.detach(updatedCouponRedemption);
        updatedCouponRedemption.discountAmount(UPDATED_DISCOUNT_AMOUNT).createdAt(UPDATED_CREATED_AT);
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(updatedCouponRedemption);

        restCouponRedemptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponRedemptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couponRedemptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCouponRedemptionToMatchAllProperties(updatedCouponRedemption);
    }

    @Test
    @Transactional
    void putNonExistingCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponRedemptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couponRedemptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couponRedemptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponRedemptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponRedemptionWithPatch() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couponRedemption using partial update
        CouponRedemption partialUpdatedCouponRedemption = new CouponRedemption();
        partialUpdatedCouponRedemption.setId(couponRedemption.getId());

        partialUpdatedCouponRedemption.discountAmount(UPDATED_DISCOUNT_AMOUNT).createdAt(UPDATED_CREATED_AT);

        restCouponRedemptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponRedemption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCouponRedemption))
            )
            .andExpect(status().isOk());

        // Validate the CouponRedemption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponRedemptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCouponRedemption, couponRedemption),
            getPersistedCouponRedemption(couponRedemption)
        );
    }

    @Test
    @Transactional
    void fullUpdateCouponRedemptionWithPatch() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couponRedemption using partial update
        CouponRedemption partialUpdatedCouponRedemption = new CouponRedemption();
        partialUpdatedCouponRedemption.setId(couponRedemption.getId());

        partialUpdatedCouponRedemption.discountAmount(UPDATED_DISCOUNT_AMOUNT).createdAt(UPDATED_CREATED_AT);

        restCouponRedemptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouponRedemption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCouponRedemption))
            )
            .andExpect(status().isOk());

        // Validate the CouponRedemption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponRedemptionUpdatableFieldsEquals(
            partialUpdatedCouponRedemption,
            getPersistedCouponRedemption(partialUpdatedCouponRedemption)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couponRedemptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponRedemptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponRedemptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCouponRedemption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couponRedemption.setId(longCount.incrementAndGet());

        // Create the CouponRedemption
        CouponRedemptionDTO couponRedemptionDTO = couponRedemptionMapper.toDto(couponRedemption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponRedemptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(couponRedemptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouponRedemption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCouponRedemption() throws Exception {
        // Initialize the database
        insertedCouponRedemption = couponRedemptionRepository.saveAndFlush(couponRedemption);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the couponRedemption
        restCouponRedemptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, couponRedemption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return couponRedemptionRepository.count();
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

    protected CouponRedemption getPersistedCouponRedemption(CouponRedemption couponRedemption) {
        return couponRedemptionRepository.findById(couponRedemption.getId()).orElseThrow();
    }

    protected void assertPersistedCouponRedemptionToMatchAllProperties(CouponRedemption expectedCouponRedemption) {
        assertCouponRedemptionAllPropertiesEquals(expectedCouponRedemption, getPersistedCouponRedemption(expectedCouponRedemption));
    }

    protected void assertPersistedCouponRedemptionToMatchUpdatableProperties(CouponRedemption expectedCouponRedemption) {
        assertCouponRedemptionAllUpdatablePropertiesEquals(
            expectedCouponRedemption,
            getPersistedCouponRedemption(expectedCouponRedemption)
        );
    }
}
