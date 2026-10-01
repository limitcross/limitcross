package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CouponRedemption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponRedemptionDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal discountAmount;

    private Instant createdAt;

    @NotNull
    private BookingDTO booking;

    @NotNull
    private UserDTO user;

    @NotNull
    private CouponDTO coupon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CouponDTO getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponDTO coupon) {
        this.coupon = coupon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouponRedemptionDTO)) {
            return false;
        }

        CouponRedemptionDTO couponRedemptionDTO = (CouponRedemptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, couponRedemptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponRedemptionDTO{" +
            "id=" + getId() +
            ", discountAmount=" + getDiscountAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", booking=" + getBooking() +
            ", user=" + getUser() +
            ", coupon=" + getCoupon() +
            "}";
    }
}
