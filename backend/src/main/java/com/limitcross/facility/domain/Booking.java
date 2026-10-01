package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.domain.enumeration.BookingPaymentStatus;
import com.limitcross.facility.domain.enumeration.BookingSource;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.PaymentMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "booking_no", length = 20, nullable = false, unique = true)
    private String bookingNo;

    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "public_id", length = 36, nullable = false, unique = true)
    private UUID publicId;

    @NotNull
    @Size(max = 120)
    @Column(name = "service_title", length = 120, nullable = false)
    private String serviceTitle;

    @Lob
    @Column(name = "address_snapshot", nullable = false)
    private String addressSnapshot;

    @NotNull
    @Column(name = "scheduled_start", nullable = false)
    private Instant scheduledStart;

    @NotNull
    @Column(name = "scheduled_end", nullable = false)
    private Instant scheduledEnd;

    @Column(name = "legacy_time_slot", length = 60)
    private String legacyTimeSlot;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private BookingPaymentStatus paymentStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @DecimalMin(value = "0")
    @Column(name = "addon_total", precision = 21, scale = 2)
    private BigDecimal addonTotal;

    @DecimalMin(value = "0")
    @Column(name = "discount_amount", precision = 21, scale = 2)
    private BigDecimal discountAmount;

    @DecimalMin(value = "0")
    @Column(name = "service_fee", precision = 21, scale = 2)
    private BigDecimal serviceFee;

    @DecimalMin(value = "0")
    @Column(name = "tax_amount", precision = 21, scale = 2)
    private BigDecimal taxAmount;

    @DecimalMin(value = "0")
    @Column(name = "tip_amount", precision = 21, scale = 2)
    private BigDecimal tipAmount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Size(max = 500)
    @Column(name = "customer_notes", length = 500)
    private String customerNotes;

    @Size(max = 6)
    @Column(name = "start_otp", length = 6)
    private String startOtp;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_by")
    private ActorType cancelledBy;

    @Size(max = 255)
    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private BookingSource source;

    @DecimalMin(value = "0")
    @Column(name = "wallet_amount_used", precision = 21, scale = 2)
    private BigDecimal walletAmountUsed;

    @Min(value = 0)
    @Column(name = "loyalty_points_used")
    private Integer loyaltyPointsUsed;

    @DecimalMin(value = "0")
    @Column(name = "cancellation_fee", precision = 21, scale = 2)
    private BigDecimal cancellationFee;

    @Min(value = 0)
    @Column(name = "reschedule_count")
    private Integer rescheduleCount;

    @DecimalMin(value = "0")
    @Column(name = "platform_commission", precision = 21, scale = 2)
    private BigDecimal platformCommission;

    @DecimalMin(value = "0")
    @Column(name = "pro_earning", precision = 21, scale = 2)
    private BigDecimal proEarning;

    @Column(name = "arrival_eta")
    private Instant arrivalEta;

    @Column(name = "priority")
    private Boolean priority;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "servicePackage", "addon", "booking" }, allowSetters = true)
    private Set<BookingItem> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<BookingStatusHistory> statusHistories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "professional", "booking" }, allowSetters = true)
    private Set<BookingAssignment> assignments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "uploadedBy", "booking" }, allowSetters = true)
    private Set<BookingMedia> media = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "items", "professional", "booking" }, allowSetters = true)
    private Set<BookingQuote> quotes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<BookingReschedule> reschedules = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customer", "city" }, allowSetters = true)
    private CustomerAddress address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "service", "city" }, allowSetters = true)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customer", "service", "servicePackage", "address", "preferredProfessional" }, allowSetters = true)
    private BookingSubscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zone", "category" }, allowSetters = true)
    private SlotCapacity slotCapacity;

    @JsonIgnoreProperties(value = { "booking", "user", "coupon" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking")
    private CouponRedemption couponRedemption;

    @JsonIgnoreProperties(value = { "booking", "customer", "professional", "service" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking")
    private Review review;

    @JsonIgnoreProperties(value = { "booking", "messages", "customer", "professional" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking")
    private ChatThread chatThread;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Booking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNo() {
        return this.bookingNo;
    }

    public Booking bookingNo(String bookingNo) {
        this.setBookingNo(bookingNo);
        return this;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public UUID getPublicId() {
        return this.publicId;
    }

    public Booking publicId(UUID publicId) {
        this.setPublicId(publicId);
        return this;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getServiceTitle() {
        return this.serviceTitle;
    }

    public Booking serviceTitle(String serviceTitle) {
        this.setServiceTitle(serviceTitle);
        return this;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getAddressSnapshot() {
        return this.addressSnapshot;
    }

    public Booking addressSnapshot(String addressSnapshot) {
        this.setAddressSnapshot(addressSnapshot);
        return this;
    }

    public void setAddressSnapshot(String addressSnapshot) {
        this.addressSnapshot = addressSnapshot;
    }

    public Instant getScheduledStart() {
        return this.scheduledStart;
    }

    public Booking scheduledStart(Instant scheduledStart) {
        this.setScheduledStart(scheduledStart);
        return this;
    }

    public void setScheduledStart(Instant scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public Instant getScheduledEnd() {
        return this.scheduledEnd;
    }

    public Booking scheduledEnd(Instant scheduledEnd) {
        this.setScheduledEnd(scheduledEnd);
        return this;
    }

    public void setScheduledEnd(Instant scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public String legacyTimeSlotValue() {
        return legacyTimeSlot;
    }

    public void storeLegacyTimeSlot(String legacyTimeSlot) {
        this.legacyTimeSlot = legacyTimeSlot;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public Booking status(BookingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingPaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Booking paymentStatus(BookingPaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(BookingPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMode getPaymentMode() {
        return this.paymentMode;
    }

    public Booking paymentMode(PaymentMode paymentMode) {
        this.setPaymentMode(paymentMode);
        return this;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Booking currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public Booking subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getAddonTotal() {
        return this.addonTotal;
    }

    public Booking addonTotal(BigDecimal addonTotal) {
        this.setAddonTotal(addonTotal);
        return this;
    }

    public void setAddonTotal(BigDecimal addonTotal) {
        this.addonTotal = addonTotal;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Booking discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getServiceFee() {
        return this.serviceFee;
    }

    public Booking serviceFee(BigDecimal serviceFee) {
        this.setServiceFee(serviceFee);
        return this;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public Booking taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTipAmount() {
        return this.tipAmount;
    }

    public Booking tipAmount(BigDecimal tipAmount) {
        this.setTipAmount(tipAmount);
        return this;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Booking totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerNotes() {
        return this.customerNotes;
    }

    public Booking customerNotes(String customerNotes) {
        this.setCustomerNotes(customerNotes);
        return this;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public String getStartOtp() {
        return this.startOtp;
    }

    public Booking startOtp(String startOtp) {
        this.setStartOtp(startOtp);
        return this;
    }

    public void setStartOtp(String startOtp) {
        this.startOtp = startOtp;
    }

    public ActorType getCancelledBy() {
        return this.cancelledBy;
    }

    public Booking cancelledBy(ActorType cancelledBy) {
        this.setCancelledBy(cancelledBy);
        return this;
    }

    public void setCancelledBy(ActorType cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public Booking cancelReason(String cancelReason) {
        this.setCancelReason(cancelReason);
        return this;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public BookingSource getSource() {
        return this.source;
    }

    public Booking source(BookingSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(BookingSource source) {
        this.source = source;
    }

    public BigDecimal getWalletAmountUsed() {
        return this.walletAmountUsed;
    }

    public Booking walletAmountUsed(BigDecimal walletAmountUsed) {
        this.setWalletAmountUsed(walletAmountUsed);
        return this;
    }

    public void setWalletAmountUsed(BigDecimal walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public Integer getLoyaltyPointsUsed() {
        return this.loyaltyPointsUsed;
    }

    public Booking loyaltyPointsUsed(Integer loyaltyPointsUsed) {
        this.setLoyaltyPointsUsed(loyaltyPointsUsed);
        return this;
    }

    public void setLoyaltyPointsUsed(Integer loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public BigDecimal getCancellationFee() {
        return this.cancellationFee;
    }

    public Booking cancellationFee(BigDecimal cancellationFee) {
        this.setCancellationFee(cancellationFee);
        return this;
    }

    public void setCancellationFee(BigDecimal cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public Integer getRescheduleCount() {
        return this.rescheduleCount;
    }

    public Booking rescheduleCount(Integer rescheduleCount) {
        this.setRescheduleCount(rescheduleCount);
        return this;
    }

    public void setRescheduleCount(Integer rescheduleCount) {
        this.rescheduleCount = rescheduleCount;
    }

    public BigDecimal getPlatformCommission() {
        return this.platformCommission;
    }

    public Booking platformCommission(BigDecimal platformCommission) {
        this.setPlatformCommission(platformCommission);
        return this;
    }

    public void setPlatformCommission(BigDecimal platformCommission) {
        this.platformCommission = platformCommission;
    }

    public BigDecimal getProEarning() {
        return this.proEarning;
    }

    public Booking proEarning(BigDecimal proEarning) {
        this.setProEarning(proEarning);
        return this;
    }

    public void setProEarning(BigDecimal proEarning) {
        this.proEarning = proEarning;
    }

    public Instant getArrivalEta() {
        return this.arrivalEta;
    }

    public Booking arrivalEta(Instant arrivalEta) {
        this.setArrivalEta(arrivalEta);
        return this;
    }

    public void setArrivalEta(Instant arrivalEta) {
        this.arrivalEta = arrivalEta;
    }

    public Boolean getPriority() {
        return this.priority;
    }

    public Booking priority(Boolean priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public Booking startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public Booking completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getCancelledAt() {
        return this.cancelledAt;
    }

    public Booking cancelledAt(Instant cancelledAt) {
        this.setCancelledAt(cancelledAt);
        return this;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Booking createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Booking updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<BookingItem> getItems() {
        return this.items;
    }

    public void setItems(Set<BookingItem> bookingItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setBooking(null));
        }
        if (bookingItems != null) {
            bookingItems.forEach(i -> i.setBooking(this));
        }
        this.items = bookingItems;
    }

    public Booking items(Set<BookingItem> bookingItems) {
        this.setItems(bookingItems);
        return this;
    }

    public Booking addItem(BookingItem bookingItem) {
        this.items.add(bookingItem);
        bookingItem.setBooking(this);
        return this;
    }

    public Booking removeItem(BookingItem bookingItem) {
        this.items.remove(bookingItem);
        bookingItem.setBooking(null);
        return this;
    }

    public Set<BookingStatusHistory> getStatusHistories() {
        return this.statusHistories;
    }

    public void setStatusHistories(Set<BookingStatusHistory> bookingStatusHistories) {
        if (this.statusHistories != null) {
            this.statusHistories.forEach(i -> i.setBooking(null));
        }
        if (bookingStatusHistories != null) {
            bookingStatusHistories.forEach(i -> i.setBooking(this));
        }
        this.statusHistories = bookingStatusHistories;
    }

    public Booking statusHistories(Set<BookingStatusHistory> bookingStatusHistories) {
        this.setStatusHistories(bookingStatusHistories);
        return this;
    }

    public Booking addStatusHistory(BookingStatusHistory bookingStatusHistory) {
        this.statusHistories.add(bookingStatusHistory);
        bookingStatusHistory.setBooking(this);
        return this;
    }

    public Booking removeStatusHistory(BookingStatusHistory bookingStatusHistory) {
        this.statusHistories.remove(bookingStatusHistory);
        bookingStatusHistory.setBooking(null);
        return this;
    }

    public Set<BookingAssignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<BookingAssignment> bookingAssignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setBooking(null));
        }
        if (bookingAssignments != null) {
            bookingAssignments.forEach(i -> i.setBooking(this));
        }
        this.assignments = bookingAssignments;
    }

    public Booking assignments(Set<BookingAssignment> bookingAssignments) {
        this.setAssignments(bookingAssignments);
        return this;
    }

    public Booking addAssignment(BookingAssignment bookingAssignment) {
        this.assignments.add(bookingAssignment);
        bookingAssignment.setBooking(this);
        return this;
    }

    public Booking removeAssignment(BookingAssignment bookingAssignment) {
        this.assignments.remove(bookingAssignment);
        bookingAssignment.setBooking(null);
        return this;
    }

    public Set<BookingMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<BookingMedia> bookingMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setBooking(null));
        }
        if (bookingMedias != null) {
            bookingMedias.forEach(i -> i.setBooking(this));
        }
        this.media = bookingMedias;
    }

    public Booking media(Set<BookingMedia> bookingMedias) {
        this.setMedia(bookingMedias);
        return this;
    }

    public Booking addMedia(BookingMedia bookingMedia) {
        this.media.add(bookingMedia);
        bookingMedia.setBooking(this);
        return this;
    }

    public Booking removeMedia(BookingMedia bookingMedia) {
        this.media.remove(bookingMedia);
        bookingMedia.setBooking(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setBooking(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setBooking(this));
        }
        this.payments = payments;
    }

    public Booking payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Booking addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setBooking(this);
        return this;
    }

    public Booking removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setBooking(null);
        return this;
    }

    public Set<BookingQuote> getQuotes() {
        return this.quotes;
    }

    public void setQuotes(Set<BookingQuote> bookingQuotes) {
        if (this.quotes != null) {
            this.quotes.forEach(i -> i.setBooking(null));
        }
        if (bookingQuotes != null) {
            bookingQuotes.forEach(i -> i.setBooking(this));
        }
        this.quotes = bookingQuotes;
    }

    public Booking quotes(Set<BookingQuote> bookingQuotes) {
        this.setQuotes(bookingQuotes);
        return this;
    }

    public Booking addQuote(BookingQuote bookingQuote) {
        this.quotes.add(bookingQuote);
        bookingQuote.setBooking(this);
        return this;
    }

    public Booking removeQuote(BookingQuote bookingQuote) {
        this.quotes.remove(bookingQuote);
        bookingQuote.setBooking(null);
        return this;
    }

    public Set<BookingReschedule> getReschedules() {
        return this.reschedules;
    }

    public void setReschedules(Set<BookingReschedule> bookingReschedules) {
        if (this.reschedules != null) {
            this.reschedules.forEach(i -> i.setBooking(null));
        }
        if (bookingReschedules != null) {
            bookingReschedules.forEach(i -> i.setBooking(this));
        }
        this.reschedules = bookingReschedules;
    }

    public Booking reschedules(Set<BookingReschedule> bookingReschedules) {
        this.setReschedules(bookingReschedules);
        return this;
    }

    public Booking addReschedule(BookingReschedule bookingReschedule) {
        this.reschedules.add(bookingReschedule);
        bookingReschedule.setBooking(this);
        return this;
    }

    public Booking removeReschedule(BookingReschedule bookingReschedule) {
        this.reschedules.remove(bookingReschedule);
        bookingReschedule.setBooking(null);
        return this;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public Booking customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public Booking service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Booking city(City city) {
        this.setCity(city);
        return this;
    }

    public CustomerAddress getAddress() {
        return this.address;
    }

    public void setAddress(CustomerAddress customerAddress) {
        this.address = customerAddress;
    }

    public Booking address(CustomerAddress customerAddress) {
        this.setAddress(customerAddress);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public Booking professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public Coupon getCoupon() {
        return this.coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Booking coupon(Coupon coupon) {
        this.setCoupon(coupon);
        return this;
    }

    public BookingSubscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(BookingSubscription bookingSubscription) {
        this.subscription = bookingSubscription;
    }

    public Booking subscription(BookingSubscription bookingSubscription) {
        this.setSubscription(bookingSubscription);
        return this;
    }

    public SlotCapacity getSlotCapacity() {
        return this.slotCapacity;
    }

    public void setSlotCapacity(SlotCapacity slotCapacity) {
        this.slotCapacity = slotCapacity;
    }

    public Booking slotCapacity(SlotCapacity slotCapacity) {
        this.setSlotCapacity(slotCapacity);
        return this;
    }

    public CouponRedemption getCouponRedemption() {
        return this.couponRedemption;
    }

    public void setCouponRedemption(CouponRedemption couponRedemption) {
        if (this.couponRedemption != null) {
            this.couponRedemption.setBooking(null);
        }
        if (couponRedemption != null) {
            couponRedemption.setBooking(this);
        }
        this.couponRedemption = couponRedemption;
    }

    public Booking couponRedemption(CouponRedemption couponRedemption) {
        this.setCouponRedemption(couponRedemption);
        return this;
    }

    public Review getReview() {
        return this.review;
    }

    public void setReview(Review review) {
        if (this.review != null) {
            this.review.setBooking(null);
        }
        if (review != null) {
            review.setBooking(this);
        }
        this.review = review;
    }

    public Booking review(Review review) {
        this.setReview(review);
        return this;
    }

    public ChatThread getChatThread() {
        return this.chatThread;
    }

    public void setChatThread(ChatThread chatThread) {
        if (this.chatThread != null) {
            this.chatThread.setBooking(null);
        }
        if (chatThread != null) {
            chatThread.setBooking(this);
        }
        this.chatThread = chatThread;
    }

    public Booking chatThread(ChatThread chatThread) {
        this.setChatThread(chatThread);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return getId() != null && getId().equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
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
            "}";
    }
}
