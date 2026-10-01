package com.limitcross.facility.service;

import com.limitcross.facility.domain.ServiceTranslation;
import com.limitcross.facility.repository.ServiceTranslationRepository;
import com.limitcross.facility.service.dto.ServiceTranslationDTO;
import com.limitcross.facility.service.mapper.ServiceTranslationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ServiceTranslation}.
 */
@Service
@Transactional
public class ServiceTranslationService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTranslationService.class);

    private final ServiceTranslationRepository serviceTranslationRepository;

    private final ServiceTranslationMapper serviceTranslationMapper;

    public ServiceTranslationService(
        ServiceTranslationRepository serviceTranslationRepository,
        ServiceTranslationMapper serviceTranslationMapper
    ) {
        this.serviceTranslationRepository = serviceTranslationRepository;
        this.serviceTranslationMapper = serviceTranslationMapper;
    }

    /**
     * Save a serviceTranslation.
     *
     * @param serviceTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceTranslationDTO save(ServiceTranslationDTO serviceTranslationDTO) {
        LOG.debug("Request to save ServiceTranslation : {}", serviceTranslationDTO);
        ServiceTranslation serviceTranslation = serviceTranslationMapper.toEntity(serviceTranslationDTO);
        serviceTranslation = serviceTranslationRepository.save(serviceTranslation);
        return serviceTranslationMapper.toDto(serviceTranslation);
    }

    /**
     * Update a serviceTranslation.
     *
     * @param serviceTranslationDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceTranslationDTO update(ServiceTranslationDTO serviceTranslationDTO) {
        LOG.debug("Request to update ServiceTranslation : {}", serviceTranslationDTO);
        ServiceTranslation serviceTranslation = serviceTranslationMapper.toEntity(serviceTranslationDTO);
        serviceTranslation = serviceTranslationRepository.save(serviceTranslation);
        return serviceTranslationMapper.toDto(serviceTranslation);
    }

    /**
     * Partially update a serviceTranslation.
     *
     * @param serviceTranslationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceTranslationDTO> partialUpdate(ServiceTranslationDTO serviceTranslationDTO) {
        LOG.debug("Request to partially update ServiceTranslation : {}", serviceTranslationDTO);

        return serviceTranslationRepository
            .findById(serviceTranslationDTO.getId())
            .map(existingServiceTranslation -> {
                serviceTranslationMapper.partialUpdate(existingServiceTranslation, serviceTranslationDTO);

                return existingServiceTranslation;
            })
            .map(serviceTranslationRepository::save)
            .map(serviceTranslationMapper::toDto);
    }

    /**
     * Get all the serviceTranslations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceTranslationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceTranslations");
        return serviceTranslationRepository.findAll(pageable).map(serviceTranslationMapper::toDto);
    }

    /**
     * Get all the serviceTranslations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServiceTranslationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceTranslationRepository.findAllWithEagerRelationships(pageable).map(serviceTranslationMapper::toDto);
    }

    /**
     * Get one serviceTranslation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceTranslationDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceTranslation : {}", id);
        return serviceTranslationRepository.findOneWithEagerRelationships(id).map(serviceTranslationMapper::toDto);
    }

    /**
     * Delete the serviceTranslation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceTranslation : {}", id);
        serviceTranslationRepository.deleteById(id);
    }
}
