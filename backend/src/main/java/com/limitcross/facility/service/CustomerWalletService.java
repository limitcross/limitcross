package com.limitcross.facility.service;

import com.limitcross.facility.domain.CustomerWallet;
import com.limitcross.facility.repository.CustomerWalletRepository;
import com.limitcross.facility.service.dto.CustomerWalletDTO;
import com.limitcross.facility.service.mapper.CustomerWalletMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CustomerWallet}.
 */
@Service
@Transactional
public class CustomerWalletService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerWalletService.class);

    private final CustomerWalletRepository customerWalletRepository;

    private final CustomerWalletMapper customerWalletMapper;

    public CustomerWalletService(CustomerWalletRepository customerWalletRepository, CustomerWalletMapper customerWalletMapper) {
        this.customerWalletRepository = customerWalletRepository;
        this.customerWalletMapper = customerWalletMapper;
    }

    /**
     * Save a customerWallet.
     *
     * @param customerWalletDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerWalletDTO save(CustomerWalletDTO customerWalletDTO) {
        LOG.debug("Request to save CustomerWallet : {}", customerWalletDTO);
        CustomerWallet customerWallet = customerWalletMapper.toEntity(customerWalletDTO);
        customerWallet = customerWalletRepository.save(customerWallet);
        return customerWalletMapper.toDto(customerWallet);
    }

    /**
     * Update a customerWallet.
     *
     * @param customerWalletDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerWalletDTO update(CustomerWalletDTO customerWalletDTO) {
        LOG.debug("Request to update CustomerWallet : {}", customerWalletDTO);
        CustomerWallet customerWallet = customerWalletMapper.toEntity(customerWalletDTO);
        customerWallet = customerWalletRepository.save(customerWallet);
        return customerWalletMapper.toDto(customerWallet);
    }

    /**
     * Partially update a customerWallet.
     *
     * @param customerWalletDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerWalletDTO> partialUpdate(CustomerWalletDTO customerWalletDTO) {
        LOG.debug("Request to partially update CustomerWallet : {}", customerWalletDTO);

        return customerWalletRepository
            .findById(customerWalletDTO.getId())
            .map(existingCustomerWallet -> {
                customerWalletMapper.partialUpdate(existingCustomerWallet, customerWalletDTO);

                return existingCustomerWallet;
            })
            .map(customerWalletRepository::save)
            .map(customerWalletMapper::toDto);
    }

    /**
     * Get all the customerWallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerWalletDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerWallets");
        return customerWalletRepository.findAll(pageable).map(customerWalletMapper::toDto);
    }

    /**
     * Get all the customerWallets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustomerWalletDTO> findAllWithEagerRelationships(Pageable pageable) {
        return customerWalletRepository.findAllWithEagerRelationships(pageable).map(customerWalletMapper::toDto);
    }

    /**
     * Get one customerWallet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerWalletDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerWallet : {}", id);
        return customerWalletRepository.findOneWithEagerRelationships(id).map(customerWalletMapper::toDto);
    }

    /**
     * Delete the customerWallet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerWallet : {}", id);
        customerWalletRepository.deleteById(id);
    }
}
