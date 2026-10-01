package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.repository.BookingRepository;
import com.limitcross.facility.service.criteria.BookingCriteria;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.mapper.BookingMapper;
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
 * Service for executing complex queries for {@link Booking} entities in the database.
 * The main input is a {@link BookingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BookingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookingQueryService extends QueryService<Booking> {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQueryService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingQueryService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page).map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Booking_.id),
                buildStringSpecification(criteria.getBookingNo(), Booking_.bookingNo),
                buildSpecification(criteria.getPublicId(), Booking_.publicId),
                buildStringSpecification(criteria.getServiceTitle(), Booking_.serviceTitle),
                buildRangeSpecification(criteria.getScheduledStart(), Booking_.scheduledStart),
                buildRangeSpecification(criteria.getScheduledEnd(), Booking_.scheduledEnd),
                buildSpecification(criteria.getStatus(), Booking_.status),
                buildSpecification(criteria.getPaymentStatus(), Booking_.paymentStatus),
                buildSpecification(criteria.getPaymentMode(), Booking_.paymentMode),
                buildStringSpecification(criteria.getCurrency(), Booking_.currency),
                buildRangeSpecification(criteria.getSubtotal(), Booking_.subtotal),
                buildRangeSpecification(criteria.getAddonTotal(), Booking_.addonTotal),
                buildRangeSpecification(criteria.getDiscountAmount(), Booking_.discountAmount),
                buildRangeSpecification(criteria.getServiceFee(), Booking_.serviceFee),
                buildRangeSpecification(criteria.getTaxAmount(), Booking_.taxAmount),
                buildRangeSpecification(criteria.getTipAmount(), Booking_.tipAmount),
                buildRangeSpecification(criteria.getTotalAmount(), Booking_.totalAmount),
                buildStringSpecification(criteria.getCustomerNotes(), Booking_.customerNotes),
                buildStringSpecification(criteria.getStartOtp(), Booking_.startOtp),
                buildSpecification(criteria.getCancelledBy(), Booking_.cancelledBy),
                buildStringSpecification(criteria.getCancelReason(), Booking_.cancelReason),
                buildSpecification(criteria.getSource(), Booking_.source),
                buildRangeSpecification(criteria.getWalletAmountUsed(), Booking_.walletAmountUsed),
                buildRangeSpecification(criteria.getLoyaltyPointsUsed(), Booking_.loyaltyPointsUsed),
                buildRangeSpecification(criteria.getCancellationFee(), Booking_.cancellationFee),
                buildRangeSpecification(criteria.getRescheduleCount(), Booking_.rescheduleCount),
                buildRangeSpecification(criteria.getPlatformCommission(), Booking_.platformCommission),
                buildRangeSpecification(criteria.getProEarning(), Booking_.proEarning),
                buildRangeSpecification(criteria.getArrivalEta(), Booking_.arrivalEta),
                buildSpecification(criteria.getPriority(), Booking_.priority),
                buildRangeSpecification(criteria.getStartedAt(), Booking_.startedAt),
                buildRangeSpecification(criteria.getCompletedAt(), Booking_.completedAt),
                buildRangeSpecification(criteria.getCancelledAt(), Booking_.cancelledAt),
                buildRangeSpecification(criteria.getCreatedAt(), Booking_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Booking_.updatedAt),
                buildSpecification(criteria.getItemId(), root -> root.join(Booking_.items, JoinType.LEFT).get(BookingItem_.id)),
                buildSpecification(criteria.getStatusHistoryId(), root ->
                    root.join(Booking_.statusHistories, JoinType.LEFT).get(BookingStatusHistory_.id)
                ),
                buildSpecification(criteria.getAssignmentId(), root ->
                    root.join(Booking_.assignments, JoinType.LEFT).get(BookingAssignment_.id)
                ),
                buildSpecification(criteria.getMediaId(), root -> root.join(Booking_.media, JoinType.LEFT).get(BookingMedia_.id)),
                buildSpecification(criteria.getPaymentId(), root -> root.join(Booking_.payments, JoinType.LEFT).get(Payment_.id)),
                buildSpecification(criteria.getQuoteId(), root -> root.join(Booking_.quotes, JoinType.LEFT).get(BookingQuote_.id)),
                buildSpecification(criteria.getRescheduleId(), root ->
                    root.join(Booking_.reschedules, JoinType.LEFT).get(BookingReschedule_.id)
                ),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Booking_.customer, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getServiceId(), root -> root.join(Booking_.service, JoinType.LEFT).get(FacilityService_.id)),
                buildSpecification(criteria.getCityId(), root -> root.join(Booking_.city, JoinType.LEFT).get(City_.id)),
                buildSpecification(criteria.getAddressId(), root -> root.join(Booking_.address, JoinType.LEFT).get(CustomerAddress_.id)),
                buildSpecification(criteria.getProfessionalId(), root ->
                    root.join(Booking_.professional, JoinType.LEFT).get(Professional_.id)
                ),
                buildSpecification(criteria.getCouponId(), root -> root.join(Booking_.coupon, JoinType.LEFT).get(Coupon_.id)),
                buildSpecification(criteria.getSubscriptionId(), root ->
                    root.join(Booking_.subscription, JoinType.LEFT).get(BookingSubscription_.id)
                ),
                buildSpecification(criteria.getSlotCapacityId(), root ->
                    root.join(Booking_.slotCapacity, JoinType.LEFT).get(SlotCapacity_.id)
                ),
                buildSpecification(criteria.getCouponRedemptionId(), root ->
                    root.join(Booking_.couponRedemption, JoinType.LEFT).get(CouponRedemption_.id)
                ),
                buildSpecification(criteria.getReviewId(), root -> root.join(Booking_.review, JoinType.LEFT).get(Review_.id)),
                buildSpecification(criteria.getChatThreadId(), root -> root.join(Booking_.chatThread, JoinType.LEFT).get(ChatThread_.id))
            );
        }
        return specification;
    }
}
