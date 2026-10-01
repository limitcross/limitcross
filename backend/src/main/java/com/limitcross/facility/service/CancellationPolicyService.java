package com.limitcross.facility.service;

import com.limitcross.facility.domain.CancellationPolicy;
import com.limitcross.facility.repository.CancellationPolicyRepository;
import com.limitcross.facility.service.dto.CancellationPolicyDTO;
import com.limitcross.facility.service.mapper.CancellationPolicyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CancellationPolicy}.
 */
@Service
@Transactional
public class CancellationPolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(CancellationPolicyService.class);

    private final CancellationPolicyRepository cancellationPolicyRepository;

    private final CancellationPolicyMapper cancellationPolicyMapper;

    public CancellationPolicyService(
        CancellationPolicyRepository cancellationPolicyRepository,
        CancellationPolicyMapper cancellationPolicyMapper
    ) {
        this.cancellationPolicyRepository = cancellationPolicyRepository;
        this.cancellationPolicyMapper = cancellationPolicyMapper;
    }

    /**
     * Save a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to save.
     * @return the persisted entity.
     */
    public CancellationPolicyDTO save(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to save CancellationPolicy : {}", cancellationPolicyDTO);
        CancellationPolicy cancellationPolicy = cancellationPolicyMapper.toEntity(cancellationPolicyDTO);
        cancellationPolicy = cancellationPolicyRepository.save(cancellationPolicy);
        return cancellationPolicyMapper.toDto(cancellationPolicy);
    }

    /**
     * Update a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to save.
     * @return the persisted entity.
     */
    public CancellationPolicyDTO update(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to update CancellationPolicy : {}", cancellationPolicyDTO);
        CancellationPolicy cancellationPolicy = cancellationPolicyMapper.toEntity(cancellationPolicyDTO);
        cancellationPolicy = cancellationPolicyRepository.save(cancellationPolicy);
        return cancellationPolicyMapper.toDto(cancellationPolicy);
    }

    /**
     * Partially update a cancellationPolicy.
     *
     * @param cancellationPolicyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CancellationPolicyDTO> partialUpdate(CancellationPolicyDTO cancellationPolicyDTO) {
        LOG.debug("Request to partially update CancellationPolicy : {}", cancellationPolicyDTO);

        return cancellationPolicyRepository
            .findById(cancellationPolicyDTO.getId())
            .map(existingCancellationPolicy -> {
                cancellationPolicyMapper.partialUpdate(existingCancellationPolicy, cancellationPolicyDTO);

                return existingCancellationPolicy;
            })
            .map(cancellationPolicyRepository::save)
            .map(cancellationPolicyMapper::toDto);
    }

    /**
     * Get all the cancellationPolicies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CancellationPolicyDTO> findAll() {
        LOG.debug("Request to get all CancellationPolicies");
        return cancellationPolicyRepository
            .findAll()
            .stream()
            .map(cancellationPolicyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the cancellationPolicies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CancellationPolicyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cancellationPolicyRepository.findAllWithEagerRelationships(pageable).map(cancellationPolicyMapper::toDto);
    }

    /**
     * Get one cancellationPolicy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CancellationPolicyDTO> findOne(Long id) {
        LOG.debug("Request to get CancellationPolicy : {}", id);
        return cancellationPolicyRepository.findOneWithEagerRelationships(id).map(cancellationPolicyMapper::toDto);
    }

    /**
     * Delete the cancellationPolicy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CancellationPolicy : {}", id);
        cancellationPolicyRepository.deleteById(id);
    }
}
