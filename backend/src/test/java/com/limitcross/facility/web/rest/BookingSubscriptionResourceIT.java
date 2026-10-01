package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingSubscriptionAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.BookingSubscription;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.SubscriptionFrequency;
import com.limitcross.facility.domain.enumeration.SubscriptionStatus;
import com.limitcross.facility.repository.BookingSubscriptionRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.BookingSubscriptionService;
import com.limitcross.facility.service.dto.BookingSubscriptionDTO;
import com.limitcross.facility.service.mapper.BookingSubscriptionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BookingSubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookingSubscriptionResourceIT {

    private static final SubscriptionFrequency DEFAULT_FREQUENCY = SubscriptionFrequency.WEEKLY;
    private static final SubscriptionFrequency UPDATED_FREQUENCY = SubscriptionFrequency.BIWEEKLY;

    private static final String DEFAULT_DAYS_OF_WEEK = "AAAAAAAAAA";
    private static final String UPDATED_DAYS_OF_WEEK = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_TIME = "23:15";
    private static final String UPDATED_PREFERRED_TIME = "17:38";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_NEXT_RUN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_RUN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final SubscriptionStatus DEFAULT_STATUS = SubscriptionStatus.ACTIVE;
    private static final SubscriptionStatus UPDATED_STATUS = SubscriptionStatus.PAUSED;

    private static final Boolean DEFAULT_AUTO_PAY = false;
    private static final Boolean UPDATED_AUTO_PAY = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/booking-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingSubscriptionRepository bookingSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BookingSubscriptionRepository bookingSubscriptionRepositoryMock;

    @Autowired
    private BookingSubscriptionMapper bookingSubscriptionMapper;

    @Mock
    private BookingSubscriptionService bookingSubscriptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingSubscriptionMockMvc;

    private BookingSubscription bookingSubscription;

    private BookingSubscription insertedBookingSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingSubscription createEntity(EntityManager em) {
        BookingSubscription bookingSubscription = new BookingSubscription()
            .frequency(DEFAULT_FREQUENCY)
            .daysOfWeek(DEFAULT_DAYS_OF_WEEK)
            .preferredTime(DEFAULT_PREFERRED_TIME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .nextRunDate(DEFAULT_NEXT_RUN_DATE)
            .status(DEFAULT_STATUS)
            .autoPay(DEFAULT_AUTO_PAY)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        bookingSubscription.setCustomer(user);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        bookingSubscription.setService(facilityService);
        // Add required entity
        ServicePackage servicePackage;
        if (TestUtil.findAll(em, ServicePackage.class).isEmpty()) {
            servicePackage = ServicePackageResourceIT.createEntity(em);
            em.persist(servicePackage);
            em.flush();
        } else {
            servicePackage = TestUtil.findAll(em, ServicePackage.class).get(0);
        }
        bookingSubscription.setServicePackage(servicePackage);
        // Add required entity
        CustomerAddress customerAddress;
        if (TestUtil.findAll(em, CustomerAddress.class).isEmpty()) {
            customerAddress = CustomerAddressResourceIT.createEntity(em);
            em.persist(customerAddress);
            em.flush();
        } else {
            customerAddress = TestUtil.findAll(em, CustomerAddress.class).get(0);
        }
        bookingSubscription.setAddress(customerAddress);
        return bookingSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingSubscription createUpdatedEntity(EntityManager em) {
        BookingSubscription updatedBookingSubscription = new BookingSubscription()
            .frequency(UPDATED_FREQUENCY)
            .daysOfWeek(UPDATED_DAYS_OF_WEEK)
            .preferredTime(UPDATED_PREFERRED_TIME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nextRunDate(UPDATED_NEXT_RUN_DATE)
            .status(UPDATED_STATUS)
            .autoPay(UPDATED_AUTO_PAY)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedBookingSubscription.setCustomer(user);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedBookingSubscription.setService(facilityService);
        // Add required entity
        ServicePackage servicePackage;
        if (TestUtil.findAll(em, ServicePackage.class).isEmpty()) {
            servicePackage = ServicePackageResourceIT.createUpdatedEntity(em);
            em.persist(servicePackage);
            em.flush();
        } else {
            servicePackage = TestUtil.findAll(em, ServicePackage.class).get(0);
        }
        updatedBookingSubscription.setServicePackage(servicePackage);
        // Add required entity
        CustomerAddress customerAddress;
        if (TestUtil.findAll(em, CustomerAddress.class).isEmpty()) {
            customerAddress = CustomerAddressResourceIT.createUpdatedEntity(em);
            em.persist(customerAddress);
            em.flush();
        } else {
            customerAddress = TestUtil.findAll(em, CustomerAddress.class).get(0);
        }
        updatedBookingSubscription.setAddress(customerAddress);
        return updatedBookingSubscription;
    }

    @BeforeEach
    void initTest() {
        bookingSubscription = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingSubscription != null) {
            bookingSubscriptionRepository.delete(insertedBookingSubscription);
            insertedBookingSubscription = null;
        }
    }

    @Test
    @Transactional
    void createBookingSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);
        var returnedBookingSubscriptionDTO = om.readValue(
            restBookingSubscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingSubscriptionDTO.class
        );

        // Validate the BookingSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingSubscription = bookingSubscriptionMapper.toEntity(returnedBookingSubscriptionDTO);
        assertBookingSubscriptionUpdatableFieldsEquals(
            returnedBookingSubscription,
            getPersistedBookingSubscription(returnedBookingSubscription)
        );

        insertedBookingSubscription = returnedBookingSubscription;
    }

    @Test
    @Transactional
    void createBookingSubscriptionWithExistingId() throws Exception {
        // Create the BookingSubscription with an existing ID
        bookingSubscription.setId(1L);
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingSubscription.setFrequency(null);

        // Create the BookingSubscription, which fails.
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreferredTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingSubscription.setPreferredTime(null);

        // Create the BookingSubscription, which fails.
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingSubscription.setStartDate(null);

        // Create the BookingSubscription, which fails.
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNextRunDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingSubscription.setNextRunDate(null);

        // Create the BookingSubscription, which fails.
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingSubscription.setStatus(null);

        // Create the BookingSubscription, which fails.
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingSubscriptions() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        // Get all the bookingSubscriptionList
        restBookingSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].daysOfWeek").value(hasItem(DEFAULT_DAYS_OF_WEEK)))
            .andExpect(jsonPath("$.[*].preferredTime").value(hasItem(DEFAULT_PREFERRED_TIME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextRunDate").value(hasItem(DEFAULT_NEXT_RUN_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].autoPay").value(hasItem(DEFAULT_AUTO_PAY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingSubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookingSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookingSubscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBookingSubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookingSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookingSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookingSubscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBookingSubscription() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        // Get the bookingSubscription
        restBookingSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingSubscription.getId().intValue()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.daysOfWeek").value(DEFAULT_DAYS_OF_WEEK))
            .andExpect(jsonPath("$.preferredTime").value(DEFAULT_PREFERRED_TIME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.nextRunDate").value(DEFAULT_NEXT_RUN_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.autoPay").value(DEFAULT_AUTO_PAY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookingSubscription() throws Exception {
        // Get the bookingSubscription
        restBookingSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingSubscription() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingSubscription
        BookingSubscription updatedBookingSubscription = bookingSubscriptionRepository.findById(bookingSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingSubscription are not directly saved in db
        em.detach(updatedBookingSubscription);
        updatedBookingSubscription
            .frequency(UPDATED_FREQUENCY)
            .daysOfWeek(UPDATED_DAYS_OF_WEEK)
            .preferredTime(UPDATED_PREFERRED_TIME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nextRunDate(UPDATED_NEXT_RUN_DATE)
            .status(UPDATED_STATUS)
            .autoPay(UPDATED_AUTO_PAY)
            .createdAt(UPDATED_CREATED_AT);
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(updatedBookingSubscription);

        restBookingSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingSubscriptionToMatchAllProperties(updatedBookingSubscription);
    }

    @Test
    @Transactional
    void putNonExistingBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingSubscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingSubscription using partial update
        BookingSubscription partialUpdatedBookingSubscription = new BookingSubscription();
        partialUpdatedBookingSubscription.setId(bookingSubscription.getId());

        partialUpdatedBookingSubscription
            .frequency(UPDATED_FREQUENCY)
            .startDate(UPDATED_START_DATE)
            .nextRunDate(UPDATED_NEXT_RUN_DATE)
            .autoPay(UPDATED_AUTO_PAY);

        restBookingSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingSubscription))
            )
            .andExpect(status().isOk());

        // Validate the BookingSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingSubscription, bookingSubscription),
            getPersistedBookingSubscription(bookingSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingSubscription using partial update
        BookingSubscription partialUpdatedBookingSubscription = new BookingSubscription();
        partialUpdatedBookingSubscription.setId(bookingSubscription.getId());

        partialUpdatedBookingSubscription
            .frequency(UPDATED_FREQUENCY)
            .daysOfWeek(UPDATED_DAYS_OF_WEEK)
            .preferredTime(UPDATED_PREFERRED_TIME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .nextRunDate(UPDATED_NEXT_RUN_DATE)
            .status(UPDATED_STATUS)
            .autoPay(UPDATED_AUTO_PAY)
            .createdAt(UPDATED_CREATED_AT);

        restBookingSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingSubscription))
            )
            .andExpect(status().isOk());

        // Validate the BookingSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingSubscriptionUpdatableFieldsEquals(
            partialUpdatedBookingSubscription,
            getPersistedBookingSubscription(partialUpdatedBookingSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingSubscription.setId(longCount.incrementAndGet());

        // Create the BookingSubscription
        BookingSubscriptionDTO bookingSubscriptionDTO = bookingSubscriptionMapper.toDto(bookingSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingSubscription() throws Exception {
        // Initialize the database
        insertedBookingSubscription = bookingSubscriptionRepository.saveAndFlush(bookingSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingSubscription
        restBookingSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingSubscriptionRepository.count();
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

    protected BookingSubscription getPersistedBookingSubscription(BookingSubscription bookingSubscription) {
        return bookingSubscriptionRepository.findById(bookingSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedBookingSubscriptionToMatchAllProperties(BookingSubscription expectedBookingSubscription) {
        assertBookingSubscriptionAllPropertiesEquals(
            expectedBookingSubscription,
            getPersistedBookingSubscription(expectedBookingSubscription)
        );
    }

    protected void assertPersistedBookingSubscriptionToMatchUpdatableProperties(BookingSubscription expectedBookingSubscription) {
        assertBookingSubscriptionAllUpdatablePropertiesEquals(
            expectedBookingSubscription,
            getPersistedBookingSubscription(expectedBookingSubscription)
        );
    }
}
