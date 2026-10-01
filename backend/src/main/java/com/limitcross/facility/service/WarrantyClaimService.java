package com.limitcross.facility.service;

import com.limitcross.facility.domain.WarrantyClaim;
import com.limitcross.facility.repository.WarrantyClaimRepository;
import com.limitcross.facility.service.dto.WarrantyClaimDTO;
import com.limitcross.facility.service.mapper.WarrantyClaimMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.WarrantyClaim}.
 */
@Service
@Transactional
public class WarrantyClaimService {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyClaimService.class);

    private final WarrantyClaimRepository warrantyClaimRepository;

    private final WarrantyClaimMapper warrantyClaimMapper;

    public WarrantyClaimService(WarrantyClaimRepository warrantyClaimRepository, WarrantyClaimMapper warrantyClaimMapper) {
        this.warrantyClaimRepository = warrantyClaimRepository;
        this.warrantyClaimMapper = warrantyClaimMapper;
    }

    /**
     * Save a warrantyClaim.
     *
     * @param warrantyClaimDTO the entity to save.
     * @return the persisted entity.
     */
    public WarrantyClaimDTO save(WarrantyClaimDTO warrantyClaimDTO) {
        LOG.debug("Request to save WarrantyClaim : {}", warrantyClaimDTO);
        WarrantyClaim warrantyClaim = warrantyClaimMapper.toEntity(warrantyClaimDTO);
        warrantyClaim = warrantyClaimRepository.save(warrantyClaim);
        return warrantyClaimMapper.toDto(warrantyClaim);
    }

    /**
     * Update a warrantyClaim.
     *
     * @param warrantyClaimDTO the entity to save.
     * @return the persisted entity.
     */
    public WarrantyClaimDTO update(WarrantyClaimDTO warrantyClaimDTO) {
        LOG.debug("Request to update WarrantyClaim : {}", warrantyClaimDTO);
        WarrantyClaim warrantyClaim = warrantyClaimMapper.toEntity(warrantyClaimDTO);
        warrantyClaim = warrantyClaimRepository.save(warrantyClaim);
        return warrantyClaimMapper.toDto(warrantyClaim);
    }

    /**
     * Partially update a warrantyClaim.
     *
     * @param warrantyClaimDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WarrantyClaimDTO> partialUpdate(WarrantyClaimDTO warrantyClaimDTO) {
        LOG.debug("Request to partially update WarrantyClaim : {}", warrantyClaimDTO);

        return warrantyClaimRepository
            .findById(warrantyClaimDTO.getId())
            .map(existingWarrantyClaim -> {
                warrantyClaimMapper.partialUpdate(existingWarrantyClaim, warrantyClaimDTO);

                return existingWarrantyClaim;
            })
            .map(warrantyClaimRepository::save)
            .map(warrantyClaimMapper::toDto);
    }

    /**
     * Get all the warrantyClaims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WarrantyClaimDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WarrantyClaims");
        return warrantyClaimRepository.findAll(pageable).map(warrantyClaimMapper::toDto);
    }

    /**
     * Get all the warrantyClaims with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WarrantyClaimDTO> findAllWithEagerRelationships(Pageable pageable) {
        return warrantyClaimRepository.findAllWithEagerRelationships(pageable).map(warrantyClaimMapper::toDto);
    }

    /**
     * Get one warrantyClaim by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WarrantyClaimDTO> findOne(Long id) {
        LOG.debug("Request to get WarrantyClaim : {}", id);
        return warrantyClaimRepository.findOneWithEagerRelationships(id).map(warrantyClaimMapper::toDto);
    }

    /**
     * Delete the warrantyClaim by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WarrantyClaim : {}", id);
        warrantyClaimRepository.deleteById(id);
    }
}
