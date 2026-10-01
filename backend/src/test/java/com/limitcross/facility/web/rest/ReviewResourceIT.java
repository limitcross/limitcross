package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ReviewAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.Review;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.repository.ReviewRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ReviewService;
import com.limitcross.facility.service.dto.ReviewDTO;
import com.limitcross.facility.service.mapper.ReviewMapper;
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
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReviewResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_REVIEW_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepositoryMock;

    @Autowired
    private ReviewMapper reviewMapper;

    @Mock
    private ReviewService reviewServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    private Review insertedReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .rating(DEFAULT_RATING)
            .reviewText(DEFAULT_REVIEW_TEXT)
            .tags(DEFAULT_TAGS)
            .visible(DEFAULT_VISIBLE)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        review.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        review.setCustomer(user);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        review.setProfessional(professional);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        review.setService(facilityService);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity(EntityManager em) {
        Review updatedReview = new Review()
            .rating(UPDATED_RATING)
            .reviewText(UPDATED_REVIEW_TEXT)
            .tags(UPDATED_TAGS)
            .visible(UPDATED_VISIBLE)
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
        updatedReview.setBooking(booking);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedReview.setCustomer(user);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedReview.setProfessional(professional);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedReview.setService(facilityService);
        return updatedReview;
    }

    @BeforeEach
    void initTest() {
        review = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReview != null) {
            reviewRepository.delete(insertedReview);
            insertedReview = null;
        }
    }

    @Test
    @Transactional
    void createReview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        var returnedReviewDTO = om.readValue(
            restReviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReviewDTO.class
        );

        // Validate the Review in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReview = reviewMapper.toEntity(returnedReviewDTO);
        assertReviewUpdatableFieldsEquals(returnedReview, getPersistedReview(returnedReview));

        insertedReview = returnedReview;
    }

    @Test
    @Transactional
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        review.setId(1L);
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        review.setRating(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        review.setVisible(null);

        // Create the Review, which fails.
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReviews() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].reviewText").value(hasItem(DEFAULT_REVIEW_TEXT)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reviewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reviewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.reviewText").value(DEFAULT_REVIEW_TEXT))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getReviewsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        Long id = review.getId();

        defaultReviewFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReviewFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReviewFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating equals to
        defaultReviewFiltering("rating.equals=" + DEFAULT_RATING, "rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating in
        defaultReviewFiltering("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING, "rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is not null
        defaultReviewFiltering("rating.specified=true", "rating.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than or equal to
        defaultReviewFiltering("rating.greaterThanOrEqual=" + DEFAULT_RATING, "rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than or equal to
        defaultReviewFiltering("rating.lessThanOrEqual=" + DEFAULT_RATING, "rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than
        defaultReviewFiltering("rating.lessThan=" + (DEFAULT_RATING + 1), "rating.lessThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than
        defaultReviewFiltering("rating.greaterThan=" + SMALLER_RATING, "rating.greaterThan=" + DEFAULT_RATING);
    }

    @Test
    @Transactional
    void getAllReviewsByReviewTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewText equals to
        defaultReviewFiltering("reviewText.equals=" + DEFAULT_REVIEW_TEXT, "reviewText.equals=" + UPDATED_REVIEW_TEXT);
    }

    @Test
    @Transactional
    void getAllReviewsByReviewTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewText in
        defaultReviewFiltering("reviewText.in=" + DEFAULT_REVIEW_TEXT + "," + UPDATED_REVIEW_TEXT, "reviewText.in=" + UPDATED_REVIEW_TEXT);
    }

    @Test
    @Transactional
    void getAllReviewsByReviewTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewText is not null
        defaultReviewFiltering("reviewText.specified=true", "reviewText.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByReviewTextContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewText contains
        defaultReviewFiltering("reviewText.contains=" + DEFAULT_REVIEW_TEXT, "reviewText.contains=" + UPDATED_REVIEW_TEXT);
    }

    @Test
    @Transactional
    void getAllReviewsByReviewTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where reviewText does not contain
        defaultReviewFiltering("reviewText.doesNotContain=" + UPDATED_REVIEW_TEXT, "reviewText.doesNotContain=" + DEFAULT_REVIEW_TEXT);
    }

    @Test
    @Transactional
    void getAllReviewsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where tags equals to
        defaultReviewFiltering("tags.equals=" + DEFAULT_TAGS, "tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllReviewsByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where tags in
        defaultReviewFiltering("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS, "tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllReviewsByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where tags is not null
        defaultReviewFiltering("tags.specified=true", "tags.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByTagsContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where tags contains
        defaultReviewFiltering("tags.contains=" + DEFAULT_TAGS, "tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllReviewsByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where tags does not contain
        defaultReviewFiltering("tags.doesNotContain=" + UPDATED_TAGS, "tags.doesNotContain=" + DEFAULT_TAGS);
    }

    @Test
    @Transactional
    void getAllReviewsByVisibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where visible equals to
        defaultReviewFiltering("visible.equals=" + DEFAULT_VISIBLE, "visible.equals=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void getAllReviewsByVisibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where visible in
        defaultReviewFiltering("visible.in=" + DEFAULT_VISIBLE + "," + UPDATED_VISIBLE, "visible.in=" + UPDATED_VISIBLE);
    }

    @Test
    @Transactional
    void getAllReviewsByVisibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where visible is not null
        defaultReviewFiltering("visible.specified=true", "visible.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdAt equals to
        defaultReviewFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdAt in
        defaultReviewFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReviewsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        // Get all the reviewList where createdAt is not null
        defaultReviewFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReviewsByBookingIsEqualToSomething() throws Exception {
        // Get already existing entity
        Booking booking = review.getBooking();
        reviewRepository.saveAndFlush(review);
        Long bookingId = booking.getId();
        // Get all the reviewList where booking equals to bookingId
        defaultReviewShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the reviewList where booking equals to (bookingId + 1)
        defaultReviewShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByCustomerIsEqualToSomething() throws Exception {
        User customer;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            customer = UserResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(customer);
        em.flush();
        review.setCustomer(customer);
        reviewRepository.saveAndFlush(review);
        Long customerId = customer.getId();
        // Get all the reviewList where customer equals to customerId
        defaultReviewShouldBeFound("customerId.equals=" + customerId);

        // Get all the reviewList where customer equals to (customerId + 1)
        defaultReviewShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByProfessionalIsEqualToSomething() throws Exception {
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            professional = ProfessionalResourceIT.createEntity(em);
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        em.persist(professional);
        em.flush();
        review.setProfessional(professional);
        reviewRepository.saveAndFlush(review);
        Long professionalId = professional.getId();
        // Get all the reviewList where professional equals to professionalId
        defaultReviewShouldBeFound("professionalId.equals=" + professionalId);

        // Get all the reviewList where professional equals to (professionalId + 1)
        defaultReviewShouldNotBeFound("professionalId.equals=" + (professionalId + 1));
    }

    @Test
    @Transactional
    void getAllReviewsByServiceIsEqualToSomething() throws Exception {
        FacilityService service;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            reviewRepository.saveAndFlush(review);
            service = FacilityServiceResourceIT.createEntity(em);
        } else {
            service = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        em.persist(service);
        em.flush();
        review.setService(service);
        reviewRepository.saveAndFlush(review);
        Long serviceId = service.getId();
        // Get all the reviewList where service equals to serviceId
        defaultReviewShouldBeFound("serviceId.equals=" + serviceId);

        // Get all the reviewList where service equals to (serviceId + 1)
        defaultReviewShouldNotBeFound("serviceId.equals=" + (serviceId + 1));
    }

    private void defaultReviewFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReviewShouldBeFound(shouldBeFound);
        defaultReviewShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].reviewText").value(hasItem(DEFAULT_REVIEW_TEXT)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview
            .rating(UPDATED_RATING)
            .reviewText(UPDATED_REVIEW_TEXT)
            .tags(UPDATED_TAGS)
            .visible(UPDATED_VISIBLE)
            .createdAt(UPDATED_CREATED_AT);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReviewToMatchAllProperties(updatedReview);
    }

    @Test
    @Transactional
    void putNonExistingReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .rating(UPDATED_RATING)
            .reviewText(UPDATED_REVIEW_TEXT)
            .tags(UPDATED_TAGS)
            .visible(UPDATED_VISIBLE)
            .createdAt(UPDATED_CREATED_AT);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReview, review), getPersistedReview(review));
    }

    @Test
    @Transactional
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .rating(UPDATED_RATING)
            .reviewText(UPDATED_REVIEW_TEXT)
            .tags(UPDATED_TAGS)
            .visible(UPDATED_VISIBLE)
            .createdAt(UPDATED_CREATED_AT);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReviewUpdatableFieldsEquals(partialUpdatedReview, getPersistedReview(partialUpdatedReview));
    }

    @Test
    @Transactional
    void patchNonExistingReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        review.setId(longCount.incrementAndGet());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReview() throws Exception {
        // Initialize the database
        insertedReview = reviewRepository.saveAndFlush(review);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the review
        restReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, review.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reviewRepository.count();
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

    protected Review getPersistedReview(Review review) {
        return reviewRepository.findById(review.getId()).orElseThrow();
    }

    protected void assertPersistedReviewToMatchAllProperties(Review expectedReview) {
        assertReviewAllPropertiesEquals(expectedReview, getPersistedReview(expectedReview));
    }

    protected void assertPersistedReviewToMatchUpdatableProperties(Review expectedReview) {
        assertReviewAllUpdatablePropertiesEquals(expectedReview, getPersistedReview(expectedReview));
    }
}
