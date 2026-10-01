package com.limitcross.facility.service;

import com.limitcross.facility.domain.BlockedEntity;
import com.limitcross.facility.repository.BlockedEntityRepository;
import com.limitcross.facility.service.dto.BlockedEntityDTO;
import com.limitcross.facility.service.mapper.BlockedEntityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BlockedEntity}.
 */
@Service
@Transactional
public class BlockedEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(BlockedEntityService.class);

    private final BlockedEntityRepository blockedEntityRepository;

    private final BlockedEntityMapper blockedEntityMapper;

    public BlockedEntityService(BlockedEntityRepository blockedEntityRepository, BlockedEntityMapper blockedEntityMapper) {
        this.blockedEntityRepository = blockedEntityRepository;
        this.blockedEntityMapper = blockedEntityMapper;
    }

    /**
     * Save a blockedEntity.
     *
     * @param blockedEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public BlockedEntityDTO save(BlockedEntityDTO blockedEntityDTO) {
        LOG.debug("Request to save BlockedEntity : {}", blockedEntityDTO);
        BlockedEntity blockedEntity = blockedEntityMapper.toEntity(blockedEntityDTO);
        blockedEntity = blockedEntityRepository.save(blockedEntity);
        return blockedEntityMapper.toDto(blockedEntity);
    }

    /**
     * Update a blockedEntity.
     *
     * @param blockedEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public BlockedEntityDTO update(BlockedEntityDTO blockedEntityDTO) {
        LOG.debug("Request to update BlockedEntity : {}", blockedEntityDTO);
        BlockedEntity blockedEntity = blockedEntityMapper.toEntity(blockedEntityDTO);
        blockedEntity = blockedEntityRepository.save(blockedEntity);
        return blockedEntityMapper.toDto(blockedEntity);
    }

    /**
     * Partially update a blockedEntity.
     *
     * @param blockedEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BlockedEntityDTO> partialUpdate(BlockedEntityDTO blockedEntityDTO) {
        LOG.debug("Request to partially update BlockedEntity : {}", blockedEntityDTO);

        return blockedEntityRepository
            .findById(blockedEntityDTO.getId())
            .map(existingBlockedEntity -> {
                blockedEntityMapper.partialUpdate(existingBlockedEntity, blockedEntityDTO);

                return existingBlockedEntity;
            })
            .map(blockedEntityRepository::save)
            .map(blockedEntityMapper::toDto);
    }

    /**
     * Get all the blockedEntities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BlockedEntityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BlockedEntities");
        return blockedEntityRepository.findAll(pageable).map(blockedEntityMapper::toDto);
    }

    /**
     * Get one blockedEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlockedEntityDTO> findOne(Long id) {
        LOG.debug("Request to get BlockedEntity : {}", id);
        return blockedEntityRepository.findById(id).map(blockedEntityMapper::toDto);
    }

    /**
     * Delete the blockedEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BlockedEntity : {}", id);
        blockedEntityRepository.deleteById(id);
    }
}
