package com.limitcross.facility.service;

import com.limitcross.facility.domain.ReferralReward;
import com.limitcross.facility.repository.ReferralRewardRepository;
import com.limitcross.facility.service.dto.ReferralRewardDTO;
import com.limitcross.facility.service.mapper.ReferralRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ReferralReward}.
 */
@Service
@Transactional
public class ReferralRewardService {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralRewardService.class);

    private final ReferralRewardRepository referralRewardRepository;

    private final ReferralRewardMapper referralRewardMapper;

    public ReferralRewardService(ReferralRewardRepository referralRewardRepository, ReferralRewardMapper referralRewardMapper) {
        this.referralRewardRepository = referralRewardRepository;
        this.referralRewardMapper = referralRewardMapper;
    }

    /**
     * Save a referralReward.
     *
     * @param referralRewardDTO the entity to save.
     * @return the persisted entity.
     */
    public ReferralRewardDTO save(ReferralRewardDTO referralRewardDTO) {
        LOG.debug("Request to save ReferralReward : {}", referralRewardDTO);
        ReferralReward referralReward = referralRewardMapper.toEntity(referralRewardDTO);
        referralReward = referralRewardRepository.save(referralReward);
        return referralRewardMapper.toDto(referralReward);
    }

    /**
     * Update a referralReward.
     *
     * @param referralRewardDTO the entity to save.
     * @return the persisted entity.
     */
    public ReferralRewardDTO update(ReferralRewardDTO referralRewardDTO) {
        LOG.debug("Request to update ReferralReward : {}", referralRewardDTO);
        ReferralReward referralReward = referralRewardMapper.toEntity(referralRewardDTO);
        referralReward = referralRewardRepository.save(referralReward);
        return referralRewardMapper.toDto(referralReward);
    }

    /**
     * Partially update a referralReward.
     *
     * @param referralRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReferralRewardDTO> partialUpdate(ReferralRewardDTO referralRewardDTO) {
        LOG.debug("Request to partially update ReferralReward : {}", referralRewardDTO);

        return referralRewardRepository
            .findById(referralRewardDTO.getId())
            .map(existingReferralReward -> {
                referralRewardMapper.partialUpdate(existingReferralReward, referralRewardDTO);

                return existingReferralReward;
            })
            .map(referralRewardRepository::save)
            .map(referralRewardMapper::toDto);
    }

    /**
     * Get all the referralRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReferralRewardDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReferralRewards");
        return referralRewardRepository.findAll(pageable).map(referralRewardMapper::toDto);
    }

    /**
     * Get all the referralRewards with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReferralRewardDTO> findAllWithEagerRelationships(Pageable pageable) {
        return referralRewardRepository.findAllWithEagerRelationships(pageable).map(referralRewardMapper::toDto);
    }

    /**
     * Get one referralReward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReferralRewardDTO> findOne(Long id) {
        LOG.debug("Request to get ReferralReward : {}", id);
        return referralRewardRepository.findOneWithEagerRelationships(id).map(referralRewardMapper::toDto);
    }

    /**
     * Delete the referralReward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReferralReward : {}", id);
        referralRewardRepository.deleteById(id);
    }
}
