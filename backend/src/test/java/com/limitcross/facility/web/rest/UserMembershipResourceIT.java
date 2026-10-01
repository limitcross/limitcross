package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.UserMembershipAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.MembershipPlan;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.UserMembership;
import com.limitcross.facility.domain.enumeration.MembershipStatus;
import com.limitcross.facility.repository.UserMembershipRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.UserMembershipService;
import com.limitcross.facility.service.dto.UserMembershipDTO;
import com.limitcross.facility.service.mapper.UserMembershipMapper;
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
 * Integration tests for the {@link UserMembershipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserMembershipResourceIT {

    private static final Instant DEFAULT_STARTS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final MembershipStatus DEFAULT_STATUS = MembershipStatus.ACTIVE;
    private static final MembershipStatus UPDATED_STATUS = MembershipStatus.EXPIRED;

    private static final String ENTITY_API_URL = "/api/user-memberships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserMembershipRepository userMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserMembershipRepository userMembershipRepositoryMock;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Mock
    private UserMembershipService userMembershipServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMembershipMockMvc;

    private UserMembership userMembership;

    private UserMembership insertedUserMembership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMembership createEntity(EntityManager em) {
        UserMembership userMembership = new UserMembership().startsAt(DEFAULT_STARTS_AT).endsAt(DEFAULT_ENDS_AT).status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userMembership.setUser(user);
        // Add required entity
        MembershipPlan membershipPlan;
        if (TestUtil.findAll(em, MembershipPlan.class).isEmpty()) {
            membershipPlan = MembershipPlanResourceIT.createEntity();
            em.persist(membershipPlan);
            em.flush();
        } else {
            membershipPlan = TestUtil.findAll(em, MembershipPlan.class).get(0);
        }
        userMembership.setPlan(membershipPlan);
        return userMembership;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMembership createUpdatedEntity(EntityManager em) {
        UserMembership updatedUserMembership = new UserMembership()
            .startsAt(UPDATED_STARTS_AT)
            .endsAt(UPDATED_ENDS_AT)
            .status(UPDATED_STATUS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserMembership.setUser(user);
        // Add required entity
        MembershipPlan membershipPlan;
        if (TestUtil.findAll(em, MembershipPlan.class).isEmpty()) {
            membershipPlan = MembershipPlanResourceIT.createUpdatedEntity();
            em.persist(membershipPlan);
            em.flush();
        } else {
            membershipPlan = TestUtil.findAll(em, MembershipPlan.class).get(0);
        }
        updatedUserMembership.setPlan(membershipPlan);
        return updatedUserMembership;
    }

    @BeforeEach
    void initTest() {
        userMembership = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserMembership != null) {
            userMembershipRepository.delete(insertedUserMembership);
            insertedUserMembership = null;
        }
    }

    @Test
    @Transactional
    void createUserMembership() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);
        var returnedUserMembershipDTO = om.readValue(
            restUserMembershipMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserMembershipDTO.class
        );

        // Validate the UserMembership in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserMembership = userMembershipMapper.toEntity(returnedUserMembershipDTO);
        assertUserMembershipUpdatableFieldsEquals(returnedUserMembership, getPersistedUserMembership(returnedUserMembership));

        insertedUserMembership = returnedUserMembership;
    }

    @Test
    @Transactional
    void createUserMembershipWithExistingId() throws Exception {
        // Create the UserMembership with an existing ID
        userMembership.setId(1L);
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartsAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userMembership.setStartsAt(null);

        // Create the UserMembership, which fails.
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        restUserMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndsAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userMembership.setEndsAt(null);

        // Create the UserMembership, which fails.
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        restUserMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userMembership.setStatus(null);

        // Create the UserMembership, which fails.
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        restUserMembershipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserMemberships() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        // Get all the userMembershipList
        restUserMembershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMembership.getId().intValue())))
            .andExpect(jsonPath("$.[*].startsAt").value(hasItem(DEFAULT_STARTS_AT.toString())))
            .andExpect(jsonPath("$.[*].endsAt").value(hasItem(DEFAULT_ENDS_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMembershipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userMembershipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMembershipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userMembershipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMembershipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userMembershipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMembershipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userMembershipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserMembership() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        // Get the userMembership
        restUserMembershipMockMvc
            .perform(get(ENTITY_API_URL_ID, userMembership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMembership.getId().intValue()))
            .andExpect(jsonPath("$.startsAt").value(DEFAULT_STARTS_AT.toString()))
            .andExpect(jsonPath("$.endsAt").value(DEFAULT_ENDS_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserMembership() throws Exception {
        // Get the userMembership
        restUserMembershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserMembership() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userMembership
        UserMembership updatedUserMembership = userMembershipRepository.findById(userMembership.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserMembership are not directly saved in db
        em.detach(updatedUserMembership);
        updatedUserMembership.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT).status(UPDATED_STATUS);
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(updatedUserMembership);

        restUserMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMembershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userMembershipDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserMembershipToMatchAllProperties(updatedUserMembership);
    }

    @Test
    @Transactional
    void putNonExistingUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMembershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userMembershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userMembershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserMembershipWithPatch() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userMembership using partial update
        UserMembership partialUpdatedUserMembership = new UserMembership();
        partialUpdatedUserMembership.setId(userMembership.getId());

        partialUpdatedUserMembership.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT);

        restUserMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserMembership))
            )
            .andExpect(status().isOk());

        // Validate the UserMembership in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserMembershipUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserMembership, userMembership),
            getPersistedUserMembership(userMembership)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserMembershipWithPatch() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userMembership using partial update
        UserMembership partialUpdatedUserMembership = new UserMembership();
        partialUpdatedUserMembership.setId(userMembership.getId());

        partialUpdatedUserMembership.startsAt(UPDATED_STARTS_AT).endsAt(UPDATED_ENDS_AT).status(UPDATED_STATUS);

        restUserMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMembership.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserMembership))
            )
            .andExpect(status().isOk());

        // Validate the UserMembership in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserMembershipUpdatableFieldsEquals(partialUpdatedUserMembership, getPersistedUserMembership(partialUpdatedUserMembership));
    }

    @Test
    @Transactional
    void patchNonExistingUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMembershipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userMembershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userMembershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMembership() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMembership.setId(longCount.incrementAndGet());

        // Create the UserMembership
        UserMembershipDTO userMembershipDTO = userMembershipMapper.toDto(userMembership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMembershipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userMembershipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMembership in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserMembership() throws Exception {
        // Initialize the database
        insertedUserMembership = userMembershipRepository.saveAndFlush(userMembership);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userMembership
        restUserMembershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMembership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userMembershipRepository.count();
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

    protected UserMembership getPersistedUserMembership(UserMembership userMembership) {
        return userMembershipRepository.findById(userMembership.getId()).orElseThrow();
    }

    protected void assertPersistedUserMembershipToMatchAllProperties(UserMembership expectedUserMembership) {
        assertUserMembershipAllPropertiesEquals(expectedUserMembership, getPersistedUserMembership(expectedUserMembership));
    }

    protected void assertPersistedUserMembershipToMatchUpdatableProperties(UserMembership expectedUserMembership) {
        assertUserMembershipAllUpdatablePropertiesEquals(expectedUserMembership, getPersistedUserMembership(expectedUserMembership));
    }
}
