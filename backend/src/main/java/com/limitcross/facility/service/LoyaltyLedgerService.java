package com.limitcross.facility.service;

import com.limitcross.facility.domain.LoyaltyLedger;
import com.limitcross.facility.repository.LoyaltyLedgerRepository;
import com.limitcross.facility.service.dto.LoyaltyLedgerDTO;
import com.limitcross.facility.service.mapper.LoyaltyLedgerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.LoyaltyLedger}.
 */
@Service
@Transactional
public class LoyaltyLedgerService {

    private static final Logger LOG = LoggerFactory.getLogger(LoyaltyLedgerService.class);

    private final LoyaltyLedgerRepository loyaltyLedgerRepository;

    private final LoyaltyLedgerMapper loyaltyLedgerMapper;

    public LoyaltyLedgerService(LoyaltyLedgerRepository loyaltyLedgerRepository, LoyaltyLedgerMapper loyaltyLedgerMapper) {
        this.loyaltyLedgerRepository = loyaltyLedgerRepository;
        this.loyaltyLedgerMapper = loyaltyLedgerMapper;
    }

    /**
     * Save a loyaltyLedger.
     *
     * @param loyaltyLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyLedgerDTO save(LoyaltyLedgerDTO loyaltyLedgerDTO) {
        LOG.debug("Request to save LoyaltyLedger : {}", loyaltyLedgerDTO);
        LoyaltyLedger loyaltyLedger = loyaltyLedgerMapper.toEntity(loyaltyLedgerDTO);
        loyaltyLedger = loyaltyLedgerRepository.save(loyaltyLedger);
        return loyaltyLedgerMapper.toDto(loyaltyLedger);
    }

    /**
     * Update a loyaltyLedger.
     *
     * @param loyaltyLedgerDTO the entity to save.
     * @return the persisted entity.
     */
    public LoyaltyLedgerDTO update(LoyaltyLedgerDTO loyaltyLedgerDTO) {
        LOG.debug("Request to update LoyaltyLedger : {}", loyaltyLedgerDTO);
        LoyaltyLedger loyaltyLedger = loyaltyLedgerMapper.toEntity(loyaltyLedgerDTO);
        loyaltyLedger = loyaltyLedgerRepository.save(loyaltyLedger);
        return loyaltyLedgerMapper.toDto(loyaltyLedger);
    }

    /**
     * Partially update a loyaltyLedger.
     *
     * @param loyaltyLedgerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoyaltyLedgerDTO> partialUpdate(LoyaltyLedgerDTO loyaltyLedgerDTO) {
        LOG.debug("Request to partially update LoyaltyLedger : {}", loyaltyLedgerDTO);

        return loyaltyLedgerRepository
            .findById(loyaltyLedgerDTO.getId())
            .map(existingLoyaltyLedger -> {
                loyaltyLedgerMapper.partialUpdate(existingLoyaltyLedger, loyaltyLedgerDTO);

                return existingLoyaltyLedger;
            })
            .map(loyaltyLedgerRepository::save)
            .map(loyaltyLedgerMapper::toDto);
    }

    /**
     * Get all the loyaltyLedgers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyLedgerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LoyaltyLedgers");
        return loyaltyLedgerRepository.findAll(pageable).map(loyaltyLedgerMapper::toDto);
    }

    /**
     * Get all the loyaltyLedgers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LoyaltyLedgerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return loyaltyLedgerRepository.findAllWithEagerRelationships(pageable).map(loyaltyLedgerMapper::toDto);
    }

    /**
     * Get one loyaltyLedger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoyaltyLedgerDTO> findOne(Long id) {
        LOG.debug("Request to get LoyaltyLedger : {}", id);
        return loyaltyLedgerRepository.findOneWithEagerRelationships(id).map(loyaltyLedgerMapper::toDto);
    }

    /**
     * Delete the loyaltyLedger by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LoyaltyLedger : {}", id);
        loyaltyLedgerRepository.deleteById(id);
    }
}
