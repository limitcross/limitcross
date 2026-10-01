package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.RefundStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Refund.
 */
@Entity
@Table(name = "refund")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefundStatus status;

    @Size(max = 80)
    @Column(name = "gateway_refund_id", length = 80)
    private String gatewayRefundId;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Payment payment;

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

    public Refund id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Refund amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return this.reason;
    }

    public Refund reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RefundStatus getStatus() {
        return this.status;
    }

    public Refund status(RefundStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getGatewayRefundId() {
        return this.gatewayRefundId;
    }

    public Refund gatewayRefundId(String gatewayRefundId) {
        this.setGatewayRefundId(gatewayRefundId);
        return this;
    }

    public void setGatewayRefundId(String gatewayRefundId) {
        this.gatewayRefundId = gatewayRefundId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Refund createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Refund payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Refund booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Refund)) {
            return false;
        }
        return getId() != null && getId().equals(((Refund) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Refund{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", reason='" + getReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", gatewayRefundId='" + getGatewayRefundId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
