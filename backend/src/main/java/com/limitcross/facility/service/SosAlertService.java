package com.limitcross.facility.service;

import com.limitcross.facility.domain.SosAlert;
import com.limitcross.facility.repository.SosAlertRepository;
import com.limitcross.facility.service.dto.SosAlertDTO;
import com.limitcross.facility.service.mapper.SosAlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.SosAlert}.
 */
@Service
@Transactional
public class SosAlertService {

    private static final Logger LOG = LoggerFactory.getLogger(SosAlertService.class);

    private final SosAlertRepository sosAlertRepository;

    private final SosAlertMapper sosAlertMapper;

    public SosAlertService(SosAlertRepository sosAlertRepository, SosAlertMapper sosAlertMapper) {
        this.sosAlertRepository = sosAlertRepository;
        this.sosAlertMapper = sosAlertMapper;
    }

    /**
     * Save a sosAlert.
     *
     * @param sosAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public SosAlertDTO save(SosAlertDTO sosAlertDTO) {
        LOG.debug("Request to save SosAlert : {}", sosAlertDTO);
        SosAlert sosAlert = sosAlertMapper.toEntity(sosAlertDTO);
        sosAlert = sosAlertRepository.save(sosAlert);
        return sosAlertMapper.toDto(sosAlert);
    }

    /**
     * Update a sosAlert.
     *
     * @param sosAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public SosAlertDTO update(SosAlertDTO sosAlertDTO) {
        LOG.debug("Request to update SosAlert : {}", sosAlertDTO);
        SosAlert sosAlert = sosAlertMapper.toEntity(sosAlertDTO);
        sosAlert = sosAlertRepository.save(sosAlert);
        return sosAlertMapper.toDto(sosAlert);
    }

    /**
     * Partially update a sosAlert.
     *
     * @param sosAlertDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SosAlertDTO> partialUpdate(SosAlertDTO sosAlertDTO) {
        LOG.debug("Request to partially update SosAlert : {}", sosAlertDTO);

        return sosAlertRepository
            .findById(sosAlertDTO.getId())
            .map(existingSosAlert -> {
                sosAlertMapper.partialUpdate(existingSosAlert, sosAlertDTO);

                return existingSosAlert;
            })
            .map(sosAlertRepository::save)
            .map(sosAlertMapper::toDto);
    }

    /**
     * Get all the sosAlerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SosAlertDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SosAlerts");
        return sosAlertRepository.findAll(pageable).map(sosAlertMapper::toDto);
    }

    /**
     * Get all the sosAlerts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SosAlertDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sosAlertRepository.findAllWithEagerRelationships(pageable).map(sosAlertMapper::toDto);
    }

    /**
     * Get one sosAlert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SosAlertDTO> findOne(Long id) {
        LOG.debug("Request to get SosAlert : {}", id);
        return sosAlertRepository.findOneWithEagerRelationships(id).map(sosAlertMapper::toDto);
    }

    /**
     * Delete the sosAlert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SosAlert : {}", id);
        sosAlertRepository.deleteById(id);
    }
}
