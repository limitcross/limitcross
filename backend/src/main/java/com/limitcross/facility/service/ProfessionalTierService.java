package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.repository.ProfessionalTierRepository;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import com.limitcross.facility.service.mapper.ProfessionalTierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalTier}.
 */
@Service
@Transactional
public class ProfessionalTierService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTierService.class);

    private final ProfessionalTierRepository professionalTierRepository;

    private final ProfessionalTierMapper professionalTierMapper;

    public ProfessionalTierService(ProfessionalTierRepository professionalTierRepository, ProfessionalTierMapper professionalTierMapper) {
        this.professionalTierRepository = professionalTierRepository;
        this.professionalTierMapper = professionalTierMapper;
    }

    /**
     * Save a professionalTier.
     *
     * @param professionalTierDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTierDTO save(ProfessionalTierDTO professionalTierDTO) {
        LOG.debug("Request to save ProfessionalTier : {}", professionalTierDTO);
        ProfessionalTier professionalTier = professionalTierMapper.toEntity(professionalTierDTO);
        professionalTier = professionalTierRepository.save(professionalTier);
        return professionalTierMapper.toDto(professionalTier);
    }

    /**
     * Update a professionalTier.
     *
     * @param professionalTierDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTierDTO update(ProfessionalTierDTO professionalTierDTO) {
        LOG.debug("Request to update ProfessionalTier : {}", professionalTierDTO);
        ProfessionalTier professionalTier = professionalTierMapper.toEntity(professionalTierDTO);
        professionalTier = professionalTierRepository.save(professionalTier);
        return professionalTierMapper.toDto(professionalTier);
    }

    /**
     * Partially update a professionalTier.
     *
     * @param professionalTierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalTierDTO> partialUpdate(ProfessionalTierDTO professionalTierDTO) {
        LOG.debug("Request to partially update ProfessionalTier : {}", professionalTierDTO);

        return professionalTierRepository
            .findById(professionalTierDTO.getId())
            .map(existingProfessionalTier -> {
                professionalTierMapper.partialUpdate(existingProfessionalTier, professionalTierDTO);

                return existingProfessionalTier;
            })
            .map(professionalTierRepository::save)
            .map(professionalTierMapper::toDto);
    }

    /**
     * Get all the professionalTiers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionalTierDTO> findAll() {
        LOG.debug("Request to get all ProfessionalTiers");
        return professionalTierRepository
            .findAll()
            .stream()
            .map(professionalTierMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one professionalTier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalTierDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalTier : {}", id);
        return professionalTierRepository.findById(id).map(professionalTierMapper::toDto);
    }

    /**
     * Delete the professionalTier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalTier : {}", id);
        professionalTierRepository.deleteById(id);
    }
}
