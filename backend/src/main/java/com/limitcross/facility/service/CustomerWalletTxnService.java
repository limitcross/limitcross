package com.limitcross.facility.service;

import com.limitcross.facility.domain.CustomerWalletTxn;
import com.limitcross.facility.repository.CustomerWalletTxnRepository;
import com.limitcross.facility.service.dto.CustomerWalletTxnDTO;
import com.limitcross.facility.service.mapper.CustomerWalletTxnMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CustomerWalletTxn}.
 */
@Service
@Transactional
public class CustomerWalletTxnService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerWalletTxnService.class);

    private final CustomerWalletTxnRepository customerWalletTxnRepository;

    private final CustomerWalletTxnMapper customerWalletTxnMapper;

    public CustomerWalletTxnService(
        CustomerWalletTxnRepository customerWalletTxnRepository,
        CustomerWalletTxnMapper customerWalletTxnMapper
    ) {
        this.customerWalletTxnRepository = customerWalletTxnRepository;
        this.customerWalletTxnMapper = customerWalletTxnMapper;
    }

    /**
     * Save a customerWalletTxn.
     *
     * @param customerWalletTxnDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerWalletTxnDTO save(CustomerWalletTxnDTO customerWalletTxnDTO) {
        LOG.debug("Request to save CustomerWalletTxn : {}", customerWalletTxnDTO);
        CustomerWalletTxn customerWalletTxn = customerWalletTxnMapper.toEntity(customerWalletTxnDTO);
        customerWalletTxn = customerWalletTxnRepository.save(customerWalletTxn);
        return customerWalletTxnMapper.toDto(customerWalletTxn);
    }

    /**
     * Update a customerWalletTxn.
     *
     * @param customerWalletTxnDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerWalletTxnDTO update(CustomerWalletTxnDTO customerWalletTxnDTO) {
        LOG.debug("Request to update CustomerWalletTxn : {}", customerWalletTxnDTO);
        CustomerWalletTxn customerWalletTxn = customerWalletTxnMapper.toEntity(customerWalletTxnDTO);
        customerWalletTxn = customerWalletTxnRepository.save(customerWalletTxn);
        return customerWalletTxnMapper.toDto(customerWalletTxn);
    }

    /**
     * Partially update a customerWalletTxn.
     *
     * @param customerWalletTxnDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerWalletTxnDTO> partialUpdate(CustomerWalletTxnDTO customerWalletTxnDTO) {
        LOG.debug("Request to partially update CustomerWalletTxn : {}", customerWalletTxnDTO);

        return customerWalletTxnRepository
            .findById(customerWalletTxnDTO.getId())
            .map(existingCustomerWalletTxn -> {
                customerWalletTxnMapper.partialUpdate(existingCustomerWalletTxn, customerWalletTxnDTO);

                return existingCustomerWalletTxn;
            })
            .map(customerWalletTxnRepository::save)
            .map(customerWalletTxnMapper::toDto);
    }

    /**
     * Get all the customerWalletTxns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerWalletTxnDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerWalletTxns");
        return customerWalletTxnRepository.findAll(pageable).map(customerWalletTxnMapper::toDto);
    }

    /**
     * Get all the customerWalletTxns with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustomerWalletTxnDTO> findAllWithEagerRelationships(Pageable pageable) {
        return customerWalletTxnRepository.findAllWithEagerRelationships(pageable).map(customerWalletTxnMapper::toDto);
    }

    /**
     * Get one customerWalletTxn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerWalletTxnDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerWalletTxn : {}", id);
        return customerWalletTxnRepository.findOneWithEagerRelationships(id).map(customerWalletTxnMapper::toDto);
    }

    /**
     * Delete the customerWalletTxn by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerWalletTxn : {}", id);
        customerWalletTxnRepository.deleteById(id);
    }
}
