package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gateway", nullable = false)
    private GatewayProvider gateway;

    @Size(max = 80)
    @Column(name = "gateway_order_id", length = 80)
    private String gatewayOrderId;

    @Size(max = 80)
    @Column(name = "gateway_payment_id", length = 80)
    private String gatewayPaymentId;

    @Size(max = 20)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @NotNull
    @Size(max = 64)
    @Column(name = "idempotency_key", length = 64, nullable = false, unique = true)
    private String idempotencyKey;

    @Size(max = 255)
    @Column(name = "failure_reason", length = 255)
    private String failureReason;

    @Lob
    @Column(name = "raw_response")
    private String rawResponse;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "items",
            "statusHistories",
            "assignments",
            "media",
            "payments",
            "quotes",
            "reschedules",
            "customer",
            "service",
            "city",
            "address",
            "professional",
            "coupon",
            "subscription",
            "slotCapacity",
            "couponRedemption",
            "review",
            "chatThread",
        },
        allowSetters = true
    )
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GatewayProvider getGateway() {
        return this.gateway;
    }

    public Payment gateway(GatewayProvider gateway) {
        this.setGateway(gateway);
        return this;
    }

    public void setGateway(GatewayProvider gateway) {
        this.gateway = gateway;
    }

    public String getGatewayOrderId() {
        return this.gatewayOrderId;
    }

    public Payment gatewayOrderId(String gatewayOrderId) {
        this.setGatewayOrderId(gatewayOrderId);
        return this;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getGatewayPaymentId() {
        return this.gatewayPaymentId;
    }

    public Payment gatewayPaymentId(String gatewayPaymentId) {
        this.setGatewayPaymentId(gatewayPaymentId);
        return this;
    }

    public void setGatewayPaymentId(String gatewayPaymentId) {
        this.gatewayPaymentId = gatewayPaymentId;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public Payment paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Payment currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public Payment status(TransactionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getIdempotencyKey() {
        return this.idempotencyKey;
    }

    public Payment idempotencyKey(String idempotencyKey) {
        this.setIdempotencyKey(idempotencyKey);
        return this;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public Payment failureReason(String failureReason) {
        this.setFailureReason(failureReason);
        return this;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getRawResponse() {
        return this.rawResponse;
    }

    public Payment rawResponse(String rawResponse) {
        this.setRawResponse(rawResponse);
        return this;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Payment createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Payment updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Payment booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", gateway='" + getGateway() + "'" +
            ", gatewayOrderId='" + getGatewayOrderId() + "'" +
            ", gatewayPaymentId='" + getGatewayPaymentId() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", idempotencyKey='" + getIdempotencyKey() + "'" +
            ", failureReason='" + getFailureReason() + "'" +
            ", rawResponse='" + getRawResponse() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
