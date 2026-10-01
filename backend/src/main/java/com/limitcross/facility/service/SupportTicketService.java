package com.limitcross.facility.service;

import com.limitcross.facility.domain.SupportTicket;
import com.limitcross.facility.repository.SupportTicketRepository;
import com.limitcross.facility.service.dto.SupportTicketDTO;
import com.limitcross.facility.service.mapper.SupportTicketMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.SupportTicket}.
 */
@Service
@Transactional
public class SupportTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(SupportTicketService.class);

    private final SupportTicketRepository supportTicketRepository;

    private final SupportTicketMapper supportTicketMapper;

    public SupportTicketService(SupportTicketRepository supportTicketRepository, SupportTicketMapper supportTicketMapper) {
        this.supportTicketRepository = supportTicketRepository;
        this.supportTicketMapper = supportTicketMapper;
    }

    /**
     * Save a supportTicket.
     *
     * @param supportTicketDTO the entity to save.
     * @return the persisted entity.
     */
    public SupportTicketDTO save(SupportTicketDTO supportTicketDTO) {
        LOG.debug("Request to save SupportTicket : {}", supportTicketDTO);
        SupportTicket supportTicket = supportTicketMapper.toEntity(supportTicketDTO);
        supportTicket = supportTicketRepository.save(supportTicket);
        return supportTicketMapper.toDto(supportTicket);
    }

    /**
     * Update a supportTicket.
     *
     * @param supportTicketDTO the entity to save.
     * @return the persisted entity.
     */
    public SupportTicketDTO update(SupportTicketDTO supportTicketDTO) {
        LOG.debug("Request to update SupportTicket : {}", supportTicketDTO);
        SupportTicket supportTicket = supportTicketMapper.toEntity(supportTicketDTO);
        supportTicket = supportTicketRepository.save(supportTicket);
        return supportTicketMapper.toDto(supportTicket);
    }

    /**
     * Partially update a supportTicket.
     *
     * @param supportTicketDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupportTicketDTO> partialUpdate(SupportTicketDTO supportTicketDTO) {
        LOG.debug("Request to partially update SupportTicket : {}", supportTicketDTO);

        return supportTicketRepository
            .findById(supportTicketDTO.getId())
            .map(existingSupportTicket -> {
                supportTicketMapper.partialUpdate(existingSupportTicket, supportTicketDTO);

                return existingSupportTicket;
            })
            .map(supportTicketRepository::save)
            .map(supportTicketMapper::toDto);
    }

    /**
     * Get all the supportTickets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupportTicketDTO> findAllWithEagerRelationships(Pageable pageable) {
        return supportTicketRepository.findAllWithEagerRelationships(pageable).map(supportTicketMapper::toDto);
    }

    /**
     * Get one supportTicket by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupportTicketDTO> findOne(Long id) {
        LOG.debug("Request to get SupportTicket : {}", id);
        return supportTicketRepository.findOneWithEagerRelationships(id).map(supportTicketMapper::toDto);
    }

    /**
     * Delete the supportTicket by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupportTicket : {}", id);
        supportTicketRepository.deleteById(id);
    }
}
