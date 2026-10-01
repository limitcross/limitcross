package com.limitcross.facility.service;

import com.limitcross.facility.domain.DeviceToken;
import com.limitcross.facility.repository.DeviceTokenRepository;
import com.limitcross.facility.service.dto.DeviceTokenDTO;
import com.limitcross.facility.service.mapper.DeviceTokenMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.DeviceToken}.
 */
@Service
@Transactional
public class DeviceTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceTokenService.class);

    private final DeviceTokenRepository deviceTokenRepository;

    private final DeviceTokenMapper deviceTokenMapper;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository, DeviceTokenMapper deviceTokenMapper) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.deviceTokenMapper = deviceTokenMapper;
    }

    /**
     * Save a deviceToken.
     *
     * @param deviceTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceTokenDTO save(DeviceTokenDTO deviceTokenDTO) {
        LOG.debug("Request to save DeviceToken : {}", deviceTokenDTO);
        DeviceToken deviceToken = deviceTokenMapper.toEntity(deviceTokenDTO);
        deviceToken = deviceTokenRepository.save(deviceToken);
        return deviceTokenMapper.toDto(deviceToken);
    }

    /**
     * Update a deviceToken.
     *
     * @param deviceTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceTokenDTO update(DeviceTokenDTO deviceTokenDTO) {
        LOG.debug("Request to update DeviceToken : {}", deviceTokenDTO);
        DeviceToken deviceToken = deviceTokenMapper.toEntity(deviceTokenDTO);
        deviceToken = deviceTokenRepository.save(deviceToken);
        return deviceTokenMapper.toDto(deviceToken);
    }

    /**
     * Partially update a deviceToken.
     *
     * @param deviceTokenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeviceTokenDTO> partialUpdate(DeviceTokenDTO deviceTokenDTO) {
        LOG.debug("Request to partially update DeviceToken : {}", deviceTokenDTO);

        return deviceTokenRepository
            .findById(deviceTokenDTO.getId())
            .map(existingDeviceToken -> {
                deviceTokenMapper.partialUpdate(existingDeviceToken, deviceTokenDTO);

                return existingDeviceToken;
            })
            .map(deviceTokenRepository::save)
            .map(deviceTokenMapper::toDto);
    }

    /**
     * Get all the deviceTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceTokenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DeviceTokens");
        return deviceTokenRepository.findAll(pageable).map(deviceTokenMapper::toDto);
    }

    /**
     * Get all the deviceTokens with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DeviceTokenDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deviceTokenRepository.findAllWithEagerRelationships(pageable).map(deviceTokenMapper::toDto);
    }

    /**
     * Get one deviceToken by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceTokenDTO> findOne(Long id) {
        LOG.debug("Request to get DeviceToken : {}", id);
        return deviceTokenRepository.findOneWithEagerRelationships(id).map(deviceTokenMapper::toDto);
    }

    /**
     * Delete the deviceToken by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DeviceToken : {}", id);
        deviceTokenRepository.deleteById(id);
    }
}
