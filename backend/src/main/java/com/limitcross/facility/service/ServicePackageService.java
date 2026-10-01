package com.limitcross.facility.service;

import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.repository.ServicePackageRepository;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import com.limitcross.facility.service.mapper.ServicePackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ServicePackage}.
 */
@Service
@Transactional
public class ServicePackageService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicePackageService.class);

    private final ServicePackageRepository servicePackageRepository;

    private final ServicePackageMapper servicePackageMapper;

    public ServicePackageService(ServicePackageRepository servicePackageRepository, ServicePackageMapper servicePackageMapper) {
        this.servicePackageRepository = servicePackageRepository;
        this.servicePackageMapper = servicePackageMapper;
    }

    /**
     * Save a servicePackage.
     *
     * @param servicePackageDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicePackageDTO save(ServicePackageDTO servicePackageDTO) {
        LOG.debug("Request to save ServicePackage : {}", servicePackageDTO);
        ServicePackage servicePackage = servicePackageMapper.toEntity(servicePackageDTO);
        servicePackage = servicePackageRepository.save(servicePackage);
        return servicePackageMapper.toDto(servicePackage);
    }

    /**
     * Update a servicePackage.
     *
     * @param servicePackageDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicePackageDTO update(ServicePackageDTO servicePackageDTO) {
        LOG.debug("Request to update ServicePackage : {}", servicePackageDTO);
        ServicePackage servicePackage = servicePackageMapper.toEntity(servicePackageDTO);
        servicePackage = servicePackageRepository.save(servicePackage);
        return servicePackageMapper.toDto(servicePackage);
    }

    /**
     * Partially update a servicePackage.
     *
     * @param servicePackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServicePackageDTO> partialUpdate(ServicePackageDTO servicePackageDTO) {
        LOG.debug("Request to partially update ServicePackage : {}", servicePackageDTO);

        return servicePackageRepository
            .findById(servicePackageDTO.getId())
            .map(existingServicePackage -> {
                servicePackageMapper.partialUpdate(existingServicePackage, servicePackageDTO);

                return existingServicePackage;
            })
            .map(servicePackageRepository::save)
            .map(servicePackageMapper::toDto);
    }

    /**
     * Get all the servicePackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicePackageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServicePackages");
        return servicePackageRepository.findAll(pageable).map(servicePackageMapper::toDto);
    }

    /**
     * Get all the servicePackages with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServicePackageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicePackageRepository.findAllWithEagerRelationships(pageable).map(servicePackageMapper::toDto);
    }

    /**
     * Get one servicePackage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicePackageDTO> findOne(Long id) {
        LOG.debug("Request to get ServicePackage : {}", id);
        return servicePackageRepository.findOneWithEagerRelationships(id).map(servicePackageMapper::toDto);
    }

    /**
     * Delete the servicePackage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServicePackage : {}", id);
        servicePackageRepository.deleteById(id);
    }
}
