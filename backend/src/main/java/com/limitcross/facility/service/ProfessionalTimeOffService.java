package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalTimeOff;
import com.limitcross.facility.repository.ProfessionalTimeOffRepository;
import com.limitcross.facility.service.dto.ProfessionalTimeOffDTO;
import com.limitcross.facility.service.mapper.ProfessionalTimeOffMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalTimeOff}.
 */
@Service
@Transactional
public class ProfessionalTimeOffService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTimeOffService.class);

    private final ProfessionalTimeOffRepository professionalTimeOffRepository;

    private final ProfessionalTimeOffMapper professionalTimeOffMapper;

    public ProfessionalTimeOffService(
        ProfessionalTimeOffRepository professionalTimeOffRepository,
        ProfessionalTimeOffMapper professionalTimeOffMapper
    ) {
        this.professionalTimeOffRepository = professionalTimeOffRepository;
        this.professionalTimeOffMapper = professionalTimeOffMapper;
    }

    /**
     * Save a professionalTimeOff.
     *
     * @param professionalTimeOffDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTimeOffDTO save(ProfessionalTimeOffDTO professionalTimeOffDTO) {
        LOG.debug("Request to save ProfessionalTimeOff : {}", professionalTimeOffDTO);
        ProfessionalTimeOff professionalTimeOff = professionalTimeOffMapper.toEntity(professionalTimeOffDTO);
        professionalTimeOff = professionalTimeOffRepository.save(professionalTimeOff);
        return professionalTimeOffMapper.toDto(professionalTimeOff);
    }

    /**
     * Update a professionalTimeOff.
     *
     * @param professionalTimeOffDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTimeOffDTO update(ProfessionalTimeOffDTO professionalTimeOffDTO) {
        LOG.debug("Request to update ProfessionalTimeOff : {}", professionalTimeOffDTO);
        ProfessionalTimeOff professionalTimeOff = professionalTimeOffMapper.toEntity(professionalTimeOffDTO);
        professionalTimeOff = professionalTimeOffRepository.save(professionalTimeOff);
        return professionalTimeOffMapper.toDto(professionalTimeOff);
    }

    /**
     * Partially update a professionalTimeOff.
     *
     * @param professionalTimeOffDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalTimeOffDTO> partialUpdate(ProfessionalTimeOffDTO professionalTimeOffDTO) {
        LOG.debug("Request to partially update ProfessionalTimeOff : {}", professionalTimeOffDTO);

        return professionalTimeOffRepository
            .findById(professionalTimeOffDTO.getId())
            .map(existingProfessionalTimeOff -> {
                professionalTimeOffMapper.partialUpdate(existingProfessionalTimeOff, professionalTimeOffDTO);

                return existingProfessionalTimeOff;
            })
            .map(professionalTimeOffRepository::save)
            .map(professionalTimeOffMapper::toDto);
    }

    /**
     * Get all the professionalTimeOffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalTimeOffDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalTimeOffs");
        return professionalTimeOffRepository.findAll(pageable).map(professionalTimeOffMapper::toDto);
    }

    /**
     * Get all the professionalTimeOffs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalTimeOffDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalTimeOffRepository.findAllWithEagerRelationships(pageable).map(professionalTimeOffMapper::toDto);
    }

    /**
     * Get one professionalTimeOff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalTimeOffDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalTimeOff : {}", id);
        return professionalTimeOffRepository.findOneWithEagerRelationships(id).map(professionalTimeOffMapper::toDto);
    }

    /**
     * Delete the professionalTimeOff by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalTimeOff : {}", id);
        professionalTimeOffRepository.deleteById(id);
    }
}
