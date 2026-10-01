package com.limitcross.facility.service;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.repository.CityRepository;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.mapper.CityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.City}.
 */
@Service
@Transactional
public class CityService {

    private static final Logger LOG = LoggerFactory.getLogger(CityService.class);

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    public CityDTO save(CityDTO cityDTO) {
        LOG.debug("Request to save City : {}", cityDTO);
        City city = cityMapper.toEntity(cityDTO);
        city = cityRepository.save(city);
        return cityMapper.toDto(city);
    }

    /**
     * Update a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    public CityDTO update(CityDTO cityDTO) {
        LOG.debug("Request to update City : {}", cityDTO);
        City city = cityMapper.toEntity(cityDTO);
        city = cityRepository.save(city);
        return cityMapper.toDto(city);
    }

    /**
     * Partially update a city.
     *
     * @param cityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CityDTO> partialUpdate(CityDTO cityDTO) {
        LOG.debug("Request to partially update City : {}", cityDTO);

        return cityRepository
            .findById(cityDTO.getId())
            .map(existingCity -> {
                cityMapper.partialUpdate(existingCity, cityDTO);

                return existingCity;
            })
            .map(cityRepository::save)
            .map(cityMapper::toDto);
    }

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cities");
        return cityRepository.findAll(pageable).map(cityMapper::toDto);
    }

    /**
     * Get one city by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CityDTO> findOne(Long id) {
        LOG.debug("Request to get City : {}", id);
        return cityRepository.findById(id).map(cityMapper::toDto);
    }

    /**
     * Delete the city by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete City : {}", id);
        cityRepository.deleteById(id);
    }
}
