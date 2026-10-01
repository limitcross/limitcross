package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalAvailability;
import com.limitcross.facility.repository.ProfessionalAvailabilityRepository;
import com.limitcross.facility.service.dto.ProfessionalAvailabilityDTO;
import com.limitcross.facility.service.mapper.ProfessionalAvailabilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalAvailability}.
 */
@Service
@Transactional
public class ProfessionalAvailabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalAvailabilityService.class);

    private final ProfessionalAvailabilityRepository professionalAvailabilityRepository;

    private final ProfessionalAvailabilityMapper professionalAvailabilityMapper;

    public ProfessionalAvailabilityService(
        ProfessionalAvailabilityRepository professionalAvailabilityRepository,
        ProfessionalAvailabilityMapper professionalAvailabilityMapper
    ) {
        this.professionalAvailabilityRepository = professionalAvailabilityRepository;
        this.professionalAvailabilityMapper = professionalAvailabilityMapper;
    }

    /**
     * Save a professionalAvailability.
     *
     * @param professionalAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalAvailabilityDTO save(ProfessionalAvailabilityDTO professionalAvailabilityDTO) {
        LOG.debug("Request to save ProfessionalAvailability : {}", professionalAvailabilityDTO);
        ProfessionalAvailability professionalAvailability = professionalAvailabilityMapper.toEntity(professionalAvailabilityDTO);
        professionalAvailability = professionalAvailabilityRepository.save(professionalAvailability);
        return professionalAvailabilityMapper.toDto(professionalAvailability);
    }

    /**
     * Update a professionalAvailability.
     *
     * @param professionalAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalAvailabilityDTO update(ProfessionalAvailabilityDTO professionalAvailabilityDTO) {
        LOG.debug("Request to update ProfessionalAvailability : {}", professionalAvailabilityDTO);
        ProfessionalAvailability professionalAvailability = professionalAvailabilityMapper.toEntity(professionalAvailabilityDTO);
        professionalAvailability = professionalAvailabilityRepository.save(professionalAvailability);
        return professionalAvailabilityMapper.toDto(professionalAvailability);
    }

    /**
     * Partially update a professionalAvailability.
     *
     * @param professionalAvailabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalAvailabilityDTO> partialUpdate(ProfessionalAvailabilityDTO professionalAvailabilityDTO) {
        LOG.debug("Request to partially update ProfessionalAvailability : {}", professionalAvailabilityDTO);

        return professionalAvailabilityRepository
            .findById(professionalAvailabilityDTO.getId())
            .map(existingProfessionalAvailability -> {
                professionalAvailabilityMapper.partialUpdate(existingProfessionalAvailability, professionalAvailabilityDTO);

                return existingProfessionalAvailability;
            })
            .map(professionalAvailabilityRepository::save)
            .map(professionalAvailabilityMapper::toDto);
    }

    /**
     * Get all the professionalAvailabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalAvailabilityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalAvailabilities");
        return professionalAvailabilityRepository.findAll(pageable).map(professionalAvailabilityMapper::toDto);
    }

    /**
     * Get all the professionalAvailabilities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalAvailabilityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalAvailabilityRepository.findAllWithEagerRelationships(pageable).map(professionalAvailabilityMapper::toDto);
    }

    /**
     * Get one professionalAvailability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalAvailabilityDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalAvailability : {}", id);
        return professionalAvailabilityRepository.findOneWithEagerRelationships(id).map(professionalAvailabilityMapper::toDto);
    }

    /**
     * Delete the professionalAvailability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalAvailability : {}", id);
        professionalAvailabilityRepository.deleteById(id);
    }
}
