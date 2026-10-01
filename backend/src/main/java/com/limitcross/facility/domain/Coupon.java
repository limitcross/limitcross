package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Coupon.
 */
@Entity
@Table(name = "coupon")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "discount_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @DecimalMin(value = "0")
    @Column(name = "max_discount", precision = 21, scale = 2)
    private BigDecimal maxDiscount;

    @DecimalMin(value = "0")
    @Column(name = "min_order_value", precision = 21, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "first_booking_only")
    private Boolean firstBookingOnly;

    @Min(value = 0)
    @Column(name = "total_usage_limit")
    private Integer totalUsageLimit;

    @Min(value = 1)
    @Column(name = "per_user_limit")
    private Integer perUserLimit;

    @Min(value = 0)
    @Column(name = "used_count")
    private Integer usedCount;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private Instant validTo;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coupon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Coupon code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Coupon description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Coupon discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public Coupon discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMaxDiscount() {
        return this.maxDiscount;
    }

    public Coupon maxDiscount(BigDecimal maxDiscount) {
        this.setMaxDiscount(maxDiscount);
        return this;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getMinOrderValue() {
        return this.minOrderValue;
    }

    public Coupon minOrderValue(BigDecimal minOrderValue) {
        this.setMinOrderValue(minOrderValue);
        return this;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Boolean getFirstBookingOnly() {
        return this.firstBookingOnly;
    }

    public Coupon firstBookingOnly(Boolean firstBookingOnly) {
        this.setFirstBookingOnly(firstBookingOnly);
        return this;
    }

    public void setFirstBookingOnly(Boolean firstBookingOnly) {
        this.firstBookingOnly = firstBookingOnly;
    }

    public Integer getTotalUsageLimit() {
        return this.totalUsageLimit;
    }

    public Coupon totalUsageLimit(Integer totalUsageLimit) {
        this.setTotalUsageLimit(totalUsageLimit);
        return this;
    }

    public void setTotalUsageLimit(Integer totalUsageLimit) {
        this.totalUsageLimit = totalUsageLimit;
    }

    public Integer getPerUserLimit() {
        return this.perUserLimit;
    }

    public Coupon perUserLimit(Integer perUserLimit) {
        this.setPerUserLimit(perUserLimit);
        return this;
    }

    public void setPerUserLimit(Integer perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public Integer getUsedCount() {
        return this.usedCount;
    }

    public Coupon usedCount(Integer usedCount) {
        this.setUsedCount(usedCount);
        return this;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public Coupon validFrom(Instant validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public Coupon validTo(Instant validTo) {
        this.setValidTo(validTo);
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Coupon active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public Coupon service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Coupon city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coupon)) {
            return false;
        }
        return getId() != null && getId().equals(((Coupon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coupon{" +
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
            "}";
    }
}
