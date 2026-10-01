package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalDailyMetrics;
import com.limitcross.facility.repository.ProfessionalDailyMetricsRepository;
import com.limitcross.facility.service.dto.ProfessionalDailyMetricsDTO;
import com.limitcross.facility.service.mapper.ProfessionalDailyMetricsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalDailyMetrics}.
 */
@Service
@Transactional
public class ProfessionalDailyMetricsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalDailyMetricsService.class);

    private final ProfessionalDailyMetricsRepository professionalDailyMetricsRepository;

    private final ProfessionalDailyMetricsMapper professionalDailyMetricsMapper;

    public ProfessionalDailyMetricsService(
        ProfessionalDailyMetricsRepository professionalDailyMetricsRepository,
        ProfessionalDailyMetricsMapper professionalDailyMetricsMapper
    ) {
        this.professionalDailyMetricsRepository = professionalDailyMetricsRepository;
        this.professionalDailyMetricsMapper = professionalDailyMetricsMapper;
    }

    /**
     * Save a professionalDailyMetrics.
     *
     * @param professionalDailyMetricsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalDailyMetricsDTO save(ProfessionalDailyMetricsDTO professionalDailyMetricsDTO) {
        LOG.debug("Request to save ProfessionalDailyMetrics : {}", professionalDailyMetricsDTO);
        ProfessionalDailyMetrics professionalDailyMetrics = professionalDailyMetricsMapper.toEntity(professionalDailyMetricsDTO);
        professionalDailyMetrics = professionalDailyMetricsRepository.save(professionalDailyMetrics);
        return professionalDailyMetricsMapper.toDto(professionalDailyMetrics);
    }

    /**
     * Update a professionalDailyMetrics.
     *
     * @param professionalDailyMetricsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalDailyMetricsDTO update(ProfessionalDailyMetricsDTO professionalDailyMetricsDTO) {
        LOG.debug("Request to update ProfessionalDailyMetrics : {}", professionalDailyMetricsDTO);
        ProfessionalDailyMetrics professionalDailyMetrics = professionalDailyMetricsMapper.toEntity(professionalDailyMetricsDTO);
        professionalDailyMetrics = professionalDailyMetricsRepository.save(professionalDailyMetrics);
        return professionalDailyMetricsMapper.toDto(professionalDailyMetrics);
    }

    /**
     * Partially update a professionalDailyMetrics.
     *
     * @param professionalDailyMetricsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalDailyMetricsDTO> partialUpdate(ProfessionalDailyMetricsDTO professionalDailyMetricsDTO) {
        LOG.debug("Request to partially update ProfessionalDailyMetrics : {}", professionalDailyMetricsDTO);

        return professionalDailyMetricsRepository
            .findById(professionalDailyMetricsDTO.getId())
            .map(existingProfessionalDailyMetrics -> {
                professionalDailyMetricsMapper.partialUpdate(existingProfessionalDailyMetrics, professionalDailyMetricsDTO);

                return existingProfessionalDailyMetrics;
            })
            .map(professionalDailyMetricsRepository::save)
            .map(professionalDailyMetricsMapper::toDto);
    }

    /**
     * Get all the professionalDailyMetrics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDailyMetricsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalDailyMetrics");
        return professionalDailyMetricsRepository.findAll(pageable).map(professionalDailyMetricsMapper::toDto);
    }

    /**
     * Get all the professionalDailyMetrics with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalDailyMetricsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalDailyMetricsRepository.findAllWithEagerRelationships(pageable).map(professionalDailyMetricsMapper::toDto);
    }

    /**
     * Get one professionalDailyMetrics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalDailyMetricsDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalDailyMetrics : {}", id);
        return professionalDailyMetricsRepository.findOneWithEagerRelationships(id).map(professionalDailyMetricsMapper::toDto);
    }

    /**
     * Delete the professionalDailyMetrics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalDailyMetrics : {}", id);
        professionalDailyMetricsRepository.deleteById(id);
    }
}
