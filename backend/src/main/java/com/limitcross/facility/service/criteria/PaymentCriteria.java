package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.TransactionStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Payment} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GatewayProvider
     */
    public static class GatewayProviderFilter extends Filter<GatewayProvider> {

        public GatewayProviderFilter() {}

        public GatewayProviderFilter(GatewayProviderFilter filter) {
            super(filter);
        }

        @Override
        public GatewayProviderFilter copy() {
            return new GatewayProviderFilter(this);
        }
    }

    /**
     * Class for filtering TransactionStatus
     */
    public static class TransactionStatusFilter extends Filter<TransactionStatus> {

        public TransactionStatusFilter() {}

        public TransactionStatusFilter(TransactionStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransactionStatusFilter copy() {
            return new TransactionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private GatewayProviderFilter gateway;

    private StringFilter gatewayOrderId;

    private StringFilter gatewayPaymentId;

    private StringFilter paymentMethod;

    private BigDecimalFilter amount;

    private StringFilter currency;

    private TransactionStatusFilter status;

    private StringFilter idempotencyKey;

    private StringFilter failureReason;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter bookingId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.gateway = other.optionalGateway().map(GatewayProviderFilter::copy).orElse(null);
        this.gatewayOrderId = other.optionalGatewayOrderId().map(StringFilter::copy).orElse(null);
        this.gatewayPaymentId = other.optionalGatewayPaymentId().map(StringFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransactionStatusFilter::copy).orElse(null);
        this.idempotencyKey = other.optionalIdempotencyKey().map(StringFilter::copy).orElse(null);
        this.failureReason = other.optionalFailureReason().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.bookingId = other.optionalBookingId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public GatewayProviderFilter getGateway() {
        return gateway;
    }

    public Optional<GatewayProviderFilter> optionalGateway() {
        return Optional.ofNullable(gateway);
    }

    public GatewayProviderFilter gateway() {
        if (gateway == null) {
            setGateway(new GatewayProviderFilter());
        }
        return gateway;
    }

    public void setGateway(GatewayProviderFilter gateway) {
        this.gateway = gateway;
    }

    public StringFilter getGatewayOrderId() {
        return gatewayOrderId;
    }

    public Optional<StringFilter> optionalGatewayOrderId() {
        return Optional.ofNullable(gatewayOrderId);
    }

    public StringFilter gatewayOrderId() {
        if (gatewayOrderId == null) {
            setGatewayOrderId(new StringFilter());
        }
        return gatewayOrderId;
    }

    public void setGatewayOrderId(StringFilter gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public StringFilter getGatewayPaymentId() {
        return gatewayPaymentId;
    }

    public Optional<StringFilter> optionalGatewayPaymentId() {
        return Optional.ofNullable(gatewayPaymentId);
    }

    public StringFilter gatewayPaymentId() {
        if (gatewayPaymentId == null) {
            setGatewayPaymentId(new StringFilter());
        }
        return gatewayPaymentId;
    }

    public void setGatewayPaymentId(StringFilter gatewayPaymentId) {
        this.gatewayPaymentId = gatewayPaymentId;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<StringFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new StringFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
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

    public TransactionStatusFilter getStatus() {
        return status;
    }

    public Optional<TransactionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TransactionStatusFilter status() {
        if (status == null) {
            setStatus(new TransactionStatusFilter());
        }
        return status;
    }

    public void setStatus(TransactionStatusFilter status) {
        this.status = status;
    }

    public StringFilter getIdempotencyKey() {
        return idempotencyKey;
    }

    public Optional<StringFilter> optionalIdempotencyKey() {
        return Optional.ofNullable(idempotencyKey);
    }

    public StringFilter idempotencyKey() {
        if (idempotencyKey == null) {
            setIdempotencyKey(new StringFilter());
        }
        return idempotencyKey;
    }

    public void setIdempotencyKey(StringFilter idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public StringFilter getFailureReason() {
        return failureReason;
    }

    public Optional<StringFilter> optionalFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public StringFilter failureReason() {
        if (failureReason == null) {
            setFailureReason(new StringFilter());
        }
        return failureReason;
    }

    public void setFailureReason(StringFilter failureReason) {
        this.failureReason = failureReason;
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

    public LongFilter getBookingId() {
        return bookingId;
    }

    public Optional<LongFilter> optionalBookingId() {
        return Optional.ofNullable(bookingId);
    }

    public LongFilter bookingId() {
        if (bookingId == null) {
            setBookingId(new LongFilter());
        }
        return bookingId;
    }

    public void setBookingId(LongFilter bookingId) {
        this.bookingId = bookingId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gateway, that.gateway) &&
            Objects.equals(gatewayOrderId, that.gatewayOrderId) &&
            Objects.equals(gatewayPaymentId, that.gatewayPaymentId) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(idempotencyKey, that.idempotencyKey) &&
            Objects.equals(failureReason, that.failureReason) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            gateway,
            gatewayOrderId,
            gatewayPaymentId,
            paymentMethod,
            amount,
            currency,
            status,
            idempotencyKey,
            failureReason,
            createdAt,
            updatedAt,
            bookingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalGateway().map(f -> "gateway=" + f + ", ").orElse("") +
            optionalGatewayOrderId().map(f -> "gatewayOrderId=" + f + ", ").orElse("") +
            optionalGatewayPaymentId().map(f -> "gatewayPaymentId=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIdempotencyKey().map(f -> "idempotencyKey=" + f + ", ").orElse("") +
            optionalFailureReason().map(f -> "failureReason=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalBookingId().map(f -> "bookingId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
