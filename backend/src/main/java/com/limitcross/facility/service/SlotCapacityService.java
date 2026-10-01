package com.limitcross.facility.service;

import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.repository.SlotCapacityRepository;
import com.limitcross.facility.service.dto.SlotCapacityDTO;
import com.limitcross.facility.service.mapper.SlotCapacityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.SlotCapacity}.
 */
@Service
@Transactional
public class SlotCapacityService {

    private static final Logger LOG = LoggerFactory.getLogger(SlotCapacityService.class);

    private final SlotCapacityRepository slotCapacityRepository;

    private final SlotCapacityMapper slotCapacityMapper;

    public SlotCapacityService(SlotCapacityRepository slotCapacityRepository, SlotCapacityMapper slotCapacityMapper) {
        this.slotCapacityRepository = slotCapacityRepository;
        this.slotCapacityMapper = slotCapacityMapper;
    }

    /**
     * Save a slotCapacity.
     *
     * @param slotCapacityDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotCapacityDTO save(SlotCapacityDTO slotCapacityDTO) {
        LOG.debug("Request to save SlotCapacity : {}", slotCapacityDTO);
        SlotCapacity slotCapacity = slotCapacityMapper.toEntity(slotCapacityDTO);
        slotCapacity = slotCapacityRepository.save(slotCapacity);
        return slotCapacityMapper.toDto(slotCapacity);
    }

    /**
     * Update a slotCapacity.
     *
     * @param slotCapacityDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotCapacityDTO update(SlotCapacityDTO slotCapacityDTO) {
        LOG.debug("Request to update SlotCapacity : {}", slotCapacityDTO);
        SlotCapacity slotCapacity = slotCapacityMapper.toEntity(slotCapacityDTO);
        slotCapacity = slotCapacityRepository.save(slotCapacity);
        return slotCapacityMapper.toDto(slotCapacity);
    }

    /**
     * Partially update a slotCapacity.
     *
     * @param slotCapacityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SlotCapacityDTO> partialUpdate(SlotCapacityDTO slotCapacityDTO) {
        LOG.debug("Request to partially update SlotCapacity : {}", slotCapacityDTO);

        return slotCapacityRepository
            .findById(slotCapacityDTO.getId())
            .map(existingSlotCapacity -> {
                slotCapacityMapper.partialUpdate(existingSlotCapacity, slotCapacityDTO);

                return existingSlotCapacity;
            })
            .map(slotCapacityRepository::save)
            .map(slotCapacityMapper::toDto);
    }

    /**
     * Get all the slotCapacities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SlotCapacityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SlotCapacities");
        return slotCapacityRepository.findAll(pageable).map(slotCapacityMapper::toDto);
    }

    /**
     * Get all the slotCapacities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SlotCapacityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return slotCapacityRepository.findAllWithEagerRelationships(pageable).map(slotCapacityMapper::toDto);
    }

    /**
     * Get one slotCapacity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SlotCapacityDTO> findOne(Long id) {
        LOG.debug("Request to get SlotCapacity : {}", id);
        return slotCapacityRepository.findOneWithEagerRelationships(id).map(slotCapacityMapper::toDto);
    }

    /**
     * Delete the slotCapacity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SlotCapacity : {}", id);
        slotCapacityRepository.deleteById(id);
    }
}
