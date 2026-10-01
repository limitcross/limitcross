package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalAsserts.*;
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
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.Gender;
import com.limitcross.facility.domain.enumeration.OnboardingStatus;
import com.limitcross.facility.repository.ProfessionalRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ProfessionalService;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.mapper.ProfessionalMapper;
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
 * Integration tests for the {@link ProfessionalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalResourceIT {

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final OnboardingStatus DEFAULT_ONBOARDING_STATUS = OnboardingStatus.APPLIED;
    private static final OnboardingStatus UPDATED_ONBOARDING_STATUS = OnboardingStatus.KYC_PENDING;

    private static final Boolean DEFAULT_ONLINE = false;
    private static final Boolean UPDATED_ONLINE = true;

    private static final Double DEFAULT_AVG_RATING = 0D;
    private static final Double UPDATED_AVG_RATING = 1D;
    private static final Double SMALLER_AVG_RATING = 0D - 1D;

    private static final Integer DEFAULT_JOBS_COMPLETED = 0;
    private static final Integer UPDATED_JOBS_COMPLETED = 1;
    private static final Integer SMALLER_JOBS_COMPLETED = 0 - 1;

    private static final Double DEFAULT_CANCELLATION_RATE = 0D;
    private static final Double UPDATED_CANCELLATION_RATE = 1D;
    private static final Double SMALLER_CANCELLATION_RATE = 0D - 1D;

    private static final Double DEFAULT_LAST_LAT = 1D;
    private static final Double UPDATED_LAST_LAT = 2D;
    private static final Double SMALLER_LAST_LAT = 1D - 1D;

    private static final Double DEFAULT_LAST_LNG = 1D;
    private static final Double UPDATED_LAST_LNG = 2D;
    private static final Double SMALLER_LAST_LNG = 1D - 1D;

    private static final Instant DEFAULT_LAST_LOCATION_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOCATION_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_JOINED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOINED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_CASH_IN_HAND = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_IN_HAND = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_IN_HAND = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CASH_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CASH_LIMIT = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_MAX_DAILY_JOBS = 1;
    private static final Integer UPDATED_MAX_DAILY_JOBS = 2;
    private static final Integer SMALLER_MAX_DAILY_JOBS = 1 - 1;

    private static final String DEFAULT_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGES = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_ENC = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_ENC = "BBBBBBBBBB";

    private static final String DEFAULT_IFSC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_IFSC_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ProfessionalRepository professionalRepositoryMock;

    @Autowired
    private ProfessionalMapper professionalMapper;

    @Mock
    private ProfessionalService professionalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalMockMvc;

    private Professional professional;

    private Professional insertedProfessional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professional createEntity(EntityManager em) {
        Professional professional = new Professional()
            .displayName(DEFAULT_DISPLAY_NAME)
            .photoUrl(DEFAULT_PHOTO_URL)
            .gender(DEFAULT_GENDER)
            .onboardingStatus(DEFAULT_ONBOARDING_STATUS)
            .online(DEFAULT_ONLINE)
            .avgRating(DEFAULT_AVG_RATING)
            .jobsCompleted(DEFAULT_JOBS_COMPLETED)
            .cancellationRate(DEFAULT_CANCELLATION_RATE)
            .lastLat(DEFAULT_LAST_LAT)
            .lastLng(DEFAULT_LAST_LNG)
            .lastLocationAt(DEFAULT_LAST_LOCATION_AT)
            .joinedAt(DEFAULT_JOINED_AT)
            .cashInHand(DEFAULT_CASH_IN_HAND)
            .cashLimit(DEFAULT_CASH_LIMIT)
            .maxDailyJobs(DEFAULT_MAX_DAILY_JOBS)
            .languages(DEFAULT_LANGUAGES)
            .bankAccountEnc(DEFAULT_BANK_ACCOUNT_ENC)
            .ifscCode(DEFAULT_IFSC_CODE)
            .deletedAt(DEFAULT_DELETED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        professional.setUser(user);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        professional.setHomeCity(city);
        return professional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professional createUpdatedEntity(EntityManager em) {
        Professional updatedProfessional = new Professional()
            .displayName(UPDATED_DISPLAY_NAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .gender(UPDATED_GENDER)
            .onboardingStatus(UPDATED_ONBOARDING_STATUS)
            .online(UPDATED_ONLINE)
            .avgRating(UPDATED_AVG_RATING)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .cancellationRate(UPDATED_CANCELLATION_RATE)
            .lastLat(UPDATED_LAST_LAT)
            .lastLng(UPDATED_LAST_LNG)
            .lastLocationAt(UPDATED_LAST_LOCATION_AT)
            .joinedAt(UPDATED_JOINED_AT)
            .cashInHand(UPDATED_CASH_IN_HAND)
            .cashLimit(UPDATED_CASH_LIMIT)
            .maxDailyJobs(UPDATED_MAX_DAILY_JOBS)
            .languages(UPDATED_LANGUAGES)
            .bankAccountEnc(UPDATED_BANK_ACCOUNT_ENC)
            .ifscCode(UPDATED_IFSC_CODE)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedProfessional.setUser(user);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity();
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        updatedProfessional.setHomeCity(city);
        return updatedProfessional;
    }

    @BeforeEach
    void initTest() {
        professional = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessional != null) {
            professionalRepository.delete(insertedProfessional);
            insertedProfessional = null;
        }
    }

    @Test
    @Transactional
    void createProfessional() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);
        var returnedProfessionalDTO = om.readValue(
            restProfessionalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalDTO.class
        );

        // Validate the Professional in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessional = professionalMapper.toEntity(returnedProfessionalDTO);
        assertProfessionalUpdatableFieldsEquals(returnedProfessional, getPersistedProfessional(returnedProfessional));

        insertedProfessional = returnedProfessional;
    }

    @Test
    @Transactional
    void createProfessionalWithExistingId() throws Exception {
        // Create the Professional with an existing ID
        professional.setId(1L);
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDisplayNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professional.setDisplayName(null);

        // Create the Professional, which fails.
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOnboardingStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professional.setOnboardingStatus(null);

        // Create the Professional, which fails.
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOnlineIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professional.setOnline(null);

        // Create the Professional, which fails.
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        restProfessionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionals() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professional.getId().intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].onboardingStatus").value(hasItem(DEFAULT_ONBOARDING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].online").value(hasItem(DEFAULT_ONLINE)))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)))
            .andExpect(jsonPath("$.[*].jobsCompleted").value(hasItem(DEFAULT_JOBS_COMPLETED)))
            .andExpect(jsonPath("$.[*].cancellationRate").value(hasItem(DEFAULT_CANCELLATION_RATE)))
            .andExpect(jsonPath("$.[*].lastLat").value(hasItem(DEFAULT_LAST_LAT)))
            .andExpect(jsonPath("$.[*].lastLng").value(hasItem(DEFAULT_LAST_LNG)))
            .andExpect(jsonPath("$.[*].lastLocationAt").value(hasItem(DEFAULT_LAST_LOCATION_AT.toString())))
            .andExpect(jsonPath("$.[*].joinedAt").value(hasItem(DEFAULT_JOINED_AT.toString())))
            .andExpect(jsonPath("$.[*].cashInHand").value(hasItem(sameNumber(DEFAULT_CASH_IN_HAND))))
            .andExpect(jsonPath("$.[*].cashLimit").value(hasItem(sameNumber(DEFAULT_CASH_LIMIT))))
            .andExpect(jsonPath("$.[*].maxDailyJobs").value(hasItem(DEFAULT_MAX_DAILY_JOBS)))
            .andExpect(jsonPath("$.[*].languages").value(hasItem(DEFAULT_LANGUAGES)))
            .andExpect(jsonPath("$.[*].bankAccountEnc").value(hasItem(DEFAULT_BANK_ACCOUNT_ENC)))
            .andExpect(jsonPath("$.[*].ifscCode").value(hasItem(DEFAULT_IFSC_CODE)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessional() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get the professional
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL_ID, professional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professional.getId().intValue()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.onboardingStatus").value(DEFAULT_ONBOARDING_STATUS.toString()))
            .andExpect(jsonPath("$.online").value(DEFAULT_ONLINE))
            .andExpect(jsonPath("$.avgRating").value(DEFAULT_AVG_RATING))
            .andExpect(jsonPath("$.jobsCompleted").value(DEFAULT_JOBS_COMPLETED))
            .andExpect(jsonPath("$.cancellationRate").value(DEFAULT_CANCELLATION_RATE))
            .andExpect(jsonPath("$.lastLat").value(DEFAULT_LAST_LAT))
            .andExpect(jsonPath("$.lastLng").value(DEFAULT_LAST_LNG))
            .andExpect(jsonPath("$.lastLocationAt").value(DEFAULT_LAST_LOCATION_AT.toString()))
            .andExpect(jsonPath("$.joinedAt").value(DEFAULT_JOINED_AT.toString()))
            .andExpect(jsonPath("$.cashInHand").value(sameNumber(DEFAULT_CASH_IN_HAND)))
            .andExpect(jsonPath("$.cashLimit").value(sameNumber(DEFAULT_CASH_LIMIT)))
            .andExpect(jsonPath("$.maxDailyJobs").value(DEFAULT_MAX_DAILY_JOBS))
            .andExpect(jsonPath("$.languages").value(DEFAULT_LANGUAGES))
            .andExpect(jsonPath("$.bankAccountEnc").value(DEFAULT_BANK_ACCOUNT_ENC))
            .andExpect(jsonPath("$.ifscCode").value(DEFAULT_IFSC_CODE))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getProfessionalsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        Long id = professional.getId();

        defaultProfessionalFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProfessionalFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProfessionalFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where displayName equals to
        defaultProfessionalFiltering("displayName.equals=" + DEFAULT_DISPLAY_NAME, "displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where displayName in
        defaultProfessionalFiltering(
            "displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME,
            "displayName.in=" + UPDATED_DISPLAY_NAME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where displayName is not null
        defaultProfessionalFiltering("displayName.specified=true", "displayName.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByDisplayNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where displayName contains
        defaultProfessionalFiltering("displayName.contains=" + DEFAULT_DISPLAY_NAME, "displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDisplayNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where displayName does not contain
        defaultProfessionalFiltering(
            "displayName.doesNotContain=" + UPDATED_DISPLAY_NAME,
            "displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where photoUrl equals to
        defaultProfessionalFiltering("photoUrl.equals=" + DEFAULT_PHOTO_URL, "photoUrl.equals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllProfessionalsByPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where photoUrl in
        defaultProfessionalFiltering("photoUrl.in=" + DEFAULT_PHOTO_URL + "," + UPDATED_PHOTO_URL, "photoUrl.in=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllProfessionalsByPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where photoUrl is not null
        defaultProfessionalFiltering("photoUrl.specified=true", "photoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where photoUrl contains
        defaultProfessionalFiltering("photoUrl.contains=" + DEFAULT_PHOTO_URL, "photoUrl.contains=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllProfessionalsByPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where photoUrl does not contain
        defaultProfessionalFiltering("photoUrl.doesNotContain=" + UPDATED_PHOTO_URL, "photoUrl.doesNotContain=" + DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void getAllProfessionalsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where gender equals to
        defaultProfessionalFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllProfessionalsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where gender in
        defaultProfessionalFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllProfessionalsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where gender is not null
        defaultProfessionalFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnboardingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where onboardingStatus equals to
        defaultProfessionalFiltering(
            "onboardingStatus.equals=" + DEFAULT_ONBOARDING_STATUS,
            "onboardingStatus.equals=" + UPDATED_ONBOARDING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnboardingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where onboardingStatus in
        defaultProfessionalFiltering(
            "onboardingStatus.in=" + DEFAULT_ONBOARDING_STATUS + "," + UPDATED_ONBOARDING_STATUS,
            "onboardingStatus.in=" + UPDATED_ONBOARDING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnboardingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where onboardingStatus is not null
        defaultProfessionalFiltering("onboardingStatus.specified=true", "onboardingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnlineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where online equals to
        defaultProfessionalFiltering("online.equals=" + DEFAULT_ONLINE, "online.equals=" + UPDATED_ONLINE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnlineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where online in
        defaultProfessionalFiltering("online.in=" + DEFAULT_ONLINE + "," + UPDATED_ONLINE, "online.in=" + UPDATED_ONLINE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByOnlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where online is not null
        defaultProfessionalFiltering("online.specified=true", "online.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating equals to
        defaultProfessionalFiltering("avgRating.equals=" + DEFAULT_AVG_RATING, "avgRating.equals=" + UPDATED_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating in
        defaultProfessionalFiltering("avgRating.in=" + DEFAULT_AVG_RATING + "," + UPDATED_AVG_RATING, "avgRating.in=" + UPDATED_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating is not null
        defaultProfessionalFiltering("avgRating.specified=true", "avgRating.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating is greater than or equal to
        defaultProfessionalFiltering(
            "avgRating.greaterThanOrEqual=" + DEFAULT_AVG_RATING,
            "avgRating.greaterThanOrEqual=" + (DEFAULT_AVG_RATING + 1)
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating is less than or equal to
        defaultProfessionalFiltering("avgRating.lessThanOrEqual=" + DEFAULT_AVG_RATING, "avgRating.lessThanOrEqual=" + SMALLER_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating is less than
        defaultProfessionalFiltering("avgRating.lessThan=" + (DEFAULT_AVG_RATING + 1), "avgRating.lessThan=" + DEFAULT_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllProfessionalsByAvgRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where avgRating is greater than
        defaultProfessionalFiltering("avgRating.greaterThan=" + SMALLER_AVG_RATING, "avgRating.greaterThan=" + DEFAULT_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted equals to
        defaultProfessionalFiltering("jobsCompleted.equals=" + DEFAULT_JOBS_COMPLETED, "jobsCompleted.equals=" + UPDATED_JOBS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted in
        defaultProfessionalFiltering(
            "jobsCompleted.in=" + DEFAULT_JOBS_COMPLETED + "," + UPDATED_JOBS_COMPLETED,
            "jobsCompleted.in=" + UPDATED_JOBS_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted is not null
        defaultProfessionalFiltering("jobsCompleted.specified=true", "jobsCompleted.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted is greater than or equal to
        defaultProfessionalFiltering(
            "jobsCompleted.greaterThanOrEqual=" + DEFAULT_JOBS_COMPLETED,
            "jobsCompleted.greaterThanOrEqual=" + UPDATED_JOBS_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted is less than or equal to
        defaultProfessionalFiltering(
            "jobsCompleted.lessThanOrEqual=" + DEFAULT_JOBS_COMPLETED,
            "jobsCompleted.lessThanOrEqual=" + SMALLER_JOBS_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted is less than
        defaultProfessionalFiltering(
            "jobsCompleted.lessThan=" + UPDATED_JOBS_COMPLETED,
            "jobsCompleted.lessThan=" + DEFAULT_JOBS_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByJobsCompletedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where jobsCompleted is greater than
        defaultProfessionalFiltering(
            "jobsCompleted.greaterThan=" + SMALLER_JOBS_COMPLETED,
            "jobsCompleted.greaterThan=" + DEFAULT_JOBS_COMPLETED
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate equals to
        defaultProfessionalFiltering(
            "cancellationRate.equals=" + DEFAULT_CANCELLATION_RATE,
            "cancellationRate.equals=" + UPDATED_CANCELLATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate in
        defaultProfessionalFiltering(
            "cancellationRate.in=" + DEFAULT_CANCELLATION_RATE + "," + UPDATED_CANCELLATION_RATE,
            "cancellationRate.in=" + UPDATED_CANCELLATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate is not null
        defaultProfessionalFiltering("cancellationRate.specified=true", "cancellationRate.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate is greater than or equal to
        defaultProfessionalFiltering(
            "cancellationRate.greaterThanOrEqual=" + DEFAULT_CANCELLATION_RATE,
            "cancellationRate.greaterThanOrEqual=" + (DEFAULT_CANCELLATION_RATE + 1)
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate is less than or equal to
        defaultProfessionalFiltering(
            "cancellationRate.lessThanOrEqual=" + DEFAULT_CANCELLATION_RATE,
            "cancellationRate.lessThanOrEqual=" + SMALLER_CANCELLATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate is less than
        defaultProfessionalFiltering(
            "cancellationRate.lessThan=" + (DEFAULT_CANCELLATION_RATE + 1),
            "cancellationRate.lessThan=" + DEFAULT_CANCELLATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCancellationRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cancellationRate is greater than
        defaultProfessionalFiltering(
            "cancellationRate.greaterThan=" + SMALLER_CANCELLATION_RATE,
            "cancellationRate.greaterThan=" + DEFAULT_CANCELLATION_RATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat equals to
        defaultProfessionalFiltering("lastLat.equals=" + DEFAULT_LAST_LAT, "lastLat.equals=" + UPDATED_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat in
        defaultProfessionalFiltering("lastLat.in=" + DEFAULT_LAST_LAT + "," + UPDATED_LAST_LAT, "lastLat.in=" + UPDATED_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat is not null
        defaultProfessionalFiltering("lastLat.specified=true", "lastLat.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat is greater than or equal to
        defaultProfessionalFiltering("lastLat.greaterThanOrEqual=" + DEFAULT_LAST_LAT, "lastLat.greaterThanOrEqual=" + UPDATED_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat is less than or equal to
        defaultProfessionalFiltering("lastLat.lessThanOrEqual=" + DEFAULT_LAST_LAT, "lastLat.lessThanOrEqual=" + SMALLER_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat is less than
        defaultProfessionalFiltering("lastLat.lessThan=" + UPDATED_LAST_LAT, "lastLat.lessThan=" + DEFAULT_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLat is greater than
        defaultProfessionalFiltering("lastLat.greaterThan=" + SMALLER_LAST_LAT, "lastLat.greaterThan=" + DEFAULT_LAST_LAT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng equals to
        defaultProfessionalFiltering("lastLng.equals=" + DEFAULT_LAST_LNG, "lastLng.equals=" + UPDATED_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng in
        defaultProfessionalFiltering("lastLng.in=" + DEFAULT_LAST_LNG + "," + UPDATED_LAST_LNG, "lastLng.in=" + UPDATED_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng is not null
        defaultProfessionalFiltering("lastLng.specified=true", "lastLng.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng is greater than or equal to
        defaultProfessionalFiltering("lastLng.greaterThanOrEqual=" + DEFAULT_LAST_LNG, "lastLng.greaterThanOrEqual=" + UPDATED_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng is less than or equal to
        defaultProfessionalFiltering("lastLng.lessThanOrEqual=" + DEFAULT_LAST_LNG, "lastLng.lessThanOrEqual=" + SMALLER_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng is less than
        defaultProfessionalFiltering("lastLng.lessThan=" + UPDATED_LAST_LNG, "lastLng.lessThan=" + DEFAULT_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLng is greater than
        defaultProfessionalFiltering("lastLng.greaterThan=" + SMALLER_LAST_LNG, "lastLng.greaterThan=" + DEFAULT_LAST_LNG);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLocationAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLocationAt equals to
        defaultProfessionalFiltering(
            "lastLocationAt.equals=" + DEFAULT_LAST_LOCATION_AT,
            "lastLocationAt.equals=" + UPDATED_LAST_LOCATION_AT
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLocationAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLocationAt in
        defaultProfessionalFiltering(
            "lastLocationAt.in=" + DEFAULT_LAST_LOCATION_AT + "," + UPDATED_LAST_LOCATION_AT,
            "lastLocationAt.in=" + UPDATED_LAST_LOCATION_AT
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByLastLocationAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where lastLocationAt is not null
        defaultProfessionalFiltering("lastLocationAt.specified=true", "lastLocationAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByJoinedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where joinedAt equals to
        defaultProfessionalFiltering("joinedAt.equals=" + DEFAULT_JOINED_AT, "joinedAt.equals=" + UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByJoinedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where joinedAt in
        defaultProfessionalFiltering("joinedAt.in=" + DEFAULT_JOINED_AT + "," + UPDATED_JOINED_AT, "joinedAt.in=" + UPDATED_JOINED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByJoinedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where joinedAt is not null
        defaultProfessionalFiltering("joinedAt.specified=true", "joinedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand equals to
        defaultProfessionalFiltering("cashInHand.equals=" + DEFAULT_CASH_IN_HAND, "cashInHand.equals=" + UPDATED_CASH_IN_HAND);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand in
        defaultProfessionalFiltering(
            "cashInHand.in=" + DEFAULT_CASH_IN_HAND + "," + UPDATED_CASH_IN_HAND,
            "cashInHand.in=" + UPDATED_CASH_IN_HAND
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand is not null
        defaultProfessionalFiltering("cashInHand.specified=true", "cashInHand.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand is greater than or equal to
        defaultProfessionalFiltering(
            "cashInHand.greaterThanOrEqual=" + DEFAULT_CASH_IN_HAND,
            "cashInHand.greaterThanOrEqual=" + UPDATED_CASH_IN_HAND
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand is less than or equal to
        defaultProfessionalFiltering(
            "cashInHand.lessThanOrEqual=" + DEFAULT_CASH_IN_HAND,
            "cashInHand.lessThanOrEqual=" + SMALLER_CASH_IN_HAND
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand is less than
        defaultProfessionalFiltering("cashInHand.lessThan=" + UPDATED_CASH_IN_HAND, "cashInHand.lessThan=" + DEFAULT_CASH_IN_HAND);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashInHandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashInHand is greater than
        defaultProfessionalFiltering("cashInHand.greaterThan=" + SMALLER_CASH_IN_HAND, "cashInHand.greaterThan=" + DEFAULT_CASH_IN_HAND);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit equals to
        defaultProfessionalFiltering("cashLimit.equals=" + DEFAULT_CASH_LIMIT, "cashLimit.equals=" + UPDATED_CASH_LIMIT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit in
        defaultProfessionalFiltering("cashLimit.in=" + DEFAULT_CASH_LIMIT + "," + UPDATED_CASH_LIMIT, "cashLimit.in=" + UPDATED_CASH_LIMIT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit is not null
        defaultProfessionalFiltering("cashLimit.specified=true", "cashLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit is greater than or equal to
        defaultProfessionalFiltering(
            "cashLimit.greaterThanOrEqual=" + DEFAULT_CASH_LIMIT,
            "cashLimit.greaterThanOrEqual=" + UPDATED_CASH_LIMIT
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit is less than or equal to
        defaultProfessionalFiltering("cashLimit.lessThanOrEqual=" + DEFAULT_CASH_LIMIT, "cashLimit.lessThanOrEqual=" + SMALLER_CASH_LIMIT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit is less than
        defaultProfessionalFiltering("cashLimit.lessThan=" + UPDATED_CASH_LIMIT, "cashLimit.lessThan=" + DEFAULT_CASH_LIMIT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCashLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where cashLimit is greater than
        defaultProfessionalFiltering("cashLimit.greaterThan=" + SMALLER_CASH_LIMIT, "cashLimit.greaterThan=" + DEFAULT_CASH_LIMIT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs equals to
        defaultProfessionalFiltering("maxDailyJobs.equals=" + DEFAULT_MAX_DAILY_JOBS, "maxDailyJobs.equals=" + UPDATED_MAX_DAILY_JOBS);
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs in
        defaultProfessionalFiltering(
            "maxDailyJobs.in=" + DEFAULT_MAX_DAILY_JOBS + "," + UPDATED_MAX_DAILY_JOBS,
            "maxDailyJobs.in=" + UPDATED_MAX_DAILY_JOBS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs is not null
        defaultProfessionalFiltering("maxDailyJobs.specified=true", "maxDailyJobs.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs is greater than or equal to
        defaultProfessionalFiltering(
            "maxDailyJobs.greaterThanOrEqual=" + DEFAULT_MAX_DAILY_JOBS,
            "maxDailyJobs.greaterThanOrEqual=" + UPDATED_MAX_DAILY_JOBS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs is less than or equal to
        defaultProfessionalFiltering(
            "maxDailyJobs.lessThanOrEqual=" + DEFAULT_MAX_DAILY_JOBS,
            "maxDailyJobs.lessThanOrEqual=" + SMALLER_MAX_DAILY_JOBS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs is less than
        defaultProfessionalFiltering("maxDailyJobs.lessThan=" + UPDATED_MAX_DAILY_JOBS, "maxDailyJobs.lessThan=" + DEFAULT_MAX_DAILY_JOBS);
    }

    @Test
    @Transactional
    void getAllProfessionalsByMaxDailyJobsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where maxDailyJobs is greater than
        defaultProfessionalFiltering(
            "maxDailyJobs.greaterThan=" + SMALLER_MAX_DAILY_JOBS,
            "maxDailyJobs.greaterThan=" + DEFAULT_MAX_DAILY_JOBS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByLanguagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where languages equals to
        defaultProfessionalFiltering("languages.equals=" + DEFAULT_LANGUAGES, "languages.equals=" + UPDATED_LANGUAGES);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLanguagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where languages in
        defaultProfessionalFiltering("languages.in=" + DEFAULT_LANGUAGES + "," + UPDATED_LANGUAGES, "languages.in=" + UPDATED_LANGUAGES);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLanguagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where languages is not null
        defaultProfessionalFiltering("languages.specified=true", "languages.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByLanguagesContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where languages contains
        defaultProfessionalFiltering("languages.contains=" + DEFAULT_LANGUAGES, "languages.contains=" + UPDATED_LANGUAGES);
    }

    @Test
    @Transactional
    void getAllProfessionalsByLanguagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where languages does not contain
        defaultProfessionalFiltering("languages.doesNotContain=" + UPDATED_LANGUAGES, "languages.doesNotContain=" + DEFAULT_LANGUAGES);
    }

    @Test
    @Transactional
    void getAllProfessionalsByBankAccountEncIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where bankAccountEnc equals to
        defaultProfessionalFiltering(
            "bankAccountEnc.equals=" + DEFAULT_BANK_ACCOUNT_ENC,
            "bankAccountEnc.equals=" + UPDATED_BANK_ACCOUNT_ENC
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByBankAccountEncIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where bankAccountEnc in
        defaultProfessionalFiltering(
            "bankAccountEnc.in=" + DEFAULT_BANK_ACCOUNT_ENC + "," + UPDATED_BANK_ACCOUNT_ENC,
            "bankAccountEnc.in=" + UPDATED_BANK_ACCOUNT_ENC
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByBankAccountEncIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where bankAccountEnc is not null
        defaultProfessionalFiltering("bankAccountEnc.specified=true", "bankAccountEnc.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByBankAccountEncContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where bankAccountEnc contains
        defaultProfessionalFiltering(
            "bankAccountEnc.contains=" + DEFAULT_BANK_ACCOUNT_ENC,
            "bankAccountEnc.contains=" + UPDATED_BANK_ACCOUNT_ENC
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByBankAccountEncNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where bankAccountEnc does not contain
        defaultProfessionalFiltering(
            "bankAccountEnc.doesNotContain=" + UPDATED_BANK_ACCOUNT_ENC,
            "bankAccountEnc.doesNotContain=" + DEFAULT_BANK_ACCOUNT_ENC
        );
    }

    @Test
    @Transactional
    void getAllProfessionalsByIfscCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where ifscCode equals to
        defaultProfessionalFiltering("ifscCode.equals=" + DEFAULT_IFSC_CODE, "ifscCode.equals=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByIfscCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where ifscCode in
        defaultProfessionalFiltering("ifscCode.in=" + DEFAULT_IFSC_CODE + "," + UPDATED_IFSC_CODE, "ifscCode.in=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByIfscCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where ifscCode is not null
        defaultProfessionalFiltering("ifscCode.specified=true", "ifscCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByIfscCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where ifscCode contains
        defaultProfessionalFiltering("ifscCode.contains=" + DEFAULT_IFSC_CODE, "ifscCode.contains=" + UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByIfscCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where ifscCode does not contain
        defaultProfessionalFiltering("ifscCode.doesNotContain=" + UPDATED_IFSC_CODE, "ifscCode.doesNotContain=" + DEFAULT_IFSC_CODE);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDeletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where deletedAt equals to
        defaultProfessionalFiltering("deletedAt.equals=" + DEFAULT_DELETED_AT, "deletedAt.equals=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDeletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where deletedAt in
        defaultProfessionalFiltering("deletedAt.in=" + DEFAULT_DELETED_AT + "," + UPDATED_DELETED_AT, "deletedAt.in=" + UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByDeletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where deletedAt is not null
        defaultProfessionalFiltering("deletedAt.specified=true", "deletedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where createdAt equals to
        defaultProfessionalFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where createdAt in
        defaultProfessionalFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where createdAt is not null
        defaultProfessionalFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where updatedAt equals to
        defaultProfessionalFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where updatedAt in
        defaultProfessionalFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProfessionalsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        // Get all the professionalList where updatedAt is not null
        defaultProfessionalFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = professional.getUser();
        professionalRepository.saveAndFlush(professional);
        Long userId = user.getId();
        // Get all the professionalList where user equals to userId
        defaultProfessionalShouldBeFound("userId.equals=" + userId);

        // Get all the professionalList where user equals to (userId + 1)
        defaultProfessionalShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionalsByHomeCityIsEqualToSomething() throws Exception {
        City homeCity;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            professionalRepository.saveAndFlush(professional);
            homeCity = CityResourceIT.createEntity();
        } else {
            homeCity = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(homeCity);
        em.flush();
        professional.setHomeCity(homeCity);
        professionalRepository.saveAndFlush(professional);
        Long homeCityId = homeCity.getId();
        // Get all the professionalList where homeCity equals to homeCityId
        defaultProfessionalShouldBeFound("homeCityId.equals=" + homeCityId);

        // Get all the professionalList where homeCity equals to (homeCityId + 1)
        defaultProfessionalShouldNotBeFound("homeCityId.equals=" + (homeCityId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionalsByTierIsEqualToSomething() throws Exception {
        ProfessionalTier tier;
        if (TestUtil.findAll(em, ProfessionalTier.class).isEmpty()) {
            professionalRepository.saveAndFlush(professional);
            tier = ProfessionalTierResourceIT.createEntity();
        } else {
            tier = TestUtil.findAll(em, ProfessionalTier.class).get(0);
        }
        em.persist(tier);
        em.flush();
        professional.setTier(tier);
        professionalRepository.saveAndFlush(professional);
        Long tierId = tier.getId();
        // Get all the professionalList where tier equals to tierId
        defaultProfessionalShouldBeFound("tierId.equals=" + tierId);

        // Get all the professionalList where tier equals to (tierId + 1)
        defaultProfessionalShouldNotBeFound("tierId.equals=" + (tierId + 1));
    }

    @Test
    @Transactional
    void getAllProfessionalsByZoneIsEqualToSomething() throws Exception {
        ServiceZone zone;
        if (TestUtil.findAll(em, ServiceZone.class).isEmpty()) {
            professionalRepository.saveAndFlush(professional);
            zone = ServiceZoneResourceIT.createEntity(em);
        } else {
            zone = TestUtil.findAll(em, ServiceZone.class).get(0);
        }
        em.persist(zone);
        em.flush();
        professional.addZone(zone);
        professionalRepository.saveAndFlush(professional);
        Long zoneId = zone.getId();
        // Get all the professionalList where zone equals to zoneId
        defaultProfessionalShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the professionalList where zone equals to (zoneId + 1)
        defaultProfessionalShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    private void defaultProfessionalFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProfessionalShouldBeFound(shouldBeFound);
        defaultProfessionalShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfessionalShouldBeFound(String filter) throws Exception {
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professional.getId().intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].onboardingStatus").value(hasItem(DEFAULT_ONBOARDING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].online").value(hasItem(DEFAULT_ONLINE)))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)))
            .andExpect(jsonPath("$.[*].jobsCompleted").value(hasItem(DEFAULT_JOBS_COMPLETED)))
            .andExpect(jsonPath("$.[*].cancellationRate").value(hasItem(DEFAULT_CANCELLATION_RATE)))
            .andExpect(jsonPath("$.[*].lastLat").value(hasItem(DEFAULT_LAST_LAT)))
            .andExpect(jsonPath("$.[*].lastLng").value(hasItem(DEFAULT_LAST_LNG)))
            .andExpect(jsonPath("$.[*].lastLocationAt").value(hasItem(DEFAULT_LAST_LOCATION_AT.toString())))
            .andExpect(jsonPath("$.[*].joinedAt").value(hasItem(DEFAULT_JOINED_AT.toString())))
            .andExpect(jsonPath("$.[*].cashInHand").value(hasItem(sameNumber(DEFAULT_CASH_IN_HAND))))
            .andExpect(jsonPath("$.[*].cashLimit").value(hasItem(sameNumber(DEFAULT_CASH_LIMIT))))
            .andExpect(jsonPath("$.[*].maxDailyJobs").value(hasItem(DEFAULT_MAX_DAILY_JOBS)))
            .andExpect(jsonPath("$.[*].languages").value(hasItem(DEFAULT_LANGUAGES)))
            .andExpect(jsonPath("$.[*].bankAccountEnc").value(hasItem(DEFAULT_BANK_ACCOUNT_ENC)))
            .andExpect(jsonPath("$.[*].ifscCode").value(hasItem(DEFAULT_IFSC_CODE)))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfessionalShouldNotBeFound(String filter) throws Exception {
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfessional() throws Exception {
        // Get the professional
        restProfessionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessional() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professional
        Professional updatedProfessional = professionalRepository.findById(professional.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessional are not directly saved in db
        em.detach(updatedProfessional);
        updatedProfessional
            .displayName(UPDATED_DISPLAY_NAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .gender(UPDATED_GENDER)
            .onboardingStatus(UPDATED_ONBOARDING_STATUS)
            .online(UPDATED_ONLINE)
            .avgRating(UPDATED_AVG_RATING)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .cancellationRate(UPDATED_CANCELLATION_RATE)
            .lastLat(UPDATED_LAST_LAT)
            .lastLng(UPDATED_LAST_LNG)
            .lastLocationAt(UPDATED_LAST_LOCATION_AT)
            .joinedAt(UPDATED_JOINED_AT)
            .cashInHand(UPDATED_CASH_IN_HAND)
            .cashLimit(UPDATED_CASH_LIMIT)
            .maxDailyJobs(UPDATED_MAX_DAILY_JOBS)
            .languages(UPDATED_LANGUAGES)
            .bankAccountEnc(UPDATED_BANK_ACCOUNT_ENC)
            .ifscCode(UPDATED_IFSC_CODE)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProfessionalDTO professionalDTO = professionalMapper.toDto(updatedProfessional);

        restProfessionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalToMatchAllProperties(updatedProfessional);
    }

    @Test
    @Transactional
    void putNonExistingProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalWithPatch() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professional using partial update
        Professional partialUpdatedProfessional = new Professional();
        partialUpdatedProfessional.setId(professional.getId());

        partialUpdatedProfessional
            .displayName(UPDATED_DISPLAY_NAME)
            .avgRating(UPDATED_AVG_RATING)
            .cancellationRate(UPDATED_CANCELLATION_RATE)
            .lastLat(UPDATED_LAST_LAT)
            .joinedAt(UPDATED_JOINED_AT)
            .cashInHand(UPDATED_CASH_IN_HAND)
            .bankAccountEnc(UPDATED_BANK_ACCOUNT_ENC)
            .ifscCode(UPDATED_IFSC_CODE);

        restProfessionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessional))
            )
            .andExpect(status().isOk());

        // Validate the Professional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessional, professional),
            getPersistedProfessional(professional)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalWithPatch() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professional using partial update
        Professional partialUpdatedProfessional = new Professional();
        partialUpdatedProfessional.setId(professional.getId());

        partialUpdatedProfessional
            .displayName(UPDATED_DISPLAY_NAME)
            .photoUrl(UPDATED_PHOTO_URL)
            .gender(UPDATED_GENDER)
            .onboardingStatus(UPDATED_ONBOARDING_STATUS)
            .online(UPDATED_ONLINE)
            .avgRating(UPDATED_AVG_RATING)
            .jobsCompleted(UPDATED_JOBS_COMPLETED)
            .cancellationRate(UPDATED_CANCELLATION_RATE)
            .lastLat(UPDATED_LAST_LAT)
            .lastLng(UPDATED_LAST_LNG)
            .lastLocationAt(UPDATED_LAST_LOCATION_AT)
            .joinedAt(UPDATED_JOINED_AT)
            .cashInHand(UPDATED_CASH_IN_HAND)
            .cashLimit(UPDATED_CASH_LIMIT)
            .maxDailyJobs(UPDATED_MAX_DAILY_JOBS)
            .languages(UPDATED_LANGUAGES)
            .bankAccountEnc(UPDATED_BANK_ACCOUNT_ENC)
            .ifscCode(UPDATED_IFSC_CODE)
            .deletedAt(UPDATED_DELETED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProfessionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessional))
            )
            .andExpect(status().isOk());

        // Validate the Professional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalUpdatableFieldsEquals(partialUpdatedProfessional, getPersistedProfessional(partialUpdatedProfessional));
    }

    @Test
    @Transactional
    void patchNonExistingProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professional.setId(longCount.incrementAndGet());

        // Create the Professional
        ProfessionalDTO professionalDTO = professionalMapper.toDto(professional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessional() throws Exception {
        // Initialize the database
        insertedProfessional = professionalRepository.saveAndFlush(professional);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professional
        restProfessionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, professional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalRepository.count();
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

    protected Professional getPersistedProfessional(Professional professional) {
        return professionalRepository.findById(professional.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalToMatchAllProperties(Professional expectedProfessional) {
        assertProfessionalAllPropertiesEquals(expectedProfessional, getPersistedProfessional(expectedProfessional));
    }

    protected void assertPersistedProfessionalToMatchUpdatableProperties(Professional expectedProfessional) {
        assertProfessionalAllUpdatablePropertiesEquals(expectedProfessional, getPersistedProfessional(expectedProfessional));
    }
}
