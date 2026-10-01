package com.limitcross.facility.service;

import com.limitcross.facility.domain.WalletTransaction;
import com.limitcross.facility.repository.WalletTransactionRepository;
import com.limitcross.facility.service.dto.WalletTransactionDTO;
import com.limitcross.facility.service.mapper.WalletTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.WalletTransaction}.
 */
@Service
@Transactional
public class WalletTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletTransactionService.class);

    private final WalletTransactionRepository walletTransactionRepository;

    private final WalletTransactionMapper walletTransactionMapper;

    public WalletTransactionService(
        WalletTransactionRepository walletTransactionRepository,
        WalletTransactionMapper walletTransactionMapper
    ) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
    }

    /**
     * Save a walletTransaction.
     *
     * @param walletTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public WalletTransactionDTO save(WalletTransactionDTO walletTransactionDTO) {
        LOG.debug("Request to save WalletTransaction : {}", walletTransactionDTO);
        WalletTransaction walletTransaction = walletTransactionMapper.toEntity(walletTransactionDTO);
        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return walletTransactionMapper.toDto(walletTransaction);
    }

    /**
     * Update a walletTransaction.
     *
     * @param walletTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    public WalletTransactionDTO update(WalletTransactionDTO walletTransactionDTO) {
        LOG.debug("Request to update WalletTransaction : {}", walletTransactionDTO);
        WalletTransaction walletTransaction = walletTransactionMapper.toEntity(walletTransactionDTO);
        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return walletTransactionMapper.toDto(walletTransaction);
    }

    /**
     * Partially update a walletTransaction.
     *
     * @param walletTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WalletTransactionDTO> partialUpdate(WalletTransactionDTO walletTransactionDTO) {
        LOG.debug("Request to partially update WalletTransaction : {}", walletTransactionDTO);

        return walletTransactionRepository
            .findById(walletTransactionDTO.getId())
            .map(existingWalletTransaction -> {
                walletTransactionMapper.partialUpdate(existingWalletTransaction, walletTransactionDTO);

                return existingWalletTransaction;
            })
            .map(walletTransactionRepository::save)
            .map(walletTransactionMapper::toDto);
    }

    /**
     * Get all the walletTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WalletTransactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WalletTransactions");
        return walletTransactionRepository.findAll(pageable).map(walletTransactionMapper::toDto);
    }

    /**
     * Get all the walletTransactions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WalletTransactionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return walletTransactionRepository.findAllWithEagerRelationships(pageable).map(walletTransactionMapper::toDto);
    }

    /**
     * Get one walletTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WalletTransactionDTO> findOne(Long id) {
        LOG.debug("Request to get WalletTransaction : {}", id);
        return walletTransactionRepository.findOneWithEagerRelationships(id).map(walletTransactionMapper::toDto);
    }

    /**
     * Delete the walletTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WalletTransaction : {}", id);
        walletTransactionRepository.deleteById(id);
    }
}
