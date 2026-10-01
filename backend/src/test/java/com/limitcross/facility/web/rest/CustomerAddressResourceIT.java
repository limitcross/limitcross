package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CustomerAddressAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.AddressLabel;
import com.limitcross.facility.repository.CustomerAddressRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.CustomerAddressService;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.mapper.CustomerAddressMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CustomerAddressResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerAddressResourceIT {

    private static final AddressLabel DEFAULT_LABEL = AddressLabel.HOME;
    private static final AddressLabel UPDATED_LABEL = AddressLabel.WORK;

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final Boolean DEFAULT_DEFAULT_ADDRESS = false;
    private static final Boolean UPDATED_DEFAULT_ADDRESS = true;

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customer-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CustomerAddressRepository customerAddressRepositoryMock;

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Mock
    private CustomerAddressService customerAddressServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerAddressMockMvc;

    private CustomerAddress customerAddress;

    private CustomerAddress insertedCustomerAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAddress createEntity(EntityManager em) {
        CustomerAddress customerAddress = new CustomerAddress()
            .label(DEFAULT_LABEL)
            .contactName(DEFAULT_CONTACT_NAME)
            .contactPhone(DEFAULT_CONTACT_PHONE)
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .landmark(DEFAULT_LANDMARK)
            .pincode(DEFAULT_PINCODE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .defaultAddress(DEFAULT_DEFAULT_ADDRESS)
            .deletedAt(DEFAULT_DELETED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        customerAddress.setCustomer(user);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        customerAddress.setCity(city);
        return customerAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAddress createUpdatedEntity(EntityManager em) {
        CustomerAddress updatedCustomerAddress = new CustomerAddress()
            .label(UPDATED_LABEL)
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .landmark(UPDATED_LANDMARK)
            .pincode(UPDATED_PINCODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCustomerAddress.setCustomer(user);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedCustomerAddress.setCity(city);
        return updatedCustomerAddress;
    }

    @BeforeEach
    void initTest() {
        customerAddress = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerAddress != null) {
            customerAddressRepository.delete(insertedCustomerAddress);
            insertedCustomerAddress = null;
        }
    }

    @Test
    @Transactional
    void createCustomerAddress() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);
        var returnedCustomerAddressDTO = om.readValue(
            restCustomerAddressMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerAddressDTO.class
        );

        // Validate the CustomerAddress in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerAddress = customerAddressMapper.toEntity(returnedCustomerAddressDTO);
        assertCustomerAddressUpdatableFieldsEquals(returnedCustomerAddress, getPersistedCustomerAddress(returnedCustomerAddress));

        insertedCustomerAddress = returnedCustomerAddress;
    }

    @Test
    @Transactional
    void createCustomerAddressWithExistingId() throws Exception {
        // Create the CustomerAddress with an existing ID
        customerAddress.setId(1L);
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setLabel(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setContactName(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setContactPhone(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLine1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setLine1(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPincodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setPincode(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerAddress.setDefaultAddress(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        restCustomerAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerAddresses() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].defaultAddress").value(hasItem(DEFAULT_DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerAddressesWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerAddressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAddressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerAddressServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerAddressesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerAddressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerAddressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerAddressRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerAddress() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get the customerAddress
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, customerAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerAddress.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.defaultAddress").value(DEFAULT_DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCustomerAddressesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        Long id = customerAddress.getId();

        defaultCustomerAddressFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCustomerAddressFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCustomerAddressFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where label equals to
        defaultCustomerAddressFiltering("label.equals=" + DEFAULT_LABEL, "label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where label in
        defaultCustomerAddressFiltering("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL, "label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where label is not null
        defaultCustomerAddressFiltering("label.specified=true", "label.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactName equals to
        defaultCustomerAddressFiltering("contactName.equals=" + DEFAULT_CONTACT_NAME, "contactName.equals=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactName in
        defaultCustomerAddressFiltering(
            "contactName.in=" + DEFAULT_CONTACT_NAME + "," + UPDATED_CONTACT_NAME,
            "contactName.in=" + UPDATED_CONTACT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactName is not null
        defaultCustomerAddressFiltering("contactName.specified=true", "contactName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactName contains
        defaultCustomerAddressFiltering("contactName.contains=" + DEFAULT_CONTACT_NAME, "contactName.contains=" + UPDATED_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactName does not contain
        defaultCustomerAddressFiltering(
            "contactName.doesNotContain=" + UPDATED_CONTACT_NAME,
            "contactName.doesNotContain=" + DEFAULT_CONTACT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactPhone equals to
        defaultCustomerAddressFiltering("contactPhone.equals=" + DEFAULT_CONTACT_PHONE, "contactPhone.equals=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactPhone in
        defaultCustomerAddressFiltering(
            "contactPhone.in=" + DEFAULT_CONTACT_PHONE + "," + UPDATED_CONTACT_PHONE,
            "contactPhone.in=" + UPDATED_CONTACT_PHONE
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactPhone is not null
        defaultCustomerAddressFiltering("contactPhone.specified=true", "contactPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactPhone contains
        defaultCustomerAddressFiltering("contactPhone.contains=" + DEFAULT_CONTACT_PHONE, "contactPhone.contains=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByContactPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where contactPhone does not contain
        defaultCustomerAddressFiltering(
            "contactPhone.doesNotContain=" + UPDATED_CONTACT_PHONE,
            "contactPhone.doesNotContain=" + DEFAULT_CONTACT_PHONE
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line1 equals to
        defaultCustomerAddressFiltering("line1.equals=" + DEFAULT_LINE_1, "line1.equals=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line1 in
        defaultCustomerAddressFiltering("line1.in=" + DEFAULT_LINE_1 + "," + UPDATED_LINE_1, "line1.in=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line1 is not null
        defaultCustomerAddressFiltering("line1.specified=true", "line1.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine1ContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line1 contains
        defaultCustomerAddressFiltering("line1.contains=" + DEFAULT_LINE_1, "line1.contains=" + UPDATED_LINE_1);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line1 does not contain
        defaultCustomerAddressFiltering("line1.doesNotContain=" + UPDATED_LINE_1, "line1.doesNotContain=" + DEFAULT_LINE_1);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line2 equals to
        defaultCustomerAddressFiltering("line2.equals=" + DEFAULT_LINE_2, "line2.equals=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line2 in
        defaultCustomerAddressFiltering("line2.in=" + DEFAULT_LINE_2 + "," + UPDATED_LINE_2, "line2.in=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line2 is not null
        defaultCustomerAddressFiltering("line2.specified=true", "line2.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine2ContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line2 contains
        defaultCustomerAddressFiltering("line2.contains=" + DEFAULT_LINE_2, "line2.contains=" + UPDATED_LINE_2);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLine2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where line2 does not contain
        defaultCustomerAddressFiltering("line2.doesNotContain=" + UPDATED_LINE_2, "line2.doesNotContain=" + DEFAULT_LINE_2);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLandmarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where landmark equals to
        defaultCustomerAddressFiltering("landmark.equals=" + DEFAULT_LANDMARK, "landmark.equals=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLandmarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where landmark in
        defaultCustomerAddressFiltering("landmark.in=" + DEFAULT_LANDMARK + "," + UPDATED_LANDMARK, "landmark.in=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLandmarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where landmark is not null
        defaultCustomerAddressFiltering("landmark.specified=true", "landmark.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLandmarkContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where landmark contains
        defaultCustomerAddressFiltering("landmark.contains=" + DEFAULT_LANDMARK, "landmark.contains=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLandmarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where landmark does not contain
        defaultCustomerAddressFiltering("landmark.doesNotContain=" + UPDATED_LANDMARK, "landmark.doesNotContain=" + DEFAULT_LANDMARK);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where pincode equals to
        defaultCustomerAddressFiltering("pincode.equals=" + DEFAULT_PINCODE, "pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where pincode in
        defaultCustomerAddressFiltering("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE, "pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where pincode is not null
        defaultCustomerAddressFiltering("pincode.specified=true", "pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByPincodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where pincode contains
        defaultCustomerAddressFiltering("pincode.contains=" + DEFAULT_PINCODE, "pincode.contains=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByPincodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where pincode does not contain
        defaultCustomerAddressFiltering("pincode.doesNotContain=" + UPDATED_PINCODE, "pincode.doesNotContain=" + DEFAULT_PINCODE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude equals to
        defaultCustomerAddressFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude in
        defaultCustomerAddressFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude is not null
        defaultCustomerAddressFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude is greater than or equal to
        defaultCustomerAddressFiltering(
            "latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE,
            "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude is less than or equal to
        defaultCustomerAddressFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude is less than
        defaultCustomerAddressFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where latitude is greater than
        defaultCustomerAddressFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude equals to
        defaultCustomerAddressFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude in
        defaultCustomerAddressFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude is not null
        defaultCustomerAddressFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude is greater than or equal to
        defaultCustomerAddressFiltering(
            "longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE,
            "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude is less than or equal to
        defaultCustomerAddressFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude is less than
        defaultCustomerAddressFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where longitude is greater than
        defaultCustomerAddressFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDefaultAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where defaultAddress equals to
        defaultCustomerAddressFiltering(
            "defaultAddress.equals=" + DEFAULT_DEFAULT_ADDRESS,
            "defaultAddress.equals=" + UPDATED_DEFAULT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDefaultAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where defaultAddress in
        defaultCustomerAddressFiltering(
            "defaultAddress.in=" + DEFAULT_DEFAULT_ADDRESS + "," + UPDATED_DEFAULT_ADDRESS,
            "defaultAddress.in=" + UPDATED_DEFAULT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDefaultAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where defaultAddress is not null
        defaultCustomerAddressFiltering("defaultAddress.specified=true", "defaultAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDeletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where deletedAt equals to
        defaultCustomerAddressFiltering("deletedAt.equals=" + DEFAULT_DELETED_AT, "deletedAt.equals=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDeletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where deletedAt in
        defaultCustomerAddressFiltering(
            "deletedAt.in=" + DEFAULT_DELETED_AT + "," + UPDATED_DELETED_AT,
            "deletedAt.in=" + UPDATED_DELETED_AT
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByDeletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where deletedAt is not null
        defaultCustomerAddressFiltering("deletedAt.specified=true", "deletedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where createdAt equals to
        defaultCustomerAddressFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where createdAt in
        defaultCustomerAddressFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where createdAt is not null
        defaultCustomerAddressFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where updatedAt equals to
        defaultCustomerAddressFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where updatedAt in
        defaultCustomerAddressFiltering(
            "updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT,
            "updatedAt.in=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddressList where updatedAt is not null
        defaultCustomerAddressFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByCustomerIsEqualToSomething() throws Exception {
        User customer;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            customerAddressRepository.saveAndFlush(customerAddress);
            customer = UserResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(customer);
        em.flush();
        customerAddress.setCustomer(customer);
        customerAddressRepository.saveAndFlush(customerAddress);
        Long customerId = customer.getId();
        // Get all the customerAddressList where customer equals to customerId
        defaultCustomerAddressShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerAddressList where customer equals to (customerId + 1)
        defaultCustomerAddressShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllCustomerAddressesByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            customerAddressRepository.saveAndFlush(customerAddress);
            city = CityResourceIT.createEntity();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        customerAddress.setCity(city);
        customerAddressRepository.saveAndFlush(customerAddress);
        Long cityId = city.getId();
        // Get all the customerAddressList where city equals to cityId
        defaultCustomerAddressShouldBeFound("cityId.equals=" + cityId);

        // Get all the customerAddressList where city equals to (cityId + 1)
        defaultCustomerAddressShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    private void defaultCustomerAddressFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCustomerAddressShouldBeFound(shouldBeFound);
        defaultCustomerAddressShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerAddressShouldBeFound(String filter) throws Exception {
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].defaultAddress").value(hasItem(DEFAULT_DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerAddressShouldNotBeFound(String filter) throws Exception {
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerAddress() throws Exception {
        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerAddress() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAddress
        CustomerAddress updatedCustomerAddress = customerAddressRepository.findById(customerAddress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerAddress are not directly saved in db
        em.detach(updatedCustomerAddress);
        updatedCustomerAddress
            .label(UPDATED_LABEL)
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .landmark(UPDATED_LANDMARK)
            .pincode(UPDATED_PINCODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(updatedCustomerAddress);

        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerAddressToMatchAllProperties(updatedCustomerAddress);
    }

    @Test
    @Transactional
    void putNonExistingCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerAddressWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAddress using partial update
        CustomerAddress partialUpdatedCustomerAddress = new CustomerAddress();
        partialUpdatedCustomerAddress.setId(customerAddress.getId());

        partialUpdatedCustomerAddress
            .label(UPDATED_LABEL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerAddress))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerAddressUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerAddress, customerAddress),
            getPersistedCustomerAddress(customerAddress)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerAddressWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerAddress using partial update
        CustomerAddress partialUpdatedCustomerAddress = new CustomerAddress();
        partialUpdatedCustomerAddress.setId(customerAddress.getId());

        partialUpdatedCustomerAddress
            .label(UPDATED_LABEL)
            .contactName(UPDATED_CONTACT_NAME)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .landmark(UPDATED_LANDMARK)
            .pincode(UPDATED_PINCODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .defaultAddress(UPDATED_DEFAULT_ADDRESS)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerAddress))
            )
            .andExpect(status().isOk());

        // Validate the CustomerAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerAddressUpdatableFieldsEquals(
            partialUpdatedCustomerAddress,
            getPersistedCustomerAddress(partialUpdatedCustomerAddress)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerAddress.setId(longCount.incrementAndGet());

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.toDto(customerAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerAddressMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerAddress() throws Exception {
        // Initialize the database
        insertedCustomerAddress = customerAddressRepository.saveAndFlush(customerAddress);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerAddress
        restCustomerAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerAddressRepository.count();
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

    protected CustomerAddress getPersistedCustomerAddress(CustomerAddress customerAddress) {
        return customerAddressRepository.findById(customerAddress.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerAddressToMatchAllProperties(CustomerAddress expectedCustomerAddress) {
        assertCustomerAddressAllPropertiesEquals(expectedCustomerAddress, getPersistedCustomerAddress(expectedCustomerAddress));
    }

    protected void assertPersistedCustomerAddressToMatchUpdatableProperties(CustomerAddress expectedCustomerAddress) {
        assertCustomerAddressAllUpdatablePropertiesEquals(expectedCustomerAddress, getPersistedCustomerAddress(expectedCustomerAddress));
    }
}
