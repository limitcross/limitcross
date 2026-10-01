package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.SupportTicket;
import com.limitcross.facility.repository.SupportTicketRepository;
import com.limitcross.facility.service.criteria.SupportTicketCriteria;
import com.limitcross.facility.service.dto.SupportTicketDTO;
import com.limitcross.facility.service.mapper.SupportTicketMapper;
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
 * Service for executing complex queries for {@link SupportTicket} entities in the database.
 * The main input is a {@link SupportTicketCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupportTicketDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupportTicketQueryService extends QueryService<SupportTicket> {

    private static final Logger LOG = LoggerFactory.getLogger(SupportTicketQueryService.class);

    private final SupportTicketRepository supportTicketRepository;

    private final SupportTicketMapper supportTicketMapper;

    public SupportTicketQueryService(SupportTicketRepository supportTicketRepository, SupportTicketMapper supportTicketMapper) {
        this.supportTicketRepository = supportTicketRepository;
        this.supportTicketMapper = supportTicketMapper;
    }

    /**
     * Return a {@link Page} of {@link SupportTicketDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupportTicketDTO> findByCriteria(SupportTicketCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupportTicket> specification = createSpecification(criteria);
        return supportTicketRepository.findAll(specification, page).map(supportTicketMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupportTicketCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupportTicket> specification = createSpecification(criteria);
        return supportTicketRepository.count(specification);
    }

    /**
     * Function to convert {@link SupportTicketCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupportTicket> createSpecification(SupportTicketCriteria criteria) {
        Specification<SupportTicket> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SupportTicket_.id),
                buildStringSpecification(criteria.getTicketNo(), SupportTicket_.ticketNo),
                buildSpecification(criteria.getCategory(), SupportTicket_.category),
                buildStringSpecification(criteria.getSubject(), SupportTicket_.subject),
                buildStringSpecification(criteria.getDescription(), SupportTicket_.description),
                buildSpecification(criteria.getStatus(), SupportTicket_.status),
                buildSpecification(criteria.getPriority(), SupportTicket_.priority),
                buildRangeSpecification(criteria.getCreatedAt(), SupportTicket_.createdAt),
                buildRangeSpecification(criteria.getResolvedAt(), SupportTicket_.resolvedAt),
                buildSpecification(criteria.getUserId(), root -> root.join(SupportTicket_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getAssignedToId(), root -> root.join(SupportTicket_.assignedTo, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getBookingId(), root -> root.join(SupportTicket_.booking, JoinType.LEFT).get(Booking_.id))
            );
        }
        return specification;
    }
}
