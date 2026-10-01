package com.limitcross.facility.service;

import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.repository.CustomerAddressRepository;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.mapper.CustomerAddressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.CustomerAddress}.
 */
@Service
@Transactional
public class CustomerAddressService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAddressService.class);

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerAddressMapper customerAddressMapper;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository, CustomerAddressMapper customerAddressMapper) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressMapper = customerAddressMapper;
    }

    /**
     * Save a customerAddress.
     *
     * @param customerAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerAddressDTO save(CustomerAddressDTO customerAddressDTO) {
        LOG.debug("Request to save CustomerAddress : {}", customerAddressDTO);
        CustomerAddress customerAddress = customerAddressMapper.toEntity(customerAddressDTO);
        customerAddress = customerAddressRepository.save(customerAddress);
        return customerAddressMapper.toDto(customerAddress);
    }

    /**
     * Update a customerAddress.
     *
     * @param customerAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerAddressDTO update(CustomerAddressDTO customerAddressDTO) {
        LOG.debug("Request to update CustomerAddress : {}", customerAddressDTO);
        CustomerAddress customerAddress = customerAddressMapper.toEntity(customerAddressDTO);
        customerAddress = customerAddressRepository.save(customerAddress);
        return customerAddressMapper.toDto(customerAddress);
    }

    /**
     * Partially update a customerAddress.
     *
     * @param customerAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerAddressDTO> partialUpdate(CustomerAddressDTO customerAddressDTO) {
        LOG.debug("Request to partially update CustomerAddress : {}", customerAddressDTO);

        return customerAddressRepository
            .findById(customerAddressDTO.getId())
            .map(existingCustomerAddress -> {
                customerAddressMapper.partialUpdate(existingCustomerAddress, customerAddressDTO);

                return existingCustomerAddress;
            })
            .map(customerAddressRepository::save)
            .map(customerAddressMapper::toDto);
    }

    /**
     * Get all the customerAddresses with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CustomerAddressDTO> findAllWithEagerRelationships(Pageable pageable) {
        return customerAddressRepository.findAllWithEagerRelationships(pageable).map(customerAddressMapper::toDto);
    }

    /**
     * Get one customerAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAddressDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerAddress : {}", id);
        return customerAddressRepository.findOneWithEagerRelationships(id).map(customerAddressMapper::toDto);
    }

    /**
     * Delete the customerAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerAddress : {}", id);
        customerAddressRepository.deleteById(id);
    }
}
