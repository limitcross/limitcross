package com.limitcross.facility.service;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.mapper.FacilityServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.FacilityService}.
 */
@Service
@Transactional
public class FacilityServiceService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityServiceService.class);

    private final FacilityServiceRepository facilityServiceRepository;

    private final FacilityServiceMapper facilityServiceMapper;

    public FacilityServiceService(FacilityServiceRepository facilityServiceRepository, FacilityServiceMapper facilityServiceMapper) {
        this.facilityServiceRepository = facilityServiceRepository;
        this.facilityServiceMapper = facilityServiceMapper;
    }

    /**
     * Save a facilityService.
     *
     * @param facilityServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public FacilityServiceDTO save(FacilityServiceDTO facilityServiceDTO) {
        LOG.debug("Request to save FacilityService : {}", facilityServiceDTO);
        FacilityService facilityService = facilityServiceMapper.toEntity(facilityServiceDTO);
        facilityService = facilityServiceRepository.save(facilityService);
        return facilityServiceMapper.toDto(facilityService);
    }

    /**
     * Update a facilityService.
     *
     * @param facilityServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public FacilityServiceDTO update(FacilityServiceDTO facilityServiceDTO) {
        LOG.debug("Request to update FacilityService : {}", facilityServiceDTO);
        FacilityService facilityService = facilityServiceMapper.toEntity(facilityServiceDTO);
        facilityService = facilityServiceRepository.save(facilityService);
        return facilityServiceMapper.toDto(facilityService);
    }

    /**
     * Partially update a facilityService.
     *
     * @param facilityServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityServiceDTO> partialUpdate(FacilityServiceDTO facilityServiceDTO) {
        LOG.debug("Request to partially update FacilityService : {}", facilityServiceDTO);

        return facilityServiceRepository
            .findById(facilityServiceDTO.getId())
            .map(existingFacilityService -> {
                facilityServiceMapper.partialUpdate(existingFacilityService, facilityServiceDTO);

                return existingFacilityService;
            })
            .map(facilityServiceRepository::save)
            .map(facilityServiceMapper::toDto);
    }

    /**
     * Get all the facilityServices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FacilityServiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return facilityServiceRepository.findAllWithEagerRelationships(pageable).map(facilityServiceMapper::toDto);
    }

    /**
     * Get one facilityService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityServiceDTO> findOne(Long id) {
        LOG.debug("Request to get FacilityService : {}", id);
        return facilityServiceRepository.findOneWithEagerRelationships(id).map(facilityServiceMapper::toDto);
    }

    /**
     * Delete the facilityService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FacilityService : {}", id);
        facilityServiceRepository.deleteById(id);
    }
}
