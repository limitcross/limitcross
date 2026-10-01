package com.limitcross.facility.service;

import com.limitcross.facility.domain.Banner;
import com.limitcross.facility.repository.BannerRepository;
import com.limitcross.facility.service.dto.BannerDTO;
import com.limitcross.facility.service.mapper.BannerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.Banner}.
 */
@Service
@Transactional
public class BannerService {

    private static final Logger LOG = LoggerFactory.getLogger(BannerService.class);

    private final BannerRepository bannerRepository;

    private final BannerMapper bannerMapper;

    public BannerService(BannerRepository bannerRepository, BannerMapper bannerMapper) {
        this.bannerRepository = bannerRepository;
        this.bannerMapper = bannerMapper;
    }

    /**
     * Save a banner.
     *
     * @param bannerDTO the entity to save.
     * @return the persisted entity.
     */
    public BannerDTO save(BannerDTO bannerDTO) {
        LOG.debug("Request to save Banner : {}", bannerDTO);
        Banner banner = bannerMapper.toEntity(bannerDTO);
        banner = bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    /**
     * Update a banner.
     *
     * @param bannerDTO the entity to save.
     * @return the persisted entity.
     */
    public BannerDTO update(BannerDTO bannerDTO) {
        LOG.debug("Request to update Banner : {}", bannerDTO);
        Banner banner = bannerMapper.toEntity(bannerDTO);
        banner = bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    /**
     * Partially update a banner.
     *
     * @param bannerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BannerDTO> partialUpdate(BannerDTO bannerDTO) {
        LOG.debug("Request to partially update Banner : {}", bannerDTO);

        return bannerRepository
            .findById(bannerDTO.getId())
            .map(existingBanner -> {
                bannerMapper.partialUpdate(existingBanner, bannerDTO);

                return existingBanner;
            })
            .map(bannerRepository::save)
            .map(bannerMapper::toDto);
    }

    /**
     * Get all the banners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BannerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Banners");
        return bannerRepository.findAll(pageable).map(bannerMapper::toDto);
    }

    /**
     * Get all the banners with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BannerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bannerRepository.findAllWithEagerRelationships(pageable).map(bannerMapper::toDto);
    }

    /**
     * Get one banner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BannerDTO> findOne(Long id) {
        LOG.debug("Request to get Banner : {}", id);
        return bannerRepository.findOneWithEagerRelationships(id).map(bannerMapper::toDto);
    }

    /**
     * Delete the banner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Banner : {}", id);
        bannerRepository.deleteById(id);
    }
}
