package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.domain.enumeration.BookingPaymentStatus;
import com.limitcross.facility.domain.enumeration.BookingSource;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.PaymentMode;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Booking} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookingStatus
     */
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter() {}

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }
    }

    /**
     * Class for filtering BookingPaymentStatus
     */
    public static class BookingPaymentStatusFilter extends Filter<BookingPaymentStatus> {

        public BookingPaymentStatusFilter() {}

        public BookingPaymentStatusFilter(BookingPaymentStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingPaymentStatusFilter copy() {
            return new BookingPaymentStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMode
     */
    public static class PaymentModeFilter extends Filter<PaymentMode> {

        public PaymentModeFilter() {}

        public PaymentModeFilter(PaymentModeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentModeFilter copy() {
            return new PaymentModeFilter(this);
        }
    }

    /**
     * Class for filtering ActorType
     */
    public static class ActorTypeFilter extends Filter<ActorType> {

        public ActorTypeFilter() {}

        public ActorTypeFilter(ActorTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActorTypeFilter copy() {
            return new ActorTypeFilter(this);
        }
    }

    /**
     * Class for filtering BookingSource
     */
    public static class BookingSourceFilter extends Filter<BookingSource> {

        public BookingSourceFilter() {}

        public BookingSourceFilter(BookingSourceFilter filter) {
            super(filter);
        }

        @Override
        public BookingSourceFilter copy() {
            return new BookingSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingNo;

    private UUIDFilter publicId;

    private StringFilter serviceTitle;

    private InstantFilter scheduledStart;

    private InstantFilter scheduledEnd;

    private BookingStatusFilter status;

    private BookingPaymentStatusFilter paymentStatus;

    private PaymentModeFilter paymentMode;

    private StringFilter currency;

    private BigDecimalFilter subtotal;

    private BigDecimalFilter addonTotal;

    private BigDecimalFilter discountAmount;

    private BigDecimalFilter serviceFee;

    private BigDecimalFilter taxAmount;

    private BigDecimalFilter tipAmount;

    private BigDecimalFilter totalAmount;

    private StringFilter customerNotes;

    private StringFilter startOtp;

    private ActorTypeFilter cancelledBy;

    private StringFilter cancelReason;

    private BookingSourceFilter source;

    private BigDecimalFilter walletAmountUsed;

    private IntegerFilter loyaltyPointsUsed;

    private BigDecimalFilter cancellationFee;

    private IntegerFilter rescheduleCount;

    private BigDecimalFilter platformCommission;

    private BigDecimalFilter proEarning;

    private InstantFilter arrivalEta;

    private BooleanFilter priority;

    private InstantFilter startedAt;

    private InstantFilter completedAt;

    private InstantFilter cancelledAt;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter itemId;

    private LongFilter statusHistoryId;

    private LongFilter assignmentId;

    private LongFilter mediaId;

    private LongFilter paymentId;

    private LongFilter quoteId;

    private LongFilter rescheduleId;

    private LongFilter customerId;

    private LongFilter serviceId;

    private LongFilter cityId;

    private LongFilter addressId;

    private LongFilter professionalId;

    private LongFilter couponId;

    private LongFilter subscriptionId;

    private LongFilter slotCapacityId;

    private LongFilter couponRedemptionId;

    private LongFilter reviewId;

    private LongFilter chatThreadId;

    private Boolean distinct;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.bookingNo = other.optionalBookingNo().map(StringFilter::copy).orElse(null);
        this.publicId = other.optionalPublicId().map(UUIDFilter::copy).orElse(null);
        this.serviceTitle = other.optionalServiceTitle().map(StringFilter::copy).orElse(null);
        this.scheduledStart = other.optionalScheduledStart().map(InstantFilter::copy).orElse(null);
        this.scheduledEnd = other.optionalScheduledEnd().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(BookingStatusFilter::copy).orElse(null);
        this.paymentStatus = other.optionalPaymentStatus().map(BookingPaymentStatusFilter::copy).orElse(null);
        this.paymentMode = other.optionalPaymentMode().map(PaymentModeFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(BigDecimalFilter::copy).orElse(null);
        this.addonTotal = other.optionalAddonTotal().map(BigDecimalFilter::copy).orElse(null);
        this.discountAmount = other.optionalDiscountAmount().map(BigDecimalFilter::copy).orElse(null);
        this.serviceFee = other.optionalServiceFee().map(BigDecimalFilter::copy).orElse(null);
        this.taxAmount = other.optionalTaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.tipAmount = other.optionalTipAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.customerNotes = other.optionalCustomerNotes().map(StringFilter::copy).orElse(null);
        this.startOtp = other.optionalStartOtp().map(StringFilter::copy).orElse(null);
        this.cancelledBy = other.optionalCancelledBy().map(ActorTypeFilter::copy).orElse(null);
        this.cancelReason = other.optionalCancelReason().map(StringFilter::copy).orElse(null);
        this.source = other.optionalSource().map(BookingSourceFilter::copy).orElse(null);
        this.walletAmountUsed = other.optionalWalletAmountUsed().map(BigDecimalFilter::copy).orElse(null);
        this.loyaltyPointsUsed = other.optionalLoyaltyPointsUsed().map(IntegerFilter::copy).orElse(null);
        this.cancellationFee = other.optionalCancellationFee().map(BigDecimalFilter::copy).orElse(null);
        this.rescheduleCount = other.optionalRescheduleCount().map(IntegerFilter::copy).orElse(null);
        this.platformCommission = other.optionalPlatformCommission().map(BigDecimalFilter::copy).orElse(null);
        this.proEarning = other.optionalProEarning().map(BigDecimalFilter::copy).orElse(null);
        this.arrivalEta = other.optionalArrivalEta().map(InstantFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(BooleanFilter::copy).orElse(null);
        this.startedAt = other.optionalStartedAt().map(InstantFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.cancelledAt = other.optionalCancelledAt().map(InstantFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(LongFilter::copy).orElse(null);
        this.statusHistoryId = other.optionalStatusHistoryId().map(LongFilter::copy).orElse(null);
        this.assignmentId = other.optionalAssignmentId().map(LongFilter::copy).orElse(null);
        this.mediaId = other.optionalMediaId().map(LongFilter::copy).orElse(null);
        this.paymentId = other.optionalPaymentId().map(LongFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(LongFilter::copy).orElse(null);
        this.rescheduleId = other.optionalRescheduleId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.serviceId = other.optionalServiceId().map(LongFilter::copy).orElse(null);
        this.cityId = other.optionalCityId().map(LongFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.professionalId = other.optionalProfessionalId().map(LongFilter::copy).orElse(null);
        this.couponId = other.optionalCouponId().map(LongFilter::copy).orElse(null);
        this.subscriptionId = other.optionalSubscriptionId().map(LongFilter::copy).orElse(null);
        this.slotCapacityId = other.optionalSlotCapacityId().map(LongFilter::copy).orElse(null);
        this.couponRedemptionId = other.optionalCouponRedemptionId().map(LongFilter::copy).orElse(null);
        this.reviewId = other.optionalReviewId().map(LongFilter::copy).orElse(null);
        this.chatThreadId = other.optionalChatThreadId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBookingNo() {
        return bookingNo;
    }

    public Optional<StringFilter> optionalBookingNo() {
        return Optional.ofNullable(bookingNo);
    }

    public StringFilter bookingNo() {
        if (bookingNo == null) {
            setBookingNo(new StringFilter());
        }
        return bookingNo;
    }

    public void setBookingNo(StringFilter bookingNo) {
        this.bookingNo = bookingNo;
    }

    public UUIDFilter getPublicId() {
        return publicId;
    }

    public Optional<UUIDFilter> optionalPublicId() {
        return Optional.ofNullable(publicId);
    }

    public UUIDFilter publicId() {
        if (publicId == null) {
            setPublicId(new UUIDFilter());
        }
        return publicId;
    }

    public void setPublicId(UUIDFilter publicId) {
        this.publicId = publicId;
    }

    public StringFilter getServiceTitle() {
        return serviceTitle;
    }

    public Optional<StringFilter> optionalServiceTitle() {
        return Optional.ofNullable(serviceTitle);
    }

    public StringFilter serviceTitle() {
        if (serviceTitle == null) {
            setServiceTitle(new StringFilter());
        }
        return serviceTitle;
    }

    public void setServiceTitle(StringFilter serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public InstantFilter getScheduledStart() {
        return scheduledStart;
    }

    public Optional<InstantFilter> optionalScheduledStart() {
        return Optional.ofNullable(scheduledStart);
    }

    public InstantFilter scheduledStart() {
        if (scheduledStart == null) {
            setScheduledStart(new InstantFilter());
        }
        return scheduledStart;
    }

    public void setScheduledStart(InstantFilter scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public InstantFilter getScheduledEnd() {
        return scheduledEnd;
    }

    public Optional<InstantFilter> optionalScheduledEnd() {
        return Optional.ofNullable(scheduledEnd);
    }

    public InstantFilter scheduledEnd() {
        if (scheduledEnd == null) {
            setScheduledEnd(new InstantFilter());
        }
        return scheduledEnd;
    }

    public void setScheduledEnd(InstantFilter scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public BookingStatusFilter getStatus() {
        return status;
    }

    public Optional<BookingStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public BookingStatusFilter status() {
        if (status == null) {
            setStatus(new BookingStatusFilter());
        }
        return status;
    }

    public void setStatus(BookingStatusFilter status) {
        this.status = status;
    }

    public BookingPaymentStatusFilter getPaymentStatus() {
        return paymentStatus;
    }

    public Optional<BookingPaymentStatusFilter> optionalPaymentStatus() {
        return Optional.ofNullable(paymentStatus);
    }

    public BookingPaymentStatusFilter paymentStatus() {
        if (paymentStatus == null) {
            setPaymentStatus(new BookingPaymentStatusFilter());
        }
        return paymentStatus;
    }

    public void setPaymentStatus(BookingPaymentStatusFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentModeFilter getPaymentMode() {
        return paymentMode;
    }

    public Optional<PaymentModeFilter> optionalPaymentMode() {
        return Optional.ofNullable(paymentMode);
    }

    public PaymentModeFilter paymentMode() {
        if (paymentMode == null) {
            setPaymentMode(new PaymentModeFilter());
        }
        return paymentMode;
    }

    public void setPaymentMode(PaymentModeFilter paymentMode) {
        this.paymentMode = paymentMode;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public BigDecimalFilter getSubtotal() {
        return subtotal;
    }

    public Optional<BigDecimalFilter> optionalSubtotal() {
        return Optional.ofNullable(subtotal);
    }

    public BigDecimalFilter subtotal() {
        if (subtotal == null) {
            setSubtotal(new BigDecimalFilter());
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimalFilter subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimalFilter getAddonTotal() {
        return addonTotal;
    }

    public Optional<BigDecimalFilter> optionalAddonTotal() {
        return Optional.ofNullable(addonTotal);
    }

    public BigDecimalFilter addonTotal() {
        if (addonTotal == null) {
            setAddonTotal(new BigDecimalFilter());
        }
        return addonTotal;
    }

    public void setAddonTotal(BigDecimalFilter addonTotal) {
        this.addonTotal = addonTotal;
    }

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public Optional<BigDecimalFilter> optionalDiscountAmount() {
        return Optional.ofNullable(discountAmount);
    }

    public BigDecimalFilter discountAmount() {
        if (discountAmount == null) {
            setDiscountAmount(new BigDecimalFilter());
        }
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimalFilter getServiceFee() {
        return serviceFee;
    }

    public Optional<BigDecimalFilter> optionalServiceFee() {
        return Optional.ofNullable(serviceFee);
    }

    public BigDecimalFilter serviceFee() {
        if (serviceFee == null) {
            setServiceFee(new BigDecimalFilter());
        }
        return serviceFee;
    }

    public void setServiceFee(BigDecimalFilter serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public Optional<BigDecimalFilter> optionalTaxAmount() {
        return Optional.ofNullable(taxAmount);
    }

    public BigDecimalFilter taxAmount() {
        if (taxAmount == null) {
            setTaxAmount(new BigDecimalFilter());
        }
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimalFilter getTipAmount() {
        return tipAmount;
    }

    public Optional<BigDecimalFilter> optionalTipAmount() {
        return Optional.ofNullable(tipAmount);
    }

    public BigDecimalFilter tipAmount() {
        if (tipAmount == null) {
            setTipAmount(new BigDecimalFilter());
        }
        return tipAmount;
    }

    public void setTipAmount(BigDecimalFilter tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StringFilter getCustomerNotes() {
        return customerNotes;
    }

    public Optional<StringFilter> optionalCustomerNotes() {
        return Optional.ofNullable(customerNotes);
    }

    public StringFilter customerNotes() {
        if (customerNotes == null) {
            setCustomerNotes(new StringFilter());
        }
        return customerNotes;
    }

    public void setCustomerNotes(StringFilter customerNotes) {
        this.customerNotes = customerNotes;
    }

    public StringFilter getStartOtp() {
        return startOtp;
    }

    public Optional<StringFilter> optionalStartOtp() {
        return Optional.ofNullable(startOtp);
    }

    public StringFilter startOtp() {
        if (startOtp == null) {
            setStartOtp(new StringFilter());
        }
        return startOtp;
    }

    public void setStartOtp(StringFilter startOtp) {
        this.startOtp = startOtp;
    }

    public ActorTypeFilter getCancelledBy() {
        return cancelledBy;
    }

    public Optional<ActorTypeFilter> optionalCancelledBy() {
        return Optional.ofNullable(cancelledBy);
    }

    public ActorTypeFilter cancelledBy() {
        if (cancelledBy == null) {
            setCancelledBy(new ActorTypeFilter());
        }
        return cancelledBy;
    }

    public void setCancelledBy(ActorTypeFilter cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public StringFilter getCancelReason() {
        return cancelReason;
    }

    public Optional<StringFilter> optionalCancelReason() {
        return Optional.ofNullable(cancelReason);
    }

    public StringFilter cancelReason() {
        if (cancelReason == null) {
            setCancelReason(new StringFilter());
        }
        return cancelReason;
    }

    public void setCancelReason(StringFilter cancelReason) {
        this.cancelReason = cancelReason;
    }

    public BookingSourceFilter getSource() {
        return source;
    }

    public Optional<BookingSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public BookingSourceFilter source() {
        if (source == null) {
            setSource(new BookingSourceFilter());
        }
        return source;
    }

    public void setSource(BookingSourceFilter source) {
        this.source = source;
    }

    public BigDecimalFilter getWalletAmountUsed() {
        return walletAmountUsed;
    }

    public Optional<BigDecimalFilter> optionalWalletAmountUsed() {
        return Optional.ofNullable(walletAmountUsed);
    }

    public BigDecimalFilter walletAmountUsed() {
        if (walletAmountUsed == null) {
            setWalletAmountUsed(new BigDecimalFilter());
        }
        return walletAmountUsed;
    }

    public void setWalletAmountUsed(BigDecimalFilter walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public IntegerFilter getLoyaltyPointsUsed() {
        return loyaltyPointsUsed;
    }

    public Optional<IntegerFilter> optionalLoyaltyPointsUsed() {
        return Optional.ofNullable(loyaltyPointsUsed);
    }

    public IntegerFilter loyaltyPointsUsed() {
        if (loyaltyPointsUsed == null) {
            setLoyaltyPointsUsed(new IntegerFilter());
        }
        return loyaltyPointsUsed;
    }

    public void setLoyaltyPointsUsed(IntegerFilter loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public BigDecimalFilter getCancellationFee() {
        return cancellationFee;
    }

    public Optional<BigDecimalFilter> optionalCancellationFee() {
        return Optional.ofNullable(cancellationFee);
    }

    public BigDecimalFilter cancellationFee() {
        if (cancellationFee == null) {
            setCancellationFee(new BigDecimalFilter());
        }
        return cancellationFee;
    }

    public void setCancellationFee(BigDecimalFilter cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public IntegerFilter getRescheduleCount() {
        return rescheduleCount;
    }

    public Optional<IntegerFilter> optionalRescheduleCount() {
        return Optional.ofNullable(rescheduleCount);
    }

    public IntegerFilter rescheduleCount() {
        if (rescheduleCount == null) {
            setRescheduleCount(new IntegerFilter());
        }
        return rescheduleCount;
    }

    public void setRescheduleCount(IntegerFilter rescheduleCount) {
        this.rescheduleCount = rescheduleCount;
    }

    public BigDecimalFilter getPlatformCommission() {
        return platformCommission;
    }

    public Optional<BigDecimalFilter> optionalPlatformCommission() {
        return Optional.ofNullable(platformCommission);
    }

    public BigDecimalFilter platformCommission() {
        if (platformCommission == null) {
            setPlatformCommission(new BigDecimalFilter());
        }
        return platformCommission;
    }

    public void setPlatformCommission(BigDecimalFilter platformCommission) {
        this.platformCommission = platformCommission;
    }

    public BigDecimalFilter getProEarning() {
        return proEarning;
    }

    public Optional<BigDecimalFilter> optionalProEarning() {
        return Optional.ofNullable(proEarning);
    }

    public BigDecimalFilter proEarning() {
        if (proEarning == null) {
            setProEarning(new BigDecimalFilter());
        }
        return proEarning;
    }

    public void setProEarning(BigDecimalFilter proEarning) {
        this.proEarning = proEarning;
    }

    public InstantFilter getArrivalEta() {
        return arrivalEta;
    }

    public Optional<InstantFilter> optionalArrivalEta() {
        return Optional.ofNullable(arrivalEta);
    }

    public InstantFilter arrivalEta() {
        if (arrivalEta == null) {
            setArrivalEta(new InstantFilter());
        }
        return arrivalEta;
    }

    public void setArrivalEta(InstantFilter arrivalEta) {
        this.arrivalEta = arrivalEta;
    }

    public BooleanFilter getPriority() {
        return priority;
    }

    public Optional<BooleanFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public BooleanFilter priority() {
        if (priority == null) {
            setPriority(new BooleanFilter());
        }
        return priority;
    }

    public void setPriority(BooleanFilter priority) {
        this.priority = priority;
    }

    public InstantFilter getStartedAt() {
        return startedAt;
    }

    public Optional<InstantFilter> optionalStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public InstantFilter startedAt() {
        if (startedAt == null) {
            setStartedAt(new InstantFilter());
        }
        return startedAt;
    }

    public void setStartedAt(InstantFilter startedAt) {
        this.startedAt = startedAt;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public InstantFilter getCancelledAt() {
        return cancelledAt;
    }

    public Optional<InstantFilter> optionalCancelledAt() {
        return Optional.ofNullable(cancelledAt);
    }

    public InstantFilter cancelledAt() {
        if (cancelledAt == null) {
            setCancelledAt(new InstantFilter());
        }
        return cancelledAt;
    }

    public void setCancelledAt(InstantFilter cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public Optional<LongFilter> optionalItemId() {
        return Optional.ofNullable(itemId);
    }

    public LongFilter itemId() {
        if (itemId == null) {
            setItemId(new LongFilter());
        }
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public LongFilter getStatusHistoryId() {
        return statusHistoryId;
    }

    public Optional<LongFilter> optionalStatusHistoryId() {
        return Optional.ofNullable(statusHistoryId);
    }

    public LongFilter statusHistoryId() {
        if (statusHistoryId == null) {
            setStatusHistoryId(new LongFilter());
        }
        return statusHistoryId;
    }

    public void setStatusHistoryId(LongFilter statusHistoryId) {
        this.statusHistoryId = statusHistoryId;
    }

    public LongFilter getAssignmentId() {
        return assignmentId;
    }

    public Optional<LongFilter> optionalAssignmentId() {
        return Optional.ofNullable(assignmentId);
    }

    public LongFilter assignmentId() {
        if (assignmentId == null) {
            setAssignmentId(new LongFilter());
        }
        return assignmentId;
    }

    public void setAssignmentId(LongFilter assignmentId) {
        this.assignmentId = assignmentId;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public Optional<LongFilter> optionalMediaId() {
        return Optional.ofNullable(mediaId);
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            setMediaId(new LongFilter());
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public Optional<LongFilter> optionalPaymentId() {
        return Optional.ofNullable(paymentId);
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            setPaymentId(new LongFilter());
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getQuoteId() {
        return quoteId;
    }

    public Optional<LongFilter> optionalQuoteId() {
        return Optional.ofNullable(quoteId);
    }

    public LongFilter quoteId() {
        if (quoteId == null) {
            setQuoteId(new LongFilter());
        }
        return quoteId;
    }

    public void setQuoteId(LongFilter quoteId) {
        this.quoteId = quoteId;
    }

    public LongFilter getRescheduleId() {
        return rescheduleId;
    }

    public Optional<LongFilter> optionalRescheduleId() {
        return Optional.ofNullable(rescheduleId);
    }

    public LongFilter rescheduleId() {
        if (rescheduleId == null) {
            setRescheduleId(new LongFilter());
        }
        return rescheduleId;
    }

    public void setRescheduleId(LongFilter rescheduleId) {
        this.rescheduleId = rescheduleId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getServiceId() {
        return serviceId;
    }

    public Optional<LongFilter> optionalServiceId() {
        return Optional.ofNullable(serviceId);
    }

    public LongFilter serviceId() {
        if (serviceId == null) {
            setServiceId(new LongFilter());
        }
        return serviceId;
    }

    public void setServiceId(LongFilter serviceId) {
        this.serviceId = serviceId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public Optional<LongFilter> optionalCityId() {
        return Optional.ofNullable(cityId);
    }

    public LongFilter cityId() {
        if (cityId == null) {
            setCityId(new LongFilter());
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getProfessionalId() {
        return professionalId;
    }

    public Optional<LongFilter> optionalProfessionalId() {
        return Optional.ofNullable(professionalId);
    }

    public LongFilter professionalId() {
        if (professionalId == null) {
            setProfessionalId(new LongFilter());
        }
        return professionalId;
    }

    public void setProfessionalId(LongFilter professionalId) {
        this.professionalId = professionalId;
    }

    public LongFilter getCouponId() {
        return couponId;
    }

    public Optional<LongFilter> optionalCouponId() {
        return Optional.ofNullable(couponId);
    }

    public LongFilter couponId() {
        if (couponId == null) {
            setCouponId(new LongFilter());
        }
        return couponId;
    }

    public void setCouponId(LongFilter couponId) {
        this.couponId = couponId;
    }

    public LongFilter getSubscriptionId() {
        return subscriptionId;
    }

    public Optional<LongFilter> optionalSubscriptionId() {
        return Optional.ofNullable(subscriptionId);
    }

    public LongFilter subscriptionId() {
        if (subscriptionId == null) {
            setSubscriptionId(new LongFilter());
        }
        return subscriptionId;
    }

    public void setSubscriptionId(LongFilter subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LongFilter getSlotCapacityId() {
        return slotCapacityId;
    }

    public Optional<LongFilter> optionalSlotCapacityId() {
        return Optional.ofNullable(slotCapacityId);
    }

    public LongFilter slotCapacityId() {
        if (slotCapacityId == null) {
            setSlotCapacityId(new LongFilter());
        }
        return slotCapacityId;
    }

    public void setSlotCapacityId(LongFilter slotCapacityId) {
        this.slotCapacityId = slotCapacityId;
    }

    public LongFilter getCouponRedemptionId() {
        return couponRedemptionId;
    }

    public Optional<LongFilter> optionalCouponRedemptionId() {
        return Optional.ofNullable(couponRedemptionId);
    }

    public LongFilter couponRedemptionId() {
        if (couponRedemptionId == null) {
            setCouponRedemptionId(new LongFilter());
        }
        return couponRedemptionId;
    }

    public void setCouponRedemptionId(LongFilter couponRedemptionId) {
        this.couponRedemptionId = couponRedemptionId;
    }

    public LongFilter getReviewId() {
        return reviewId;
    }

    public Optional<LongFilter> optionalReviewId() {
        return Optional.ofNullable(reviewId);
    }

    public LongFilter reviewId() {
        if (reviewId == null) {
            setReviewId(new LongFilter());
        }
        return reviewId;
    }

    public void setReviewId(LongFilter reviewId) {
        this.reviewId = reviewId;
    }

    public LongFilter getChatThreadId() {
        return chatThreadId;
    }

    public Optional<LongFilter> optionalChatThreadId() {
        return Optional.ofNullable(chatThreadId);
    }

    public LongFilter chatThreadId() {
        if (chatThreadId == null) {
            setChatThreadId(new LongFilter());
        }
        return chatThreadId;
    }

    public void setChatThreadId(LongFilter chatThreadId) {
        this.chatThreadId = chatThreadId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingNo, that.bookingNo) &&
            Objects.equals(publicId, that.publicId) &&
            Objects.equals(serviceTitle, that.serviceTitle) &&
            Objects.equals(scheduledStart, that.scheduledStart) &&
            Objects.equals(scheduledEnd, that.scheduledEnd) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(paymentMode, that.paymentMode) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(addonTotal, that.addonTotal) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(serviceFee, that.serviceFee) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(tipAmount, that.tipAmount) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(customerNotes, that.customerNotes) &&
            Objects.equals(startOtp, that.startOtp) &&
            Objects.equals(cancelledBy, that.cancelledBy) &&
            Objects.equals(cancelReason, that.cancelReason) &&
            Objects.equals(source, that.source) &&
            Objects.equals(walletAmountUsed, that.walletAmountUsed) &&
            Objects.equals(loyaltyPointsUsed, that.loyaltyPointsUsed) &&
            Objects.equals(cancellationFee, that.cancellationFee) &&
            Objects.equals(rescheduleCount, that.rescheduleCount) &&
            Objects.equals(platformCommission, that.platformCommission) &&
            Objects.equals(proEarning, that.proEarning) &&
            Objects.equals(arrivalEta, that.arrivalEta) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(startedAt, that.startedAt) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(cancelledAt, that.cancelledAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(statusHistoryId, that.statusHistoryId) &&
            Objects.equals(assignmentId, that.assignmentId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(rescheduleId, that.rescheduleId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(serviceId, that.serviceId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(professionalId, that.professionalId) &&
            Objects.equals(couponId, that.couponId) &&
            Objects.equals(subscriptionId, that.subscriptionId) &&
            Objects.equals(slotCapacityId, that.slotCapacityId) &&
            Objects.equals(couponRedemptionId, that.couponRedemptionId) &&
            Objects.equals(reviewId, that.reviewId) &&
            Objects.equals(chatThreadId, that.chatThreadId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            bookingNo,
            publicId,
            serviceTitle,
            scheduledStart,
            scheduledEnd,
            status,
            paymentStatus,
            paymentMode,
            currency,
            subtotal,
            addonTotal,
            discountAmount,
            serviceFee,
            taxAmount,
            tipAmount,
            totalAmount,
            customerNotes,
            startOtp,
            cancelledBy,
            cancelReason,
            source,
            walletAmountUsed,
            loyaltyPointsUsed,
            cancellationFee,
            rescheduleCount,
            platformCommission,
            proEarning,
            arrivalEta,
            priority,
            startedAt,
            completedAt,
            cancelledAt,
            createdAt,
            updatedAt,
            itemId,
            statusHistoryId,
            assignmentId,
            mediaId,
            paymentId,
            quoteId,
            rescheduleId,
            customerId,
            serviceId,
            cityId,
            addressId,
            professionalId,
            couponId,
            subscriptionId,
            slotCapacityId,
            couponRedemptionId,
            reviewId,
            chatThreadId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBookingNo().map(f -> "bookingNo=" + f + ", ").orElse("") +
            optionalPublicId().map(f -> "publicId=" + f + ", ").orElse("") +
            optionalServiceTitle().map(f -> "serviceTitle=" + f + ", ").orElse("") +
            optionalScheduledStart().map(f -> "scheduledStart=" + f + ", ").orElse("") +
            optionalScheduledEnd().map(f -> "scheduledEnd=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPaymentStatus().map(f -> "paymentStatus=" + f + ", ").orElse("") +
            optionalPaymentMode().map(f -> "paymentMode=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalAddonTotal().map(f -> "addonTotal=" + f + ", ").orElse("") +
            optionalDiscountAmount().map(f -> "discountAmount=" + f + ", ").orElse("") +
            optionalServiceFee().map(f -> "serviceFee=" + f + ", ").orElse("") +
            optionalTaxAmount().map(f -> "taxAmount=" + f + ", ").orElse("") +
            optionalTipAmount().map(f -> "tipAmount=" + f + ", ").orElse("") +
            optionalTotalAmount().map(f -> "totalAmount=" + f + ", ").orElse("") +
            optionalCustomerNotes().map(f -> "customerNotes=" + f + ", ").orElse("") +
            optionalStartOtp().map(f -> "startOtp=" + f + ", ").orElse("") +
            optionalCancelledBy().map(f -> "cancelledBy=" + f + ", ").orElse("") +
            optionalCancelReason().map(f -> "cancelReason=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalWalletAmountUsed().map(f -> "walletAmountUsed=" + f + ", ").orElse("") +
            optionalLoyaltyPointsUsed().map(f -> "loyaltyPointsUsed=" + f + ", ").orElse("") +
            optionalCancellationFee().map(f -> "cancellationFee=" + f + ", ").orElse("") +
            optionalRescheduleCount().map(f -> "rescheduleCount=" + f + ", ").orElse("") +
            optionalPlatformCommission().map(f -> "platformCommission=" + f + ", ").orElse("") +
            optionalProEarning().map(f -> "proEarning=" + f + ", ").orElse("") +
            optionalArrivalEta().map(f -> "arrivalEta=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalStartedAt().map(f -> "startedAt=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalCancelledAt().map(f -> "cancelledAt=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalStatusHistoryId().map(f -> "statusHistoryId=" + f + ", ").orElse("") +
            optionalAssignmentId().map(f -> "assignmentId=" + f + ", ").orElse("") +
            optionalMediaId().map(f -> "mediaId=" + f + ", ").orElse("") +
            optionalPaymentId().map(f -> "paymentId=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalRescheduleId().map(f -> "rescheduleId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalServiceId().map(f -> "serviceId=" + f + ", ").orElse("") +
            optionalCityId().map(f -> "cityId=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalProfessionalId().map(f -> "professionalId=" + f + ", ").orElse("") +
            optionalCouponId().map(f -> "couponId=" + f + ", ").orElse("") +
            optionalSubscriptionId().map(f -> "subscriptionId=" + f + ", ").orElse("") +
            optionalSlotCapacityId().map(f -> "slotCapacityId=" + f + ", ").orElse("") +
            optionalCouponRedemptionId().map(f -> "couponRedemptionId=" + f + ", ").orElse("") +
            optionalReviewId().map(f -> "reviewId=" + f + ", ").orElse("") +
            optionalChatThreadId().map(f -> "chatThreadId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
