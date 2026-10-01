package com.limitcross.facility.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BookingCriteriaTest {

    @Test
    void newBookingCriteriaHasAllFiltersNullTest() {
        var bookingCriteria = new BookingCriteria();
        assertThat(bookingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bookingCriteriaFluentMethodsCreatesFiltersTest() {
        var bookingCriteria = new BookingCriteria();

        setAllFilters(bookingCriteria);

        assertThat(bookingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bookingCriteriaCopyCreatesNullFilterTest() {
        var bookingCriteria = new BookingCriteria();
        var copy = bookingCriteria.copy();

        assertThat(bookingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bookingCriteria)
        );
    }

    @Test
    void bookingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bookingCriteria = new BookingCriteria();
        setAllFilters(bookingCriteria);

        var copy = bookingCriteria.copy();

        assertThat(bookingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bookingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bookingCriteria = new BookingCriteria();

        assertThat(bookingCriteria).hasToString("BookingCriteria{}");
    }

    private static void setAllFilters(BookingCriteria bookingCriteria) {
        bookingCriteria.id();
        bookingCriteria.bookingNo();
        bookingCriteria.publicId();
        bookingCriteria.serviceTitle();
        bookingCriteria.scheduledStart();
        bookingCriteria.scheduledEnd();
        bookingCriteria.status();
        bookingCriteria.paymentStatus();
        bookingCriteria.paymentMode();
        bookingCriteria.currency();
        bookingCriteria.subtotal();
        bookingCriteria.addonTotal();
        bookingCriteria.discountAmount();
        bookingCriteria.serviceFee();
        bookingCriteria.taxAmount();
        bookingCriteria.tipAmount();
        bookingCriteria.totalAmount();
        bookingCriteria.customerNotes();
        bookingCriteria.startOtp();
        bookingCriteria.cancelledBy();
        bookingCriteria.cancelReason();
        bookingCriteria.source();
        bookingCriteria.walletAmountUsed();
        bookingCriteria.loyaltyPointsUsed();
        bookingCriteria.cancellationFee();
        bookingCriteria.rescheduleCount();
        bookingCriteria.platformCommission();
        bookingCriteria.proEarning();
        bookingCriteria.arrivalEta();
        bookingCriteria.priority();
        bookingCriteria.startedAt();
        bookingCriteria.completedAt();
        bookingCriteria.cancelledAt();
        bookingCriteria.createdAt();
        bookingCriteria.updatedAt();
        bookingCriteria.itemId();
        bookingCriteria.statusHistoryId();
        bookingCriteria.assignmentId();
        bookingCriteria.mediaId();
        bookingCriteria.paymentId();
        bookingCriteria.quoteId();
        bookingCriteria.rescheduleId();
        bookingCriteria.customerId();
        bookingCriteria.serviceId();
        bookingCriteria.cityId();
        bookingCriteria.addressId();
        bookingCriteria.professionalId();
        bookingCriteria.couponId();
        bookingCriteria.subscriptionId();
        bookingCriteria.slotCapacityId();
        bookingCriteria.couponRedemptionId();
        bookingCriteria.reviewId();
        bookingCriteria.chatThreadId();
        bookingCriteria.distinct();
    }

    private static Condition<BookingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBookingNo()) &&
                condition.apply(criteria.getPublicId()) &&
                condition.apply(criteria.getServiceTitle()) &&
                condition.apply(criteria.getScheduledStart()) &&
                condition.apply(criteria.getScheduledEnd()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPaymentStatus()) &&
                condition.apply(criteria.getPaymentMode()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getAddonTotal()) &&
                condition.apply(criteria.getDiscountAmount()) &&
                condition.apply(criteria.getServiceFee()) &&
                condition.apply(criteria.getTaxAmount()) &&
                condition.apply(criteria.getTipAmount()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getCustomerNotes()) &&
                condition.apply(criteria.getStartOtp()) &&
                condition.apply(criteria.getCancelledBy()) &&
                condition.apply(criteria.getCancelReason()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getWalletAmountUsed()) &&
                condition.apply(criteria.getLoyaltyPointsUsed()) &&
                condition.apply(criteria.getCancellationFee()) &&
                condition.apply(criteria.getRescheduleCount()) &&
                condition.apply(criteria.getPlatformCommission()) &&
                condition.apply(criteria.getProEarning()) &&
                condition.apply(criteria.getArrivalEta()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getCancelledAt()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getItemId()) &&
                condition.apply(criteria.getStatusHistoryId()) &&
                condition.apply(criteria.getAssignmentId()) &&
                condition.apply(criteria.getMediaId()) &&
                condition.apply(criteria.getPaymentId()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getRescheduleId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getServiceId()) &&
                condition.apply(criteria.getCityId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getProfessionalId()) &&
                condition.apply(criteria.getCouponId()) &&
                condition.apply(criteria.getSubscriptionId()) &&
                condition.apply(criteria.getSlotCapacityId()) &&
                condition.apply(criteria.getCouponRedemptionId()) &&
                condition.apply(criteria.getReviewId()) &&
                condition.apply(criteria.getChatThreadId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BookingCriteria> copyFiltersAre(BookingCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBookingNo(), copy.getBookingNo()) &&
                condition.apply(criteria.getPublicId(), copy.getPublicId()) &&
                condition.apply(criteria.getServiceTitle(), copy.getServiceTitle()) &&
                condition.apply(criteria.getScheduledStart(), copy.getScheduledStart()) &&
                condition.apply(criteria.getScheduledEnd(), copy.getScheduledEnd()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPaymentStatus(), copy.getPaymentStatus()) &&
                condition.apply(criteria.getPaymentMode(), copy.getPaymentMode()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getAddonTotal(), copy.getAddonTotal()) &&
                condition.apply(criteria.getDiscountAmount(), copy.getDiscountAmount()) &&
                condition.apply(criteria.getServiceFee(), copy.getServiceFee()) &&
                condition.apply(criteria.getTaxAmount(), copy.getTaxAmount()) &&
                condition.apply(criteria.getTipAmount(), copy.getTipAmount()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getCustomerNotes(), copy.getCustomerNotes()) &&
                condition.apply(criteria.getStartOtp(), copy.getStartOtp()) &&
                condition.apply(criteria.getCancelledBy(), copy.getCancelledBy()) &&
                condition.apply(criteria.getCancelReason(), copy.getCancelReason()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getWalletAmountUsed(), copy.getWalletAmountUsed()) &&
                condition.apply(criteria.getLoyaltyPointsUsed(), copy.getLoyaltyPointsUsed()) &&
                condition.apply(criteria.getCancellationFee(), copy.getCancellationFee()) &&
                condition.apply(criteria.getRescheduleCount(), copy.getRescheduleCount()) &&
                condition.apply(criteria.getPlatformCommission(), copy.getPlatformCommission()) &&
                condition.apply(criteria.getProEarning(), copy.getProEarning()) &&
                condition.apply(criteria.getArrivalEta(), copy.getArrivalEta()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getCancelledAt(), copy.getCancelledAt()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getItemId(), copy.getItemId()) &&
                condition.apply(criteria.getStatusHistoryId(), copy.getStatusHistoryId()) &&
                condition.apply(criteria.getAssignmentId(), copy.getAssignmentId()) &&
                condition.apply(criteria.getMediaId(), copy.getMediaId()) &&
                condition.apply(criteria.getPaymentId(), copy.getPaymentId()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getRescheduleId(), copy.getRescheduleId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getServiceId(), copy.getServiceId()) &&
                condition.apply(criteria.getCityId(), copy.getCityId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getProfessionalId(), copy.getProfessionalId()) &&
                condition.apply(criteria.getCouponId(), copy.getCouponId()) &&
                condition.apply(criteria.getSubscriptionId(), copy.getSubscriptionId()) &&
                condition.apply(criteria.getSlotCapacityId(), copy.getSlotCapacityId()) &&
                condition.apply(criteria.getCouponRedemptionId(), copy.getCouponRedemptionId()) &&
                condition.apply(criteria.getReviewId(), copy.getReviewId()) &&
                condition.apply(criteria.getChatThreadId(), copy.getChatThreadId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
