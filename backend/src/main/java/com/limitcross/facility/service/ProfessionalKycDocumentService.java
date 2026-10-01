package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalKycDocument;
import com.limitcross.facility.repository.ProfessionalKycDocumentRepository;
import com.limitcross.facility.service.dto.ProfessionalKycDocumentDTO;
import com.limitcross.facility.service.mapper.ProfessionalKycDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalKycDocument}.
 */
@Service
@Transactional
public class ProfessionalKycDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalKycDocumentService.class);

    private final ProfessionalKycDocumentRepository professionalKycDocumentRepository;

    private final ProfessionalKycDocumentMapper professionalKycDocumentMapper;

    public ProfessionalKycDocumentService(
        ProfessionalKycDocumentRepository professionalKycDocumentRepository,
        ProfessionalKycDocumentMapper professionalKycDocumentMapper
    ) {
        this.professionalKycDocumentRepository = professionalKycDocumentRepository;
        this.professionalKycDocumentMapper = professionalKycDocumentMapper;
    }

    /**
     * Save a professionalKycDocument.
     *
     * @param professionalKycDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalKycDocumentDTO save(ProfessionalKycDocumentDTO professionalKycDocumentDTO) {
        LOG.debug("Request to save ProfessionalKycDocument : {}", professionalKycDocumentDTO);
        ProfessionalKycDocument professionalKycDocument = professionalKycDocumentMapper.toEntity(professionalKycDocumentDTO);
        professionalKycDocument = professionalKycDocumentRepository.save(professionalKycDocument);
        return professionalKycDocumentMapper.toDto(professionalKycDocument);
    }

    /**
     * Update a professionalKycDocument.
     *
     * @param professionalKycDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalKycDocumentDTO update(ProfessionalKycDocumentDTO professionalKycDocumentDTO) {
        LOG.debug("Request to update ProfessionalKycDocument : {}", professionalKycDocumentDTO);
        ProfessionalKycDocument professionalKycDocument = professionalKycDocumentMapper.toEntity(professionalKycDocumentDTO);
        professionalKycDocument = professionalKycDocumentRepository.save(professionalKycDocument);
        return professionalKycDocumentMapper.toDto(professionalKycDocument);
    }

    /**
     * Partially update a professionalKycDocument.
     *
     * @param professionalKycDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalKycDocumentDTO> partialUpdate(ProfessionalKycDocumentDTO professionalKycDocumentDTO) {
        LOG.debug("Request to partially update ProfessionalKycDocument : {}", professionalKycDocumentDTO);

        return professionalKycDocumentRepository
            .findById(professionalKycDocumentDTO.getId())
            .map(existingProfessionalKycDocument -> {
                professionalKycDocumentMapper.partialUpdate(existingProfessionalKycDocument, professionalKycDocumentDTO);

                return existingProfessionalKycDocument;
            })
            .map(professionalKycDocumentRepository::save)
            .map(professionalKycDocumentMapper::toDto);
    }

    /**
     * Get all the professionalKycDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalKycDocumentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalKycDocuments");
        return professionalKycDocumentRepository.findAll(pageable).map(professionalKycDocumentMapper::toDto);
    }

    /**
     * Get all the professionalKycDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalKycDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalKycDocumentRepository.findAllWithEagerRelationships(pageable).map(professionalKycDocumentMapper::toDto);
    }

    /**
     * Get one professionalKycDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalKycDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalKycDocument : {}", id);
        return professionalKycDocumentRepository.findOneWithEagerRelationships(id).map(professionalKycDocumentMapper::toDto);
    }

    /**
     * Delete the professionalKycDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalKycDocument : {}", id);
        professionalKycDocumentRepository.deleteById(id);
    }
}
