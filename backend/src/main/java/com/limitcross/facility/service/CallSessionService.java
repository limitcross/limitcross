package com.limitcross.facility.service;

import com.limitcross.facility.domain.CallSession;
import com.limitcross.facility.repository.CallSessionRepository;
import com.limitcross.facility.service.dto.CallSessionDTO;
import com.limitcross.facility.service.mapper.CallSessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CallSession}.
 */
@Service
@Transactional
public class CallSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(CallSessionService.class);

    private final CallSessionRepository callSessionRepository;

    private final CallSessionMapper callSessionMapper;

    public CallSessionService(CallSessionRepository callSessionRepository, CallSessionMapper callSessionMapper) {
        this.callSessionRepository = callSessionRepository;
        this.callSessionMapper = callSessionMapper;
    }

    /**
     * Save a callSession.
     *
     * @param callSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public CallSessionDTO save(CallSessionDTO callSessionDTO) {
        LOG.debug("Request to save CallSession : {}", callSessionDTO);
        CallSession callSession = callSessionMapper.toEntity(callSessionDTO);
        callSession = callSessionRepository.save(callSession);
        return callSessionMapper.toDto(callSession);
    }

    /**
     * Update a callSession.
     *
     * @param callSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public CallSessionDTO update(CallSessionDTO callSessionDTO) {
        LOG.debug("Request to update CallSession : {}", callSessionDTO);
        CallSession callSession = callSessionMapper.toEntity(callSessionDTO);
        callSession = callSessionRepository.save(callSession);
        return callSessionMapper.toDto(callSession);
    }

    /**
     * Partially update a callSession.
     *
     * @param callSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CallSessionDTO> partialUpdate(CallSessionDTO callSessionDTO) {
        LOG.debug("Request to partially update CallSession : {}", callSessionDTO);

        return callSessionRepository
            .findById(callSessionDTO.getId())
            .map(existingCallSession -> {
                callSessionMapper.partialUpdate(existingCallSession, callSessionDTO);

                return existingCallSession;
            })
            .map(callSessionRepository::save)
            .map(callSessionMapper::toDto);
    }

    /**
     * Get all the callSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CallSessionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CallSessions");
        return callSessionRepository.findAll(pageable).map(callSessionMapper::toDto);
    }

    /**
     * Get all the callSessions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CallSessionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return callSessionRepository.findAllWithEagerRelationships(pageable).map(callSessionMapper::toDto);
    }

    /**
     * Get one callSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CallSessionDTO> findOne(Long id) {
        LOG.debug("Request to get CallSession : {}", id);
        return callSessionRepository.findOneWithEagerRelationships(id).map(callSessionMapper::toDto);
    }

    /**
     * Delete the callSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CallSession : {}", id);
        callSessionRepository.deleteById(id);
    }
}
