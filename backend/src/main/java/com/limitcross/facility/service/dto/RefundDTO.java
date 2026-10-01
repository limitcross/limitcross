package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.RefundStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Refund} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefundDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @Size(max = 255)
    private String reason;

    @NotNull
    private RefundStatus status;

    @Size(max = 80)
    private String gatewayRefundId;

    private Instant createdAt;

    @NotNull
    private PaymentDTO payment;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getGatewayRefundId() {
        return gatewayRefundId;
    }

    public void setGatewayRefundId(String gatewayRefundId) {
        this.gatewayRefundId = gatewayRefundId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefundDTO)) {
            return false;
        }

        RefundDTO refundDTO = (RefundDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refundDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", reason='" + getReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", gatewayRefundId='" + getGatewayRefundId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", payment=" + getPayment() +
            ", booking=" + getBooking() +
            "}";
    }
}
