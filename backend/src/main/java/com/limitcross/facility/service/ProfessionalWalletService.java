package com.limitcross.facility.service;

import com.limitcross.facility.domain.ProfessionalWallet;
import com.limitcross.facility.repository.ProfessionalWalletRepository;
import com.limitcross.facility.service.dto.ProfessionalWalletDTO;
import com.limitcross.facility.service.mapper.ProfessionalWalletMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ProfessionalWallet}.
 */
@Service
@Transactional
public class ProfessionalWalletService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalWalletService.class);

    private final ProfessionalWalletRepository professionalWalletRepository;

    private final ProfessionalWalletMapper professionalWalletMapper;

    public ProfessionalWalletService(
        ProfessionalWalletRepository professionalWalletRepository,
        ProfessionalWalletMapper professionalWalletMapper
    ) {
        this.professionalWalletRepository = professionalWalletRepository;
        this.professionalWalletMapper = professionalWalletMapper;
    }

    /**
     * Save a professionalWallet.
     *
     * @param professionalWalletDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalWalletDTO save(ProfessionalWalletDTO professionalWalletDTO) {
        LOG.debug("Request to save ProfessionalWallet : {}", professionalWalletDTO);
        ProfessionalWallet professionalWallet = professionalWalletMapper.toEntity(professionalWalletDTO);
        professionalWallet = professionalWalletRepository.save(professionalWallet);
        return professionalWalletMapper.toDto(professionalWallet);
    }

    /**
     * Update a professionalWallet.
     *
     * @param professionalWalletDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalWalletDTO update(ProfessionalWalletDTO professionalWalletDTO) {
        LOG.debug("Request to update ProfessionalWallet : {}", professionalWalletDTO);
        ProfessionalWallet professionalWallet = professionalWalletMapper.toEntity(professionalWalletDTO);
        professionalWallet = professionalWalletRepository.save(professionalWallet);
        return professionalWalletMapper.toDto(professionalWallet);
    }

    /**
     * Partially update a professionalWallet.
     *
     * @param professionalWalletDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalWalletDTO> partialUpdate(ProfessionalWalletDTO professionalWalletDTO) {
        LOG.debug("Request to partially update ProfessionalWallet : {}", professionalWalletDTO);

        return professionalWalletRepository
            .findById(professionalWalletDTO.getId())
            .map(existingProfessionalWallet -> {
                professionalWalletMapper.partialUpdate(existingProfessionalWallet, professionalWalletDTO);

                return existingProfessionalWallet;
            })
            .map(professionalWalletRepository::save)
            .map(professionalWalletMapper::toDto);
    }

    /**
     * Get all the professionalWallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalWalletDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessionalWallets");
        return professionalWalletRepository.findAll(pageable).map(professionalWalletMapper::toDto);
    }

    /**
     * Get all the professionalWallets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfessionalWalletDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professionalWalletRepository.findAllWithEagerRelationships(pageable).map(professionalWalletMapper::toDto);
    }

    /**
     * Get one professionalWallet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalWalletDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalWallet : {}", id);
        return professionalWalletRepository.findOneWithEagerRelationships(id).map(professionalWalletMapper::toDto);
    }

    /**
     * Delete the professionalWallet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalWallet : {}", id);
        professionalWalletRepository.deleteById(id);
    }
}
