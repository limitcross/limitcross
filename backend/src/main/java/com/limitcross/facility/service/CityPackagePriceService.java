package com.limitcross.facility.service;

import com.limitcross.facility.domain.CityPackagePrice;
import com.limitcross.facility.repository.CityPackagePriceRepository;
import com.limitcross.facility.service.dto.CityPackagePriceDTO;
import com.limitcross.facility.service.mapper.CityPackagePriceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CityPackagePrice}.
 */
@Service
@Transactional
public class CityPackagePriceService {

    private static final Logger LOG = LoggerFactory.getLogger(CityPackagePriceService.class);

    private final CityPackagePriceRepository cityPackagePriceRepository;

    private final CityPackagePriceMapper cityPackagePriceMapper;

    public CityPackagePriceService(CityPackagePriceRepository cityPackagePriceRepository, CityPackagePriceMapper cityPackagePriceMapper) {
        this.cityPackagePriceRepository = cityPackagePriceRepository;
        this.cityPackagePriceMapper = cityPackagePriceMapper;
    }

    /**
     * Save a cityPackagePrice.
     *
     * @param cityPackagePriceDTO the entity to save.
     * @return the persisted entity.
     */
    public CityPackagePriceDTO save(CityPackagePriceDTO cityPackagePriceDTO) {
        LOG.debug("Request to save CityPackagePrice : {}", cityPackagePriceDTO);
        CityPackagePrice cityPackagePrice = cityPackagePriceMapper.toEntity(cityPackagePriceDTO);
        cityPackagePrice = cityPackagePriceRepository.save(cityPackagePrice);
        return cityPackagePriceMapper.toDto(cityPackagePrice);
    }

    /**
     * Update a cityPackagePrice.
     *
     * @param cityPackagePriceDTO the entity to save.
     * @return the persisted entity.
     */
    public CityPackagePriceDTO update(CityPackagePriceDTO cityPackagePriceDTO) {
        LOG.debug("Request to update CityPackagePrice : {}", cityPackagePriceDTO);
        CityPackagePrice cityPackagePrice = cityPackagePriceMapper.toEntity(cityPackagePriceDTO);
        cityPackagePrice = cityPackagePriceRepository.save(cityPackagePrice);
        return cityPackagePriceMapper.toDto(cityPackagePrice);
    }

    /**
     * Partially update a cityPackagePrice.
     *
     * @param cityPackagePriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CityPackagePriceDTO> partialUpdate(CityPackagePriceDTO cityPackagePriceDTO) {
        LOG.debug("Request to partially update CityPackagePrice : {}", cityPackagePriceDTO);

        return cityPackagePriceRepository
            .findById(cityPackagePriceDTO.getId())
            .map(existingCityPackagePrice -> {
                cityPackagePriceMapper.partialUpdate(existingCityPackagePrice, cityPackagePriceDTO);

                return existingCityPackagePrice;
            })
            .map(cityPackagePriceRepository::save)
            .map(cityPackagePriceMapper::toDto);
    }

    /**
     * Get all the cityPackagePrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CityPackagePriceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CityPackagePrices");
        return cityPackagePriceRepository.findAll(pageable).map(cityPackagePriceMapper::toDto);
    }

    /**
     * Get all the cityPackagePrices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CityPackagePriceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cityPackagePriceRepository.findAllWithEagerRelationships(pageable).map(cityPackagePriceMapper::toDto);
    }

    /**
     * Get one cityPackagePrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CityPackagePriceDTO> findOne(Long id) {
        LOG.debug("Request to get CityPackagePrice : {}", id);
        return cityPackagePriceRepository.findOneWithEagerRelationships(id).map(cityPackagePriceMapper::toDto);
    }

    /**
     * Delete the cityPackagePrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CityPackagePrice : {}", id);
        cityPackagePriceRepository.deleteById(id);
    }
}
