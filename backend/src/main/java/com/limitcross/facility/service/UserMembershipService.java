package com.limitcross.facility.service;

import com.limitcross.facility.domain.UserMembership;
import com.limitcross.facility.repository.UserMembershipRepository;
import com.limitcross.facility.service.dto.UserMembershipDTO;
import com.limitcross.facility.service.mapper.UserMembershipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.UserMembership}.
 */
@Service
@Transactional
public class UserMembershipService {

    private static final Logger LOG = LoggerFactory.getLogger(UserMembershipService.class);

    private final UserMembershipRepository userMembershipRepository;

    private final UserMembershipMapper userMembershipMapper;

    public UserMembershipService(UserMembershipRepository userMembershipRepository, UserMembershipMapper userMembershipMapper) {
        this.userMembershipRepository = userMembershipRepository;
        this.userMembershipMapper = userMembershipMapper;
    }

    /**
     * Save a userMembership.
     *
     * @param userMembershipDTO the entity to save.
     * @return the persisted entity.
     */
    public UserMembershipDTO save(UserMembershipDTO userMembershipDTO) {
        LOG.debug("Request to save UserMembership : {}", userMembershipDTO);
        UserMembership userMembership = userMembershipMapper.toEntity(userMembershipDTO);
        userMembership = userMembershipRepository.save(userMembership);
        return userMembershipMapper.toDto(userMembership);
    }

    /**
     * Update a userMembership.
     *
     * @param userMembershipDTO the entity to save.
     * @return the persisted entity.
     */
    public UserMembershipDTO update(UserMembershipDTO userMembershipDTO) {
        LOG.debug("Request to update UserMembership : {}", userMembershipDTO);
        UserMembership userMembership = userMembershipMapper.toEntity(userMembershipDTO);
        userMembership = userMembershipRepository.save(userMembership);
        return userMembershipMapper.toDto(userMembership);
    }

    /**
     * Partially update a userMembership.
     *
     * @param userMembershipDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserMembershipDTO> partialUpdate(UserMembershipDTO userMembershipDTO) {
        LOG.debug("Request to partially update UserMembership : {}", userMembershipDTO);

        return userMembershipRepository
            .findById(userMembershipDTO.getId())
            .map(existingUserMembership -> {
                userMembershipMapper.partialUpdate(existingUserMembership, userMembershipDTO);

                return existingUserMembership;
            })
            .map(userMembershipRepository::save)
            .map(userMembershipMapper::toDto);
    }

    /**
     * Get all the userMemberships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserMembershipDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserMemberships");
        return userMembershipRepository.findAll(pageable).map(userMembershipMapper::toDto);
    }

    /**
     * Get all the userMemberships with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserMembershipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userMembershipRepository.findAllWithEagerRelationships(pageable).map(userMembershipMapper::toDto);
    }

    /**
     * Get one userMembership by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserMembershipDTO> findOne(Long id) {
        LOG.debug("Request to get UserMembership : {}", id);
        return userMembershipRepository.findOneWithEagerRelationships(id).map(userMembershipMapper::toDto);
    }

    /**
     * Delete the userMembership by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserMembership : {}", id);
        userMembershipRepository.deleteById(id);
    }
}
