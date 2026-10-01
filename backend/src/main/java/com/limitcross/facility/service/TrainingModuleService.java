package com.limitcross.facility.service;

import com.limitcross.facility.domain.TrainingModule;
import com.limitcross.facility.repository.TrainingModuleRepository;
import com.limitcross.facility.service.dto.TrainingModuleDTO;
import com.limitcross.facility.service.mapper.TrainingModuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.TrainingModule}.
 */
@Service
@Transactional
public class TrainingModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingModuleService.class);

    private final TrainingModuleRepository trainingModuleRepository;

    private final TrainingModuleMapper trainingModuleMapper;

    public TrainingModuleService(TrainingModuleRepository trainingModuleRepository, TrainingModuleMapper trainingModuleMapper) {
        this.trainingModuleRepository = trainingModuleRepository;
        this.trainingModuleMapper = trainingModuleMapper;
    }

    /**
     * Save a trainingModule.
     *
     * @param trainingModuleDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingModuleDTO save(TrainingModuleDTO trainingModuleDTO) {
        LOG.debug("Request to save TrainingModule : {}", trainingModuleDTO);
        TrainingModule trainingModule = trainingModuleMapper.toEntity(trainingModuleDTO);
        trainingModule = trainingModuleRepository.save(trainingModule);
        return trainingModuleMapper.toDto(trainingModule);
    }

    /**
     * Update a trainingModule.
     *
     * @param trainingModuleDTO the entity to save.
     * @return the persisted entity.
     */
    public TrainingModuleDTO update(TrainingModuleDTO trainingModuleDTO) {
        LOG.debug("Request to update TrainingModule : {}", trainingModuleDTO);
        TrainingModule trainingModule = trainingModuleMapper.toEntity(trainingModuleDTO);
        trainingModule = trainingModuleRepository.save(trainingModule);
        return trainingModuleMapper.toDto(trainingModule);
    }

    /**
     * Partially update a trainingModule.
     *
     * @param trainingModuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrainingModuleDTO> partialUpdate(TrainingModuleDTO trainingModuleDTO) {
        LOG.debug("Request to partially update TrainingModule : {}", trainingModuleDTO);

        return trainingModuleRepository
            .findById(trainingModuleDTO.getId())
            .map(existingTrainingModule -> {
                trainingModuleMapper.partialUpdate(existingTrainingModule, trainingModuleDTO);

                return existingTrainingModule;
            })
            .map(trainingModuleRepository::save)
            .map(trainingModuleMapper::toDto);
    }

    /**
     * Get all the trainingModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingModuleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TrainingModules");
        return trainingModuleRepository.findAll(pageable).map(trainingModuleMapper::toDto);
    }

    /**
     * Get all the trainingModules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrainingModuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trainingModuleRepository.findAllWithEagerRelationships(pageable).map(trainingModuleMapper::toDto);
    }

    /**
     * Get one trainingModule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrainingModuleDTO> findOne(Long id) {
        LOG.debug("Request to get TrainingModule : {}", id);
        return trainingModuleRepository.findOneWithEagerRelationships(id).map(trainingModuleMapper::toDto);
    }

    /**
     * Delete the trainingModule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrainingModule : {}", id);
        trainingModuleRepository.deleteById(id);
    }
}
