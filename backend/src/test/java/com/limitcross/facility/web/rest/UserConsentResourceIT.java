package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.UserConsentAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.UserConsent;
import com.limitcross.facility.domain.enumeration.ConsentPurpose;
import com.limitcross.facility.repository.UserConsentRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.UserConsentService;
import com.limitcross.facility.service.dto.UserConsentDTO;
import com.limitcross.facility.service.mapper.UserConsentMapper;
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
 * Integration tests for the {@link UserConsentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserConsentResourceIT {

    private static final ConsentPurpose DEFAULT_PURPOSE = ConsentPurpose.TERMS;
    private static final ConsentPurpose UPDATED_PURPOSE = ConsentPurpose.PRIVACY;

    private static final String DEFAULT_POLICY_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_VERSION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GRANTED = false;
    private static final Boolean UPDATED_GRANTED = true;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-consents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserConsentRepository userConsentRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserConsentRepository userConsentRepositoryMock;

    @Autowired
    private UserConsentMapper userConsentMapper;

    @Mock
    private UserConsentService userConsentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserConsentMockMvc;

    private UserConsent userConsent;

    private UserConsent insertedUserConsent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConsent createEntity(EntityManager em) {
        UserConsent userConsent = new UserConsent()
            .purpose(DEFAULT_PURPOSE)
            .policyVersion(DEFAULT_POLICY_VERSION)
            .granted(DEFAULT_GRANTED)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userConsent.setUser(user);
        return userConsent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConsent createUpdatedEntity(EntityManager em) {
        UserConsent updatedUserConsent = new UserConsent()
            .purpose(UPDATED_PURPOSE)
            .policyVersion(UPDATED_POLICY_VERSION)
            .granted(UPDATED_GRANTED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserConsent.setUser(user);
        return updatedUserConsent;
    }

    @BeforeEach
    void initTest() {
        userConsent = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserConsent != null) {
            userConsentRepository.delete(insertedUserConsent);
            insertedUserConsent = null;
        }
    }

    @Test
    @Transactional
    void createUserConsent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);
        var returnedUserConsentDTO = om.readValue(
            restUserConsentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserConsentDTO.class
        );

        // Validate the UserConsent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserConsent = userConsentMapper.toEntity(returnedUserConsentDTO);
        assertUserConsentUpdatableFieldsEquals(returnedUserConsent, getPersistedUserConsent(returnedUserConsent));

        insertedUserConsent = returnedUserConsent;
    }

    @Test
    @Transactional
    void createUserConsentWithExistingId() throws Exception {
        // Create the UserConsent with an existing ID
        userConsent.setId(1L);
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPurposeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userConsent.setPurpose(null);

        // Create the UserConsent, which fails.
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        restUserConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPolicyVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userConsent.setPolicyVersion(null);

        // Create the UserConsent, which fails.
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        restUserConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGrantedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userConsent.setGranted(null);

        // Create the UserConsent, which fails.
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        restUserConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserConsents() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        // Get all the userConsentList
        restUserConsentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConsent.getId().intValue())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())))
            .andExpect(jsonPath("$.[*].policyVersion").value(hasItem(DEFAULT_POLICY_VERSION)))
            .andExpect(jsonPath("$.[*].granted").value(hasItem(DEFAULT_GRANTED)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConsentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userConsentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConsentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userConsentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConsentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userConsentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConsentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userConsentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserConsent() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        // Get the userConsent
        restUserConsentMockMvc
            .perform(get(ENTITY_API_URL_ID, userConsent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userConsent.getId().intValue()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE.toString()))
            .andExpect(jsonPath("$.policyVersion").value(DEFAULT_POLICY_VERSION))
            .andExpect(jsonPath("$.granted").value(DEFAULT_GRANTED))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserConsent() throws Exception {
        // Get the userConsent
        restUserConsentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserConsent() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConsent
        UserConsent updatedUserConsent = userConsentRepository.findById(userConsent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserConsent are not directly saved in db
        em.detach(updatedUserConsent);
        updatedUserConsent
            .purpose(UPDATED_PURPOSE)
            .policyVersion(UPDATED_POLICY_VERSION)
            .granted(UPDATED_GRANTED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .createdAt(UPDATED_CREATED_AT);
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(updatedUserConsent);

        restUserConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConsentDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserConsentToMatchAllProperties(updatedUserConsent);
    }

    @Test
    @Transactional
    void putNonExistingUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserConsentWithPatch() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConsent using partial update
        UserConsent partialUpdatedUserConsent = new UserConsent();
        partialUpdatedUserConsent.setId(userConsent.getId());

        partialUpdatedUserConsent.purpose(UPDATED_PURPOSE).policyVersion(UPDATED_POLICY_VERSION);

        restUserConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConsent))
            )
            .andExpect(status().isOk());

        // Validate the UserConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConsentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserConsent, userConsent),
            getPersistedUserConsent(userConsent)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserConsentWithPatch() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConsent using partial update
        UserConsent partialUpdatedUserConsent = new UserConsent();
        partialUpdatedUserConsent.setId(userConsent.getId());

        partialUpdatedUserConsent
            .purpose(UPDATED_PURPOSE)
            .policyVersion(UPDATED_POLICY_VERSION)
            .granted(UPDATED_GRANTED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .createdAt(UPDATED_CREATED_AT);

        restUserConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConsent))
            )
            .andExpect(status().isOk());

        // Validate the UserConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConsentUpdatableFieldsEquals(partialUpdatedUserConsent, getPersistedUserConsent(partialUpdatedUserConsent));
    }

    @Test
    @Transactional
    void patchNonExistingUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userConsentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConsent.setId(longCount.incrementAndGet());

        // Create the UserConsent
        UserConsentDTO userConsentDTO = userConsentMapper.toDto(userConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConsentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserConsent() throws Exception {
        // Initialize the database
        insertedUserConsent = userConsentRepository.saveAndFlush(userConsent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userConsent
        restUserConsentMockMvc
            .perform(delete(ENTITY_API_URL_ID, userConsent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userConsentRepository.count();
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

    protected UserConsent getPersistedUserConsent(UserConsent userConsent) {
        return userConsentRepository.findById(userConsent.getId()).orElseThrow();
    }

    protected void assertPersistedUserConsentToMatchAllProperties(UserConsent expectedUserConsent) {
        assertUserConsentAllPropertiesEquals(expectedUserConsent, getPersistedUserConsent(expectedUserConsent));
    }

    protected void assertPersistedUserConsentToMatchUpdatableProperties(UserConsent expectedUserConsent) {
        assertUserConsentAllUpdatablePropertiesEquals(expectedUserConsent, getPersistedUserConsent(expectedUserConsent));
    }
}
