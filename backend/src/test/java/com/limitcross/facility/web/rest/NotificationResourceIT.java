package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.NotificationAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Notification;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.NotificationChannel;
import com.limitcross.facility.domain.enumeration.NotificationStatus;
import com.limitcross.facility.repository.NotificationRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.NotificationService;
import com.limitcross.facility.service.dto.NotificationDTO;
import com.limitcross.facility.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NotificationResourceIT {

    private static final NotificationChannel DEFAULT_CHANNEL = NotificationChannel.PUSH;
    private static final NotificationChannel UPDATED_CHANNEL = NotificationChannel.SMS;

    private static final String DEFAULT_NOTIFICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final NotificationStatus DEFAULT_STATUS = NotificationStatus.PENDING;
    private static final NotificationStatus UPDATED_STATUS = NotificationStatus.SENT;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_READ_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepositoryMock;

    @Autowired
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationService notificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    private Notification insertedNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .channel(DEFAULT_CHANNEL)
            .notificationType(DEFAULT_NOTIFICATION_TYPE)
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .data(DEFAULT_DATA)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .sentAt(DEFAULT_SENT_AT)
            .readAt(DEFAULT_READ_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        notification.setUser(user);
        return notification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification updatedNotification = new Notification()
            .channel(UPDATED_CHANNEL)
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .data(UPDATED_DATA)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .sentAt(UPDATED_SENT_AT)
            .readAt(UPDATED_READ_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedNotification.setUser(user);
        return updatedNotification;
    }

    @BeforeEach
    void initTest() {
        notification = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedNotification != null) {
            notificationRepository.delete(insertedNotification);
            insertedNotification = null;
        }
    }

    @Test
    @Transactional
    void createNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        var returnedNotificationDTO = om.readValue(
            restNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationDTO.class
        );

        // Validate the Notification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotification = notificationMapper.toEntity(returnedNotificationDTO);
        assertNotificationUpdatableFieldsEquals(returnedNotification, getPersistedNotification(returnedNotification));

        insertedNotification = returnedNotification;
    }

    @Test
    @Transactional
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setChannel(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setNotificationType(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBodyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setBody(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setStatus(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].readAt").value(hasItem(DEFAULT_READ_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(notificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(notificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(notificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(notificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.notificationType").value(DEFAULT_NOTIFICATION_TYPE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.sentAt").value(DEFAULT_SENT_AT.toString()))
            .andExpect(jsonPath("$.readAt").value(DEFAULT_READ_AT.toString()));
    }

    @Test
    @Transactional
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel equals to
        defaultNotificationFiltering("channel.equals=" + DEFAULT_CHANNEL, "channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel in
        defaultNotificationFiltering("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL, "channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel is not null
        defaultNotificationFiltering("channel.specified=true", "channel.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationType equals to
        defaultNotificationFiltering(
            "notificationType.equals=" + DEFAULT_NOTIFICATION_TYPE,
            "notificationType.equals=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationType in
        defaultNotificationFiltering(
            "notificationType.in=" + DEFAULT_NOTIFICATION_TYPE + "," + UPDATED_NOTIFICATION_TYPE,
            "notificationType.in=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationType is not null
        defaultNotificationFiltering("notificationType.specified=true", "notificationType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationType contains
        defaultNotificationFiltering(
            "notificationType.contains=" + DEFAULT_NOTIFICATION_TYPE,
            "notificationType.contains=" + UPDATED_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByNotificationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notificationType does not contain
        defaultNotificationFiltering(
            "notificationType.doesNotContain=" + UPDATED_NOTIFICATION_TYPE,
            "notificationType.doesNotContain=" + DEFAULT_NOTIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title equals to
        defaultNotificationFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title in
        defaultNotificationFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title is not null
        defaultNotificationFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title contains
        defaultNotificationFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title does not contain
        defaultNotificationFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where body equals to
        defaultNotificationFiltering("body.equals=" + DEFAULT_BODY, "body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllNotificationsByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where body in
        defaultNotificationFiltering("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY, "body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllNotificationsByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where body is not null
        defaultNotificationFiltering("body.specified=true", "body.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByBodyContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where body contains
        defaultNotificationFiltering("body.contains=" + DEFAULT_BODY, "body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllNotificationsByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where body does not contain
        defaultNotificationFiltering("body.doesNotContain=" + UPDATED_BODY, "body.doesNotContain=" + DEFAULT_BODY);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status equals to
        defaultNotificationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status in
        defaultNotificationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllNotificationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status is not null
        defaultNotificationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdAt equals to
        defaultNotificationFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdAt in
        defaultNotificationFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdAt is not null
        defaultNotificationFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsBySentAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentAt equals to
        defaultNotificationFiltering("sentAt.equals=" + DEFAULT_SENT_AT, "sentAt.equals=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySentAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentAt in
        defaultNotificationFiltering("sentAt.in=" + DEFAULT_SENT_AT + "," + UPDATED_SENT_AT, "sentAt.in=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsBySentAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentAt is not null
        defaultNotificationFiltering("sentAt.specified=true", "sentAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByReadAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readAt equals to
        defaultNotificationFiltering("readAt.equals=" + DEFAULT_READ_AT, "readAt.equals=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByReadAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readAt in
        defaultNotificationFiltering("readAt.in=" + DEFAULT_READ_AT + "," + UPDATED_READ_AT, "readAt.in=" + UPDATED_READ_AT);
    }

    @Test
    @Transactional
    void getAllNotificationsByReadAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readAt is not null
        defaultNotificationFiltering("readAt.specified=true", "readAt.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            notificationRepository.saveAndFlush(notification);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        notification.setUser(user);
        notificationRepository.saveAndFlush(notification);
        Long userId = user.getId();
        // Get all the notificationList where user equals to userId
        defaultNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the notificationList where user equals to (userId + 1)
        defaultNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationShouldBeFound(shouldBeFound);
        defaultNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].notificationType").value(hasItem(DEFAULT_NOTIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].readAt").value(hasItem(DEFAULT_READ_AT.toString())));

        // Check, that the count call also returns 1
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .channel(UPDATED_CHANNEL)
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .data(UPDATED_DATA)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .sentAt(UPDATED_SENT_AT)
            .readAt(UPDATED_READ_AT);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationToMatchAllProperties(updatedNotification);
    }

    @Test
    @Transactional
    void putNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification.channel(UPDATED_CHANNEL).data(UPDATED_DATA).createdAt(UPDATED_CREATED_AT).readAt(UPDATED_READ_AT);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotification, notification),
            getPersistedNotification(notification)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .channel(UPDATED_CHANNEL)
            .notificationType(UPDATED_NOTIFICATION_TYPE)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .data(UPDATED_DATA)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .sentAt(UPDATED_SENT_AT)
            .readAt(UPDATED_READ_AT);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationUpdatableFieldsEquals(partialUpdatedNotification, getPersistedNotification(partialUpdatedNotification));
    }

    @Test
    @Transactional
    void patchNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notification
        restNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationRepository.count();
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

    protected Notification getPersistedNotification(Notification notification) {
        return notificationRepository.findById(notification.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationToMatchAllProperties(Notification expectedNotification) {
        assertNotificationAllPropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
    }

    protected void assertPersistedNotificationToMatchUpdatableProperties(Notification expectedNotification) {
        assertNotificationAllUpdatablePropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
    }
}
