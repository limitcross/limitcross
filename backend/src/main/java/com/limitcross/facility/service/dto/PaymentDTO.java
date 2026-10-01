package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.TransactionStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private GatewayProvider gateway;

    @Size(max = 80)
    private String gatewayOrderId;

    @Size(max = 80)
    private String gatewayPaymentId;

    @Size(max = 20)
    private String paymentMethod;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @NotNull
    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    private TransactionStatus status;

    @NotNull
    @Size(max = 64)
    private String idempotencyKey;

    @Size(max = 255)
    private String failureReason;

    @Lob
    private String rawResponse;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GatewayProvider getGateway() {
        return gateway;
    }

    public void setGateway(GatewayProvider gateway) {
        this.gateway = gateway;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getGatewayPaymentId() {
        return gatewayPaymentId;
    }

    public void setGatewayPaymentId(String gatewayPaymentId) {
        this.gatewayPaymentId = gatewayPaymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
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
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
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
            ", booking=" + getBooking() +
            "}";
    }
}
