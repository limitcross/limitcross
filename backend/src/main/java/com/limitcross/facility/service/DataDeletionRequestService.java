package com.limitcross.facility.service;

import com.limitcross.facility.domain.DataDeletionRequest;
import com.limitcross.facility.repository.DataDeletionRequestRepository;
import com.limitcross.facility.service.dto.DataDeletionRequestDTO;
import com.limitcross.facility.service.mapper.DataDeletionRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.DataDeletionRequest}.
 */
@Service
@Transactional
public class DataDeletionRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(DataDeletionRequestService.class);

    private final DataDeletionRequestRepository dataDeletionRequestRepository;

    private final DataDeletionRequestMapper dataDeletionRequestMapper;

    public DataDeletionRequestService(
        DataDeletionRequestRepository dataDeletionRequestRepository,
        DataDeletionRequestMapper dataDeletionRequestMapper
    ) {
        this.dataDeletionRequestRepository = dataDeletionRequestRepository;
        this.dataDeletionRequestMapper = dataDeletionRequestMapper;
    }

    /**
     * Save a dataDeletionRequest.
     *
     * @param dataDeletionRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public DataDeletionRequestDTO save(DataDeletionRequestDTO dataDeletionRequestDTO) {
        LOG.debug("Request to save DataDeletionRequest : {}", dataDeletionRequestDTO);
        DataDeletionRequest dataDeletionRequest = dataDeletionRequestMapper.toEntity(dataDeletionRequestDTO);
        dataDeletionRequest = dataDeletionRequestRepository.save(dataDeletionRequest);
        return dataDeletionRequestMapper.toDto(dataDeletionRequest);
    }

    /**
     * Update a dataDeletionRequest.
     *
     * @param dataDeletionRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public DataDeletionRequestDTO update(DataDeletionRequestDTO dataDeletionRequestDTO) {
        LOG.debug("Request to update DataDeletionRequest : {}", dataDeletionRequestDTO);
        DataDeletionRequest dataDeletionRequest = dataDeletionRequestMapper.toEntity(dataDeletionRequestDTO);
        dataDeletionRequest = dataDeletionRequestRepository.save(dataDeletionRequest);
        return dataDeletionRequestMapper.toDto(dataDeletionRequest);
    }

    /**
     * Partially update a dataDeletionRequest.
     *
     * @param dataDeletionRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DataDeletionRequestDTO> partialUpdate(DataDeletionRequestDTO dataDeletionRequestDTO) {
        LOG.debug("Request to partially update DataDeletionRequest : {}", dataDeletionRequestDTO);

        return dataDeletionRequestRepository
            .findById(dataDeletionRequestDTO.getId())
            .map(existingDataDeletionRequest -> {
                dataDeletionRequestMapper.partialUpdate(existingDataDeletionRequest, dataDeletionRequestDTO);

                return existingDataDeletionRequest;
            })
            .map(dataDeletionRequestRepository::save)
            .map(dataDeletionRequestMapper::toDto);
    }

    /**
     * Get all the dataDeletionRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DataDeletionRequestDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DataDeletionRequests");
        return dataDeletionRequestRepository.findAll(pageable).map(dataDeletionRequestMapper::toDto);
    }

    /**
     * Get all the dataDeletionRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DataDeletionRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dataDeletionRequestRepository.findAllWithEagerRelationships(pageable).map(dataDeletionRequestMapper::toDto);
    }

    /**
     * Get one dataDeletionRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataDeletionRequestDTO> findOne(Long id) {
        LOG.debug("Request to get DataDeletionRequest : {}", id);
        return dataDeletionRequestRepository.findOneWithEagerRelationships(id).map(dataDeletionRequestMapper::toDto);
    }

    /**
     * Delete the dataDeletionRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DataDeletionRequest : {}", id);
        dataDeletionRequestRepository.deleteById(id);
    }
}
