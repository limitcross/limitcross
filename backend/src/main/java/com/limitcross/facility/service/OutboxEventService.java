package com.limitcross.facility.service;

import com.limitcross.facility.domain.OutboxEvent;
import com.limitcross.facility.repository.OutboxEventRepository;
import com.limitcross.facility.service.dto.OutboxEventDTO;
import com.limitcross.facility.service.mapper.OutboxEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.OutboxEvent}.
 */
@Service
@Transactional
public class OutboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(OutboxEventService.class);

    private final OutboxEventRepository outboxEventRepository;

    private final OutboxEventMapper outboxEventMapper;

    public OutboxEventService(OutboxEventRepository outboxEventRepository, OutboxEventMapper outboxEventMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.outboxEventMapper = outboxEventMapper;
    }

    /**
     * Save a outboxEvent.
     *
     * @param outboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public OutboxEventDTO save(OutboxEventDTO outboxEventDTO) {
        LOG.debug("Request to save OutboxEvent : {}", outboxEventDTO);
        OutboxEvent outboxEvent = outboxEventMapper.toEntity(outboxEventDTO);
        outboxEvent = outboxEventRepository.save(outboxEvent);
        return outboxEventMapper.toDto(outboxEvent);
    }

    /**
     * Update a outboxEvent.
     *
     * @param outboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public OutboxEventDTO update(OutboxEventDTO outboxEventDTO) {
        LOG.debug("Request to update OutboxEvent : {}", outboxEventDTO);
        OutboxEvent outboxEvent = outboxEventMapper.toEntity(outboxEventDTO);
        outboxEvent = outboxEventRepository.save(outboxEvent);
        return outboxEventMapper.toDto(outboxEvent);
    }

    /**
     * Partially update a outboxEvent.
     *
     * @param outboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OutboxEventDTO> partialUpdate(OutboxEventDTO outboxEventDTO) {
        LOG.debug("Request to partially update OutboxEvent : {}", outboxEventDTO);

        return outboxEventRepository
            .findById(outboxEventDTO.getId())
            .map(existingOutboxEvent -> {
                outboxEventMapper.partialUpdate(existingOutboxEvent, outboxEventDTO);

                return existingOutboxEvent;
            })
            .map(outboxEventRepository::save)
            .map(outboxEventMapper::toDto);
    }

    /**
     * Get all the outboxEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OutboxEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OutboxEvents");
        return outboxEventRepository.findAll(pageable).map(outboxEventMapper::toDto);
    }

    /**
     * Get one outboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OutboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get OutboxEvent : {}", id);
        return outboxEventRepository.findById(id).map(outboxEventMapper::toDto);
    }

    /**
     * Delete the outboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OutboxEvent : {}", id);
        outboxEventRepository.deleteById(id);
    }
}
