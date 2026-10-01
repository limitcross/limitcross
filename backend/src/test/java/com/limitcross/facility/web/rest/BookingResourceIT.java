package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingAsserts.*;
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
import com.limitcross.facility.domain.BookingSubscription;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.domain.enumeration.BookingPaymentStatus;
import com.limitcross.facility.domain.enumeration.BookingSource;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.PaymentMode;
import com.limitcross.facility.repository.BookingRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.BookingService;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.mapper.BookingMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link BookingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingResourceIT {

    private static final String DEFAULT_BOOKING_NO = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_NO = "BBBBBBBBBB";

    private static final UUID DEFAULT_PUBLIC_ID = UUID.randomUUID();
    private static final UUID UPDATED_PUBLIC_ID = UUID.randomUUID();

    private static final String DEFAULT_SERVICE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_SNAPSHOT = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_SNAPSHOT = "BBBBBBBBBB";

    private static final Instant DEFAULT_SCHEDULED_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SCHEDULED_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BookingStatus DEFAULT_STATUS = BookingStatus.REQUESTED;
    private static final BookingStatus UPDATED_STATUS = BookingStatus.PENDING_ASSIGNMENT;

    private static final BookingPaymentStatus DEFAULT_PAYMENT_STATUS = BookingPaymentStatus.PENDING;
    private static final BookingPaymentStatus UPDATED_PAYMENT_STATUS = BookingPaymentStatus.AUTHORIZED;

    private static final PaymentMode DEFAULT_PAYMENT_MODE = PaymentMode.ONLINE;
    private static final PaymentMode UPDATED_PAYMENT_MODE = PaymentMode.CASH;

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_SUBTOTAL = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_ADDON_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_ADDON_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_ADDON_TOTAL = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISCOUNT_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_SERVICE_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_SERVICE_FEE = new BigDecimal(1);
    private static final BigDecimal SMALLER_SERVICE_FEE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TAX_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TAX_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TIP_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TIP_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TIP_AMOUNT = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(0 - 1);

    private static final String DEFAULT_CUSTOMER_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_START_OTP = "AAAAAA";
    private static final String UPDATED_START_OTP = "BBBBBB";

    private static final ActorType DEFAULT_CANCELLED_BY = ActorType.CUSTOMER;
    private static final ActorType UPDATED_CANCELLED_BY = ActorType.PRO;

    private static final String DEFAULT_CANCEL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_REASON = "BBBBBBBBBB";

    private static final BookingSource DEFAULT_SOURCE = BookingSource.APP;
    private static final BookingSource UPDATED_SOURCE = BookingSource.WEB;

    private static final BigDecimal DEFAULT_WALLET_AMOUNT_USED = new BigDecimal(0);
    private static final BigDecimal UPDATED_WALLET_AMOUNT_USED = new BigDecimal(1);
    private static final BigDecimal SMALLER_WALLET_AMOUNT_USED = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_LOYALTY_POINTS_USED = 0;
    private static final Integer UPDATED_LOYALTY_POINTS_USED = 1;
    private static final Integer SMALLER_LOYALTY_POINTS_USED = 0 - 1;

    private static final BigDecimal DEFAULT_CANCELLATION_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_CANCELLATION_FEE = new BigDecimal(1);
    private static final BigDecimal SMALLER_CANCELLATION_FEE = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_RESCHEDULE_COUNT = 0;
    private static final Integer UPDATED_RESCHEDULE_COUNT = 1;
    private static final Integer SMALLER_RESCHEDULE_COUNT = 0 - 1;

    private static final BigDecimal DEFAULT_PLATFORM_COMMISSION = new BigDecimal(0);
    private static final BigDecimal UPDATED_PLATFORM_COMMISSION = new BigDecimal(1);
    private static final BigDecimal SMALLER_PLATFORM_COMMISSION = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_PRO_EARNING = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRO_EARNING = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRO_EARNING = new BigDecimal(0 - 1);

    private static final Instant DEFAULT_ARRIVAL_ETA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVAL_ETA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PRIORITY = false;
    private static final Boolean UPDATED_PRIORITY = true;

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CANCELLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCELLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Autowired
    private BookingMapper bookingMapper;

    @Mock
    private BookingService bookingServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    private Booking insertedBooking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .bookingNo(DEFAULT_BOOKING_NO)
            .publicId(DEFAULT_PUBLIC_ID)
            .serviceTitle(DEFAULT_SERVICE_TITLE)
            .addressSnapshot(DEFAULT_ADDRESS_SNAPSHOT)
            .scheduledStart(DEFAULT_SCHEDULED_START)
            .scheduledEnd(DEFAULT_SCHEDULED_END)
            .status(DEFAULT_STATUS)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .currency(DEFAULT_CURRENCY)
            .subtotal(DEFAULT_SUBTOTAL)
            .addonTotal(DEFAULT_ADDON_TOTAL)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .serviceFee(DEFAULT_SERVICE_FEE)
            .taxAmount(DEFAULT_TAX_AMOUNT)
            .tipAmount(DEFAULT_TIP_AMOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .customerNotes(DEFAULT_CUSTOMER_NOTES)
            .startOtp(DEFAULT_START_OTP)
            .cancelledBy(DEFAULT_CANCELLED_BY)
            .cancelReason(DEFAULT_CANCEL_REASON)
            .source(DEFAULT_SOURCE)
            .walletAmountUsed(DEFAULT_WALLET_AMOUNT_USED)
            .loyaltyPointsUsed(DEFAULT_LOYALTY_POINTS_USED)
            .cancellationFee(DEFAULT_CANCELLATION_FEE)
            .rescheduleCount(DEFAULT_RESCHEDULE_COUNT)
            .platformCommission(DEFAULT_PLATFORM_COMMISSION)
            .proEarning(DEFAULT_PRO_EARNING)
            .arrivalEta(DEFAULT_ARRIVAL_ETA)
            .priority(DEFAULT_PRIORITY)
            .startedAt(DEFAULT_STARTED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .cancelledAt(DEFAULT_CANCELLED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        booking.setCustomer(user);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        booking.setService(facilityService);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        booking.setCity(city);
        return booking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking updatedBooking = new Booking()
            .bookingNo(UPDATED_BOOKING_NO)
            .publicId(UPDATED_PUBLIC_ID)
            .serviceTitle(UPDATED_SERVICE_TITLE)
            .addressSnapshot(UPDATED_ADDRESS_SNAPSHOT)
            .scheduledStart(UPDATED_SCHEDULED_START)
            .scheduledEnd(UPDATED_SCHEDULED_END)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .addonTotal(UPDATED_ADDON_TOTAL)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .serviceFee(UPDATED_SERVICE_FEE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .tipAmount(UPDATED_TIP_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .customerNotes(UPDATED_CUSTOMER_NOTES)
            .startOtp(UPDATED_START_OTP)
            .cancelledBy(UPDATED_CANCELLED_BY)
            .cancelReason(UPDATED_CANCEL_REASON)
            .source(UPDATED_SOURCE)
            .walletAmountUsed(UPDATED_WALLET_AMOUNT_USED)
            .loyaltyPointsUsed(UPDATED_LOYALTY_POINTS_USED)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .rescheduleCount(UPDATED_RESCHEDULE_COUNT)
            .platformCommission(UPDATED_PLATFORM_COMMISSION)
            .proEarning(UPDATED_PRO_EARNING)
            .arrivalEta(UPDATED_ARRIVAL_ETA)
            .priority(UPDATED_PRIORITY)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedBooking.setCustomer(user);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedBooking.setService(facilityService);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedBooking.setCity(city);
        return updatedBooking;
    }

    @BeforeEach
    void initTest() {
        booking = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBooking != null) {
            bookingRepository.delete(insertedBooking);
            insertedBooking = null;
        }
    }

    @Test
    @Transactional
    void createBooking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        var returnedBookingDTO = om.readValue(
            restBookingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingDTO.class
        );

        // Validate the Booking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBooking = bookingMapper.toEntity(returnedBookingDTO);
        assertBookingUpdatableFieldsEquals(returnedBooking, getPersistedBooking(returnedBooking));

        insertedBooking = returnedBooking;
    }

    @Test
    @Transactional
    void createBookingWithExistingId() throws Exception {
        // Create the Booking with an existing ID
        booking.setId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBookingNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setBookingNo(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublicIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setPublicId(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setServiceTitle(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduledStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setScheduledStart(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduledEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setScheduledEnd(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setStatus(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setPaymentStatus(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentModeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setPaymentMode(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setCurrency(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setSubtotal(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        booking.setTotalAmount(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookings() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].publicId").value(hasItem(DEFAULT_PUBLIC_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceTitle").value(hasItem(DEFAULT_SERVICE_TITLE)))
            .andExpect(jsonPath("$.[*].addressSnapshot").value(hasItem(DEFAULT_ADDRESS_SNAPSHOT)))
            .andExpect(jsonPath("$.[*].scheduledStart").value(hasItem(DEFAULT_SCHEDULED_START.toString())))
            .andExpect(jsonPath("$.[*].scheduledEnd").value(hasItem(DEFAULT_SCHEDULED_END.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].addonTotal").value(hasItem(sameNumber(DEFAULT_ADDON_TOTAL))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].serviceFee").value(hasItem(sameNumber(DEFAULT_SERVICE_FEE))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].tipAmount").value(hasItem(sameNumber(DEFAULT_TIP_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].customerNotes").value(hasItem(DEFAULT_CUSTOMER_NOTES)))
            .andExpect(jsonPath("$.[*].startOtp").value(hasItem(DEFAULT_START_OTP)))
            .andExpect(jsonPath("$.[*].cancelledBy").value(hasItem(DEFAULT_CANCELLED_BY.toString())))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].walletAmountUsed").value(hasItem(sameNumber(DEFAULT_WALLET_AMOUNT_USED))))
            .andExpect(jsonPath("$.[*].loyaltyPointsUsed").value(hasItem(DEFAULT_LOYALTY_POINTS_USED)))
            .andExpect(jsonPath("$.[*].cancellationFee").value(hasItem(sameNumber(DEFAULT_CANCELLATION_FEE))))
            .andExpect(jsonPath("$.[*].rescheduleCount").value(hasItem(DEFAULT_RESCHEDULE_COUNT)))
            .andExpect(jsonPath("$.[*].platformCommission").value(hasItem(sameNumber(DEFAULT_PLATFORM_COMMISSION))))
            .andExpect(jsonPath("$.[*].proEarning").value(hasItem(sameNumber(DEFAULT_PRO_EARNING))))
            .andExpect(jsonPath("$.[*].arrivalEta").value(hasItem(DEFAULT_ARRIVAL_ETA.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelledAt").value(hasItem(DEFAULT_CANCELLED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.bookingNo").value(DEFAULT_BOOKING_NO))
            .andExpect(jsonPath("$.publicId").value(DEFAULT_PUBLIC_ID.toString()))
            .andExpect(jsonPath("$.serviceTitle").value(DEFAULT_SERVICE_TITLE))
            .andExpect(jsonPath("$.addressSnapshot").value(DEFAULT_ADDRESS_SNAPSHOT))
            .andExpect(jsonPath("$.scheduledStart").value(DEFAULT_SCHEDULED_START.toString()))
            .andExpect(jsonPath("$.scheduledEnd").value(DEFAULT_SCHEDULED_END.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.subtotal").value(sameNumber(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.addonTotal").value(sameNumber(DEFAULT_ADDON_TOTAL)))
            .andExpect(jsonPath("$.discountAmount").value(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.serviceFee").value(sameNumber(DEFAULT_SERVICE_FEE)))
            .andExpect(jsonPath("$.taxAmount").value(sameNumber(DEFAULT_TAX_AMOUNT)))
            .andExpect(jsonPath("$.tipAmount").value(sameNumber(DEFAULT_TIP_AMOUNT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.customerNotes").value(DEFAULT_CUSTOMER_NOTES))
            .andExpect(jsonPath("$.startOtp").value(DEFAULT_START_OTP))
            .andExpect(jsonPath("$.cancelledBy").value(DEFAULT_CANCELLED_BY.toString()))
            .andExpect(jsonPath("$.cancelReason").value(DEFAULT_CANCEL_REASON))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.walletAmountUsed").value(sameNumber(DEFAULT_WALLET_AMOUNT_USED)))
            .andExpect(jsonPath("$.loyaltyPointsUsed").value(DEFAULT_LOYALTY_POINTS_USED))
            .andExpect(jsonPath("$.cancellationFee").value(sameNumber(DEFAULT_CANCELLATION_FEE)))
            .andExpect(jsonPath("$.rescheduleCount").value(DEFAULT_RESCHEDULE_COUNT))
            .andExpect(jsonPath("$.platformCommission").value(sameNumber(DEFAULT_PLATFORM_COMMISSION)))
            .andExpect(jsonPath("$.proEarning").value(sameNumber(DEFAULT_PRO_EARNING)))
            .andExpect(jsonPath("$.arrivalEta").value(DEFAULT_ARRIVAL_ETA.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.cancelledAt").value(DEFAULT_CANCELLED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        Long id = booking.getId();

        defaultBookingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBookingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBookingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingNo equals to
        defaultBookingFiltering("bookingNo.equals=" + DEFAULT_BOOKING_NO, "bookingNo.equals=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingNo in
        defaultBookingFiltering("bookingNo.in=" + DEFAULT_BOOKING_NO + "," + UPDATED_BOOKING_NO, "bookingNo.in=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingNo is not null
        defaultBookingFiltering("bookingNo.specified=true", "bookingNo.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByBookingNoContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingNo contains
        defaultBookingFiltering("bookingNo.contains=" + DEFAULT_BOOKING_NO, "bookingNo.contains=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllBookingsByBookingNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingNo does not contain
        defaultBookingFiltering("bookingNo.doesNotContain=" + UPDATED_BOOKING_NO, "bookingNo.doesNotContain=" + DEFAULT_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllBookingsByPublicIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where publicId equals to
        defaultBookingFiltering("publicId.equals=" + DEFAULT_PUBLIC_ID, "publicId.equals=" + UPDATED_PUBLIC_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByPublicIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where publicId in
        defaultBookingFiltering("publicId.in=" + DEFAULT_PUBLIC_ID + "," + UPDATED_PUBLIC_ID, "publicId.in=" + UPDATED_PUBLIC_ID);
    }

    @Test
    @Transactional
    void getAllBookingsByPublicIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where publicId is not null
        defaultBookingFiltering("publicId.specified=true", "publicId.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByServiceTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceTitle equals to
        defaultBookingFiltering("serviceTitle.equals=" + DEFAULT_SERVICE_TITLE, "serviceTitle.equals=" + UPDATED_SERVICE_TITLE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceTitle in
        defaultBookingFiltering(
            "serviceTitle.in=" + DEFAULT_SERVICE_TITLE + "," + UPDATED_SERVICE_TITLE,
            "serviceTitle.in=" + UPDATED_SERVICE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByServiceTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceTitle is not null
        defaultBookingFiltering("serviceTitle.specified=true", "serviceTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByServiceTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceTitle contains
        defaultBookingFiltering("serviceTitle.contains=" + DEFAULT_SERVICE_TITLE, "serviceTitle.contains=" + UPDATED_SERVICE_TITLE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceTitle does not contain
        defaultBookingFiltering(
            "serviceTitle.doesNotContain=" + UPDATED_SERVICE_TITLE,
            "serviceTitle.doesNotContain=" + DEFAULT_SERVICE_TITLE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledStart equals to
        defaultBookingFiltering("scheduledStart.equals=" + DEFAULT_SCHEDULED_START, "scheduledStart.equals=" + UPDATED_SCHEDULED_START);
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledStart in
        defaultBookingFiltering(
            "scheduledStart.in=" + DEFAULT_SCHEDULED_START + "," + UPDATED_SCHEDULED_START,
            "scheduledStart.in=" + UPDATED_SCHEDULED_START
        );
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledStart is not null
        defaultBookingFiltering("scheduledStart.specified=true", "scheduledStart.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledEndIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledEnd equals to
        defaultBookingFiltering("scheduledEnd.equals=" + DEFAULT_SCHEDULED_END, "scheduledEnd.equals=" + UPDATED_SCHEDULED_END);
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledEndIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledEnd in
        defaultBookingFiltering(
            "scheduledEnd.in=" + DEFAULT_SCHEDULED_END + "," + UPDATED_SCHEDULED_END,
            "scheduledEnd.in=" + UPDATED_SCHEDULED_END
        );
    }

    @Test
    @Transactional
    void getAllBookingsByScheduledEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where scheduledEnd is not null
        defaultBookingFiltering("scheduledEnd.specified=true", "scheduledEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status equals to
        defaultBookingFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status in
        defaultBookingFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status is not null
        defaultBookingFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentStatus equals to
        defaultBookingFiltering("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS, "paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentStatus in
        defaultBookingFiltering(
            "paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS,
            "paymentStatus.in=" + UPDATED_PAYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentStatus is not null
        defaultBookingFiltering("paymentStatus.specified=true", "paymentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentMode equals to
        defaultBookingFiltering("paymentMode.equals=" + DEFAULT_PAYMENT_MODE, "paymentMode.equals=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentMode in
        defaultBookingFiltering(
            "paymentMode.in=" + DEFAULT_PAYMENT_MODE + "," + UPDATED_PAYMENT_MODE,
            "paymentMode.in=" + UPDATED_PAYMENT_MODE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPaymentModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where paymentMode is not null
        defaultBookingFiltering("paymentMode.specified=true", "paymentMode.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where currency equals to
        defaultBookingFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBookingsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where currency in
        defaultBookingFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBookingsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where currency is not null
        defaultBookingFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where currency contains
        defaultBookingFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBookingsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where currency does not contain
        defaultBookingFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal equals to
        defaultBookingFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal in
        defaultBookingFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal is not null
        defaultBookingFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal is greater than or equal to
        defaultBookingFiltering("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal is less than or equal to
        defaultBookingFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal is less than
        defaultBookingFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where subtotal is greater than
        defaultBookingFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal equals to
        defaultBookingFiltering("addonTotal.equals=" + DEFAULT_ADDON_TOTAL, "addonTotal.equals=" + UPDATED_ADDON_TOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal in
        defaultBookingFiltering("addonTotal.in=" + DEFAULT_ADDON_TOTAL + "," + UPDATED_ADDON_TOTAL, "addonTotal.in=" + UPDATED_ADDON_TOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal is not null
        defaultBookingFiltering("addonTotal.specified=true", "addonTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal is greater than or equal to
        defaultBookingFiltering(
            "addonTotal.greaterThanOrEqual=" + DEFAULT_ADDON_TOTAL,
            "addonTotal.greaterThanOrEqual=" + UPDATED_ADDON_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal is less than or equal to
        defaultBookingFiltering("addonTotal.lessThanOrEqual=" + DEFAULT_ADDON_TOTAL, "addonTotal.lessThanOrEqual=" + SMALLER_ADDON_TOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal is less than
        defaultBookingFiltering("addonTotal.lessThan=" + UPDATED_ADDON_TOTAL, "addonTotal.lessThan=" + DEFAULT_ADDON_TOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByAddonTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where addonTotal is greater than
        defaultBookingFiltering("addonTotal.greaterThan=" + SMALLER_ADDON_TOTAL, "addonTotal.greaterThan=" + DEFAULT_ADDON_TOTAL);
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount equals to
        defaultBookingFiltering("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT, "discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount in
        defaultBookingFiltering(
            "discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT,
            "discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount is not null
        defaultBookingFiltering("discountAmount.specified=true", "discountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount is greater than or equal to
        defaultBookingFiltering(
            "discountAmount.greaterThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.greaterThanOrEqual=" + UPDATED_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount is less than or equal to
        defaultBookingFiltering(
            "discountAmount.lessThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT,
            "discountAmount.lessThanOrEqual=" + SMALLER_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount is less than
        defaultBookingFiltering("discountAmount.lessThan=" + UPDATED_DISCOUNT_AMOUNT, "discountAmount.lessThan=" + DEFAULT_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where discountAmount is greater than
        defaultBookingFiltering(
            "discountAmount.greaterThan=" + SMALLER_DISCOUNT_AMOUNT,
            "discountAmount.greaterThan=" + DEFAULT_DISCOUNT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee equals to
        defaultBookingFiltering("serviceFee.equals=" + DEFAULT_SERVICE_FEE, "serviceFee.equals=" + UPDATED_SERVICE_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee in
        defaultBookingFiltering("serviceFee.in=" + DEFAULT_SERVICE_FEE + "," + UPDATED_SERVICE_FEE, "serviceFee.in=" + UPDATED_SERVICE_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee is not null
        defaultBookingFiltering("serviceFee.specified=true", "serviceFee.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee is greater than or equal to
        defaultBookingFiltering(
            "serviceFee.greaterThanOrEqual=" + DEFAULT_SERVICE_FEE,
            "serviceFee.greaterThanOrEqual=" + UPDATED_SERVICE_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee is less than or equal to
        defaultBookingFiltering("serviceFee.lessThanOrEqual=" + DEFAULT_SERVICE_FEE, "serviceFee.lessThanOrEqual=" + SMALLER_SERVICE_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee is less than
        defaultBookingFiltering("serviceFee.lessThan=" + UPDATED_SERVICE_FEE, "serviceFee.lessThan=" + DEFAULT_SERVICE_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByServiceFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where serviceFee is greater than
        defaultBookingFiltering("serviceFee.greaterThan=" + SMALLER_SERVICE_FEE, "serviceFee.greaterThan=" + DEFAULT_SERVICE_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount equals to
        defaultBookingFiltering("taxAmount.equals=" + DEFAULT_TAX_AMOUNT, "taxAmount.equals=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount in
        defaultBookingFiltering("taxAmount.in=" + DEFAULT_TAX_AMOUNT + "," + UPDATED_TAX_AMOUNT, "taxAmount.in=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount is not null
        defaultBookingFiltering("taxAmount.specified=true", "taxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount is greater than or equal to
        defaultBookingFiltering("taxAmount.greaterThanOrEqual=" + DEFAULT_TAX_AMOUNT, "taxAmount.greaterThanOrEqual=" + UPDATED_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount is less than or equal to
        defaultBookingFiltering("taxAmount.lessThanOrEqual=" + DEFAULT_TAX_AMOUNT, "taxAmount.lessThanOrEqual=" + SMALLER_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount is less than
        defaultBookingFiltering("taxAmount.lessThan=" + UPDATED_TAX_AMOUNT, "taxAmount.lessThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where taxAmount is greater than
        defaultBookingFiltering("taxAmount.greaterThan=" + SMALLER_TAX_AMOUNT, "taxAmount.greaterThan=" + DEFAULT_TAX_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount equals to
        defaultBookingFiltering("tipAmount.equals=" + DEFAULT_TIP_AMOUNT, "tipAmount.equals=" + UPDATED_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount in
        defaultBookingFiltering("tipAmount.in=" + DEFAULT_TIP_AMOUNT + "," + UPDATED_TIP_AMOUNT, "tipAmount.in=" + UPDATED_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount is not null
        defaultBookingFiltering("tipAmount.specified=true", "tipAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount is greater than or equal to
        defaultBookingFiltering("tipAmount.greaterThanOrEqual=" + DEFAULT_TIP_AMOUNT, "tipAmount.greaterThanOrEqual=" + UPDATED_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount is less than or equal to
        defaultBookingFiltering("tipAmount.lessThanOrEqual=" + DEFAULT_TIP_AMOUNT, "tipAmount.lessThanOrEqual=" + SMALLER_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount is less than
        defaultBookingFiltering("tipAmount.lessThan=" + UPDATED_TIP_AMOUNT, "tipAmount.lessThan=" + DEFAULT_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTipAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where tipAmount is greater than
        defaultBookingFiltering("tipAmount.greaterThan=" + SMALLER_TIP_AMOUNT, "tipAmount.greaterThan=" + DEFAULT_TIP_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount equals to
        defaultBookingFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount in
        defaultBookingFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount is not null
        defaultBookingFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount is greater than or equal to
        defaultBookingFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount is less than or equal to
        defaultBookingFiltering(
            "totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount is less than
        defaultBookingFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where totalAmount is greater than
        defaultBookingFiltering("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT, "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerNotes equals to
        defaultBookingFiltering("customerNotes.equals=" + DEFAULT_CUSTOMER_NOTES, "customerNotes.equals=" + UPDATED_CUSTOMER_NOTES);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerNotes in
        defaultBookingFiltering(
            "customerNotes.in=" + DEFAULT_CUSTOMER_NOTES + "," + UPDATED_CUSTOMER_NOTES,
            "customerNotes.in=" + UPDATED_CUSTOMER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerNotes is not null
        defaultBookingFiltering("customerNotes.specified=true", "customerNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerNotes contains
        defaultBookingFiltering("customerNotes.contains=" + DEFAULT_CUSTOMER_NOTES, "customerNotes.contains=" + UPDATED_CUSTOMER_NOTES);
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where customerNotes does not contain
        defaultBookingFiltering(
            "customerNotes.doesNotContain=" + UPDATED_CUSTOMER_NOTES,
            "customerNotes.doesNotContain=" + DEFAULT_CUSTOMER_NOTES
        );
    }

    @Test
    @Transactional
    void getAllBookingsByStartOtpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startOtp equals to
        defaultBookingFiltering("startOtp.equals=" + DEFAULT_START_OTP, "startOtp.equals=" + UPDATED_START_OTP);
    }

    @Test
    @Transactional
    void getAllBookingsByStartOtpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startOtp in
        defaultBookingFiltering("startOtp.in=" + DEFAULT_START_OTP + "," + UPDATED_START_OTP, "startOtp.in=" + UPDATED_START_OTP);
    }

    @Test
    @Transactional
    void getAllBookingsByStartOtpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startOtp is not null
        defaultBookingFiltering("startOtp.specified=true", "startOtp.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByStartOtpContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startOtp contains
        defaultBookingFiltering("startOtp.contains=" + DEFAULT_START_OTP, "startOtp.contains=" + UPDATED_START_OTP);
    }

    @Test
    @Transactional
    void getAllBookingsByStartOtpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startOtp does not contain
        defaultBookingFiltering("startOtp.doesNotContain=" + UPDATED_START_OTP, "startOtp.doesNotContain=" + DEFAULT_START_OTP);
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledBy equals to
        defaultBookingFiltering("cancelledBy.equals=" + DEFAULT_CANCELLED_BY, "cancelledBy.equals=" + UPDATED_CANCELLED_BY);
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledBy in
        defaultBookingFiltering(
            "cancelledBy.in=" + DEFAULT_CANCELLED_BY + "," + UPDATED_CANCELLED_BY,
            "cancelledBy.in=" + UPDATED_CANCELLED_BY
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledBy is not null
        defaultBookingFiltering("cancelledBy.specified=true", "cancelledBy.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCancelReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelReason equals to
        defaultBookingFiltering("cancelReason.equals=" + DEFAULT_CANCEL_REASON, "cancelReason.equals=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllBookingsByCancelReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelReason in
        defaultBookingFiltering(
            "cancelReason.in=" + DEFAULT_CANCEL_REASON + "," + UPDATED_CANCEL_REASON,
            "cancelReason.in=" + UPDATED_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancelReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelReason is not null
        defaultBookingFiltering("cancelReason.specified=true", "cancelReason.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCancelReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelReason contains
        defaultBookingFiltering("cancelReason.contains=" + DEFAULT_CANCEL_REASON, "cancelReason.contains=" + UPDATED_CANCEL_REASON);
    }

    @Test
    @Transactional
    void getAllBookingsByCancelReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelReason does not contain
        defaultBookingFiltering(
            "cancelReason.doesNotContain=" + UPDATED_CANCEL_REASON,
            "cancelReason.doesNotContain=" + DEFAULT_CANCEL_REASON
        );
    }

    @Test
    @Transactional
    void getAllBookingsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where source equals to
        defaultBookingFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllBookingsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where source in
        defaultBookingFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllBookingsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where source is not null
        defaultBookingFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed equals to
        defaultBookingFiltering(
            "walletAmountUsed.equals=" + DEFAULT_WALLET_AMOUNT_USED,
            "walletAmountUsed.equals=" + UPDATED_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed in
        defaultBookingFiltering(
            "walletAmountUsed.in=" + DEFAULT_WALLET_AMOUNT_USED + "," + UPDATED_WALLET_AMOUNT_USED,
            "walletAmountUsed.in=" + UPDATED_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed is not null
        defaultBookingFiltering("walletAmountUsed.specified=true", "walletAmountUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed is greater than or equal to
        defaultBookingFiltering(
            "walletAmountUsed.greaterThanOrEqual=" + DEFAULT_WALLET_AMOUNT_USED,
            "walletAmountUsed.greaterThanOrEqual=" + UPDATED_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed is less than or equal to
        defaultBookingFiltering(
            "walletAmountUsed.lessThanOrEqual=" + DEFAULT_WALLET_AMOUNT_USED,
            "walletAmountUsed.lessThanOrEqual=" + SMALLER_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed is less than
        defaultBookingFiltering(
            "walletAmountUsed.lessThan=" + UPDATED_WALLET_AMOUNT_USED,
            "walletAmountUsed.lessThan=" + DEFAULT_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByWalletAmountUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where walletAmountUsed is greater than
        defaultBookingFiltering(
            "walletAmountUsed.greaterThan=" + SMALLER_WALLET_AMOUNT_USED,
            "walletAmountUsed.greaterThan=" + DEFAULT_WALLET_AMOUNT_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed equals to
        defaultBookingFiltering(
            "loyaltyPointsUsed.equals=" + DEFAULT_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.equals=" + UPDATED_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed in
        defaultBookingFiltering(
            "loyaltyPointsUsed.in=" + DEFAULT_LOYALTY_POINTS_USED + "," + UPDATED_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.in=" + UPDATED_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed is not null
        defaultBookingFiltering("loyaltyPointsUsed.specified=true", "loyaltyPointsUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed is greater than or equal to
        defaultBookingFiltering(
            "loyaltyPointsUsed.greaterThanOrEqual=" + DEFAULT_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.greaterThanOrEqual=" + UPDATED_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed is less than or equal to
        defaultBookingFiltering(
            "loyaltyPointsUsed.lessThanOrEqual=" + DEFAULT_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.lessThanOrEqual=" + SMALLER_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed is less than
        defaultBookingFiltering(
            "loyaltyPointsUsed.lessThan=" + UPDATED_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.lessThan=" + DEFAULT_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByLoyaltyPointsUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where loyaltyPointsUsed is greater than
        defaultBookingFiltering(
            "loyaltyPointsUsed.greaterThan=" + SMALLER_LOYALTY_POINTS_USED,
            "loyaltyPointsUsed.greaterThan=" + DEFAULT_LOYALTY_POINTS_USED
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee equals to
        defaultBookingFiltering("cancellationFee.equals=" + DEFAULT_CANCELLATION_FEE, "cancellationFee.equals=" + UPDATED_CANCELLATION_FEE);
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee in
        defaultBookingFiltering(
            "cancellationFee.in=" + DEFAULT_CANCELLATION_FEE + "," + UPDATED_CANCELLATION_FEE,
            "cancellationFee.in=" + UPDATED_CANCELLATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee is not null
        defaultBookingFiltering("cancellationFee.specified=true", "cancellationFee.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee is greater than or equal to
        defaultBookingFiltering(
            "cancellationFee.greaterThanOrEqual=" + DEFAULT_CANCELLATION_FEE,
            "cancellationFee.greaterThanOrEqual=" + UPDATED_CANCELLATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee is less than or equal to
        defaultBookingFiltering(
            "cancellationFee.lessThanOrEqual=" + DEFAULT_CANCELLATION_FEE,
            "cancellationFee.lessThanOrEqual=" + SMALLER_CANCELLATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee is less than
        defaultBookingFiltering(
            "cancellationFee.lessThan=" + UPDATED_CANCELLATION_FEE,
            "cancellationFee.lessThan=" + DEFAULT_CANCELLATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancellationFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancellationFee is greater than
        defaultBookingFiltering(
            "cancellationFee.greaterThan=" + SMALLER_CANCELLATION_FEE,
            "cancellationFee.greaterThan=" + DEFAULT_CANCELLATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount equals to
        defaultBookingFiltering("rescheduleCount.equals=" + DEFAULT_RESCHEDULE_COUNT, "rescheduleCount.equals=" + UPDATED_RESCHEDULE_COUNT);
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount in
        defaultBookingFiltering(
            "rescheduleCount.in=" + DEFAULT_RESCHEDULE_COUNT + "," + UPDATED_RESCHEDULE_COUNT,
            "rescheduleCount.in=" + UPDATED_RESCHEDULE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount is not null
        defaultBookingFiltering("rescheduleCount.specified=true", "rescheduleCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount is greater than or equal to
        defaultBookingFiltering(
            "rescheduleCount.greaterThanOrEqual=" + DEFAULT_RESCHEDULE_COUNT,
            "rescheduleCount.greaterThanOrEqual=" + UPDATED_RESCHEDULE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount is less than or equal to
        defaultBookingFiltering(
            "rescheduleCount.lessThanOrEqual=" + DEFAULT_RESCHEDULE_COUNT,
            "rescheduleCount.lessThanOrEqual=" + SMALLER_RESCHEDULE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount is less than
        defaultBookingFiltering(
            "rescheduleCount.lessThan=" + UPDATED_RESCHEDULE_COUNT,
            "rescheduleCount.lessThan=" + DEFAULT_RESCHEDULE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByRescheduleCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where rescheduleCount is greater than
        defaultBookingFiltering(
            "rescheduleCount.greaterThan=" + SMALLER_RESCHEDULE_COUNT,
            "rescheduleCount.greaterThan=" + DEFAULT_RESCHEDULE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission equals to
        defaultBookingFiltering(
            "platformCommission.equals=" + DEFAULT_PLATFORM_COMMISSION,
            "platformCommission.equals=" + UPDATED_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission in
        defaultBookingFiltering(
            "platformCommission.in=" + DEFAULT_PLATFORM_COMMISSION + "," + UPDATED_PLATFORM_COMMISSION,
            "platformCommission.in=" + UPDATED_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission is not null
        defaultBookingFiltering("platformCommission.specified=true", "platformCommission.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission is greater than or equal to
        defaultBookingFiltering(
            "platformCommission.greaterThanOrEqual=" + DEFAULT_PLATFORM_COMMISSION,
            "platformCommission.greaterThanOrEqual=" + UPDATED_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission is less than or equal to
        defaultBookingFiltering(
            "platformCommission.lessThanOrEqual=" + DEFAULT_PLATFORM_COMMISSION,
            "platformCommission.lessThanOrEqual=" + SMALLER_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission is less than
        defaultBookingFiltering(
            "platformCommission.lessThan=" + UPDATED_PLATFORM_COMMISSION,
            "platformCommission.lessThan=" + DEFAULT_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByPlatformCommissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where platformCommission is greater than
        defaultBookingFiltering(
            "platformCommission.greaterThan=" + SMALLER_PLATFORM_COMMISSION,
            "platformCommission.greaterThan=" + DEFAULT_PLATFORM_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning equals to
        defaultBookingFiltering("proEarning.equals=" + DEFAULT_PRO_EARNING, "proEarning.equals=" + UPDATED_PRO_EARNING);
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning in
        defaultBookingFiltering("proEarning.in=" + DEFAULT_PRO_EARNING + "," + UPDATED_PRO_EARNING, "proEarning.in=" + UPDATED_PRO_EARNING);
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning is not null
        defaultBookingFiltering("proEarning.specified=true", "proEarning.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning is greater than or equal to
        defaultBookingFiltering(
            "proEarning.greaterThanOrEqual=" + DEFAULT_PRO_EARNING,
            "proEarning.greaterThanOrEqual=" + UPDATED_PRO_EARNING
        );
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning is less than or equal to
        defaultBookingFiltering("proEarning.lessThanOrEqual=" + DEFAULT_PRO_EARNING, "proEarning.lessThanOrEqual=" + SMALLER_PRO_EARNING);
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning is less than
        defaultBookingFiltering("proEarning.lessThan=" + UPDATED_PRO_EARNING, "proEarning.lessThan=" + DEFAULT_PRO_EARNING);
    }

    @Test
    @Transactional
    void getAllBookingsByProEarningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where proEarning is greater than
        defaultBookingFiltering("proEarning.greaterThan=" + SMALLER_PRO_EARNING, "proEarning.greaterThan=" + DEFAULT_PRO_EARNING);
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalEtaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalEta equals to
        defaultBookingFiltering("arrivalEta.equals=" + DEFAULT_ARRIVAL_ETA, "arrivalEta.equals=" + UPDATED_ARRIVAL_ETA);
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalEtaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalEta in
        defaultBookingFiltering("arrivalEta.in=" + DEFAULT_ARRIVAL_ETA + "," + UPDATED_ARRIVAL_ETA, "arrivalEta.in=" + UPDATED_ARRIVAL_ETA);
    }

    @Test
    @Transactional
    void getAllBookingsByArrivalEtaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where arrivalEta is not null
        defaultBookingFiltering("arrivalEta.specified=true", "arrivalEta.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where priority equals to
        defaultBookingFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllBookingsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where priority in
        defaultBookingFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllBookingsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where priority is not null
        defaultBookingFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startedAt equals to
        defaultBookingFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startedAt in
        defaultBookingFiltering("startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT, "startedAt.in=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startedAt is not null
        defaultBookingFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where completedAt equals to
        defaultBookingFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where completedAt in
        defaultBookingFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where completedAt is not null
        defaultBookingFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledAt equals to
        defaultBookingFiltering("cancelledAt.equals=" + DEFAULT_CANCELLED_AT, "cancelledAt.equals=" + UPDATED_CANCELLED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledAt in
        defaultBookingFiltering(
            "cancelledAt.in=" + DEFAULT_CANCELLED_AT + "," + UPDATED_CANCELLED_AT,
            "cancelledAt.in=" + UPDATED_CANCELLED_AT
        );
    }

    @Test
    @Transactional
    void getAllBookingsByCancelledAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where cancelledAt is not null
        defaultBookingFiltering("cancelledAt.specified=true", "cancelledAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where createdAt equals to
        defaultBookingFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where createdAt in
        defaultBookingFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where createdAt is not null
        defaultBookingFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where updatedAt equals to
        defaultBookingFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where updatedAt in
        defaultBookingFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBookingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where updatedAt is not null
        defaultBookingFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIsEqualToSomething() throws Exception {
        User customer;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            customer = UserResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(customer);
        em.flush();
        booking.setCustomer(customer);
        bookingRepository.saveAndFlush(booking);
        Long customerId = customer.getId();
        // Get all the bookingList where customer equals to customerId
        defaultBookingShouldBeFound("customerId.equals=" + customerId);

        // Get all the bookingList where customer equals to (customerId + 1)
        defaultBookingShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByServiceIsEqualToSomething() throws Exception {
        FacilityService service;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            service = FacilityServiceResourceIT.createEntity(em);
        } else {
            service = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        em.persist(service);
        em.flush();
        booking.setService(service);
        bookingRepository.saveAndFlush(booking);
        Long serviceId = service.getId();
        // Get all the bookingList where service equals to serviceId
        defaultBookingShouldBeFound("serviceId.equals=" + serviceId);

        // Get all the bookingList where service equals to (serviceId + 1)
        defaultBookingShouldNotBeFound("serviceId.equals=" + (serviceId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            city = CityResourceIT.createEntity();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        booking.setCity(city);
        bookingRepository.saveAndFlush(booking);
        Long cityId = city.getId();
        // Get all the bookingList where city equals to cityId
        defaultBookingShouldBeFound("cityId.equals=" + cityId);

        // Get all the bookingList where city equals to (cityId + 1)
        defaultBookingShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByAddressIsEqualToSomething() throws Exception {
        CustomerAddress address;
        if (TestUtil.findAll(em, CustomerAddress.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            address = CustomerAddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, CustomerAddress.class).get(0);
        }
        em.persist(address);
        em.flush();
        booking.setAddress(address);
        bookingRepository.saveAndFlush(booking);
        Long addressId = address.getId();
        // Get all the bookingList where address equals to addressId
        defaultBookingShouldBeFound("addressId.equals=" + addressId);

        // Get all the bookingList where address equals to (addressId + 1)
        defaultBookingShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByProfessionalIsEqualToSomething() throws Exception {
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            professional = ProfessionalResourceIT.createEntity(em);
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        em.persist(professional);
        em.flush();
        booking.setProfessional(professional);
        bookingRepository.saveAndFlush(booking);
        Long professionalId = professional.getId();
        // Get all the bookingList where professional equals to professionalId
        defaultBookingShouldBeFound("professionalId.equals=" + professionalId);

        // Get all the bookingList where professional equals to (professionalId + 1)
        defaultBookingShouldNotBeFound("professionalId.equals=" + (professionalId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByCouponIsEqualToSomething() throws Exception {
        Coupon coupon;
        if (TestUtil.findAll(em, Coupon.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            coupon = CouponResourceIT.createEntity();
        } else {
            coupon = TestUtil.findAll(em, Coupon.class).get(0);
        }
        em.persist(coupon);
        em.flush();
        booking.setCoupon(coupon);
        bookingRepository.saveAndFlush(booking);
        Long couponId = coupon.getId();
        // Get all the bookingList where coupon equals to couponId
        defaultBookingShouldBeFound("couponId.equals=" + couponId);

        // Get all the bookingList where coupon equals to (couponId + 1)
        defaultBookingShouldNotBeFound("couponId.equals=" + (couponId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsBySubscriptionIsEqualToSomething() throws Exception {
        BookingSubscription subscription;
        if (TestUtil.findAll(em, BookingSubscription.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            subscription = BookingSubscriptionResourceIT.createEntity(em);
        } else {
            subscription = TestUtil.findAll(em, BookingSubscription.class).get(0);
        }
        em.persist(subscription);
        em.flush();
        booking.setSubscription(subscription);
        bookingRepository.saveAndFlush(booking);
        Long subscriptionId = subscription.getId();
        // Get all the bookingList where subscription equals to subscriptionId
        defaultBookingShouldBeFound("subscriptionId.equals=" + subscriptionId);

        // Get all the bookingList where subscription equals to (subscriptionId + 1)
        defaultBookingShouldNotBeFound("subscriptionId.equals=" + (subscriptionId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsBySlotCapacityIsEqualToSomething() throws Exception {
        SlotCapacity slotCapacity;
        if (TestUtil.findAll(em, SlotCapacity.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            slotCapacity = SlotCapacityResourceIT.createEntity(em);
        } else {
            slotCapacity = TestUtil.findAll(em, SlotCapacity.class).get(0);
        }
        em.persist(slotCapacity);
        em.flush();
        booking.setSlotCapacity(slotCapacity);
        bookingRepository.saveAndFlush(booking);
        Long slotCapacityId = slotCapacity.getId();
        // Get all the bookingList where slotCapacity equals to slotCapacityId
        defaultBookingShouldBeFound("slotCapacityId.equals=" + slotCapacityId);

        // Get all the bookingList where slotCapacity equals to (slotCapacityId + 1)
        defaultBookingShouldNotBeFound("slotCapacityId.equals=" + (slotCapacityId + 1));
    }

    private void defaultBookingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBookingShouldBeFound(shouldBeFound);
        defaultBookingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].publicId").value(hasItem(DEFAULT_PUBLIC_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceTitle").value(hasItem(DEFAULT_SERVICE_TITLE)))
            .andExpect(jsonPath("$.[*].addressSnapshot").value(hasItem(DEFAULT_ADDRESS_SNAPSHOT)))
            .andExpect(jsonPath("$.[*].scheduledStart").value(hasItem(DEFAULT_SCHEDULED_START.toString())))
            .andExpect(jsonPath("$.[*].scheduledEnd").value(hasItem(DEFAULT_SCHEDULED_END.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].addonTotal").value(hasItem(sameNumber(DEFAULT_ADDON_TOTAL))))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].serviceFee").value(hasItem(sameNumber(DEFAULT_SERVICE_FEE))))
            .andExpect(jsonPath("$.[*].taxAmount").value(hasItem(sameNumber(DEFAULT_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].tipAmount").value(hasItem(sameNumber(DEFAULT_TIP_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].customerNotes").value(hasItem(DEFAULT_CUSTOMER_NOTES)))
            .andExpect(jsonPath("$.[*].startOtp").value(hasItem(DEFAULT_START_OTP)))
            .andExpect(jsonPath("$.[*].cancelledBy").value(hasItem(DEFAULT_CANCELLED_BY.toString())))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].walletAmountUsed").value(hasItem(sameNumber(DEFAULT_WALLET_AMOUNT_USED))))
            .andExpect(jsonPath("$.[*].loyaltyPointsUsed").value(hasItem(DEFAULT_LOYALTY_POINTS_USED)))
            .andExpect(jsonPath("$.[*].cancellationFee").value(hasItem(sameNumber(DEFAULT_CANCELLATION_FEE))))
            .andExpect(jsonPath("$.[*].rescheduleCount").value(hasItem(DEFAULT_RESCHEDULE_COUNT)))
            .andExpect(jsonPath("$.[*].platformCommission").value(hasItem(sameNumber(DEFAULT_PLATFORM_COMMISSION))))
            .andExpect(jsonPath("$.[*].proEarning").value(hasItem(sameNumber(DEFAULT_PRO_EARNING))))
            .andExpect(jsonPath("$.[*].arrivalEta").value(hasItem(DEFAULT_ARRIVAL_ETA.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].cancelledAt").value(hasItem(DEFAULT_CANCELLED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .bookingNo(UPDATED_BOOKING_NO)
            .publicId(UPDATED_PUBLIC_ID)
            .serviceTitle(UPDATED_SERVICE_TITLE)
            .addressSnapshot(UPDATED_ADDRESS_SNAPSHOT)
            .scheduledStart(UPDATED_SCHEDULED_START)
            .scheduledEnd(UPDATED_SCHEDULED_END)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .addonTotal(UPDATED_ADDON_TOTAL)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .serviceFee(UPDATED_SERVICE_FEE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .tipAmount(UPDATED_TIP_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .customerNotes(UPDATED_CUSTOMER_NOTES)
            .startOtp(UPDATED_START_OTP)
            .cancelledBy(UPDATED_CANCELLED_BY)
            .cancelReason(UPDATED_CANCEL_REASON)
            .source(UPDATED_SOURCE)
            .walletAmountUsed(UPDATED_WALLET_AMOUNT_USED)
            .loyaltyPointsUsed(UPDATED_LOYALTY_POINTS_USED)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .rescheduleCount(UPDATED_RESCHEDULE_COUNT)
            .platformCommission(UPDATED_PLATFORM_COMMISSION)
            .proEarning(UPDATED_PRO_EARNING)
            .arrivalEta(UPDATED_ARRIVAL_ETA)
            .priority(UPDATED_PRIORITY)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingToMatchAllProperties(updatedBooking);
    }

    @Test
    @Transactional
    void putNonExistingBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking
            .serviceTitle(UPDATED_SERVICE_TITLE)
            .addressSnapshot(UPDATED_ADDRESS_SNAPSHOT)
            .scheduledEnd(UPDATED_SCHEDULED_END)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .addonTotal(UPDATED_ADDON_TOTAL)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .cancelReason(UPDATED_CANCEL_REASON)
            .source(UPDATED_SOURCE)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .platformCommission(UPDATED_PLATFORM_COMMISSION)
            .priority(UPDATED_PRIORITY)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBooking, booking), getPersistedBooking(booking));
    }

    @Test
    @Transactional
    void fullUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setId(booking.getId());

        partialUpdatedBooking
            .bookingNo(UPDATED_BOOKING_NO)
            .publicId(UPDATED_PUBLIC_ID)
            .serviceTitle(UPDATED_SERVICE_TITLE)
            .addressSnapshot(UPDATED_ADDRESS_SNAPSHOT)
            .scheduledStart(UPDATED_SCHEDULED_START)
            .scheduledEnd(UPDATED_SCHEDULED_END)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .currency(UPDATED_CURRENCY)
            .subtotal(UPDATED_SUBTOTAL)
            .addonTotal(UPDATED_ADDON_TOTAL)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .serviceFee(UPDATED_SERVICE_FEE)
            .taxAmount(UPDATED_TAX_AMOUNT)
            .tipAmount(UPDATED_TIP_AMOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .customerNotes(UPDATED_CUSTOMER_NOTES)
            .startOtp(UPDATED_START_OTP)
            .cancelledBy(UPDATED_CANCELLED_BY)
            .cancelReason(UPDATED_CANCEL_REASON)
            .source(UPDATED_SOURCE)
            .walletAmountUsed(UPDATED_WALLET_AMOUNT_USED)
            .loyaltyPointsUsed(UPDATED_LOYALTY_POINTS_USED)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .rescheduleCount(UPDATED_RESCHEDULE_COUNT)
            .platformCommission(UPDATED_PLATFORM_COMMISSION)
            .proEarning(UPDATED_PRO_EARNING)
            .arrivalEta(UPDATED_ARRIVAL_ETA)
            .priority(UPDATED_PRIORITY)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .cancelledAt(UPDATED_CANCELLED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingUpdatableFieldsEquals(partialUpdatedBooking, getPersistedBooking(partialUpdatedBooking));
    }

    @Test
    @Transactional
    void patchNonExistingBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        booking.setId(longCount.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooking() throws Exception {
        // Initialize the database
        insertedBooking = bookingRepository.saveAndFlush(booking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the booking
        restBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, booking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingRepository.count();
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

    protected Booking getPersistedBooking(Booking booking) {
        return bookingRepository.findById(booking.getId()).orElseThrow();
    }

    protected void assertPersistedBookingToMatchAllProperties(Booking expectedBooking) {
        assertBookingAllPropertiesEquals(expectedBooking, getPersistedBooking(expectedBooking));
    }

    protected void assertPersistedBookingToMatchUpdatableProperties(Booking expectedBooking) {
        assertBookingAllUpdatablePropertiesEquals(expectedBooking, getPersistedBooking(expectedBooking));
    }
}
