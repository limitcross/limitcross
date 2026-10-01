package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalIncentiveAward;
import com.limitcross.facility.repository.ProfessionalIncentiveAwardRepository;
import com.limitcross.facility.service.dto.ProfessionalIncentiveAwardDTO;
import com.limitcross.facility.service.mapper.ProfessionalIncentiveAwardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalIncentiveAward}.
 */
@Service
@Transactional
public class ProfessionalIncentiveAwardService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalIncentiveAwardService.class);

    private final ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepository;

    private final ProfessionalIncentiveAwardMapper professionalIncentiveAwardMapper;

    public ProfessionalIncentiveAwardService(
        ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepository,
        ProfessionalIncentiveAwardMapper professionalIncentiveAwardMapper
    ) {
        this.professionalIncentiveAwardRepository = professionalIncentiveAwardRepository;
        this.professionalIncentiveAwardMapper = professionalIncentiveAwardMapper;
    }

    /**
     * Save a professionalIncentiveAward.
     *
     * @param professionalIncentiveAwardDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalIncentiveAwardDTO save(ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO) {
        LOG.debug("Request to save ProfessionalIncentiveAward : {}", professionalIncentiveAwardDTO);
        ProfessionalIncentiveAward professionalIncentiveAward = professionalIncentiveAwardMapper.toEntity(professionalIncentiveAwardDTO);
        professionalIncentiveAward = professionalIncentiveAwardRepository.save(professionalIncentiveAward);
        return professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);
    }

    /**
     * Update a professionalIncentiveAward.
     *
     * @param professionalIncentiveAwardDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalIncentiveAwardDTO update(ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO) {
        LOG.debug("Request to update ProfessionalIncentiveAward : {}", professionalIncentiveAwardDTO);
        ProfessionalIncentiveAward professionalIncentiveAward = professionalIncentiveAwardMapper.toEntity(professionalIncentiveAwardDTO);
        professionalIncentiveAward = professionalIncentiveAwardRepository.save(professionalIncentiveAward);
        return professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);
    }

    /**
     * Partially update a professionalIncentiveAward.
     *
     * @param professionalIncentiveAwardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalIncentiveAwardDTO> partialUpdate(ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO) {
        LOG.debug("Request to partially update ProfessionalIncentiveAward : {}", professionalIncentiveAwardDTO);

        return professionalIncentiveAwardRepository
            .findById(professionalIncentiveAwardDTO.getId())
            .map(existingProfessionalIncentiveAward -> {
                professionalIncentiveAwardMapper.partialUpdate(existingProfessionalIncentiveAward, professionalIncentiveAwardDTO);

                return existingProfessionalIncentiveAward;
            })
            .map(professionalIncentiveAwardRepository::save)
            .map(professionalIncentiveAwardMapper::toDto);
    }

    /**
     * Get all the professionalIncentiveAwards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalIncentiveAwardDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalIncentiveAwards");
        return professionalIncentiveAwardRepository.findAll(pageable).map(professionalIncentiveAwardMapper::toDto);
    }

    /**
     * Get all the professionalIncentiveAwards with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalIncentiveAwardDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalIncentiveAwardRepository.findAllWithEagerRelationships(pageable).map(professionalIncentiveAwardMapper::toDto);
    }

    /**
     * Get one professionalIncentiveAward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalIncentiveAwardDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalIncentiveAward : {}", id);
        return professionalIncentiveAwardRepository.findOneWithEagerRelationships(id).map(professionalIncentiveAwardMapper::toDto);
    }

    /**
     * Delete the professionalIncentiveAward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalIncentiveAward : {}", id);
        professionalIncentiveAwardRepository.deleteById(id);
    }
}
