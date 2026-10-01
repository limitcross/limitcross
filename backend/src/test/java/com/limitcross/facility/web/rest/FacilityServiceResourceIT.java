package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.FacilityServiceAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.domain.enumeration.Gender;
import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.service.FacilityServiceService;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.mapper.FacilityServiceMapper;
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
 * Integration tests for the {@link FacilityServiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacilityServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_EMOJI = "AAAAAAAAAA";
    private static final String UPDATED_EMOJI = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HIGHLIGHTS = "AAAAAAAAAA";
    private static final String UPDATED_HIGHLIGHTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION_MINUTES = 1;
    private static final Integer UPDATED_DURATION_MINUTES = 2;
    private static final Integer SMALLER_DURATION_MINUTES = 1 - 1;

    private static final Integer DEFAULT_WARRANTY_DAYS = 0;
    private static final Integer UPDATED_WARRANTY_DAYS = 1;
    private static final Integer SMALLER_WARRANTY_DAYS = 0 - 1;

    private static final String DEFAULT_SAC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAC_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_GST_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_GST_PERCENT = new BigDecimal(1);
    private static final BigDecimal SMALLER_GST_PERCENT = new BigDecimal(0 - 1);

    private static final Gender DEFAULT_GENDER_SPECIFIC = Gender.MALE;
    private static final Gender UPDATED_GENDER_SPECIFIC = Gender.FEMALE;

    private static final Boolean DEFAULT_REQUIRES_VISIT_CHARGE = false;
    private static final Boolean UPDATED_REQUIRES_VISIT_CHARGE = true;

    private static final Boolean DEFAULT_POPULAR = false;
    private static final Boolean UPDATED_POPULAR = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Double DEFAULT_AVG_RATING = 0D;
    private static final Double UPDATED_AVG_RATING = 1D;
    private static final Double SMALLER_AVG_RATING = 0D - 1D;

    private static final Integer DEFAULT_REVIEWS_COUNT = 0;
    private static final Integer UPDATED_REVIEWS_COUNT = 1;
    private static final Integer SMALLER_REVIEWS_COUNT = 0 - 1;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/facility-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacilityServiceRepository facilityServiceRepository;

    @Mock
    private FacilityServiceRepository facilityServiceRepositoryMock;

    @Autowired
    private FacilityServiceMapper facilityServiceMapper;

    @Mock
    private FacilityServiceService facilityServiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityServiceMockMvc;

    private FacilityService facilityService;

    private FacilityService insertedFacilityService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityService createEntity(EntityManager em) {
        FacilityService facilityService = new FacilityService()
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .slug(DEFAULT_SLUG)
            .emoji(DEFAULT_EMOJI)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .highlights(DEFAULT_HIGHLIGHTS)
            .durationMinutes(DEFAULT_DURATION_MINUTES)
            .warrantyDays(DEFAULT_WARRANTY_DAYS)
            .sacCode(DEFAULT_SAC_CODE)
            .gstPercent(DEFAULT_GST_PERCENT)
            .genderSpecific(DEFAULT_GENDER_SPECIFIC)
            .requiresVisitCharge(DEFAULT_REQUIRES_VISIT_CHARGE)
            .popular(DEFAULT_POPULAR)
            .active(DEFAULT_ACTIVE)
            .avgRating(DEFAULT_AVG_RATING)
            .reviewsCount(DEFAULT_REVIEWS_COUNT)
            .sortOrder(DEFAULT_SORT_ORDER)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        facilityService.setCategory(serviceCategory);
        return facilityService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityService createUpdatedEntity(EntityManager em) {
        FacilityService updatedFacilityService = new FacilityService()
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .emoji(UPDATED_EMOJI)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .highlights(UPDATED_HIGHLIGHTS)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .warrantyDays(UPDATED_WARRANTY_DAYS)
            .sacCode(UPDATED_SAC_CODE)
            .gstPercent(UPDATED_GST_PERCENT)
            .genderSpecific(UPDATED_GENDER_SPECIFIC)
            .requiresVisitCharge(UPDATED_REQUIRES_VISIT_CHARGE)
            .popular(UPDATED_POPULAR)
            .active(UPDATED_ACTIVE)
            .avgRating(UPDATED_AVG_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .sortOrder(UPDATED_SORT_ORDER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        ServiceCategory serviceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            serviceCategory = ServiceCategoryResourceIT.createUpdatedEntity();
            em.persist(serviceCategory);
            em.flush();
        } else {
            serviceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        updatedFacilityService.setCategory(serviceCategory);
        return updatedFacilityService;
    }

    @BeforeEach
    void initTest() {
        facilityService = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacilityService != null) {
            facilityServiceRepository.delete(insertedFacilityService);
            insertedFacilityService = null;
        }
    }

    @Test
    @Transactional
    void createFacilityService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);
        var returnedFacilityServiceDTO = om.readValue(
            restFacilityServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacilityServiceDTO.class
        );

        // Validate the FacilityService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFacilityService = facilityServiceMapper.toEntity(returnedFacilityServiceDTO);
        assertFacilityServiceUpdatableFieldsEquals(returnedFacilityService, getPersistedFacilityService(returnedFacilityService));

        insertedFacilityService = returnedFacilityService;
    }

    @Test
    @Transactional
    void createFacilityServiceWithExistingId() throws Exception {
        // Create the FacilityService with an existing ID
        facilityService.setId(1L);
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setCode(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setTitle(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setSlug(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setDescription(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationMinutesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setDurationMinutes(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facilityService.setActive(null);

        // Create the FacilityService, which fails.
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        restFacilityServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilityServices() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityService.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].emoji").value(hasItem(DEFAULT_EMOJI)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].highlights").value(hasItem(DEFAULT_HIGHLIGHTS)))
            .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].warrantyDays").value(hasItem(DEFAULT_WARRANTY_DAYS)))
            .andExpect(jsonPath("$.[*].sacCode").value(hasItem(DEFAULT_SAC_CODE)))
            .andExpect(jsonPath("$.[*].gstPercent").value(hasItem(sameNumber(DEFAULT_GST_PERCENT))))
            .andExpect(jsonPath("$.[*].genderSpecific").value(hasItem(DEFAULT_GENDER_SPECIFIC.toString())))
            .andExpect(jsonPath("$.[*].requiresVisitCharge").value(hasItem(DEFAULT_REQUIRES_VISIT_CHARGE)))
            .andExpect(jsonPath("$.[*].popular").value(hasItem(DEFAULT_POPULAR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)))
            .andExpect(jsonPath("$.[*].reviewsCount").value(hasItem(DEFAULT_REVIEWS_COUNT)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityServicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facilityServiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facilityServiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacilityServicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facilityServiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacilityServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(facilityServiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacilityService() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get the facilityService
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityService.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.emoji").value(DEFAULT_EMOJI))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.highlights").value(DEFAULT_HIGHLIGHTS))
            .andExpect(jsonPath("$.durationMinutes").value(DEFAULT_DURATION_MINUTES))
            .andExpect(jsonPath("$.warrantyDays").value(DEFAULT_WARRANTY_DAYS))
            .andExpect(jsonPath("$.sacCode").value(DEFAULT_SAC_CODE))
            .andExpect(jsonPath("$.gstPercent").value(sameNumber(DEFAULT_GST_PERCENT)))
            .andExpect(jsonPath("$.genderSpecific").value(DEFAULT_GENDER_SPECIFIC.toString()))
            .andExpect(jsonPath("$.requiresVisitCharge").value(DEFAULT_REQUIRES_VISIT_CHARGE))
            .andExpect(jsonPath("$.popular").value(DEFAULT_POPULAR))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.avgRating").value(DEFAULT_AVG_RATING))
            .andExpect(jsonPath("$.reviewsCount").value(DEFAULT_REVIEWS_COUNT))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getFacilityServicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        Long id = facilityService.getId();

        defaultFacilityServiceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacilityServiceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacilityServiceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where code equals to
        defaultFacilityServiceFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where code in
        defaultFacilityServiceFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where code is not null
        defaultFacilityServiceFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where code contains
        defaultFacilityServiceFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where code does not contain
        defaultFacilityServiceFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where title equals to
        defaultFacilityServiceFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where title in
        defaultFacilityServiceFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where title is not null
        defaultFacilityServiceFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where title contains
        defaultFacilityServiceFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where title does not contain
        defaultFacilityServiceFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where slug equals to
        defaultFacilityServiceFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where slug in
        defaultFacilityServiceFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where slug is not null
        defaultFacilityServiceFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where slug contains
        defaultFacilityServiceFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where slug does not contain
        defaultFacilityServiceFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByEmojiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where emoji equals to
        defaultFacilityServiceFiltering("emoji.equals=" + DEFAULT_EMOJI, "emoji.equals=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByEmojiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where emoji in
        defaultFacilityServiceFiltering("emoji.in=" + DEFAULT_EMOJI + "," + UPDATED_EMOJI, "emoji.in=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByEmojiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where emoji is not null
        defaultFacilityServiceFiltering("emoji.specified=true", "emoji.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByEmojiContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where emoji contains
        defaultFacilityServiceFiltering("emoji.contains=" + DEFAULT_EMOJI, "emoji.contains=" + UPDATED_EMOJI);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByEmojiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where emoji does not contain
        defaultFacilityServiceFiltering("emoji.doesNotContain=" + UPDATED_EMOJI, "emoji.doesNotContain=" + DEFAULT_EMOJI);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where imageUrl equals to
        defaultFacilityServiceFiltering("imageUrl.equals=" + DEFAULT_IMAGE_URL, "imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where imageUrl in
        defaultFacilityServiceFiltering("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where imageUrl is not null
        defaultFacilityServiceFiltering("imageUrl.specified=true", "imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where imageUrl contains
        defaultFacilityServiceFiltering("imageUrl.contains=" + DEFAULT_IMAGE_URL, "imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where imageUrl does not contain
        defaultFacilityServiceFiltering("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL, "imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where description equals to
        defaultFacilityServiceFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where description in
        defaultFacilityServiceFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where description is not null
        defaultFacilityServiceFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where description contains
        defaultFacilityServiceFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where description does not contain
        defaultFacilityServiceFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes equals to
        defaultFacilityServiceFiltering(
            "durationMinutes.equals=" + DEFAULT_DURATION_MINUTES,
            "durationMinutes.equals=" + UPDATED_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes in
        defaultFacilityServiceFiltering(
            "durationMinutes.in=" + DEFAULT_DURATION_MINUTES + "," + UPDATED_DURATION_MINUTES,
            "durationMinutes.in=" + UPDATED_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes is not null
        defaultFacilityServiceFiltering("durationMinutes.specified=true", "durationMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes is greater than or equal to
        defaultFacilityServiceFiltering(
            "durationMinutes.greaterThanOrEqual=" + DEFAULT_DURATION_MINUTES,
            "durationMinutes.greaterThanOrEqual=" + UPDATED_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes is less than or equal to
        defaultFacilityServiceFiltering(
            "durationMinutes.lessThanOrEqual=" + DEFAULT_DURATION_MINUTES,
            "durationMinutes.lessThanOrEqual=" + SMALLER_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes is less than
        defaultFacilityServiceFiltering(
            "durationMinutes.lessThan=" + UPDATED_DURATION_MINUTES,
            "durationMinutes.lessThan=" + DEFAULT_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByDurationMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where durationMinutes is greater than
        defaultFacilityServiceFiltering(
            "durationMinutes.greaterThan=" + SMALLER_DURATION_MINUTES,
            "durationMinutes.greaterThan=" + DEFAULT_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays equals to
        defaultFacilityServiceFiltering("warrantyDays.equals=" + DEFAULT_WARRANTY_DAYS, "warrantyDays.equals=" + UPDATED_WARRANTY_DAYS);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays in
        defaultFacilityServiceFiltering(
            "warrantyDays.in=" + DEFAULT_WARRANTY_DAYS + "," + UPDATED_WARRANTY_DAYS,
            "warrantyDays.in=" + UPDATED_WARRANTY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays is not null
        defaultFacilityServiceFiltering("warrantyDays.specified=true", "warrantyDays.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays is greater than or equal to
        defaultFacilityServiceFiltering(
            "warrantyDays.greaterThanOrEqual=" + DEFAULT_WARRANTY_DAYS,
            "warrantyDays.greaterThanOrEqual=" + UPDATED_WARRANTY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays is less than or equal to
        defaultFacilityServiceFiltering(
            "warrantyDays.lessThanOrEqual=" + DEFAULT_WARRANTY_DAYS,
            "warrantyDays.lessThanOrEqual=" + SMALLER_WARRANTY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays is less than
        defaultFacilityServiceFiltering("warrantyDays.lessThan=" + UPDATED_WARRANTY_DAYS, "warrantyDays.lessThan=" + DEFAULT_WARRANTY_DAYS);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByWarrantyDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where warrantyDays is greater than
        defaultFacilityServiceFiltering(
            "warrantyDays.greaterThan=" + SMALLER_WARRANTY_DAYS,
            "warrantyDays.greaterThan=" + DEFAULT_WARRANTY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySacCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sacCode equals to
        defaultFacilityServiceFiltering("sacCode.equals=" + DEFAULT_SAC_CODE, "sacCode.equals=" + UPDATED_SAC_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySacCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sacCode in
        defaultFacilityServiceFiltering("sacCode.in=" + DEFAULT_SAC_CODE + "," + UPDATED_SAC_CODE, "sacCode.in=" + UPDATED_SAC_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySacCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sacCode is not null
        defaultFacilityServiceFiltering("sacCode.specified=true", "sacCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySacCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sacCode contains
        defaultFacilityServiceFiltering("sacCode.contains=" + DEFAULT_SAC_CODE, "sacCode.contains=" + UPDATED_SAC_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySacCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sacCode does not contain
        defaultFacilityServiceFiltering("sacCode.doesNotContain=" + UPDATED_SAC_CODE, "sacCode.doesNotContain=" + DEFAULT_SAC_CODE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent equals to
        defaultFacilityServiceFiltering("gstPercent.equals=" + DEFAULT_GST_PERCENT, "gstPercent.equals=" + UPDATED_GST_PERCENT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent in
        defaultFacilityServiceFiltering(
            "gstPercent.in=" + DEFAULT_GST_PERCENT + "," + UPDATED_GST_PERCENT,
            "gstPercent.in=" + UPDATED_GST_PERCENT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent is not null
        defaultFacilityServiceFiltering("gstPercent.specified=true", "gstPercent.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent is greater than or equal to
        defaultFacilityServiceFiltering(
            "gstPercent.greaterThanOrEqual=" + DEFAULT_GST_PERCENT,
            "gstPercent.greaterThanOrEqual=" + UPDATED_GST_PERCENT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent is less than or equal to
        defaultFacilityServiceFiltering(
            "gstPercent.lessThanOrEqual=" + DEFAULT_GST_PERCENT,
            "gstPercent.lessThanOrEqual=" + SMALLER_GST_PERCENT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent is less than
        defaultFacilityServiceFiltering("gstPercent.lessThan=" + UPDATED_GST_PERCENT, "gstPercent.lessThan=" + DEFAULT_GST_PERCENT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGstPercentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where gstPercent is greater than
        defaultFacilityServiceFiltering("gstPercent.greaterThan=" + SMALLER_GST_PERCENT, "gstPercent.greaterThan=" + DEFAULT_GST_PERCENT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGenderSpecificIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where genderSpecific equals to
        defaultFacilityServiceFiltering(
            "genderSpecific.equals=" + DEFAULT_GENDER_SPECIFIC,
            "genderSpecific.equals=" + UPDATED_GENDER_SPECIFIC
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGenderSpecificIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where genderSpecific in
        defaultFacilityServiceFiltering(
            "genderSpecific.in=" + DEFAULT_GENDER_SPECIFIC + "," + UPDATED_GENDER_SPECIFIC,
            "genderSpecific.in=" + UPDATED_GENDER_SPECIFIC
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByGenderSpecificIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where genderSpecific is not null
        defaultFacilityServiceFiltering("genderSpecific.specified=true", "genderSpecific.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByRequiresVisitChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where requiresVisitCharge equals to
        defaultFacilityServiceFiltering(
            "requiresVisitCharge.equals=" + DEFAULT_REQUIRES_VISIT_CHARGE,
            "requiresVisitCharge.equals=" + UPDATED_REQUIRES_VISIT_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByRequiresVisitChargeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where requiresVisitCharge in
        defaultFacilityServiceFiltering(
            "requiresVisitCharge.in=" + DEFAULT_REQUIRES_VISIT_CHARGE + "," + UPDATED_REQUIRES_VISIT_CHARGE,
            "requiresVisitCharge.in=" + UPDATED_REQUIRES_VISIT_CHARGE
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByRequiresVisitChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where requiresVisitCharge is not null
        defaultFacilityServiceFiltering("requiresVisitCharge.specified=true", "requiresVisitCharge.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByPopularIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where popular equals to
        defaultFacilityServiceFiltering("popular.equals=" + DEFAULT_POPULAR, "popular.equals=" + UPDATED_POPULAR);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByPopularIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where popular in
        defaultFacilityServiceFiltering("popular.in=" + DEFAULT_POPULAR + "," + UPDATED_POPULAR, "popular.in=" + UPDATED_POPULAR);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByPopularIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where popular is not null
        defaultFacilityServiceFiltering("popular.specified=true", "popular.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where active equals to
        defaultFacilityServiceFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where active in
        defaultFacilityServiceFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where active is not null
        defaultFacilityServiceFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating equals to
        defaultFacilityServiceFiltering("avgRating.equals=" + DEFAULT_AVG_RATING, "avgRating.equals=" + UPDATED_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating in
        defaultFacilityServiceFiltering(
            "avgRating.in=" + DEFAULT_AVG_RATING + "," + UPDATED_AVG_RATING,
            "avgRating.in=" + UPDATED_AVG_RATING
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating is not null
        defaultFacilityServiceFiltering("avgRating.specified=true", "avgRating.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating is greater than or equal to
        defaultFacilityServiceFiltering(
            "avgRating.greaterThanOrEqual=" + DEFAULT_AVG_RATING,
            "avgRating.greaterThanOrEqual=" + (DEFAULT_AVG_RATING + 1)
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating is less than or equal to
        defaultFacilityServiceFiltering(
            "avgRating.lessThanOrEqual=" + DEFAULT_AVG_RATING,
            "avgRating.lessThanOrEqual=" + SMALLER_AVG_RATING
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating is less than
        defaultFacilityServiceFiltering("avgRating.lessThan=" + (DEFAULT_AVG_RATING + 1), "avgRating.lessThan=" + DEFAULT_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByAvgRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where avgRating is greater than
        defaultFacilityServiceFiltering("avgRating.greaterThan=" + SMALLER_AVG_RATING, "avgRating.greaterThan=" + DEFAULT_AVG_RATING);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount equals to
        defaultFacilityServiceFiltering("reviewsCount.equals=" + DEFAULT_REVIEWS_COUNT, "reviewsCount.equals=" + UPDATED_REVIEWS_COUNT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount in
        defaultFacilityServiceFiltering(
            "reviewsCount.in=" + DEFAULT_REVIEWS_COUNT + "," + UPDATED_REVIEWS_COUNT,
            "reviewsCount.in=" + UPDATED_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount is not null
        defaultFacilityServiceFiltering("reviewsCount.specified=true", "reviewsCount.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount is greater than or equal to
        defaultFacilityServiceFiltering(
            "reviewsCount.greaterThanOrEqual=" + DEFAULT_REVIEWS_COUNT,
            "reviewsCount.greaterThanOrEqual=" + UPDATED_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount is less than or equal to
        defaultFacilityServiceFiltering(
            "reviewsCount.lessThanOrEqual=" + DEFAULT_REVIEWS_COUNT,
            "reviewsCount.lessThanOrEqual=" + SMALLER_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount is less than
        defaultFacilityServiceFiltering("reviewsCount.lessThan=" + UPDATED_REVIEWS_COUNT, "reviewsCount.lessThan=" + DEFAULT_REVIEWS_COUNT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByReviewsCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where reviewsCount is greater than
        defaultFacilityServiceFiltering(
            "reviewsCount.greaterThan=" + SMALLER_REVIEWS_COUNT,
            "reviewsCount.greaterThan=" + DEFAULT_REVIEWS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder equals to
        defaultFacilityServiceFiltering("sortOrder.equals=" + DEFAULT_SORT_ORDER, "sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder in
        defaultFacilityServiceFiltering(
            "sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER,
            "sortOrder.in=" + UPDATED_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder is not null
        defaultFacilityServiceFiltering("sortOrder.specified=true", "sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder is greater than or equal to
        defaultFacilityServiceFiltering(
            "sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER,
            "sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder is less than or equal to
        defaultFacilityServiceFiltering(
            "sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER,
            "sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder is less than
        defaultFacilityServiceFiltering("sortOrder.lessThan=" + UPDATED_SORT_ORDER, "sortOrder.lessThan=" + DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllFacilityServicesBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where sortOrder is greater than
        defaultFacilityServiceFiltering("sortOrder.greaterThan=" + SMALLER_SORT_ORDER, "sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where createdAt equals to
        defaultFacilityServiceFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where createdAt in
        defaultFacilityServiceFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where createdAt is not null
        defaultFacilityServiceFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where updatedAt equals to
        defaultFacilityServiceFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllFacilityServicesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where updatedAt in
        defaultFacilityServiceFiltering(
            "updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT,
            "updatedAt.in=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllFacilityServicesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        // Get all the facilityServiceList where updatedAt is not null
        defaultFacilityServiceFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFacilityServicesByCategoryIsEqualToSomething() throws Exception {
        ServiceCategory category;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            facilityServiceRepository.saveAndFlush(facilityService);
            category = ServiceCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        facilityService.setCategory(category);
        facilityServiceRepository.saveAndFlush(facilityService);
        Long categoryId = category.getId();
        // Get all the facilityServiceList where category equals to categoryId
        defaultFacilityServiceShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the facilityServiceList where category equals to (categoryId + 1)
        defaultFacilityServiceShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultFacilityServiceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacilityServiceShouldBeFound(shouldBeFound);
        defaultFacilityServiceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacilityServiceShouldBeFound(String filter) throws Exception {
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityService.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].emoji").value(hasItem(DEFAULT_EMOJI)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].highlights").value(hasItem(DEFAULT_HIGHLIGHTS)))
            .andExpect(jsonPath("$.[*].durationMinutes").value(hasItem(DEFAULT_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].warrantyDays").value(hasItem(DEFAULT_WARRANTY_DAYS)))
            .andExpect(jsonPath("$.[*].sacCode").value(hasItem(DEFAULT_SAC_CODE)))
            .andExpect(jsonPath("$.[*].gstPercent").value(hasItem(sameNumber(DEFAULT_GST_PERCENT))))
            .andExpect(jsonPath("$.[*].genderSpecific").value(hasItem(DEFAULT_GENDER_SPECIFIC.toString())))
            .andExpect(jsonPath("$.[*].requiresVisitCharge").value(hasItem(DEFAULT_REQUIRES_VISIT_CHARGE)))
            .andExpect(jsonPath("$.[*].popular").value(hasItem(DEFAULT_POPULAR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING)))
            .andExpect(jsonPath("$.[*].reviewsCount").value(hasItem(DEFAULT_REVIEWS_COUNT)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacilityServiceShouldNotBeFound(String filter) throws Exception {
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacilityServiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacilityService() throws Exception {
        // Get the facilityService
        restFacilityServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityService() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityService
        FacilityService updatedFacilityService = facilityServiceRepository.findById(facilityService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacilityService are not directly saved in db
        em.detach(updatedFacilityService);
        updatedFacilityService
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .emoji(UPDATED_EMOJI)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .highlights(UPDATED_HIGHLIGHTS)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .warrantyDays(UPDATED_WARRANTY_DAYS)
            .sacCode(UPDATED_SAC_CODE)
            .gstPercent(UPDATED_GST_PERCENT)
            .genderSpecific(UPDATED_GENDER_SPECIFIC)
            .requiresVisitCharge(UPDATED_REQUIRES_VISIT_CHARGE)
            .popular(UPDATED_POPULAR)
            .active(UPDATED_ACTIVE)
            .avgRating(UPDATED_AVG_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .sortOrder(UPDATED_SORT_ORDER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(updatedFacilityService);

        restFacilityServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacilityServiceToMatchAllProperties(updatedFacilityService);
    }

    @Test
    @Transactional
    void putNonExistingFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facilityServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityServiceWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityService using partial update
        FacilityService partialUpdatedFacilityService = new FacilityService();
        partialUpdatedFacilityService.setId(facilityService.getId());

        partialUpdatedFacilityService
            .title(UPDATED_TITLE)
            .highlights(UPDATED_HIGHLIGHTS)
            .warrantyDays(UPDATED_WARRANTY_DAYS)
            .sacCode(UPDATED_SAC_CODE)
            .genderSpecific(UPDATED_GENDER_SPECIFIC)
            .popular(UPDATED_POPULAR)
            .active(UPDATED_ACTIVE)
            .avgRating(UPDATED_AVG_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFacilityServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityService))
            )
            .andExpect(status().isOk());

        // Validate the FacilityService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFacilityService, facilityService),
            getPersistedFacilityService(facilityService)
        );
    }

    @Test
    @Transactional
    void fullUpdateFacilityServiceWithPatch() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facilityService using partial update
        FacilityService partialUpdatedFacilityService = new FacilityService();
        partialUpdatedFacilityService.setId(facilityService.getId());

        partialUpdatedFacilityService
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .emoji(UPDATED_EMOJI)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .highlights(UPDATED_HIGHLIGHTS)
            .durationMinutes(UPDATED_DURATION_MINUTES)
            .warrantyDays(UPDATED_WARRANTY_DAYS)
            .sacCode(UPDATED_SAC_CODE)
            .gstPercent(UPDATED_GST_PERCENT)
            .genderSpecific(UPDATED_GENDER_SPECIFIC)
            .requiresVisitCharge(UPDATED_REQUIRES_VISIT_CHARGE)
            .popular(UPDATED_POPULAR)
            .active(UPDATED_ACTIVE)
            .avgRating(UPDATED_AVG_RATING)
            .reviewsCount(UPDATED_REVIEWS_COUNT)
            .sortOrder(UPDATED_SORT_ORDER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFacilityServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacilityService))
            )
            .andExpect(status().isOk());

        // Validate the FacilityService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacilityServiceUpdatableFieldsEquals(
            partialUpdatedFacilityService,
            getPersistedFacilityService(partialUpdatedFacilityService)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facilityServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facilityService.setId(longCount.incrementAndGet());

        // Create the FacilityService
        FacilityServiceDTO facilityServiceDTO = facilityServiceMapper.toDto(facilityService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facilityServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityService() throws Exception {
        // Initialize the database
        insertedFacilityService = facilityServiceRepository.saveAndFlush(facilityService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facilityService
        restFacilityServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facilityServiceRepository.count();
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

    protected FacilityService getPersistedFacilityService(FacilityService facilityService) {
        return facilityServiceRepository.findById(facilityService.getId()).orElseThrow();
    }

    protected void assertPersistedFacilityServiceToMatchAllProperties(FacilityService expectedFacilityService) {
        assertFacilityServiceAllPropertiesEquals(expectedFacilityService, getPersistedFacilityService(expectedFacilityService));
    }

    protected void assertPersistedFacilityServiceToMatchUpdatableProperties(FacilityService expectedFacilityService) {
        assertFacilityServiceAllUpdatablePropertiesEquals(expectedFacilityService, getPersistedFacilityService(expectedFacilityService));
    }
}
