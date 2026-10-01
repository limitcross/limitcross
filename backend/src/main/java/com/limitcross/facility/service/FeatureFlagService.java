package com.limitcross.facility.service;

import com.limitcross.facility.domain.FeatureFlag;
import com.limitcross.facility.repository.FeatureFlagRepository;
import com.limitcross.facility.service.dto.FeatureFlagDTO;
import com.limitcross.facility.service.mapper.FeatureFlagMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.FeatureFlag}.
 */
@Service
@Transactional
public class FeatureFlagService {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagService.class);

    private final FeatureFlagRepository featureFlagRepository;

    private final FeatureFlagMapper featureFlagMapper;

    public FeatureFlagService(FeatureFlagRepository featureFlagRepository, FeatureFlagMapper featureFlagMapper) {
        this.featureFlagRepository = featureFlagRepository;
        this.featureFlagMapper = featureFlagMapper;
    }

    /**
     * Save a featureFlag.
     *
     * @param featureFlagDTO the entity to save.
     * @return the persisted entity.
     */
    public FeatureFlagDTO save(FeatureFlagDTO featureFlagDTO) {
        LOG.debug("Request to save FeatureFlag : {}", featureFlagDTO);
        FeatureFlag featureFlag = featureFlagMapper.toEntity(featureFlagDTO);
        featureFlag = featureFlagRepository.save(featureFlag);
        return featureFlagMapper.toDto(featureFlag);
    }

    /**
     * Update a featureFlag.
     *
     * @param featureFlagDTO the entity to save.
     * @return the persisted entity.
     */
    public FeatureFlagDTO update(FeatureFlagDTO featureFlagDTO) {
        LOG.debug("Request to update FeatureFlag : {}", featureFlagDTO);
        FeatureFlag featureFlag = featureFlagMapper.toEntity(featureFlagDTO);
        featureFlag = featureFlagRepository.save(featureFlag);
        return featureFlagMapper.toDto(featureFlag);
    }

    /**
     * Partially update a featureFlag.
     *
     * @param featureFlagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeatureFlagDTO> partialUpdate(FeatureFlagDTO featureFlagDTO) {
        LOG.debug("Request to partially update FeatureFlag : {}", featureFlagDTO);

        return featureFlagRepository
            .findById(featureFlagDTO.getId())
            .map(existingFeatureFlag -> {
                featureFlagMapper.partialUpdate(existingFeatureFlag, featureFlagDTO);

                return existingFeatureFlag;
            })
            .map(featureFlagRepository::save)
            .map(featureFlagMapper::toDto);
    }

    /**
     * Get all the featureFlags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FeatureFlagDTO> findAll() {
        LOG.debug("Request to get all FeatureFlags");
        return featureFlagRepository.findAll().stream().map(featureFlagMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one featureFlag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeatureFlagDTO> findOne(Long id) {
        LOG.debug("Request to get FeatureFlag : {}", id);
        return featureFlagRepository.findById(id).map(featureFlagMapper::toDto);
    }

    /**
     * Delete the featureFlag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FeatureFlag : {}", id);
        featureFlagRepository.deleteById(id);
    }
}
