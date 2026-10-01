package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.DiscountType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Coupon} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @Size(max = 255)
    private String description;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0")
    private BigDecimal maxDiscount;

    @DecimalMin(value = "0")
    private BigDecimal minOrderValue;

    private Boolean firstBookingOnly;

    @Min(value = 0)
    private Integer totalUsageLimit;

    @Min(value = 1)
    private Integer perUserLimit;

    @Min(value = 0)
    private Integer usedCount;

    @NotNull
    private Instant validFrom;

    @NotNull
    private Instant validTo;

    @NotNull
    private Boolean active;

    private FacilityServiceDTO service;

    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Boolean getFirstBookingOnly() {
        return firstBookingOnly;
    }

    public void setFirstBookingOnly(Boolean firstBookingOnly) {
        this.firstBookingOnly = firstBookingOnly;
    }

    public Integer getTotalUsageLimit() {
        return totalUsageLimit;
    }

    public void setTotalUsageLimit(Integer totalUsageLimit) {
        this.totalUsageLimit = totalUsageLimit;
    }

    public Integer getPerUserLimit() {
        return perUserLimit;
    }

    public void setPerUserLimit(Integer perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CouponDTO)) {
            return false;
        }

        CouponDTO couponDTO = (CouponDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, couponDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", maxDiscount=" + getMaxDiscount() +
            ", minOrderValue=" + getMinOrderValue() +
            ", firstBookingOnly='" + getFirstBookingOnly() + "'" +
            ", totalUsageLimit=" + getTotalUsageLimit() +
            ", perUserLimit=" + getPerUserLimit() +
            ", usedCount=" + getUsedCount() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", active='" + getActive() + "'" +
            ", service=" + getService() +
            ", city=" + getCity() +
            "}";
    }
}
