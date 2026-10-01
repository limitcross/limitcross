package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.Payment;
import com.limitcross.facility.repository.PaymentRepository;
import com.limitcross.facility.service.criteria.PaymentCriteria;
import com.limitcross.facility.service.dto.PaymentDTO;
import com.limitcross.facility.service.mapper.PaymentMapper;
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
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentQueryService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByCriteria(PaymentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page).map(paymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Payment_.id),
                buildSpecification(criteria.getGateway(), Payment_.gateway),
                buildStringSpecification(criteria.getGatewayOrderId(), Payment_.gatewayOrderId),
                buildStringSpecification(criteria.getGatewayPaymentId(), Payment_.gatewayPaymentId),
                buildStringSpecification(criteria.getPaymentMethod(), Payment_.paymentMethod),
                buildRangeSpecification(criteria.getAmount(), Payment_.amount),
                buildStringSpecification(criteria.getCurrency(), Payment_.currency),
                buildSpecification(criteria.getStatus(), Payment_.status),
                buildStringSpecification(criteria.getIdempotencyKey(), Payment_.idempotencyKey),
                buildStringSpecification(criteria.getFailureReason(), Payment_.failureReason),
                buildRangeSpecification(criteria.getCreatedAt(), Payment_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Payment_.updatedAt),
                buildSpecification(criteria.getBookingId(), root -> root.join(Payment_.booking, JoinType.LEFT).get(Booking_.id))
            );
        }
        return specification;
    }
}
