package com.limitcross.facility.service;

import com.limitcross.facility.domain.CityDailyMetrics;
import com.limitcross.facility.repository.CityDailyMetricsRepository;
import com.limitcross.facility.service.dto.CityDailyMetricsDTO;
import com.limitcross.facility.service.mapper.CityDailyMetricsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CityDailyMetrics}.
 */
@Service
@Transactional
public class CityDailyMetricsService {

    private static final Logger LOG = LoggerFactory.getLogger(CityDailyMetricsService.class);

    private final CityDailyMetricsRepository cityDailyMetricsRepository;

    private final CityDailyMetricsMapper cityDailyMetricsMapper;

    public CityDailyMetricsService(CityDailyMetricsRepository cityDailyMetricsRepository, CityDailyMetricsMapper cityDailyMetricsMapper) {
        this.cityDailyMetricsRepository = cityDailyMetricsRepository;
        this.cityDailyMetricsMapper = cityDailyMetricsMapper;
    }

    /**
     * Save a cityDailyMetrics.
     *
     * @param cityDailyMetricsDTO the entity to save.
     * @return the persisted entity.
     */
    public CityDailyMetricsDTO save(CityDailyMetricsDTO cityDailyMetricsDTO) {
        LOG.debug("Request to save CityDailyMetrics : {}", cityDailyMetricsDTO);
        CityDailyMetrics cityDailyMetrics = cityDailyMetricsMapper.toEntity(cityDailyMetricsDTO);
        cityDailyMetrics = cityDailyMetricsRepository.save(cityDailyMetrics);
        return cityDailyMetricsMapper.toDto(cityDailyMetrics);
    }

    /**
     * Update a cityDailyMetrics.
     *
     * @param cityDailyMetricsDTO the entity to save.
     * @return the persisted entity.
     */
    public CityDailyMetricsDTO update(CityDailyMetricsDTO cityDailyMetricsDTO) {
        LOG.debug("Request to update CityDailyMetrics : {}", cityDailyMetricsDTO);
        CityDailyMetrics cityDailyMetrics = cityDailyMetricsMapper.toEntity(cityDailyMetricsDTO);
        cityDailyMetrics = cityDailyMetricsRepository.save(cityDailyMetrics);
        return cityDailyMetricsMapper.toDto(cityDailyMetrics);
    }

    /**
     * Partially update a cityDailyMetrics.
     *
     * @param cityDailyMetricsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CityDailyMetricsDTO> partialUpdate(CityDailyMetricsDTO cityDailyMetricsDTO) {
        LOG.debug("Request to partially update CityDailyMetrics : {}", cityDailyMetricsDTO);

        return cityDailyMetricsRepository
            .findById(cityDailyMetricsDTO.getId())
            .map(existingCityDailyMetrics -> {
                cityDailyMetricsMapper.partialUpdate(existingCityDailyMetrics, cityDailyMetricsDTO);

                return existingCityDailyMetrics;
            })
            .map(cityDailyMetricsRepository::save)
            .map(cityDailyMetricsMapper::toDto);
    }

    /**
     * Get all the cityDailyMetrics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CityDailyMetricsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CityDailyMetrics");
        return cityDailyMetricsRepository.findAll(pageable).map(cityDailyMetricsMapper::toDto);
    }

    /**
     * Get all the cityDailyMetrics with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CityDailyMetricsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cityDailyMetricsRepository.findAllWithEagerRelationships(pageable).map(cityDailyMetricsMapper::toDto);
    }

    /**
     * Get one cityDailyMetrics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CityDailyMetricsDTO> findOne(Long id) {
        LOG.debug("Request to get CityDailyMetrics : {}", id);
        return cityDailyMetricsRepository.findOneWithEagerRelationships(id).map(cityDailyMetricsMapper::toDto);
    }

    /**
     * Delete the cityDailyMetrics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CityDailyMetrics : {}", id);
        cityDailyMetricsRepository.deleteById(id);
    }
}
