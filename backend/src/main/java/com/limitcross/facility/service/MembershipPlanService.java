package com.limitcross.facility.service;

import com.limitcross.facility.domain.MembershipPlan;
import com.limitcross.facility.repository.MembershipPlanRepository;
import com.limitcross.facility.service.dto.MembershipPlanDTO;
import com.limitcross.facility.service.mapper.MembershipPlanMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.MembershipPlan}.
 */
@Service
@Transactional
public class MembershipPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipPlanService.class);

    private final MembershipPlanRepository membershipPlanRepository;

    private final MembershipPlanMapper membershipPlanMapper;

    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository, MembershipPlanMapper membershipPlanMapper) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipPlanMapper = membershipPlanMapper;
    }

    /**
     * Save a membershipPlan.
     *
     * @param membershipPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public MembershipPlanDTO save(MembershipPlanDTO membershipPlanDTO) {
        LOG.debug("Request to save MembershipPlan : {}", membershipPlanDTO);
        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(membershipPlanDTO);
        membershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toDto(membershipPlan);
    }

    /**
     * Update a membershipPlan.
     *
     * @param membershipPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public MembershipPlanDTO update(MembershipPlanDTO membershipPlanDTO) {
        LOG.debug("Request to update MembershipPlan : {}", membershipPlanDTO);
        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(membershipPlanDTO);
        membershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toDto(membershipPlan);
    }

    /**
     * Partially update a membershipPlan.
     *
     * @param membershipPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MembershipPlanDTO> partialUpdate(MembershipPlanDTO membershipPlanDTO) {
        LOG.debug("Request to partially update MembershipPlan : {}", membershipPlanDTO);

        return membershipPlanRepository
            .findById(membershipPlanDTO.getId())
            .map(existingMembershipPlan -> {
                membershipPlanMapper.partialUpdate(existingMembershipPlan, membershipPlanDTO);

                return existingMembershipPlan;
            })
            .map(membershipPlanRepository::save)
            .map(membershipPlanMapper::toDto);
    }

    /**
     * Get all the membershipPlans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> findAll() {
        LOG.debug("Request to get all MembershipPlans");
        return membershipPlanRepository
            .findAll()
            .stream()
            .map(membershipPlanMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one membershipPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MembershipPlanDTO> findOne(Long id) {
        LOG.debug("Request to get MembershipPlan : {}", id);
        return membershipPlanRepository.findById(id).map(membershipPlanMapper::toDto);
    }

    /**
     * Delete the membershipPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MembershipPlan : {}", id);
        membershipPlanRepository.deleteById(id);
    }
}
