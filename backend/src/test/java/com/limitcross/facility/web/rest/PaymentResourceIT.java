package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.PaymentAsserts.*;
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
import com.limitcross.facility.domain.Payment;
import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.TransactionStatus;
import com.limitcross.facility.repository.PaymentRepository;
import com.limitcross.facility.service.PaymentService;
import com.limitcross.facility.service.dto.PaymentDTO;
import com.limitcross.facility.service.mapper.PaymentMapper;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final GatewayProvider DEFAULT_GATEWAY = GatewayProvider.RAZORPAY;
    private static final GatewayProvider UPDATED_GATEWAY = GatewayProvider.CASHFREE;

    private static final String DEFAULT_GATEWAY_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GATEWAY_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_PAYMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(0 - 1);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.CREATED;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.AUTHORIZED;

    private static final String DEFAULT_IDEMPOTENCY_KEY = "AAAAAAAAAA";
    private static final String UPDATED_IDEMPOTENCY_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FAILURE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_FAILURE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_RESPONSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Autowired
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentService paymentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    private Payment insertedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .gateway(DEFAULT_GATEWAY)
            .gatewayOrderId(DEFAULT_GATEWAY_ORDER_ID)
            .gatewayPaymentId(DEFAULT_GATEWAY_PAYMENT_ID)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS)
            .idempotencyKey(DEFAULT_IDEMPOTENCY_KEY)
            .failureReason(DEFAULT_FAILURE_REASON)
            .rawResponse(DEFAULT_RAW_RESPONSE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        payment.setBooking(booking);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment updatedPayment = new Payment()
            .gateway(UPDATED_GATEWAY)
            .gatewayOrderId(UPDATED_GATEWAY_ORDER_ID)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .idempotencyKey(UPDATED_IDEMPOTENCY_KEY)
            .failureReason(UPDATED_FAILURE_REASON)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedPayment.setBooking(booking);
        return updatedPayment;
    }

    @BeforeEach
    void initTest() {
        payment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPayment != null) {
            paymentRepository.delete(insertedPayment);
            insertedPayment = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        var returnedPaymentDTO = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDTO.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayment = paymentMapper.toEntity(returnedPaymentDTO);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));

        insertedPayment = returnedPayment;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGatewayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setGateway(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setCurrency(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setStatus(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdempotencyKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setIdempotencyKey(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].gateway").value(hasItem(DEFAULT_GATEWAY.toString())))
            .andExpect(jsonPath("$.[*].gatewayOrderId").value(hasItem(DEFAULT_GATEWAY_ORDER_ID)))
            .andExpect(jsonPath("$.[*].gatewayPaymentId").value(hasItem(DEFAULT_GATEWAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].idempotencyKey").value(hasItem(DEFAULT_IDEMPOTENCY_KEY)))
            .andExpect(jsonPath("$.[*].failureReason").value(hasItem(DEFAULT_FAILURE_REASON)))
            .andExpect(jsonPath("$.[*].rawResponse").value(hasItem(DEFAULT_RAW_RESPONSE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.gateway").value(DEFAULT_GATEWAY.toString()))
            .andExpect(jsonPath("$.gatewayOrderId").value(DEFAULT_GATEWAY_ORDER_ID))
            .andExpect(jsonPath("$.gatewayPaymentId").value(DEFAULT_GATEWAY_PAYMENT_ID))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.idempotencyKey").value(DEFAULT_IDEMPOTENCY_KEY))
            .andExpect(jsonPath("$.failureReason").value(DEFAULT_FAILURE_REASON))
            .andExpect(jsonPath("$.rawResponse").value(DEFAULT_RAW_RESPONSE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway equals to
        defaultPaymentFiltering("gateway.equals=" + DEFAULT_GATEWAY, "gateway.equals=" + UPDATED_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway in
        defaultPaymentFiltering("gateway.in=" + DEFAULT_GATEWAY + "," + UPDATED_GATEWAY, "gateway.in=" + UPDATED_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway is not null
        defaultPaymentFiltering("gateway.specified=true", "gateway.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayOrderId equals to
        defaultPaymentFiltering("gatewayOrderId.equals=" + DEFAULT_GATEWAY_ORDER_ID, "gatewayOrderId.equals=" + UPDATED_GATEWAY_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayOrderId in
        defaultPaymentFiltering(
            "gatewayOrderId.in=" + DEFAULT_GATEWAY_ORDER_ID + "," + UPDATED_GATEWAY_ORDER_ID,
            "gatewayOrderId.in=" + UPDATED_GATEWAY_ORDER_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayOrderId is not null
        defaultPaymentFiltering("gatewayOrderId.specified=true", "gatewayOrderId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayOrderIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayOrderId contains
        defaultPaymentFiltering(
            "gatewayOrderId.contains=" + DEFAULT_GATEWAY_ORDER_ID,
            "gatewayOrderId.contains=" + UPDATED_GATEWAY_ORDER_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayOrderIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayOrderId does not contain
        defaultPaymentFiltering(
            "gatewayOrderId.doesNotContain=" + UPDATED_GATEWAY_ORDER_ID,
            "gatewayOrderId.doesNotContain=" + DEFAULT_GATEWAY_ORDER_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId equals to
        defaultPaymentFiltering(
            "gatewayPaymentId.equals=" + DEFAULT_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.equals=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId in
        defaultPaymentFiltering(
            "gatewayPaymentId.in=" + DEFAULT_GATEWAY_PAYMENT_ID + "," + UPDATED_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.in=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId is not null
        defaultPaymentFiltering("gatewayPaymentId.specified=true", "gatewayPaymentId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId contains
        defaultPaymentFiltering(
            "gatewayPaymentId.contains=" + DEFAULT_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.contains=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId does not contain
        defaultPaymentFiltering(
            "gatewayPaymentId.doesNotContain=" + UPDATED_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.doesNotContain=" + DEFAULT_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod equals to
        defaultPaymentFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod in
        defaultPaymentFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod is not null
        defaultPaymentFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod contains
        defaultPaymentFiltering("paymentMethod.contains=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.contains=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod does not contain
        defaultPaymentFiltering(
            "paymentMethod.doesNotContain=" + UPDATED_PAYMENT_METHOD,
            "paymentMethod.doesNotContain=" + DEFAULT_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to
        defaultPaymentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in
        defaultPaymentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to
        defaultPaymentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to
        defaultPaymentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than
        defaultPaymentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than
        defaultPaymentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency equals to
        defaultPaymentFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency in
        defaultPaymentFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency is not null
        defaultPaymentFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency contains
        defaultPaymentFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency does not contain
        defaultPaymentFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status equals to
        defaultPaymentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status in
        defaultPaymentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status is not null
        defaultPaymentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByIdempotencyKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where idempotencyKey equals to
        defaultPaymentFiltering("idempotencyKey.equals=" + DEFAULT_IDEMPOTENCY_KEY, "idempotencyKey.equals=" + UPDATED_IDEMPOTENCY_KEY);
    }

    @Test
    @Transactional
    void getAllPaymentsByIdempotencyKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where idempotencyKey in
        defaultPaymentFiltering(
            "idempotencyKey.in=" + DEFAULT_IDEMPOTENCY_KEY + "," + UPDATED_IDEMPOTENCY_KEY,
            "idempotencyKey.in=" + UPDATED_IDEMPOTENCY_KEY
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByIdempotencyKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where idempotencyKey is not null
        defaultPaymentFiltering("idempotencyKey.specified=true", "idempotencyKey.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByIdempotencyKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where idempotencyKey contains
        defaultPaymentFiltering("idempotencyKey.contains=" + DEFAULT_IDEMPOTENCY_KEY, "idempotencyKey.contains=" + UPDATED_IDEMPOTENCY_KEY);
    }

    @Test
    @Transactional
    void getAllPaymentsByIdempotencyKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where idempotencyKey does not contain
        defaultPaymentFiltering(
            "idempotencyKey.doesNotContain=" + UPDATED_IDEMPOTENCY_KEY,
            "idempotencyKey.doesNotContain=" + DEFAULT_IDEMPOTENCY_KEY
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByFailureReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where failureReason equals to
        defaultPaymentFiltering("failureReason.equals=" + DEFAULT_FAILURE_REASON, "failureReason.equals=" + UPDATED_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllPaymentsByFailureReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where failureReason in
        defaultPaymentFiltering(
            "failureReason.in=" + DEFAULT_FAILURE_REASON + "," + UPDATED_FAILURE_REASON,
            "failureReason.in=" + UPDATED_FAILURE_REASON
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByFailureReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where failureReason is not null
        defaultPaymentFiltering("failureReason.specified=true", "failureReason.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByFailureReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where failureReason contains
        defaultPaymentFiltering("failureReason.contains=" + DEFAULT_FAILURE_REASON, "failureReason.contains=" + UPDATED_FAILURE_REASON);
    }

    @Test
    @Transactional
    void getAllPaymentsByFailureReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where failureReason does not contain
        defaultPaymentFiltering(
            "failureReason.doesNotContain=" + UPDATED_FAILURE_REASON,
            "failureReason.doesNotContain=" + DEFAULT_FAILURE_REASON
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdAt equals to
        defaultPaymentFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdAt in
        defaultPaymentFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdAt is not null
        defaultPaymentFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedAt equals to
        defaultPaymentFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedAt in
        defaultPaymentFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedAt is not null
        defaultPaymentFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByBookingIsEqualToSomething() throws Exception {
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            booking = BookingResourceIT.createEntity(em);
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        em.persist(booking);
        em.flush();
        payment.setBooking(booking);
        paymentRepository.saveAndFlush(payment);
        Long bookingId = booking.getId();
        // Get all the paymentList where booking equals to bookingId
        defaultPaymentShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the paymentList where booking equals to (bookingId + 1)
        defaultPaymentShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    private void defaultPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentShouldBeFound(shouldBeFound);
        defaultPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].gateway").value(hasItem(DEFAULT_GATEWAY.toString())))
            .andExpect(jsonPath("$.[*].gatewayOrderId").value(hasItem(DEFAULT_GATEWAY_ORDER_ID)))
            .andExpect(jsonPath("$.[*].gatewayPaymentId").value(hasItem(DEFAULT_GATEWAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].idempotencyKey").value(hasItem(DEFAULT_IDEMPOTENCY_KEY)))
            .andExpect(jsonPath("$.[*].failureReason").value(hasItem(DEFAULT_FAILURE_REASON)))
            .andExpect(jsonPath("$.[*].rawResponse").value(hasItem(DEFAULT_RAW_RESPONSE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .gateway(UPDATED_GATEWAY)
            .gatewayOrderId(UPDATED_GATEWAY_ORDER_ID)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .idempotencyKey(UPDATED_IDEMPOTENCY_KEY)
            .failureReason(UPDATED_FAILURE_REASON)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .idempotencyKey(UPDATED_IDEMPOTENCY_KEY);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .gateway(UPDATED_GATEWAY)
            .gatewayOrderId(UPDATED_GATEWAY_ORDER_ID)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .idempotencyKey(UPDATED_IDEMPOTENCY_KEY)
            .failureReason(UPDATED_FAILURE_REASON)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
