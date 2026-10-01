package com.limitcross.facility.service;

import com.limitcross.facility.domain.Refund;
import com.limitcross.facility.repository.RefundRepository;
import com.limitcross.facility.service.dto.RefundDTO;
import com.limitcross.facility.service.mapper.RefundMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.Refund}.
 */
@Service
@Transactional
public class RefundService {

    private static final Logger LOG = LoggerFactory.getLogger(RefundService.class);

    private final RefundRepository refundRepository;

    private final RefundMapper refundMapper;

    public RefundService(RefundRepository refundRepository, RefundMapper refundMapper) {
        this.refundRepository = refundRepository;
        this.refundMapper = refundMapper;
    }

    /**
     * Save a refund.
     *
     * @param refundDTO the entity to save.
     * @return the persisted entity.
     */
    public RefundDTO save(RefundDTO refundDTO) {
        LOG.debug("Request to save Refund : {}", refundDTO);
        Refund refund = refundMapper.toEntity(refundDTO);
        refund = refundRepository.save(refund);
        return refundMapper.toDto(refund);
    }

    /**
     * Update a refund.
     *
     * @param refundDTO the entity to save.
     * @return the persisted entity.
     */
    public RefundDTO update(RefundDTO refundDTO) {
        LOG.debug("Request to update Refund : {}", refundDTO);
        Refund refund = refundMapper.toEntity(refundDTO);
        refund = refundRepository.save(refund);
        return refundMapper.toDto(refund);
    }

    /**
     * Partially update a refund.
     *
     * @param refundDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RefundDTO> partialUpdate(RefundDTO refundDTO) {
        LOG.debug("Request to partially update Refund : {}", refundDTO);

        return refundRepository
            .findById(refundDTO.getId())
            .map(existingRefund -> {
                refundMapper.partialUpdate(existingRefund, refundDTO);

                return existingRefund;
            })
            .map(refundRepository::save)
            .map(refundMapper::toDto);
    }

    /**
     * Get all the refunds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RefundDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Refunds");
        return refundRepository.findAll(pageable).map(refundMapper::toDto);
    }

    /**
     * Get all the refunds with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RefundDTO> findAllWithEagerRelationships(Pageable pageable) {
        return refundRepository.findAllWithEagerRelationships(pageable).map(refundMapper::toDto);
    }

    /**
     * Get one refund by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RefundDTO> findOne(Long id) {
        LOG.debug("Request to get Refund : {}", id);
        return refundRepository.findOneWithEagerRelationships(id).map(refundMapper::toDto);
    }

    /**
     * Delete the refund by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Refund : {}", id);
        refundRepository.deleteById(id);
    }
}
