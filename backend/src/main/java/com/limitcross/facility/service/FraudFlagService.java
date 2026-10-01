package com.limitcross.facility.service;

import com.limitcross.facility.domain.FraudFlag;
import com.limitcross.facility.repository.FraudFlagRepository;
import com.limitcross.facility.service.dto.FraudFlagDTO;
import com.limitcross.facility.service.mapper.FraudFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.FraudFlag}.
 */
@Service
@Transactional
public class FraudFlagService {

    private static final Logger LOG = LoggerFactory.getLogger(FraudFlagService.class);

    private final FraudFlagRepository fraudFlagRepository;

    private final FraudFlagMapper fraudFlagMapper;

    public FraudFlagService(FraudFlagRepository fraudFlagRepository, FraudFlagMapper fraudFlagMapper) {
        this.fraudFlagRepository = fraudFlagRepository;
        this.fraudFlagMapper = fraudFlagMapper;
    }

    /**
     * Save a fraudFlag.
     *
     * @param fraudFlagDTO the entity to save.
     * @return the persisted entity.
     */
    public FraudFlagDTO save(FraudFlagDTO fraudFlagDTO) {
        LOG.debug("Request to save FraudFlag : {}", fraudFlagDTO);
        FraudFlag fraudFlag = fraudFlagMapper.toEntity(fraudFlagDTO);
        fraudFlag = fraudFlagRepository.save(fraudFlag);
        return fraudFlagMapper.toDto(fraudFlag);
    }

    /**
     * Update a fraudFlag.
     *
     * @param fraudFlagDTO the entity to save.
     * @return the persisted entity.
     */
    public FraudFlagDTO update(FraudFlagDTO fraudFlagDTO) {
        LOG.debug("Request to update FraudFlag : {}", fraudFlagDTO);
        FraudFlag fraudFlag = fraudFlagMapper.toEntity(fraudFlagDTO);
        fraudFlag = fraudFlagRepository.save(fraudFlag);
        return fraudFlagMapper.toDto(fraudFlag);
    }

    /**
     * Partially update a fraudFlag.
     *
     * @param fraudFlagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FraudFlagDTO> partialUpdate(FraudFlagDTO fraudFlagDTO) {
        LOG.debug("Request to partially update FraudFlag : {}", fraudFlagDTO);

        return fraudFlagRepository
            .findById(fraudFlagDTO.getId())
            .map(existingFraudFlag -> {
                fraudFlagMapper.partialUpdate(existingFraudFlag, fraudFlagDTO);

                return existingFraudFlag;
            })
            .map(fraudFlagRepository::save)
            .map(fraudFlagMapper::toDto);
    }

    /**
     * Get all the fraudFlags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FraudFlagDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FraudFlags");
        return fraudFlagRepository.findAll(pageable).map(fraudFlagMapper::toDto);
    }

    /**
     * Get one fraudFlag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FraudFlagDTO> findOne(Long id) {
        LOG.debug("Request to get FraudFlag : {}", id);
        return fraudFlagRepository.findById(id).map(fraudFlagMapper::toDto);
    }

    /**
     * Delete the fraudFlag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FraudFlag : {}", id);
        fraudFlagRepository.deleteById(id);
    }
}
