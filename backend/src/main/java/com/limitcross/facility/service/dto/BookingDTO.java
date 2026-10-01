package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.domain.enumeration.BookingPaymentStatus;
import com.limitcross.facility.domain.enumeration.BookingSource;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.PaymentMode;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Booking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String bookingNo;

    @NotNull
    private UUID publicId;

    @NotNull
    @Size(max = 120)
    private String serviceTitle;

    @Lob
    private String addressSnapshot;

    @NotNull
    private Instant scheduledStart;

    @NotNull
    private Instant scheduledEnd;

    @NotNull
    private BookingStatus status;

    @NotNull
    private BookingPaymentStatus paymentStatus;

    @NotNull
    private PaymentMode paymentMode;

    @NotNull
    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal subtotal;

    @DecimalMin(value = "0")
    private BigDecimal addonTotal;

    @DecimalMin(value = "0")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0")
    private BigDecimal serviceFee;

    @DecimalMin(value = "0")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0")
    private BigDecimal tipAmount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;

    @Size(max = 500)
    private String customerNotes;

    @Size(max = 6)
    private String startOtp;

    private ActorType cancelledBy;

    @Size(max = 255)
    private String cancelReason;

    private BookingSource source;

    @DecimalMin(value = "0")
    private BigDecimal walletAmountUsed;

    @Min(value = 0)
    private Integer loyaltyPointsUsed;

    @DecimalMin(value = "0")
    private BigDecimal cancellationFee;

    @Min(value = 0)
    private Integer rescheduleCount;

    @DecimalMin(value = "0")
    private BigDecimal platformCommission;

    @DecimalMin(value = "0")
    private BigDecimal proEarning;

    private Instant arrivalEta;

    private Boolean priority;

    private Instant startedAt;

    private Instant completedAt;

    private Instant cancelledAt;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private UserDTO customer;

    @NotNull
    private FacilityServiceDTO service;

    @NotNull
    private CityDTO city;

    private CustomerAddressDTO address;

    private ProfessionalDTO professional;

    private CouponDTO coupon;

    private BookingSubscriptionDTO subscription;

    private SlotCapacityDTO slotCapacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getAddressSnapshot() {
        return addressSnapshot;
    }

    public void setAddressSnapshot(String addressSnapshot) {
        this.addressSnapshot = addressSnapshot;
    }

    public Instant getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(Instant scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public Instant getScheduledEnd() {
        return scheduledEnd;
    }

    public void setScheduledEnd(Instant scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(BookingPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getAddonTotal() {
        return addonTotal;
    }

    public void setAddonTotal(BigDecimal addonTotal) {
        this.addonTotal = addonTotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public String getStartOtp() {
        return startOtp;
    }

    public void setStartOtp(String startOtp) {
        this.startOtp = startOtp;
    }

    public ActorType getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(ActorType cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public BookingSource getSource() {
        return source;
    }

    public void setSource(BookingSource source) {
        this.source = source;
    }

    public BigDecimal getWalletAmountUsed() {
        return walletAmountUsed;
    }

    public void setWalletAmountUsed(BigDecimal walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public Integer getLoyaltyPointsUsed() {
        return loyaltyPointsUsed;
    }

    public void setLoyaltyPointsUsed(Integer loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public BigDecimal getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(BigDecimal cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public Integer getRescheduleCount() {
        return rescheduleCount;
    }

    public void setRescheduleCount(Integer rescheduleCount) {
        this.rescheduleCount = rescheduleCount;
    }

    public BigDecimal getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(BigDecimal platformCommission) {
        this.platformCommission = platformCommission;
    }

    public BigDecimal getProEarning() {
        return proEarning;
    }

    public void setProEarning(BigDecimal proEarning) {
        this.proEarning = proEarning;
    }

    public Instant getArrivalEta() {
        return arrivalEta;
    }

    public void setArrivalEta(Instant arrivalEta) {
        this.arrivalEta = arrivalEta;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public CustomerAddressDTO getAddress() {
        return address;
    }

    public void setAddress(CustomerAddressDTO address) {
        this.address = address;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    public CouponDTO getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponDTO coupon) {
        this.coupon = coupon;
    }

    public BookingSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(BookingSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    public SlotCapacityDTO getSlotCapacity() {
        return slotCapacity;
    }

    public void setSlotCapacity(SlotCapacityDTO slotCapacity) {
        this.slotCapacity = slotCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", bookingNo='" + getBookingNo() + "'" +
            ", publicId='" + getPublicId() + "'" +
            ", serviceTitle='" + getServiceTitle() + "'" +
            ", addressSnapshot='" + getAddressSnapshot() + "'" +
            ", scheduledStart='" + getScheduledStart() + "'" +
            ", scheduledEnd='" + getScheduledEnd() + "'" +
            ", status='" + getStatus() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentMode='" + getPaymentMode() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", subtotal=" + getSubtotal() +
            ", addonTotal=" + getAddonTotal() +
            ", discountAmount=" + getDiscountAmount() +
            ", serviceFee=" + getServiceFee() +
            ", taxAmount=" + getTaxAmount() +
            ", tipAmount=" + getTipAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", customerNotes='" + getCustomerNotes() + "'" +
            ", startOtp='" + getStartOtp() + "'" +
            ", cancelledBy='" + getCancelledBy() + "'" +
            ", cancelReason='" + getCancelReason() + "'" +
            ", source='" + getSource() + "'" +
            ", walletAmountUsed=" + getWalletAmountUsed() +
            ", loyaltyPointsUsed=" + getLoyaltyPointsUsed() +
            ", cancellationFee=" + getCancellationFee() +
            ", rescheduleCount=" + getRescheduleCount() +
            ", platformCommission=" + getPlatformCommission() +
            ", proEarning=" + getProEarning() +
            ", arrivalEta='" + getArrivalEta() + "'" +
            ", priority='" + getPriority() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", cancelledAt='" + getCancelledAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", customer=" + getCustomer() +
            ", service=" + getService() +
            ", city=" + getCity() +
            ", address=" + getAddress() +
            ", professional=" + getProfessional() +
            ", coupon=" + getCoupon() +
            ", subscription=" + getSubscription() +
            ", slotCapacity=" + getSlotCapacity() +
            "}";
    }
}
