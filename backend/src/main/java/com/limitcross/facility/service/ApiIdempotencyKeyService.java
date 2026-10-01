package com.limitcross.facility.service;

import com.limitcross.facility.domain.ApiIdempotencyKey;
import com.limitcross.facility.repository.ApiIdempotencyKeyRepository;
import com.limitcross.facility.service.dto.ApiIdempotencyKeyDTO;
import com.limitcross.facility.service.mapper.ApiIdempotencyKeyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ApiIdempotencyKey}.
 */
@Service
@Transactional
public class ApiIdempotencyKeyService {

    private static final Logger LOG = LoggerFactory.getLogger(ApiIdempotencyKeyService.class);

    private final ApiIdempotencyKeyRepository apiIdempotencyKeyRepository;

    private final ApiIdempotencyKeyMapper apiIdempotencyKeyMapper;

    public ApiIdempotencyKeyService(
        ApiIdempotencyKeyRepository apiIdempotencyKeyRepository,
        ApiIdempotencyKeyMapper apiIdempotencyKeyMapper
    ) {
        this.apiIdempotencyKeyRepository = apiIdempotencyKeyRepository;
        this.apiIdempotencyKeyMapper = apiIdempotencyKeyMapper;
    }

    /**
     * Save a apiIdempotencyKey.
     *
     * @param apiIdempotencyKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public ApiIdempotencyKeyDTO save(ApiIdempotencyKeyDTO apiIdempotencyKeyDTO) {
        LOG.debug("Request to save ApiIdempotencyKey : {}", apiIdempotencyKeyDTO);
        ApiIdempotencyKey apiIdempotencyKey = apiIdempotencyKeyMapper.toEntity(apiIdempotencyKeyDTO);
        apiIdempotencyKey = apiIdempotencyKeyRepository.save(apiIdempotencyKey);
        return apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);
    }

    /**
     * Update a apiIdempotencyKey.
     *
     * @param apiIdempotencyKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public ApiIdempotencyKeyDTO update(ApiIdempotencyKeyDTO apiIdempotencyKeyDTO) {
        LOG.debug("Request to update ApiIdempotencyKey : {}", apiIdempotencyKeyDTO);
        ApiIdempotencyKey apiIdempotencyKey = apiIdempotencyKeyMapper.toEntity(apiIdempotencyKeyDTO);
        apiIdempotencyKey = apiIdempotencyKeyRepository.save(apiIdempotencyKey);
        return apiIdempotencyKeyMapper.toDto(apiIdempotencyKey);
    }

    /**
     * Partially update a apiIdempotencyKey.
     *
     * @param apiIdempotencyKeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApiIdempotencyKeyDTO> partialUpdate(ApiIdempotencyKeyDTO apiIdempotencyKeyDTO) {
        LOG.debug("Request to partially update ApiIdempotencyKey : {}", apiIdempotencyKeyDTO);

        return apiIdempotencyKeyRepository
            .findById(apiIdempotencyKeyDTO.getId())
            .map(existingApiIdempotencyKey -> {
                apiIdempotencyKeyMapper.partialUpdate(existingApiIdempotencyKey, apiIdempotencyKeyDTO);

                return existingApiIdempotencyKey;
            })
            .map(apiIdempotencyKeyRepository::save)
            .map(apiIdempotencyKeyMapper::toDto);
    }

    /**
     * Get all the apiIdempotencyKeys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApiIdempotencyKeyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ApiIdempotencyKeys");
        return apiIdempotencyKeyRepository.findAll(pageable).map(apiIdempotencyKeyMapper::toDto);
    }

    /**
     * Get all the apiIdempotencyKeys with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ApiIdempotencyKeyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return apiIdempotencyKeyRepository.findAllWithEagerRelationships(pageable).map(apiIdempotencyKeyMapper::toDto);
    }

    /**
     * Get one apiIdempotencyKey by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApiIdempotencyKeyDTO> findOne(Long id) {
        LOG.debug("Request to get ApiIdempotencyKey : {}", id);
        return apiIdempotencyKeyRepository.findOneWithEagerRelationships(id).map(apiIdempotencyKeyMapper::toDto);
    }

    /**
     * Delete the apiIdempotencyKey by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ApiIdempotencyKey : {}", id);
        apiIdempotencyKeyRepository.deleteById(id);
    }
}
