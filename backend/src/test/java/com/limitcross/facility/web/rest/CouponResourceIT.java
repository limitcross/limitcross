package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CouponAsserts.*;
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
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.enumeration.DiscountType;
import com.limitcross.facility.repository.CouponRepository;
import com.limitcross.facility.service.CouponService;
import com.limitcross.facility.service.dto.CouponDTO;
import com.limitcross.facility.service.mapper.CouponMapper;
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
 * Integration tests for the {@link CouponResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CouponResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENT;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.FLAT;

    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISCOUNT_VALUE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_MAX_DISCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MAX_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_MAX_DISCOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_MIN_ORDER_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_MIN_ORDER_VALUE = new BigDecimal(1);
    private static final BigDecimal SMALLER_MIN_ORDER_VALUE = new BigDecimal(0 - 1);

    private static final Boolean DEFAULT_FIRST_BOOKING_ONLY = false;
    private static final Boolean UPDATED_FIRST_BOOKING_ONLY = true;

    private static final Integer DEFAULT_TOTAL_USAGE_LIMIT = 0;
    private static final Integer UPDATED_TOTAL_USAGE_LIMIT = 1;
    private static final Integer SMALLER_TOTAL_USAGE_LIMIT = 0 - 1;

    private static final Integer DEFAULT_PER_USER_LIMIT = 1;
    private static final Integer UPDATED_PER_USER_LIMIT = 2;
    private static final Integer SMALLER_PER_USER_LIMIT = 1 - 1;

    private static final Integer DEFAULT_USED_COUNT = 0;
    private static final Integer UPDATED_USED_COUNT = 1;
    private static final Integer SMALLER_USED_COUNT = 0 - 1;

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/coupons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CouponRepository couponRepository;

    @Mock
    private CouponRepository couponRepositoryMock;

    @Autowired
    private CouponMapper couponMapper;

    @Mock
    private CouponService couponServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouponMockMvc;

    private Coupon coupon;

    private Coupon insertedCoupon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createEntity() {
        return new Coupon()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .maxDiscount(DEFAULT_MAX_DISCOUNT)
            .minOrderValue(DEFAULT_MIN_ORDER_VALUE)
            .firstBookingOnly(DEFAULT_FIRST_BOOKING_ONLY)
            .totalUsageLimit(DEFAULT_TOTAL_USAGE_LIMIT)
            .perUserLimit(DEFAULT_PER_USER_LIMIT)
            .usedCount(DEFAULT_USED_COUNT)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coupon createUpdatedEntity() {
        return new Coupon()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxDiscount(UPDATED_MAX_DISCOUNT)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .firstBookingOnly(UPDATED_FIRST_BOOKING_ONLY)
            .totalUsageLimit(UPDATED_TOTAL_USAGE_LIMIT)
            .perUserLimit(UPDATED_PER_USER_LIMIT)
            .usedCount(UPDATED_USED_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        coupon = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCoupon != null) {
            couponRepository.delete(insertedCoupon);
            insertedCoupon = null;
        }
    }

    @Test
    @Transactional
    void createCoupon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);
        var returnedCouponDTO = om.readValue(
            restCouponMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CouponDTO.class
        );

        // Validate the Coupon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCoupon = couponMapper.toEntity(returnedCouponDTO);
        assertCouponUpdatableFieldsEquals(returnedCoupon, getPersistedCoupon(returnedCoupon));

        insertedCoupon = returnedCoupon;
    }

    @Test
    @Transactional
    void createCouponWithExistingId() throws Exception {
        // Create the Coupon with an existing ID
        coupon.setId(1L);
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setCode(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setDiscountType(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setDiscountValue(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidFromIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setValidFrom(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValidToIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setValidTo(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        coupon.setActive(null);

        // Create the Coupon, which fails.
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        restCouponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoupons() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].maxDiscount").value(hasItem(sameNumber(DEFAULT_MAX_DISCOUNT))))
            .andExpect(jsonPath("$.[*].minOrderValue").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_VALUE))))
            .andExpect(jsonPath("$.[*].firstBookingOnly").value(hasItem(DEFAULT_FIRST_BOOKING_ONLY)))
            .andExpect(jsonPath("$.[*].totalUsageLimit").value(hasItem(DEFAULT_TOTAL_USAGE_LIMIT)))
            .andExpect(jsonPath("$.[*].perUserLimit").value(hasItem(DEFAULT_PER_USER_LIMIT)))
            .andExpect(jsonPath("$.[*].usedCount").value(hasItem(DEFAULT_USED_COUNT)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouponsWithEagerRelationshipsIsEnabled() throws Exception {
        when(couponServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCouponMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(couponServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouponsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(couponServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCouponMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(couponRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get the coupon
        restCouponMockMvc
            .perform(get(ENTITY_API_URL_ID, coupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coupon.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discountValue").value(sameNumber(DEFAULT_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.maxDiscount").value(sameNumber(DEFAULT_MAX_DISCOUNT)))
            .andExpect(jsonPath("$.minOrderValue").value(sameNumber(DEFAULT_MIN_ORDER_VALUE)))
            .andExpect(jsonPath("$.firstBookingOnly").value(DEFAULT_FIRST_BOOKING_ONLY))
            .andExpect(jsonPath("$.totalUsageLimit").value(DEFAULT_TOTAL_USAGE_LIMIT))
            .andExpect(jsonPath("$.perUserLimit").value(DEFAULT_PER_USER_LIMIT))
            .andExpect(jsonPath("$.usedCount").value(DEFAULT_USED_COUNT))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getCouponsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        Long id = coupon.getId();

        defaultCouponFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCouponFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCouponFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code equals to
        defaultCouponFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code in
        defaultCouponFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code is not null
        defaultCouponFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code contains
        defaultCouponFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where code does not contain
        defaultCouponFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCouponsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where description equals to
        defaultCouponFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCouponsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where description in
        defaultCouponFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where description is not null
        defaultCouponFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where description contains
        defaultCouponFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCouponsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where description does not contain
        defaultCouponFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType equals to
        defaultCouponFiltering("discountType.equals=" + DEFAULT_DISCOUNT_TYPE, "discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType in
        defaultCouponFiltering(
            "discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE,
            "discountType.in=" + UPDATED_DISCOUNT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountType is not null
        defaultCouponFiltering("discountType.specified=true", "discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue equals to
        defaultCouponFiltering("discountValue.equals=" + DEFAULT_DISCOUNT_VALUE, "discountValue.equals=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue in
        defaultCouponFiltering(
            "discountValue.in=" + DEFAULT_DISCOUNT_VALUE + "," + UPDATED_DISCOUNT_VALUE,
            "discountValue.in=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is not null
        defaultCouponFiltering("discountValue.specified=true", "discountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is greater than or equal to
        defaultCouponFiltering(
            "discountValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is less than or equal to
        defaultCouponFiltering(
            "discountValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_VALUE,
            "discountValue.lessThanOrEqual=" + SMALLER_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is less than
        defaultCouponFiltering("discountValue.lessThan=" + UPDATED_DISCOUNT_VALUE, "discountValue.lessThan=" + DEFAULT_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where discountValue is greater than
        defaultCouponFiltering(
            "discountValue.greaterThan=" + SMALLER_DISCOUNT_VALUE,
            "discountValue.greaterThan=" + DEFAULT_DISCOUNT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount equals to
        defaultCouponFiltering("maxDiscount.equals=" + DEFAULT_MAX_DISCOUNT, "maxDiscount.equals=" + UPDATED_MAX_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount in
        defaultCouponFiltering(
            "maxDiscount.in=" + DEFAULT_MAX_DISCOUNT + "," + UPDATED_MAX_DISCOUNT,
            "maxDiscount.in=" + UPDATED_MAX_DISCOUNT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount is not null
        defaultCouponFiltering("maxDiscount.specified=true", "maxDiscount.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount is greater than or equal to
        defaultCouponFiltering(
            "maxDiscount.greaterThanOrEqual=" + DEFAULT_MAX_DISCOUNT,
            "maxDiscount.greaterThanOrEqual=" + UPDATED_MAX_DISCOUNT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount is less than or equal to
        defaultCouponFiltering(
            "maxDiscount.lessThanOrEqual=" + DEFAULT_MAX_DISCOUNT,
            "maxDiscount.lessThanOrEqual=" + SMALLER_MAX_DISCOUNT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount is less than
        defaultCouponFiltering("maxDiscount.lessThan=" + UPDATED_MAX_DISCOUNT, "maxDiscount.lessThan=" + DEFAULT_MAX_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByMaxDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where maxDiscount is greater than
        defaultCouponFiltering("maxDiscount.greaterThan=" + SMALLER_MAX_DISCOUNT, "maxDiscount.greaterThan=" + DEFAULT_MAX_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue equals to
        defaultCouponFiltering("minOrderValue.equals=" + DEFAULT_MIN_ORDER_VALUE, "minOrderValue.equals=" + UPDATED_MIN_ORDER_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue in
        defaultCouponFiltering(
            "minOrderValue.in=" + DEFAULT_MIN_ORDER_VALUE + "," + UPDATED_MIN_ORDER_VALUE,
            "minOrderValue.in=" + UPDATED_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is not null
        defaultCouponFiltering("minOrderValue.specified=true", "minOrderValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is greater than or equal to
        defaultCouponFiltering(
            "minOrderValue.greaterThanOrEqual=" + DEFAULT_MIN_ORDER_VALUE,
            "minOrderValue.greaterThanOrEqual=" + UPDATED_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is less than or equal to
        defaultCouponFiltering(
            "minOrderValue.lessThanOrEqual=" + DEFAULT_MIN_ORDER_VALUE,
            "minOrderValue.lessThanOrEqual=" + SMALLER_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is less than
        defaultCouponFiltering("minOrderValue.lessThan=" + UPDATED_MIN_ORDER_VALUE, "minOrderValue.lessThan=" + DEFAULT_MIN_ORDER_VALUE);
    }

    @Test
    @Transactional
    void getAllCouponsByMinOrderValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where minOrderValue is greater than
        defaultCouponFiltering(
            "minOrderValue.greaterThan=" + SMALLER_MIN_ORDER_VALUE,
            "minOrderValue.greaterThan=" + DEFAULT_MIN_ORDER_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCouponsByFirstBookingOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where firstBookingOnly equals to
        defaultCouponFiltering(
            "firstBookingOnly.equals=" + DEFAULT_FIRST_BOOKING_ONLY,
            "firstBookingOnly.equals=" + UPDATED_FIRST_BOOKING_ONLY
        );
    }

    @Test
    @Transactional
    void getAllCouponsByFirstBookingOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where firstBookingOnly in
        defaultCouponFiltering(
            "firstBookingOnly.in=" + DEFAULT_FIRST_BOOKING_ONLY + "," + UPDATED_FIRST_BOOKING_ONLY,
            "firstBookingOnly.in=" + UPDATED_FIRST_BOOKING_ONLY
        );
    }

    @Test
    @Transactional
    void getAllCouponsByFirstBookingOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where firstBookingOnly is not null
        defaultCouponFiltering("firstBookingOnly.specified=true", "firstBookingOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit equals to
        defaultCouponFiltering(
            "totalUsageLimit.equals=" + DEFAULT_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.equals=" + UPDATED_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit in
        defaultCouponFiltering(
            "totalUsageLimit.in=" + DEFAULT_TOTAL_USAGE_LIMIT + "," + UPDATED_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.in=" + UPDATED_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit is not null
        defaultCouponFiltering("totalUsageLimit.specified=true", "totalUsageLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit is greater than or equal to
        defaultCouponFiltering(
            "totalUsageLimit.greaterThanOrEqual=" + DEFAULT_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.greaterThanOrEqual=" + UPDATED_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit is less than or equal to
        defaultCouponFiltering(
            "totalUsageLimit.lessThanOrEqual=" + DEFAULT_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.lessThanOrEqual=" + SMALLER_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit is less than
        defaultCouponFiltering(
            "totalUsageLimit.lessThan=" + UPDATED_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.lessThan=" + DEFAULT_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByTotalUsageLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where totalUsageLimit is greater than
        defaultCouponFiltering(
            "totalUsageLimit.greaterThan=" + SMALLER_TOTAL_USAGE_LIMIT,
            "totalUsageLimit.greaterThan=" + DEFAULT_TOTAL_USAGE_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit equals to
        defaultCouponFiltering("perUserLimit.equals=" + DEFAULT_PER_USER_LIMIT, "perUserLimit.equals=" + UPDATED_PER_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit in
        defaultCouponFiltering(
            "perUserLimit.in=" + DEFAULT_PER_USER_LIMIT + "," + UPDATED_PER_USER_LIMIT,
            "perUserLimit.in=" + UPDATED_PER_USER_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit is not null
        defaultCouponFiltering("perUserLimit.specified=true", "perUserLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit is greater than or equal to
        defaultCouponFiltering(
            "perUserLimit.greaterThanOrEqual=" + DEFAULT_PER_USER_LIMIT,
            "perUserLimit.greaterThanOrEqual=" + UPDATED_PER_USER_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit is less than or equal to
        defaultCouponFiltering(
            "perUserLimit.lessThanOrEqual=" + DEFAULT_PER_USER_LIMIT,
            "perUserLimit.lessThanOrEqual=" + SMALLER_PER_USER_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit is less than
        defaultCouponFiltering("perUserLimit.lessThan=" + UPDATED_PER_USER_LIMIT, "perUserLimit.lessThan=" + DEFAULT_PER_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllCouponsByPerUserLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where perUserLimit is greater than
        defaultCouponFiltering("perUserLimit.greaterThan=" + SMALLER_PER_USER_LIMIT, "perUserLimit.greaterThan=" + DEFAULT_PER_USER_LIMIT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount equals to
        defaultCouponFiltering("usedCount.equals=" + DEFAULT_USED_COUNT, "usedCount.equals=" + UPDATED_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount in
        defaultCouponFiltering("usedCount.in=" + DEFAULT_USED_COUNT + "," + UPDATED_USED_COUNT, "usedCount.in=" + UPDATED_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount is not null
        defaultCouponFiltering("usedCount.specified=true", "usedCount.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount is greater than or equal to
        defaultCouponFiltering("usedCount.greaterThanOrEqual=" + DEFAULT_USED_COUNT, "usedCount.greaterThanOrEqual=" + UPDATED_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount is less than or equal to
        defaultCouponFiltering("usedCount.lessThanOrEqual=" + DEFAULT_USED_COUNT, "usedCount.lessThanOrEqual=" + SMALLER_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount is less than
        defaultCouponFiltering("usedCount.lessThan=" + UPDATED_USED_COUNT, "usedCount.lessThan=" + DEFAULT_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByUsedCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where usedCount is greater than
        defaultCouponFiltering("usedCount.greaterThan=" + SMALLER_USED_COUNT, "usedCount.greaterThan=" + DEFAULT_USED_COUNT);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom equals to
        defaultCouponFiltering("validFrom.equals=" + DEFAULT_VALID_FROM, "validFrom.equals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom in
        defaultCouponFiltering("validFrom.in=" + DEFAULT_VALID_FROM + "," + UPDATED_VALID_FROM, "validFrom.in=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllCouponsByValidFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validFrom is not null
        defaultCouponFiltering("validFrom.specified=true", "validFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByValidToIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validTo equals to
        defaultCouponFiltering("validTo.equals=" + DEFAULT_VALID_TO, "validTo.equals=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllCouponsByValidToIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validTo in
        defaultCouponFiltering("validTo.in=" + DEFAULT_VALID_TO + "," + UPDATED_VALID_TO, "validTo.in=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllCouponsByValidToIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where validTo is not null
        defaultCouponFiltering("validTo.specified=true", "validTo.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where active equals to
        defaultCouponFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCouponsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where active in
        defaultCouponFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCouponsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        // Get all the couponList where active is not null
        defaultCouponFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllCouponsByServiceIsEqualToSomething() throws Exception {
        FacilityService service;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            couponRepository.saveAndFlush(coupon);
            service = FacilityServiceResourceIT.createEntity(em);
        } else {
            service = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        em.persist(service);
        em.flush();
        coupon.setService(service);
        couponRepository.saveAndFlush(coupon);
        Long serviceId = service.getId();
        // Get all the couponList where service equals to serviceId
        defaultCouponShouldBeFound("serviceId.equals=" + serviceId);

        // Get all the couponList where service equals to (serviceId + 1)
        defaultCouponShouldNotBeFound("serviceId.equals=" + (serviceId + 1));
    }

    @Test
    @Transactional
    void getAllCouponsByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            couponRepository.saveAndFlush(coupon);
            city = CityResourceIT.createEntity();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        coupon.setCity(city);
        couponRepository.saveAndFlush(coupon);
        Long cityId = city.getId();
        // Get all the couponList where city equals to cityId
        defaultCouponShouldBeFound("cityId.equals=" + cityId);

        // Get all the couponList where city equals to (cityId + 1)
        defaultCouponShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    private void defaultCouponFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCouponShouldBeFound(shouldBeFound);
        defaultCouponShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCouponShouldBeFound(String filter) throws Exception {
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].maxDiscount").value(hasItem(sameNumber(DEFAULT_MAX_DISCOUNT))))
            .andExpect(jsonPath("$.[*].minOrderValue").value(hasItem(sameNumber(DEFAULT_MIN_ORDER_VALUE))))
            .andExpect(jsonPath("$.[*].firstBookingOnly").value(hasItem(DEFAULT_FIRST_BOOKING_ONLY)))
            .andExpect(jsonPath("$.[*].totalUsageLimit").value(hasItem(DEFAULT_TOTAL_USAGE_LIMIT)))
            .andExpect(jsonPath("$.[*].perUserLimit").value(hasItem(DEFAULT_PER_USER_LIMIT)))
            .andExpect(jsonPath("$.[*].usedCount").value(hasItem(DEFAULT_USED_COUNT)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCouponShouldNotBeFound(String filter) throws Exception {
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCouponMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCoupon() throws Exception {
        // Get the coupon
        restCouponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon
        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCoupon are not directly saved in db
        em.detach(updatedCoupon);
        updatedCoupon
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxDiscount(UPDATED_MAX_DISCOUNT)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .firstBookingOnly(UPDATED_FIRST_BOOKING_ONLY)
            .totalUsageLimit(UPDATED_TOTAL_USAGE_LIMIT)
            .perUserLimit(UPDATED_PER_USER_LIMIT)
            .usedCount(UPDATED_USED_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .active(UPDATED_ACTIVE);
        CouponDTO couponDTO = couponMapper.toDto(updatedCoupon);

        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCouponToMatchAllProperties(updatedCoupon);
    }

    @Test
    @Transactional
    void putNonExistingCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .code(UPDATED_CODE)
            .maxDiscount(UPDATED_MAX_DISCOUNT)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .firstBookingOnly(UPDATED_FIRST_BOOKING_ONLY)
            .totalUsageLimit(UPDATED_TOTAL_USAGE_LIMIT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCoupon, coupon), getPersistedCoupon(coupon));
    }

    @Test
    @Transactional
    void fullUpdateCouponWithPatch() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the coupon using partial update
        Coupon partialUpdatedCoupon = new Coupon();
        partialUpdatedCoupon.setId(coupon.getId());

        partialUpdatedCoupon
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxDiscount(UPDATED_MAX_DISCOUNT)
            .minOrderValue(UPDATED_MIN_ORDER_VALUE)
            .firstBookingOnly(UPDATED_FIRST_BOOKING_ONLY)
            .totalUsageLimit(UPDATED_TOTAL_USAGE_LIMIT)
            .perUserLimit(UPDATED_PER_USER_LIMIT)
            .usedCount(UPDATED_USED_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .active(UPDATED_ACTIVE);

        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCoupon))
            )
            .andExpect(status().isOk());

        // Validate the Coupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouponUpdatableFieldsEquals(partialUpdatedCoupon, getPersistedCoupon(partialUpdatedCoupon));
    }

    @Test
    @Transactional
    void patchNonExistingCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couponDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        coupon.setId(longCount.incrementAndGet());

        // Create the Coupon
        CouponDTO couponDTO = couponMapper.toDto(coupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouponMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(couponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoupon() throws Exception {
        // Initialize the database
        insertedCoupon = couponRepository.saveAndFlush(coupon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the coupon
        restCouponMockMvc
            .perform(delete(ENTITY_API_URL_ID, coupon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return couponRepository.count();
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

    protected Coupon getPersistedCoupon(Coupon coupon) {
        return couponRepository.findById(coupon.getId()).orElseThrow();
    }

    protected void assertPersistedCouponToMatchAllProperties(Coupon expectedCoupon) {
        assertCouponAllPropertiesEquals(expectedCoupon, getPersistedCoupon(expectedCoupon));
    }

    protected void assertPersistedCouponToMatchUpdatableProperties(Coupon expectedCoupon) {
        assertCouponAllUpdatablePropertiesEquals(expectedCoupon, getPersistedCoupon(expectedCoupon));
    }
}
