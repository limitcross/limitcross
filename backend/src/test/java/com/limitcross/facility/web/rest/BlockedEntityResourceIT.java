package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BlockedEntityAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.BlockedEntity;
import com.limitcross.facility.domain.enumeration.BlockedEntityType;
import com.limitcross.facility.repository.BlockedEntityRepository;
import com.limitcross.facility.service.dto.BlockedEntityDTO;
import com.limitcross.facility.service.mapper.BlockedEntityMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BlockedEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlockedEntityResourceIT {

    private static final BlockedEntityType DEFAULT_ENTITY_TYPE = BlockedEntityType.PHONE;
    private static final BlockedEntityType UPDATED_ENTITY_TYPE = BlockedEntityType.EMAIL;

    private static final String DEFAULT_VALUE_HASH = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_BLOCKED_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BLOCKED_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/blocked-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlockedEntityRepository blockedEntityRepository;

    @Autowired
    private BlockedEntityMapper blockedEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockedEntityMockMvc;

    private BlockedEntity blockedEntity;

    private BlockedEntity insertedBlockedEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlockedEntity createEntity() {
        return new BlockedEntity()
            .entityType(DEFAULT_ENTITY_TYPE)
            .valueHash(DEFAULT_VALUE_HASH)
            .reason(DEFAULT_REASON)
            .blockedUntil(DEFAULT_BLOCKED_UNTIL)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlockedEntity createUpdatedEntity() {
        return new BlockedEntity()
            .entityType(UPDATED_ENTITY_TYPE)
            .valueHash(UPDATED_VALUE_HASH)
            .reason(UPDATED_REASON)
            .blockedUntil(UPDATED_BLOCKED_UNTIL)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        blockedEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBlockedEntity != null) {
            blockedEntityRepository.delete(insertedBlockedEntity);
            insertedBlockedEntity = null;
        }
    }

    @Test
    @Transactional
    void createBlockedEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);
        var returnedBlockedEntityDTO = om.readValue(
            restBlockedEntityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockedEntityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BlockedEntityDTO.class
        );

        // Validate the BlockedEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBlockedEntity = blockedEntityMapper.toEntity(returnedBlockedEntityDTO);
        assertBlockedEntityUpdatableFieldsEquals(returnedBlockedEntity, getPersistedBlockedEntity(returnedBlockedEntity));

        insertedBlockedEntity = returnedBlockedEntity;
    }

    @Test
    @Transactional
    void createBlockedEntityWithExistingId() throws Exception {
        // Create the BlockedEntity with an existing ID
        blockedEntity.setId(1L);
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockedEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockedEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blockedEntity.setEntityType(null);

        // Create the BlockedEntity, which fails.
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        restBlockedEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockedEntityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blockedEntity.setValueHash(null);

        // Create the BlockedEntity, which fails.
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        restBlockedEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockedEntityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlockedEntities() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        // Get all the blockedEntityList
        restBlockedEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockedEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].valueHash").value(hasItem(DEFAULT_VALUE_HASH)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].blockedUntil").value(hasItem(DEFAULT_BLOCKED_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBlockedEntity() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        // Get the blockedEntity
        restBlockedEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, blockedEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blockedEntity.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.valueHash").value(DEFAULT_VALUE_HASH))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.blockedUntil").value(DEFAULT_BLOCKED_UNTIL.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBlockedEntity() throws Exception {
        // Get the blockedEntity
        restBlockedEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlockedEntity() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blockedEntity
        BlockedEntity updatedBlockedEntity = blockedEntityRepository.findById(blockedEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlockedEntity are not directly saved in db
        em.detach(updatedBlockedEntity);
        updatedBlockedEntity
            .entityType(UPDATED_ENTITY_TYPE)
            .valueHash(UPDATED_VALUE_HASH)
            .reason(UPDATED_REASON)
            .blockedUntil(UPDATED_BLOCKED_UNTIL)
            .createdAt(UPDATED_CREATED_AT);
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(updatedBlockedEntity);

        restBlockedEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockedEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blockedEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlockedEntityToMatchAllProperties(updatedBlockedEntity);
    }

    @Test
    @Transactional
    void putNonExistingBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockedEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blockedEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blockedEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockedEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlockedEntityWithPatch() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blockedEntity using partial update
        BlockedEntity partialUpdatedBlockedEntity = new BlockedEntity();
        partialUpdatedBlockedEntity.setId(blockedEntity.getId());

        partialUpdatedBlockedEntity.entityType(UPDATED_ENTITY_TYPE).valueHash(UPDATED_VALUE_HASH).reason(UPDATED_REASON);

        restBlockedEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockedEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlockedEntity))
            )
            .andExpect(status().isOk());

        // Validate the BlockedEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlockedEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBlockedEntity, blockedEntity),
            getPersistedBlockedEntity(blockedEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateBlockedEntityWithPatch() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blockedEntity using partial update
        BlockedEntity partialUpdatedBlockedEntity = new BlockedEntity();
        partialUpdatedBlockedEntity.setId(blockedEntity.getId());

        partialUpdatedBlockedEntity
            .entityType(UPDATED_ENTITY_TYPE)
            .valueHash(UPDATED_VALUE_HASH)
            .reason(UPDATED_REASON)
            .blockedUntil(UPDATED_BLOCKED_UNTIL)
            .createdAt(UPDATED_CREATED_AT);

        restBlockedEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockedEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlockedEntity))
            )
            .andExpect(status().isOk());

        // Validate the BlockedEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlockedEntityUpdatableFieldsEquals(partialUpdatedBlockedEntity, getPersistedBlockedEntity(partialUpdatedBlockedEntity));
    }

    @Test
    @Transactional
    void patchNonExistingBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blockedEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blockedEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blockedEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlockedEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockedEntity.setId(longCount.incrementAndGet());

        // Create the BlockedEntity
        BlockedEntityDTO blockedEntityDTO = blockedEntityMapper.toDto(blockedEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockedEntityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blockedEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlockedEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlockedEntity() throws Exception {
        // Initialize the database
        insertedBlockedEntity = blockedEntityRepository.saveAndFlush(blockedEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the blockedEntity
        restBlockedEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, blockedEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return blockedEntityRepository.count();
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

    protected BlockedEntity getPersistedBlockedEntity(BlockedEntity blockedEntity) {
        return blockedEntityRepository.findById(blockedEntity.getId()).orElseThrow();
    }

    protected void assertPersistedBlockedEntityToMatchAllProperties(BlockedEntity expectedBlockedEntity) {
        assertBlockedEntityAllPropertiesEquals(expectedBlockedEntity, getPersistedBlockedEntity(expectedBlockedEntity));
    }

    protected void assertPersistedBlockedEntityToMatchUpdatableProperties(BlockedEntity expectedBlockedEntity) {
        assertBlockedEntityAllUpdatablePropertiesEquals(expectedBlockedEntity, getPersistedBlockedEntity(expectedBlockedEntity));
    }
}
