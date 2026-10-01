package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalTierHistory;
import com.limitcross.facility.repository.ProfessionalTierHistoryRepository;
import com.limitcross.facility.service.dto.ProfessionalTierHistoryDTO;
import com.limitcross.facility.service.mapper.ProfessionalTierHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalTierHistory}.
 */
@Service
@Transactional
public class ProfessionalTierHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTierHistoryService.class);

    private final ProfessionalTierHistoryRepository professionalTierHistoryRepository;

    private final ProfessionalTierHistoryMapper professionalTierHistoryMapper;

    public ProfessionalTierHistoryService(
        ProfessionalTierHistoryRepository professionalTierHistoryRepository,
        ProfessionalTierHistoryMapper professionalTierHistoryMapper
    ) {
        this.professionalTierHistoryRepository = professionalTierHistoryRepository;
        this.professionalTierHistoryMapper = professionalTierHistoryMapper;
    }

    /**
     * Save a professionalTierHistory.
     *
     * @param professionalTierHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTierHistoryDTO save(ProfessionalTierHistoryDTO professionalTierHistoryDTO) {
        LOG.debug("Request to save ProfessionalTierHistory : {}", professionalTierHistoryDTO);
        ProfessionalTierHistory professionalTierHistory = professionalTierHistoryMapper.toEntity(professionalTierHistoryDTO);
        professionalTierHistory = professionalTierHistoryRepository.save(professionalTierHistory);
        return professionalTierHistoryMapper.toDto(professionalTierHistory);
    }

    /**
     * Update a professionalTierHistory.
     *
     * @param professionalTierHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalTierHistoryDTO update(ProfessionalTierHistoryDTO professionalTierHistoryDTO) {
        LOG.debug("Request to update ProfessionalTierHistory : {}", professionalTierHistoryDTO);
        ProfessionalTierHistory professionalTierHistory = professionalTierHistoryMapper.toEntity(professionalTierHistoryDTO);
        professionalTierHistory = professionalTierHistoryRepository.save(professionalTierHistory);
        return professionalTierHistoryMapper.toDto(professionalTierHistory);
    }

    /**
     * Partially update a professionalTierHistory.
     *
     * @param professionalTierHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalTierHistoryDTO> partialUpdate(ProfessionalTierHistoryDTO professionalTierHistoryDTO) {
        LOG.debug("Request to partially update ProfessionalTierHistory : {}", professionalTierHistoryDTO);

        return professionalTierHistoryRepository
            .findById(professionalTierHistoryDTO.getId())
            .map(existingProfessionalTierHistory -> {
                professionalTierHistoryMapper.partialUpdate(existingProfessionalTierHistory, professionalTierHistoryDTO);

                return existingProfessionalTierHistory;
            })
            .map(professionalTierHistoryRepository::save)
            .map(professionalTierHistoryMapper::toDto);
    }

    /**
     * Get all the professionalTierHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalTierHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalTierHistories");
        return professionalTierHistoryRepository.findAll(pageable).map(professionalTierHistoryMapper::toDto);
    }

    /**
     * Get all the professionalTierHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalTierHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalTierHistoryRepository.findAllWithEagerRelationships(pageable).map(professionalTierHistoryMapper::toDto);
    }

    /**
     * Get one professionalTierHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalTierHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalTierHistory : {}", id);
        return professionalTierHistoryRepository.findOneWithEagerRelationships(id).map(professionalTierHistoryMapper::toDto);
    }

    /**
     * Delete the professionalTierHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalTierHistory : {}", id);
        professionalTierHistoryRepository.deleteById(id);
    }
}
