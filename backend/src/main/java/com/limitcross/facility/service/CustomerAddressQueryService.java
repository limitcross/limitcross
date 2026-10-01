package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.repository.CustomerAddressRepository;
import com.limitcross.facility.service.criteria.CustomerAddressCriteria;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.mapper.CustomerAddressMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustomerAddress} entities in the database.
 * The main input is a {@link CustomerAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerAddressQueryService extends QueryService<CustomerAddress> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAddressQueryService.class);

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerAddressMapper customerAddressMapper;

    public CustomerAddressQueryService(CustomerAddressRepository customerAddressRepository, CustomerAddressMapper customerAddressMapper) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressMapper = customerAddressMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAddressDTO> findByCriteria(CustomerAddressCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerAddress> specification = createSpecification(criteria);
        return customerAddressRepository.findAll(specification, page).map(customerAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerAddressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerAddress> specification = createSpecification(criteria);
        return customerAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerAddress> createSpecification(CustomerAddressCriteria criteria) {
        Specification<CustomerAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CustomerAddress_.id),
                buildSpecification(criteria.getLabel(), CustomerAddress_.label),
                buildStringSpecification(criteria.getContactName(), CustomerAddress_.contactName),
                buildStringSpecification(criteria.getContactPhone(), CustomerAddress_.contactPhone),
                buildStringSpecification(criteria.getLine1(), CustomerAddress_.line1),
                buildStringSpecification(criteria.getLine2(), CustomerAddress_.line2),
                buildStringSpecification(criteria.getLandmark(), CustomerAddress_.landmark),
                buildStringSpecification(criteria.getPincode(), CustomerAddress_.pincode),
                buildRangeSpecification(criteria.getLatitude(), CustomerAddress_.latitude),
                buildRangeSpecification(criteria.getLongitude(), CustomerAddress_.longitude),
                buildSpecification(criteria.getDefaultAddress(), CustomerAddress_.defaultAddress),
                buildRangeSpecification(criteria.getDeletedAt(), CustomerAddress_.deletedAt),
                buildRangeSpecification(criteria.getCreatedAt(), CustomerAddress_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), CustomerAddress_.updatedAt),
                buildSpecification(criteria.getCustomerId(), root -> root.join(CustomerAddress_.customer, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getCityId(), root -> root.join(CustomerAddress_.city, JoinType.LEFT).get(City_.id))
            );
        }
        return specification;
    }
}
