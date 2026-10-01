package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.SupportTicketAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.SupportTicket;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.TicketCategory;
import com.limitcross.facility.domain.enumeration.TicketPriority;
import com.limitcross.facility.domain.enumeration.TicketStatus;
import com.limitcross.facility.repository.SupportTicketRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.SupportTicketService;
import com.limitcross.facility.service.dto.SupportTicketDTO;
import com.limitcross.facility.service.mapper.SupportTicketMapper;
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
 * Integration tests for the {@link SupportTicketResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupportTicketResourceIT {

    private static final String DEFAULT_TICKET_NO = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_NO = "BBBBBBBBBB";

    private static final TicketCategory DEFAULT_CATEGORY = TicketCategory.PAYMENT;
    private static final TicketCategory UPDATED_CATEGORY = TicketCategory.QUALITY;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TicketStatus DEFAULT_STATUS = TicketStatus.OPEN;
    private static final TicketStatus UPDATED_STATUS = TicketStatus.IN_PROGRESS;

    private static final TicketPriority DEFAULT_PRIORITY = TicketPriority.LOW;
    private static final TicketPriority UPDATED_PRIORITY = TicketPriority.NORMAL;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESOLVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/support-tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SupportTicketRepository supportTicketRepositoryMock;

    @Autowired
    private SupportTicketMapper supportTicketMapper;

    @Mock
    private SupportTicketService supportTicketServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupportTicketMockMvc;

    private SupportTicket supportTicket;

    private SupportTicket insertedSupportTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicket createEntity(EntityManager em) {
        SupportTicket supportTicket = new SupportTicket()
            .ticketNo(DEFAULT_TICKET_NO)
            .category(DEFAULT_CATEGORY)
            .subject(DEFAULT_SUBJECT)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .createdAt(DEFAULT_CREATED_AT)
            .resolvedAt(DEFAULT_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        supportTicket.setUser(user);
        return supportTicket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicket createUpdatedEntity(EntityManager em) {
        SupportTicket updatedSupportTicket = new SupportTicket()
            .ticketNo(UPDATED_TICKET_NO)
            .category(UPDATED_CATEGORY)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSupportTicket.setUser(user);
        return updatedSupportTicket;
    }

    @BeforeEach
    void initTest() {
        supportTicket = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSupportTicket != null) {
            supportTicketRepository.delete(insertedSupportTicket);
            insertedSupportTicket = null;
        }
    }

    @Test
    @Transactional
    void createSupportTicket() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);
        var returnedSupportTicketDTO = om.readValue(
            restSupportTicketMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupportTicketDTO.class
        );

        // Validate the SupportTicket in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupportTicket = supportTicketMapper.toEntity(returnedSupportTicketDTO);
        assertSupportTicketUpdatableFieldsEquals(returnedSupportTicket, getPersistedSupportTicket(returnedSupportTicket));

        insertedSupportTicket = returnedSupportTicket;
    }

    @Test
    @Transactional
    void createSupportTicketWithExistingId() throws Exception {
        // Create the SupportTicket with an existing ID
        supportTicket.setId(1L);
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTicketNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supportTicket.setTicketNo(null);

        // Create the SupportTicket, which fails.
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supportTicket.setCategory(null);

        // Create the SupportTicket, which fails.
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supportTicket.setSubject(null);

        // Create the SupportTicket, which fails.
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supportTicket.setStatus(null);

        // Create the SupportTicket, which fails.
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supportTicket.setPriority(null);

        // Create the SupportTicket, which fails.
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        restSupportTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupportTickets() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicket.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketNo").value(hasItem(DEFAULT_TICKET_NO)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(DEFAULT_RESOLVED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupportTicketsWithEagerRelationshipsIsEnabled() throws Exception {
        when(supportTicketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupportTicketMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supportTicketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupportTicketsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supportTicketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupportTicketMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supportTicketRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupportTicket() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get the supportTicket
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, supportTicket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supportTicket.getId().intValue()))
            .andExpect(jsonPath("$.ticketNo").value(DEFAULT_TICKET_NO))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.resolvedAt").value(DEFAULT_RESOLVED_AT.toString()));
    }

    @Test
    @Transactional
    void getSupportTicketsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        Long id = supportTicket.getId();

        defaultSupportTicketFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupportTicketFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupportTicketFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByTicketNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where ticketNo equals to
        defaultSupportTicketFiltering("ticketNo.equals=" + DEFAULT_TICKET_NO, "ticketNo.equals=" + UPDATED_TICKET_NO);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByTicketNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where ticketNo in
        defaultSupportTicketFiltering("ticketNo.in=" + DEFAULT_TICKET_NO + "," + UPDATED_TICKET_NO, "ticketNo.in=" + UPDATED_TICKET_NO);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByTicketNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where ticketNo is not null
        defaultSupportTicketFiltering("ticketNo.specified=true", "ticketNo.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByTicketNoContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where ticketNo contains
        defaultSupportTicketFiltering("ticketNo.contains=" + DEFAULT_TICKET_NO, "ticketNo.contains=" + UPDATED_TICKET_NO);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByTicketNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where ticketNo does not contain
        defaultSupportTicketFiltering("ticketNo.doesNotContain=" + UPDATED_TICKET_NO, "ticketNo.doesNotContain=" + DEFAULT_TICKET_NO);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where category equals to
        defaultSupportTicketFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where category in
        defaultSupportTicketFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where category is not null
        defaultSupportTicketFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where subject equals to
        defaultSupportTicketFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where subject in
        defaultSupportTicketFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where subject is not null
        defaultSupportTicketFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where subject contains
        defaultSupportTicketFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where subject does not contain
        defaultSupportTicketFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where description equals to
        defaultSupportTicketFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where description in
        defaultSupportTicketFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSupportTicketsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where description is not null
        defaultSupportTicketFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where description contains
        defaultSupportTicketFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where description does not contain
        defaultSupportTicketFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSupportTicketsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where status equals to
        defaultSupportTicketFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where status in
        defaultSupportTicketFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where status is not null
        defaultSupportTicketFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where priority equals to
        defaultSupportTicketFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where priority in
        defaultSupportTicketFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where priority is not null
        defaultSupportTicketFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where createdAt equals to
        defaultSupportTicketFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where createdAt in
        defaultSupportTicketFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllSupportTicketsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where createdAt is not null
        defaultSupportTicketFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByResolvedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where resolvedAt equals to
        defaultSupportTicketFiltering("resolvedAt.equals=" + DEFAULT_RESOLVED_AT, "resolvedAt.equals=" + UPDATED_RESOLVED_AT);
    }

    @Test
    @Transactional
    void getAllSupportTicketsByResolvedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where resolvedAt in
        defaultSupportTicketFiltering(
            "resolvedAt.in=" + DEFAULT_RESOLVED_AT + "," + UPDATED_RESOLVED_AT,
            "resolvedAt.in=" + UPDATED_RESOLVED_AT
        );
    }

    @Test
    @Transactional
    void getAllSupportTicketsByResolvedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        // Get all the supportTicketList where resolvedAt is not null
        defaultSupportTicketFiltering("resolvedAt.specified=true", "resolvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSupportTicketsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            supportTicketRepository.saveAndFlush(supportTicket);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        supportTicket.setUser(user);
        supportTicketRepository.saveAndFlush(supportTicket);
        Long userId = user.getId();
        // Get all the supportTicketList where user equals to userId
        defaultSupportTicketShouldBeFound("userId.equals=" + userId);

        // Get all the supportTicketList where user equals to (userId + 1)
        defaultSupportTicketShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllSupportTicketsByAssignedToIsEqualToSomething() throws Exception {
        User assignedTo;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            supportTicketRepository.saveAndFlush(supportTicket);
            assignedTo = UserResourceIT.createEntity();
        } else {
            assignedTo = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedTo);
        em.flush();
        supportTicket.setAssignedTo(assignedTo);
        supportTicketRepository.saveAndFlush(supportTicket);
        Long assignedToId = assignedTo.getId();
        // Get all the supportTicketList where assignedTo equals to assignedToId
        defaultSupportTicketShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the supportTicketList where assignedTo equals to (assignedToId + 1)
        defaultSupportTicketShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    @Test
    @Transactional
    void getAllSupportTicketsByBookingIsEqualToSomething() throws Exception {
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            supportTicketRepository.saveAndFlush(supportTicket);
            booking = BookingResourceIT.createEntity(em);
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        em.persist(booking);
        em.flush();
        supportTicket.setBooking(booking);
        supportTicketRepository.saveAndFlush(supportTicket);
        Long bookingId = booking.getId();
        // Get all the supportTicketList where booking equals to bookingId
        defaultSupportTicketShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the supportTicketList where booking equals to (bookingId + 1)
        defaultSupportTicketShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    private void defaultSupportTicketFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupportTicketShouldBeFound(shouldBeFound);
        defaultSupportTicketShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupportTicketShouldBeFound(String filter) throws Exception {
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicket.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketNo").value(hasItem(DEFAULT_TICKET_NO)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(DEFAULT_RESOLVED_AT.toString())));

        // Check, that the count call also returns 1
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupportTicketShouldNotBeFound(String filter) throws Exception {
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupportTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupportTicket() throws Exception {
        // Get the supportTicket
        restSupportTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupportTicket() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicket
        SupportTicket updatedSupportTicket = supportTicketRepository.findById(supportTicket.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupportTicket are not directly saved in db
        em.detach(updatedSupportTicket);
        updatedSupportTicket
            .ticketNo(UPDATED_TICKET_NO)
            .category(UPDATED_CATEGORY)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(updatedSupportTicket);

        restSupportTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supportTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupportTicketToMatchAllProperties(updatedSupportTicket);
    }

    @Test
    @Transactional
    void putNonExistingSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supportTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupportTicketWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicket using partial update
        SupportTicket partialUpdatedSupportTicket = new SupportTicket();
        partialUpdatedSupportTicket.setId(supportTicket.getId());

        partialUpdatedSupportTicket
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);

        restSupportTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicket))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupportTicket, supportTicket),
            getPersistedSupportTicket(supportTicket)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupportTicketWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicket using partial update
        SupportTicket partialUpdatedSupportTicket = new SupportTicket();
        partialUpdatedSupportTicket.setId(supportTicket.getId());

        partialUpdatedSupportTicket
            .ticketNo(UPDATED_TICKET_NO)
            .category(UPDATED_CATEGORY)
            .subject(UPDATED_SUBJECT)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);

        restSupportTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicket))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicket in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketUpdatableFieldsEquals(partialUpdatedSupportTicket, getPersistedSupportTicket(partialUpdatedSupportTicket));
    }

    @Test
    @Transactional
    void patchNonExistingSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supportTicketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupportTicket() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicket.setId(longCount.incrementAndGet());

        // Create the SupportTicket
        SupportTicketDTO supportTicketDTO = supportTicketMapper.toDto(supportTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supportTicketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicket in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupportTicket() throws Exception {
        // Initialize the database
        insertedSupportTicket = supportTicketRepository.saveAndFlush(supportTicket);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supportTicket
        restSupportTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, supportTicket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supportTicketRepository.count();
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

    protected SupportTicket getPersistedSupportTicket(SupportTicket supportTicket) {
        return supportTicketRepository.findById(supportTicket.getId()).orElseThrow();
    }

    protected void assertPersistedSupportTicketToMatchAllProperties(SupportTicket expectedSupportTicket) {
        assertSupportTicketAllPropertiesEquals(expectedSupportTicket, getPersistedSupportTicket(expectedSupportTicket));
    }

    protected void assertPersistedSupportTicketToMatchUpdatableProperties(SupportTicket expectedSupportTicket) {
        assertSupportTicketAllUpdatablePropertiesEquals(expectedSupportTicket, getPersistedSupportTicket(expectedSupportTicket));
    }
}
