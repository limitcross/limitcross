package com.limitcross.facility.service;

import com.limitcross.facility.domain.ServiceAddon;
import com.limitcross.facility.repository.ServiceAddonRepository;
import com.limitcross.facility.service.dto.ServiceAddonDTO;
import com.limitcross.facility.service.mapper.ServiceAddonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ServiceAddon}.
 */
@Service
@Transactional
public class ServiceAddonService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceAddonService.class);

    private final ServiceAddonRepository serviceAddonRepository;

    private final ServiceAddonMapper serviceAddonMapper;

    public ServiceAddonService(ServiceAddonRepository serviceAddonRepository, ServiceAddonMapper serviceAddonMapper) {
        this.serviceAddonRepository = serviceAddonRepository;
        this.serviceAddonMapper = serviceAddonMapper;
    }

    /**
     * Save a serviceAddon.
     *
     * @param serviceAddonDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceAddonDTO save(ServiceAddonDTO serviceAddonDTO) {
        LOG.debug("Request to save ServiceAddon : {}", serviceAddonDTO);
        ServiceAddon serviceAddon = serviceAddonMapper.toEntity(serviceAddonDTO);
        serviceAddon = serviceAddonRepository.save(serviceAddon);
        return serviceAddonMapper.toDto(serviceAddon);
    }

    /**
     * Update a serviceAddon.
     *
     * @param serviceAddonDTO the entity to save.
     * @return the persisted entity.
     */
    public ServiceAddonDTO update(ServiceAddonDTO serviceAddonDTO) {
        LOG.debug("Request to update ServiceAddon : {}", serviceAddonDTO);
        ServiceAddon serviceAddon = serviceAddonMapper.toEntity(serviceAddonDTO);
        serviceAddon = serviceAddonRepository.save(serviceAddon);
        return serviceAddonMapper.toDto(serviceAddon);
    }

    /**
     * Partially update a serviceAddon.
     *
     * @param serviceAddonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceAddonDTO> partialUpdate(ServiceAddonDTO serviceAddonDTO) {
        LOG.debug("Request to partially update ServiceAddon : {}", serviceAddonDTO);

        return serviceAddonRepository
            .findById(serviceAddonDTO.getId())
            .map(existingServiceAddon -> {
                serviceAddonMapper.partialUpdate(existingServiceAddon, serviceAddonDTO);

                return existingServiceAddon;
            })
            .map(serviceAddonRepository::save)
            .map(serviceAddonMapper::toDto);
    }

    /**
     * Get all the serviceAddons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceAddonDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ServiceAddons");
        return serviceAddonRepository.findAll(pageable).map(serviceAddonMapper::toDto);
    }

    /**
     * Get all the serviceAddons with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServiceAddonDTO> findAllWithEagerRelationships(Pageable pageable) {
        return serviceAddonRepository.findAllWithEagerRelationships(pageable).map(serviceAddonMapper::toDto);
    }

    /**
     * Get one serviceAddon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceAddonDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceAddon : {}", id);
        return serviceAddonRepository.findOneWithEagerRelationships(id).map(serviceAddonMapper::toDto);
    }

    /**
     * Delete the serviceAddon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceAddon : {}", id);
        serviceAddonRepository.deleteById(id);
    }
}
