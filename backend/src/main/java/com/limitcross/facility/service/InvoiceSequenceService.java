package com.limitcross.facility.service;

import com.limitcross.facility.domain.InvoiceSequence;
import com.limitcross.facility.repository.InvoiceSequenceRepository;
import com.limitcross.facility.service.dto.InvoiceSequenceDTO;
import com.limitcross.facility.service.mapper.InvoiceSequenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.InvoiceSequence}.
 */
@Service
@Transactional
public class InvoiceSequenceService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSequenceService.class);

    private final InvoiceSequenceRepository invoiceSequenceRepository;

    private final InvoiceSequenceMapper invoiceSequenceMapper;

    public InvoiceSequenceService(InvoiceSequenceRepository invoiceSequenceRepository, InvoiceSequenceMapper invoiceSequenceMapper) {
        this.invoiceSequenceRepository = invoiceSequenceRepository;
        this.invoiceSequenceMapper = invoiceSequenceMapper;
    }

    /**
     * Save a invoiceSequence.
     *
     * @param invoiceSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSequenceDTO save(InvoiceSequenceDTO invoiceSequenceDTO) {
        LOG.debug("Request to save InvoiceSequence : {}", invoiceSequenceDTO);
        InvoiceSequence invoiceSequence = invoiceSequenceMapper.toEntity(invoiceSequenceDTO);
        invoiceSequence = invoiceSequenceRepository.save(invoiceSequence);
        return invoiceSequenceMapper.toDto(invoiceSequence);
    }

    /**
     * Update a invoiceSequence.
     *
     * @param invoiceSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSequenceDTO update(InvoiceSequenceDTO invoiceSequenceDTO) {
        LOG.debug("Request to update InvoiceSequence : {}", invoiceSequenceDTO);
        InvoiceSequence invoiceSequence = invoiceSequenceMapper.toEntity(invoiceSequenceDTO);
        invoiceSequence = invoiceSequenceRepository.save(invoiceSequence);
        return invoiceSequenceMapper.toDto(invoiceSequence);
    }

    /**
     * Partially update a invoiceSequence.
     *
     * @param invoiceSequenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceSequenceDTO> partialUpdate(InvoiceSequenceDTO invoiceSequenceDTO) {
        LOG.debug("Request to partially update InvoiceSequence : {}", invoiceSequenceDTO);

        return invoiceSequenceRepository
            .findById(invoiceSequenceDTO.getId())
            .map(existingInvoiceSequence -> {
                invoiceSequenceMapper.partialUpdate(existingInvoiceSequence, invoiceSequenceDTO);

                return existingInvoiceSequence;
            })
            .map(invoiceSequenceRepository::save)
            .map(invoiceSequenceMapper::toDto);
    }

    /**
     * Get all the invoiceSequences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceSequenceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all InvoiceSequences");
        return invoiceSequenceRepository.findAll(pageable).map(invoiceSequenceMapper::toDto);
    }

    /**
     * Get one invoiceSequence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceSequenceDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceSequence : {}", id);
        return invoiceSequenceRepository.findById(id).map(invoiceSequenceMapper::toDto);
    }

    /**
     * Delete the invoiceSequence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceSequence : {}", id);
        invoiceSequenceRepository.deleteById(id);
    }
}
