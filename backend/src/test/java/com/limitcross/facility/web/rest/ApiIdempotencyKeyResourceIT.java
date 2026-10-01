package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ApiIdempotencyKeyAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.ApiIdempotencyKey;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.repository.ApiIdempotencyKeyRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.ApiIdempotencyKeyService;
import com.limitcross.facility.service.dto.ApiIdempotencyKeyDTO;
import com.limitcross.facility.service.mapper.ApiIdempotencyKeyMapper;
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
 * Integration tests for the {@link ApiIdempotencyKeyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApiIdempotencyKeyResourceIT {

    private static final String DEFAULT_IDEM_KEY = "AAAAAAAAAA";
    private static final String UPDATED_IDEM_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_HASH = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_HASH = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESPONSE_CODE = 1;
    private static final Integer UPDATED_RESPONSE_CODE = 2;

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/api-idempotency-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApiIdempotencyKeyRepository apiIdempotencyKeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ApiIdempotencyKeyRepository apiIdempotencyKeyRepositoryMock;

    @Autowired
    private ApiIdempotencyKeyMapper apiIdempotencyKeyMapper;

    @Mock
    private ApiIdempotencyKeyService apiIdempotencyKeyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiIdempotencyKeyMockMvc;

    private ApiIdempotencyKey apiIdempotencyKey;

    private ApiIdempotencyKey insertedApiIdempotencyKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiIdempotencyKey createEntity(EntityManager em) {
        ApiIdempotencyKey apiIdempotencyKey = new ApiIdempotencyKey()
            .idemKey(DEFAULT_IDEM_KEY)
            .requestHash(DEFAULT_REQUEST_HASH)
            .responseCode(DEFAULT_RESPONSE_CODE)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .createdAt(DEFAULT_CREATED_AT)
            .expiresAt(DEFAULT_EXPIRES_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        apiIdempotencyKey.setUser(user);
        return apiIdempotencyKey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiIdempotencyKey createUpdatedEntity(EntityManager em) {
        ApiIdempotencyKey updatedApiIdempotencyKey = new ApiIdempotencyKey()
            .idemKey(UPDATED_IDEM_KEY)
            .requestHash(UPDATED_REQUEST_HASH)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .expiresAt(UPDATED_EXPIRES_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedApiIdempotencyKey.setUser(user);
        return updatedApiIdempotencyKey;
    }

    @BeforeEach
    void initTest() {
        apiIdempotencyKey = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedApiIdempotencyKey != null) {
            apiIdempotencyKeyRepository.delete(insertedApiIdempotencyKey);
            insertedApiIdempotencyKey = null;
        }
    }

    @Test
    @Transactional
    void createApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);
        var returnedApiIdempotencyKeyDTO = om.readValue(
            restApiIdempotencyKeyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApiIdempotencyKeyDTO.class
        );

        // Validate the ApiIdempotencyKey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApiIdempotencyKey = apiIdempotencyKeyMapper.toEntity(returnedApiIdempotencyKeyDTO);
        assertApiIdempotencyKeyUpdatableFieldsEquals(returnedApiIdempotencyKey, getPersistedApiIdempotencyKey(returnedApiIdempotencyKey));

        insertedApiIdempotencyKey = returnedApiIdempotencyKey;
    }

    @Test
    @Transactional
    void createApiIdempotencyKeyWithExistingId() throws Exception {
        // Create the ApiIdempotencyKey with an existing ID
        apiIdempotencyKey.setId(1L);
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdemKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiIdempotencyKey.setIdemKey(null);

        // Create the ApiIdempotencyKey, which fails.
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        restApiIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiIdempotencyKey.setRequestHash(null);

        // Create the ApiIdempotencyKey, which fails.
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        restApiIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiresAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        apiIdempotencyKey.setExpiresAt(null);

        // Create the ApiIdempotencyKey, which fails.
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        restApiIdempotencyKeyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApiIdempotencyKeys() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        // Get all the apiIdempotencyKeyList
        restApiIdempotencyKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiIdempotencyKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].idemKey").value(hasItem(DEFAULT_IDEM_KEY)))
            .andExpect(jsonPath("$.[*].requestHash").value(hasItem(DEFAULT_REQUEST_HASH)))
            .andExpect(jsonPath("$.[*].responseCode").value(hasItem(DEFAULT_RESPONSE_CODE)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApiIdempotencyKeysWithEagerRelationshipsIsEnabled() throws Exception {
        when(apiIdempotencyKeyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApiIdempotencyKeyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(apiIdempotencyKeyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApiIdempotencyKeysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(apiIdempotencyKeyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApiIdempotencyKeyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(apiIdempotencyKeyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getApiIdempotencyKey() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        // Get the apiIdempotencyKey
        restApiIdempotencyKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, apiIdempotencyKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiIdempotencyKey.getId().intValue()))
            .andExpect(jsonPath("$.idemKey").value(DEFAULT_IDEM_KEY))
            .andExpect(jsonPath("$.requestHash").value(DEFAULT_REQUEST_HASH))
            .andExpect(jsonPath("$.responseCode").value(DEFAULT_RESPONSE_CODE))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApiIdempotencyKey() throws Exception {
        // Get the apiIdempotencyKey
        restApiIdempotencyKeyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApiIdempotencyKey() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiIdempotencyKey
        ApiIdempotencyKey updatedApiIdempotencyKey = apiIdempotencyKeyRepository.findById(apiIdempotencyKey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApiIdempotencyKey are not directly saved in db
        em.detach(updatedApiIdempotencyKey);
        updatedApiIdempotencyKey
            .idemKey(UPDATED_IDEM_KEY)
            .requestHash(UPDATED_REQUEST_HASH)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .expiresAt(UPDATED_EXPIRES_AT);
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(updatedApiIdempotencyKey);

        restApiIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiIdempotencyKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiIdempotencyKeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApiIdempotencyKeyToMatchAllProperties(updatedApiIdempotencyKey);
    }

    @Test
    @Transactional
    void putNonExistingApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiIdempotencyKeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiIdempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiIdempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiIdempotencyKeyWithPatch() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiIdempotencyKey using partial update
        ApiIdempotencyKey partialUpdatedApiIdempotencyKey = new ApiIdempotencyKey();
        partialUpdatedApiIdempotencyKey.setId(apiIdempotencyKey.getId());

        partialUpdatedApiIdempotencyKey.responseBody(UPDATED_RESPONSE_BODY);

        restApiIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiIdempotencyKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiIdempotencyKey))
            )
            .andExpect(status().isOk());

        // Validate the ApiIdempotencyKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiIdempotencyKeyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApiIdempotencyKey, apiIdempotencyKey),
            getPersistedApiIdempotencyKey(apiIdempotencyKey)
        );
    }

    @Test
    @Transactional
    void fullUpdateApiIdempotencyKeyWithPatch() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiIdempotencyKey using partial update
        ApiIdempotencyKey partialUpdatedApiIdempotencyKey = new ApiIdempotencyKey();
        partialUpdatedApiIdempotencyKey.setId(apiIdempotencyKey.getId());

        partialUpdatedApiIdempotencyKey
            .idemKey(UPDATED_IDEM_KEY)
            .requestHash(UPDATED_REQUEST_HASH)
            .responseCode(UPDATED_RESPONSE_CODE)
            .responseBody(UPDATED_RESPONSE_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .expiresAt(UPDATED_EXPIRES_AT);

        restApiIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiIdempotencyKey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiIdempotencyKey))
            )
            .andExpect(status().isOk());

        // Validate the ApiIdempotencyKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiIdempotencyKeyUpdatableFieldsEquals(
            partialUpdatedApiIdempotencyKey,
            getPersistedApiIdempotencyKey(partialUpdatedApiIdempotencyKey)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiIdempotencyKeyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiIdempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiIdempotencyKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiIdempotencyKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiIdempotencyKey.setId(longCount.incrementAndGet());

        // Create the ApiIdempotencyKey
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiIdempotencyKeyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(apiIdempotencyKeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiIdempotencyKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiIdempotencyKey() throws Exception {
        // Initialize the database
        insertedApiIdempotencyKey = apiIdempotencyKeyRepository.saveAndFlush(apiIdempotencyKey);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the apiIdempotencyKey
        restApiIdempotencyKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiIdempotencyKey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return apiIdempotencyKeyRepository.count();
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

    protected ApiIdempotencyKey getPersistedApiIdempotencyKey(ApiIdempotencyKey apiIdempotencyKey) {
        return apiIdempotencyKeyRepository.findById(apiIdempotencyKey.getId()).orElseThrow();
    }

    protected void assertPersistedApiIdempotencyKeyToMatchAllProperties(ApiIdempotencyKey expectedApiIdempotencyKey) {
        assertApiIdempotencyKeyAllPropertiesEquals(expectedApiIdempotencyKey, getPersistedApiIdempotencyKey(expectedApiIdempotencyKey));
    }

    protected void assertPersistedApiIdempotencyKeyToMatchUpdatableProperties(ApiIdempotencyKey expectedApiIdempotencyKey) {
        assertApiIdempotencyKeyAllUpdatablePropertiesEquals(
            expectedApiIdempotencyKey,
            getPersistedApiIdempotencyKey(expectedApiIdempotencyKey)
        );
    }
}
