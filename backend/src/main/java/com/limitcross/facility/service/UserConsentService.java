package com.limitcross.facility.service;

import com.limitcross.facility.domain.UserConsent;
import com.limitcross.facility.repository.UserConsentRepository;
import com.limitcross.facility.service.dto.UserConsentDTO;
import com.limitcross.facility.service.mapper.UserConsentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.UserConsent}.
 */
@Service
@Transactional
public class UserConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(UserConsentService.class);

    private final UserConsentRepository userConsentRepository;

    private final UserConsentMapper userConsentMapper;

    public UserConsentService(UserConsentRepository userConsentRepository, UserConsentMapper userConsentMapper) {
        this.userConsentRepository = userConsentRepository;
        this.userConsentMapper = userConsentMapper;
    }

    /**
     * Save a userConsent.
     *
     * @param userConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public UserConsentDTO save(UserConsentDTO userConsentDTO) {
        LOG.debug("Request to save UserConsent : {}", userConsentDTO);
        UserConsent userConsent = userConsentMapper.toEntity(userConsentDTO);
        userConsent = userConsentRepository.save(userConsent);
        return userConsentMapper.toDto(userConsent);
    }

    /**
     * Update a userConsent.
     *
     * @param userConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public UserConsentDTO update(UserConsentDTO userConsentDTO) {
        LOG.debug("Request to update UserConsent : {}", userConsentDTO);
        UserConsent userConsent = userConsentMapper.toEntity(userConsentDTO);
        userConsent = userConsentRepository.save(userConsent);
        return userConsentMapper.toDto(userConsent);
    }

    /**
     * Partially update a userConsent.
     *
     * @param userConsentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserConsentDTO> partialUpdate(UserConsentDTO userConsentDTO) {
        LOG.debug("Request to partially update UserConsent : {}", userConsentDTO);

        return userConsentRepository
            .findById(userConsentDTO.getId())
            .map(existingUserConsent -> {
                userConsentMapper.partialUpdate(existingUserConsent, userConsentDTO);

                return existingUserConsent;
            })
            .map(userConsentRepository::save)
            .map(userConsentMapper::toDto);
    }

    /**
     * Get all the userConsents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserConsentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserConsents");
        return userConsentRepository.findAll(pageable).map(userConsentMapper::toDto);
    }

    /**
     * Get all the userConsents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserConsentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userConsentRepository.findAllWithEagerRelationships(pageable).map(userConsentMapper::toDto);
    }

    /**
     * Get one userConsent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserConsentDTO> findOne(Long id) {
        LOG.debug("Request to get UserConsent : {}", id);
        return userConsentRepository.findOneWithEagerRelationships(id).map(userConsentMapper::toDto);
    }

    /**
     * Delete the userConsent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserConsent : {}", id);
        userConsentRepository.deleteById(id);
    }
}
