package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalLocationLog;
import com.limitcross.facility.repository.ProfessionalLocationLogRepository;
import com.limitcross.facility.service.dto.ProfessionalLocationLogDTO;
import com.limitcross.facility.service.mapper.ProfessionalLocationLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalLocationLog}.
 */
@Service
@Transactional
public class ProfessionalLocationLogService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalLocationLogService.class);

    private final ProfessionalLocationLogRepository professionalLocationLogRepository;

    private final ProfessionalLocationLogMapper professionalLocationLogMapper;

    public ProfessionalLocationLogService(
        ProfessionalLocationLogRepository professionalLocationLogRepository,
        ProfessionalLocationLogMapper professionalLocationLogMapper
    ) {
        this.professionalLocationLogRepository = professionalLocationLogRepository;
        this.professionalLocationLogMapper = professionalLocationLogMapper;
    }

    /**
     * Save a professionalLocationLog.
     *
     * @param professionalLocationLogDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalLocationLogDTO save(ProfessionalLocationLogDTO professionalLocationLogDTO) {
        LOG.debug("Request to save ProfessionalLocationLog : {}", professionalLocationLogDTO);
        ProfessionalLocationLog professionalLocationLog = professionalLocationLogMapper.toEntity(professionalLocationLogDTO);
        professionalLocationLog = professionalLocationLogRepository.save(professionalLocationLog);
        return professionalLocationLogMapper.toDto(professionalLocationLog);
    }

    /**
     * Update a professionalLocationLog.
     *
     * @param professionalLocationLogDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalLocationLogDTO update(ProfessionalLocationLogDTO professionalLocationLogDTO) {
        LOG.debug("Request to update ProfessionalLocationLog : {}", professionalLocationLogDTO);
        ProfessionalLocationLog professionalLocationLog = professionalLocationLogMapper.toEntity(professionalLocationLogDTO);
        professionalLocationLog = professionalLocationLogRepository.save(professionalLocationLog);
        return professionalLocationLogMapper.toDto(professionalLocationLog);
    }

    /**
     * Partially update a professionalLocationLog.
     *
     * @param professionalLocationLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalLocationLogDTO> partialUpdate(ProfessionalLocationLogDTO professionalLocationLogDTO) {
        LOG.debug("Request to partially update ProfessionalLocationLog : {}", professionalLocationLogDTO);

        return professionalLocationLogRepository
            .findById(professionalLocationLogDTO.getId())
            .map(existingProfessionalLocationLog -> {
                professionalLocationLogMapper.partialUpdate(existingProfessionalLocationLog, professionalLocationLogDTO);

                return existingProfessionalLocationLog;
            })
            .map(professionalLocationLogRepository::save)
            .map(professionalLocationLogMapper::toDto);
    }

    /**
     * Get all the professionalLocationLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalLocationLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalLocationLogs");
        return professionalLocationLogRepository.findAll(pageable).map(professionalLocationLogMapper::toDto);
    }

    /**
     * Get one professionalLocationLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalLocationLogDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalLocationLog : {}", id);
        return professionalLocationLogRepository.findById(id).map(professionalLocationLogMapper::toDto);
    }

    /**
     * Delete the professionalLocationLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalLocationLog : {}", id);
        professionalLocationLogRepository.deleteById(id);
    }
}
