package com.limitcross.facility.service;

import com.limitcross.facility.domain.CouponRedemption;
import com.limitcross.facility.repository.CouponRedemptionRepository;
import com.limitcross.facility.service.dto.CouponRedemptionDTO;
import com.limitcross.facility.service.mapper.CouponRedemptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CouponRedemption}.
 */
@Service
@Transactional
public class CouponRedemptionService {

    private static final Logger LOG = LoggerFactory.getLogger(CouponRedemptionService.class);

    private final CouponRedemptionRepository couponRedemptionRepository;

    private final CouponRedemptionMapper couponRedemptionMapper;

    public CouponRedemptionService(CouponRedemptionRepository couponRedemptionRepository, CouponRedemptionMapper couponRedemptionMapper) {
        this.couponRedemptionRepository = couponRedemptionRepository;
        this.couponRedemptionMapper = couponRedemptionMapper;
    }

    /**
     * Save a couponRedemption.
     *
     * @param couponRedemptionDTO the entity to save.
     * @return the persisted entity.
     */
    public CouponRedemptionDTO save(CouponRedemptionDTO couponRedemptionDTO) {
        LOG.debug("Request to save CouponRedemption : {}", couponRedemptionDTO);
        CouponRedemption couponRedemption = couponRedemptionMapper.toEntity(couponRedemptionDTO);
        couponRedemption = couponRedemptionRepository.save(couponRedemption);
        return couponRedemptionMapper.toDto(couponRedemption);
    }

    /**
     * Update a couponRedemption.
     *
     * @param couponRedemptionDTO the entity to save.
     * @return the persisted entity.
     */
    public CouponRedemptionDTO update(CouponRedemptionDTO couponRedemptionDTO) {
        LOG.debug("Request to update CouponRedemption : {}", couponRedemptionDTO);
        CouponRedemption couponRedemption = couponRedemptionMapper.toEntity(couponRedemptionDTO);
        couponRedemption = couponRedemptionRepository.save(couponRedemption);
        return couponRedemptionMapper.toDto(couponRedemption);
    }

    /**
     * Partially update a couponRedemption.
     *
     * @param couponRedemptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CouponRedemptionDTO> partialUpdate(CouponRedemptionDTO couponRedemptionDTO) {
        LOG.debug("Request to partially update CouponRedemption : {}", couponRedemptionDTO);

        return couponRedemptionRepository
            .findById(couponRedemptionDTO.getId())
            .map(existingCouponRedemption -> {
                couponRedemptionMapper.partialUpdate(existingCouponRedemption, couponRedemptionDTO);

                return existingCouponRedemption;
            })
            .map(couponRedemptionRepository::save)
            .map(couponRedemptionMapper::toDto);
    }

    /**
     * Get all the couponRedemptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CouponRedemptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CouponRedemptions");
        return couponRedemptionRepository.findAll(pageable).map(couponRedemptionMapper::toDto);
    }

    /**
     * Get all the couponRedemptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CouponRedemptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return couponRedemptionRepository.findAllWithEagerRelationships(pageable).map(couponRedemptionMapper::toDto);
    }

    /**
     * Get one couponRedemption by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CouponRedemptionDTO> findOne(Long id) {
        LOG.debug("Request to get CouponRedemption : {}", id);
        return couponRedemptionRepository.findOneWithEagerRelationships(id).map(couponRedemptionMapper::toDto);
    }

    /**
     * Delete the couponRedemption by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CouponRedemption : {}", id);
        couponRedemptionRepository.deleteById(id);
    }
}
