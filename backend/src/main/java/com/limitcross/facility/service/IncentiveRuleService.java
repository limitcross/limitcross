package com.limitcross.facility.service;

import com.limitcross.facility.domain.IncentiveRule;
import com.limitcross.facility.repository.IncentiveRuleRepository;
import com.limitcross.facility.service.dto.IncentiveRuleDTO;
import com.limitcross.facility.service.mapper.IncentiveRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.IncentiveRule}.
 */
@Service
@Transactional
public class IncentiveRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(IncentiveRuleService.class);

    private final IncentiveRuleRepository incentiveRuleRepository;

    private final IncentiveRuleMapper incentiveRuleMapper;

    public IncentiveRuleService(IncentiveRuleRepository incentiveRuleRepository, IncentiveRuleMapper incentiveRuleMapper) {
        this.incentiveRuleRepository = incentiveRuleRepository;
        this.incentiveRuleMapper = incentiveRuleMapper;
    }

    /**
     * Save a incentiveRule.
     *
     * @param incentiveRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public IncentiveRuleDTO save(IncentiveRuleDTO incentiveRuleDTO) {
        LOG.debug("Request to save IncentiveRule : {}", incentiveRuleDTO);
        IncentiveRule incentiveRule = incentiveRuleMapper.toEntity(incentiveRuleDTO);
        incentiveRule = incentiveRuleRepository.save(incentiveRule);
        return incentiveRuleMapper.toDto(incentiveRule);
    }

    /**
     * Update a incentiveRule.
     *
     * @param incentiveRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public IncentiveRuleDTO update(IncentiveRuleDTO incentiveRuleDTO) {
        LOG.debug("Request to update IncentiveRule : {}", incentiveRuleDTO);
        IncentiveRule incentiveRule = incentiveRuleMapper.toEntity(incentiveRuleDTO);
        incentiveRule = incentiveRuleRepository.save(incentiveRule);
        return incentiveRuleMapper.toDto(incentiveRule);
    }

    /**
     * Partially update a incentiveRule.
     *
     * @param incentiveRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IncentiveRuleDTO> partialUpdate(IncentiveRuleDTO incentiveRuleDTO) {
        LOG.debug("Request to partially update IncentiveRule : {}", incentiveRuleDTO);

        return incentiveRuleRepository
            .findById(incentiveRuleDTO.getId())
            .map(existingIncentiveRule -> {
                incentiveRuleMapper.partialUpdate(existingIncentiveRule, incentiveRuleDTO);

                return existingIncentiveRule;
            })
            .map(incentiveRuleRepository::save)
            .map(incentiveRuleMapper::toDto);
    }

    /**
     * Get all the incentiveRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IncentiveRuleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all IncentiveRules");
        return incentiveRuleRepository.findAll(pageable).map(incentiveRuleMapper::toDto);
    }

    /**
     * Get all the incentiveRules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IncentiveRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return incentiveRuleRepository.findAllWithEagerRelationships(pageable).map(incentiveRuleMapper::toDto);
    }

    /**
     * Get one incentiveRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncentiveRuleDTO> findOne(Long id) {
        LOG.debug("Request to get IncentiveRule : {}", id);
        return incentiveRuleRepository.findOneWithEagerRelationships(id).map(incentiveRuleMapper::toDto);
    }

    /**
     * Delete the incentiveRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IncentiveRule : {}", id);
        incentiveRuleRepository.deleteById(id);
    }
}
