package com.limitcross.facility.service;

import com.limitcross.facility.domain.CommissionRule;
import com.limitcross.facility.repository.CommissionRuleRepository;
import com.limitcross.facility.service.dto.CommissionRuleDTO;
import com.limitcross.facility.service.mapper.CommissionRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CommissionRule}.
 */
@Service
@Transactional
public class CommissionRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(CommissionRuleService.class);

    private final CommissionRuleRepository commissionRuleRepository;

    private final CommissionRuleMapper commissionRuleMapper;

    public CommissionRuleService(CommissionRuleRepository commissionRuleRepository, CommissionRuleMapper commissionRuleMapper) {
        this.commissionRuleRepository = commissionRuleRepository;
        this.commissionRuleMapper = commissionRuleMapper;
    }

    /**
     * Save a commissionRule.
     *
     * @param commissionRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public CommissionRuleDTO save(CommissionRuleDTO commissionRuleDTO) {
        LOG.debug("Request to save CommissionRule : {}", commissionRuleDTO);
        CommissionRule commissionRule = commissionRuleMapper.toEntity(commissionRuleDTO);
        commissionRule = commissionRuleRepository.save(commissionRule);
        return commissionRuleMapper.toDto(commissionRule);
    }

    /**
     * Update a commissionRule.
     *
     * @param commissionRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public CommissionRuleDTO update(CommissionRuleDTO commissionRuleDTO) {
        LOG.debug("Request to update CommissionRule : {}", commissionRuleDTO);
        CommissionRule commissionRule = commissionRuleMapper.toEntity(commissionRuleDTO);
        commissionRule = commissionRuleRepository.save(commissionRule);
        return commissionRuleMapper.toDto(commissionRule);
    }

    /**
     * Partially update a commissionRule.
     *
     * @param commissionRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommissionRuleDTO> partialUpdate(CommissionRuleDTO commissionRuleDTO) {
        LOG.debug("Request to partially update CommissionRule : {}", commissionRuleDTO);

        return commissionRuleRepository
            .findById(commissionRuleDTO.getId())
            .map(existingCommissionRule -> {
                commissionRuleMapper.partialUpdate(existingCommissionRule, commissionRuleDTO);

                return existingCommissionRule;
            })
            .map(commissionRuleRepository::save)
            .map(commissionRuleMapper::toDto);
    }

    /**
     * Get all the commissionRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommissionRuleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CommissionRules");
        return commissionRuleRepository.findAll(pageable).map(commissionRuleMapper::toDto);
    }

    /**
     * Get all the commissionRules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CommissionRuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return commissionRuleRepository.findAllWithEagerRelationships(pageable).map(commissionRuleMapper::toDto);
    }

    /**
     * Get one commissionRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommissionRuleDTO> findOne(Long id) {
        LOG.debug("Request to get CommissionRule : {}", id);
        return commissionRuleRepository.findOneWithEagerRelationships(id).map(commissionRuleMapper::toDto);
    }

    /**
     * Delete the commissionRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CommissionRule : {}", id);
        commissionRuleRepository.deleteById(id);
    }
}
