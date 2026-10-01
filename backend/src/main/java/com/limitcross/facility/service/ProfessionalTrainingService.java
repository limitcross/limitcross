package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalTraining;
import com.limitcross.facility.repository.ProfessionalTrainingRepository;
import com.limitcross.facility.service.dto.ProfessionalTrainingDTO;
import com.limitcross.facility.service.mapper.ProfessionalTrainingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalTraining}.
 */
@Service
@Transactional
public class ProfessionalTrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTrainingService.class);

    private final ProfessionalTrainingRepository professionalTrainingRepository;

    private final ProfessionalTrainingMapper professionalTrainingMapper;

    public ProfessionalTrainingService(
        ProfessionalTrainingRepository professionalTrainingRepository,
        ProfessionalTrainingMapper professionalTrainingMapper
    ) {
        this.professionalTrainingRepository = professionalTrainingRepository;
        this.professionalTrainingMapper = professionalTrainingMapper;
    }

    /**
     * Save a professionalTraining.
     *
     * @param professionalTrainingDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTrainingDTO save(ProfessionalTrainingDTO professionalTrainingDTO) {
        LOG.debug("Request to save ProfessionalTraining : {}", professionalTrainingDTO);
        ProfessionalTraining professionalTraining = professionalTrainingMapper.toEntity(professionalTrainingDTO);
        professionalTraining = professionalTrainingRepository.save(professionalTraining);
        return professionalTrainingMapper.toDto(professionalTraining);
    }

    /**
     * Update a professionalTraining.
     *
     * @param professionalTrainingDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTrainingDTO update(ProfessionalTrainingDTO professionalTrainingDTO) {
        LOG.debug("Request to update ProfessionalTraining : {}", professionalTrainingDTO);
        ProfessionalTraining professionalTraining = professionalTrainingMapper.toEntity(professionalTrainingDTO);
        professionalTraining = professionalTrainingRepository.save(professionalTraining);
        return professionalTrainingMapper.toDto(professionalTraining);
    }

    /**
     * Partially update a professionalTraining.
     *
     * @param professionalTrainingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalTrainingDTO> partialUpdate(ProfessionalTrainingDTO professionalTrainingDTO) {
        LOG.debug("Request to partially update ProfessionalTraining : {}", professionalTrainingDTO);

        return professionalTrainingRepository
            .findById(professionalTrainingDTO.getId())
            .map(existingProfessionalTraining -> {
                professionalTrainingMapper.partialUpdate(existingProfessionalTraining, professionalTrainingDTO);

                return existingProfessionalTraining;
            })
            .map(professionalTrainingRepository::save)
            .map(professionalTrainingMapper::toDto);
    }

    /**
     * Get all the professionalTrainings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalTrainingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalTrainings");
        return professionalTrainingRepository.findAll(pageable).map(professionalTrainingMapper::toDto);
    }

    /**
     * Get all the professionalTrainings with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalTrainingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalTrainingRepository.findAllWithEagerRelationships(pageable).map(professionalTrainingMapper::toDto);
    }

    /**
     * Get one professionalTraining by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalTrainingDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalTraining : {}", id);
        return professionalTrainingRepository.findOneWithEagerRelationships(id).map(professionalTrainingMapper::toDto);
    }

    /**
     * Delete the professionalTraining by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalTraining : {}", id);
        professionalTrainingRepository.deleteById(id);
    }
}
