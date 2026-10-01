package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalSkill;
import com.limitcross.facility.repository.ProfessionalSkillRepository;
import com.limitcross.facility.service.dto.ProfessionalSkillDTO;
import com.limitcross.facility.service.mapper.ProfessionalSkillMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalSkill}.
 */
@Service
@Transactional
public class ProfessionalSkillService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalSkillService.class);

    private final ProfessionalSkillRepository professionalSkillRepository;

    private final ProfessionalSkillMapper professionalSkillMapper;

    public ProfessionalSkillService(
        ProfessionalSkillRepository professionalSkillRepository,
        ProfessionalSkillMapper professionalSkillMapper
    ) {
        this.professionalSkillRepository = professionalSkillRepository;
        this.professionalSkillMapper = professionalSkillMapper;
    }

    /**
     * Save a professionalSkill.
     *
     * @param professionalSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalSkillDTO save(ProfessionalSkillDTO professionalSkillDTO) {
        LOG.debug("Request to save ProfessionalSkill : {}", professionalSkillDTO);
        ProfessionalSkill professionalSkill = professionalSkillMapper.toEntity(professionalSkillDTO);
        professionalSkill = professionalSkillRepository.save(professionalSkill);
        return professionalSkillMapper.toDto(professionalSkill);
    }

    /**
     * Update a professionalSkill.
     *
     * @param professionalSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalSkillDTO update(ProfessionalSkillDTO professionalSkillDTO) {
        LOG.debug("Request to update ProfessionalSkill : {}", professionalSkillDTO);
        ProfessionalSkill professionalSkill = professionalSkillMapper.toEntity(professionalSkillDTO);
        professionalSkill = professionalSkillRepository.save(professionalSkill);
        return professionalSkillMapper.toDto(professionalSkill);
    }

    /**
     * Partially update a professionalSkill.
     *
     * @param professionalSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalSkillDTO> partialUpdate(ProfessionalSkillDTO professionalSkillDTO) {
        LOG.debug("Request to partially update ProfessionalSkill : {}", professionalSkillDTO);

        return professionalSkillRepository
            .findById(professionalSkillDTO.getId())
            .map(existingProfessionalSkill -> {
                professionalSkillMapper.partialUpdate(existingProfessionalSkill, professionalSkillDTO);

                return existingProfessionalSkill;
            })
            .map(professionalSkillRepository::save)
            .map(professionalSkillMapper::toDto);
    }

    /**
     * Get all the professionalSkills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalSkillDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalSkills");
        return professionalSkillRepository.findAll(pageable).map(professionalSkillMapper::toDto);
    }

    /**
     * Get all the professionalSkills with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalSkillDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalSkillRepository.findAllWithEagerRelationships(pageable).map(professionalSkillMapper::toDto);
    }

    /**
     * Get one professionalSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalSkillDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalSkill : {}", id);
        return professionalSkillRepository.findOneWithEagerRelationships(id).map(professionalSkillMapper::toDto);
    }

    /**
     * Delete the professionalSkill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalSkill : {}", id);
        professionalSkillRepository.deleteById(id);
    }
}
