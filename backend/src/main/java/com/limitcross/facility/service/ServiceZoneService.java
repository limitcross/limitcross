package com.limitcross.facility.service;

import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.repository.ServiceZoneRepository;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
import com.limitcross.facility.service.mapper.ServiceZoneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ServiceZone}.
 */
@Service
@Transactional
public class ServiceZoneService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceZoneService.class);

    private final ServiceZoneRepository serviceZoneRepository;

    private final ServiceZoneMapper serviceZoneMapper;

    public ServiceZoneService(ServiceZoneRepository serviceZoneRepository, ServiceZoneMapper serviceZoneMapper) {
        this.serviceZoneRepository = serviceZoneRepository;
        this.serviceZoneMapper = serviceZoneMapper;
    }

    /**
     * Save a serviceZone.
     *
     * @param serviceZoneDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceZoneDTO save(ServiceZoneDTO serviceZoneDTO) {
        LOG.debug("Request to save ServiceZone : {}", serviceZoneDTO);
        ServiceZone serviceZone = serviceZoneMapper.toEntity(serviceZoneDTO);
        serviceZone = serviceZoneRepository.save(serviceZone);
        return serviceZoneMapper.toDto(serviceZone);
    }

    /**
     * Update a serviceZone.
     *
     * @param serviceZoneDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceZoneDTO update(ServiceZoneDTO serviceZoneDTO) {
        LOG.debug("Request to update ServiceZone : {}", serviceZoneDTO);
        ServiceZone serviceZone = serviceZoneMapper.toEntity(serviceZoneDTO);
        serviceZone = serviceZoneRepository.save(serviceZone);
        return serviceZoneMapper.toDto(serviceZone);
    }

    /**
     * Partially update a serviceZone.
     *
     * @param serviceZoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceZoneDTO> partialUpdate(ServiceZoneDTO serviceZoneDTO) {
        LOG.debug("Request to partially update ServiceZone : {}", serviceZoneDTO);

        return serviceZoneRepository
            .findById(serviceZoneDTO.getId())
            .map(existingServiceZone -> {
                serviceZoneMapper.partialUpdate(existingServiceZone, serviceZoneDTO);

                return existingServiceZone;
            })
            .map(serviceZoneRepository::save)
            .map(serviceZoneMapper::toDto);
    }

    /**
     * Get all the serviceZones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceZoneDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceZones");
        return serviceZoneRepository.findAll(pageable).map(serviceZoneMapper::toDto);
    }

    /**
     * Get all the serviceZones with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServiceZoneDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceZoneRepository.findAllWithEagerRelationships(pageable).map(serviceZoneMapper::toDto);
    }

    /**
     * Get one serviceZone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceZoneDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceZone : {}", id);
        return serviceZoneRepository.findOneWithEagerRelationships(id).map(serviceZoneMapper::toDto);
    }

    /**
     * Delete the serviceZone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceZone : {}", id);
        serviceZoneRepository.deleteById(id);
    }
}
