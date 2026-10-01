package com.limitcross.facility.service;

import com.limitcross.facility.domain.SlotHold;
import com.limitcross.facility.repository.SlotHoldRepository;
import com.limitcross.facility.service.dto.SlotHoldDTO;
import com.limitcross.facility.service.mapper.SlotHoldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.SlotHold}.
 */
@Service
@Transactional
public class SlotHoldService {

    private static final Logger LOG = LoggerFactory.getLogger(SlotHoldService.class);

    private final SlotHoldRepository slotHoldRepository;

    private final SlotHoldMapper slotHoldMapper;

    public SlotHoldService(SlotHoldRepository slotHoldRepository, SlotHoldMapper slotHoldMapper) {
        this.slotHoldRepository = slotHoldRepository;
        this.slotHoldMapper = slotHoldMapper;
    }

    /**
     * Save a slotHold.
     *
     * @param slotHoldDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotHoldDTO save(SlotHoldDTO slotHoldDTO) {
        LOG.debug("Request to save SlotHold : {}", slotHoldDTO);
        SlotHold slotHold = slotHoldMapper.toEntity(slotHoldDTO);
        slotHold = slotHoldRepository.save(slotHold);
        return slotHoldMapper.toDto(slotHold);
    }

    /**
     * Update a slotHold.
     *
     * @param slotHoldDTO the entity to save.
     * @return the persisted entity.
     */
    public SlotHoldDTO update(SlotHoldDTO slotHoldDTO) {
        LOG.debug("Request to update SlotHold : {}", slotHoldDTO);
        SlotHold slotHold = slotHoldMapper.toEntity(slotHoldDTO);
        slotHold = slotHoldRepository.save(slotHold);
        return slotHoldMapper.toDto(slotHold);
    }

    /**
     * Partially update a slotHold.
     *
     * @param slotHoldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SlotHoldDTO> partialUpdate(SlotHoldDTO slotHoldDTO) {
        LOG.debug("Request to partially update SlotHold : {}", slotHoldDTO);

        return slotHoldRepository
            .findById(slotHoldDTO.getId())
            .map(existingSlotHold -> {
                slotHoldMapper.partialUpdate(existingSlotHold, slotHoldDTO);

                return existingSlotHold;
            })
            .map(slotHoldRepository::save)
            .map(slotHoldMapper::toDto);
    }

    /**
     * Get all the slotHolds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SlotHoldDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SlotHolds");
        return slotHoldRepository.findAll(pageable).map(slotHoldMapper::toDto);
    }

    /**
     * Get all the slotHolds with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SlotHoldDTO> findAllWithEagerRelationships(Pageable pageable) {
        return slotHoldRepository.findAllWithEagerRelationships(pageable).map(slotHoldMapper::toDto);
    }

    /**
     * Get one slotHold by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SlotHoldDTO> findOne(Long id) {
        LOG.debug("Request to get SlotHold : {}", id);
        return slotHoldRepository.findOneWithEagerRelationships(id).map(slotHoldMapper::toDto);
    }

    /**
     * Delete the slotHold by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SlotHold : {}", id);
        slotHoldRepository.deleteById(id);
    }
}
