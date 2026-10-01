package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.DiscountType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Coupon} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.CouponResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /coupons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CouponCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DiscountType
     */
    public static class DiscountTypeFilter extends Filter<DiscountType> {

        public DiscountTypeFilter() {}

        public DiscountTypeFilter(DiscountTypeFilter filter) {
            super(filter);
        }

        @Override
        public DiscountTypeFilter copy() {
            return new DiscountTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private DiscountTypeFilter discountType;

    private BigDecimalFilter discountValue;

    private BigDecimalFilter maxDiscount;

    private BigDecimalFilter minOrderValue;

    private BooleanFilter firstBookingOnly;

    private IntegerFilter totalUsageLimit;

    private IntegerFilter perUserLimit;

    private IntegerFilter usedCount;

    private InstantFilter validFrom;

    private InstantFilter validTo;

    private BooleanFilter active;

    private LongFilter serviceId;

    private LongFilter cityId;

    private Boolean distinct;

    public CouponCriteria() {}

    public CouponCriteria(CouponCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(DiscountTypeFilter::copy).orElse(null);
        this.discountValue = other.optionalDiscountValue().map(BigDecimalFilter::copy).orElse(null);
        this.maxDiscount = other.optionalMaxDiscount().map(BigDecimalFilter::copy).orElse(null);
        this.minOrderValue = other.optionalMinOrderValue().map(BigDecimalFilter::copy).orElse(null);
        this.firstBookingOnly = other.optionalFirstBookingOnly().map(BooleanFilter::copy).orElse(null);
        this.totalUsageLimit = other.optionalTotalUsageLimit().map(IntegerFilter::copy).orElse(null);
        this.perUserLimit = other.optionalPerUserLimit().map(IntegerFilter::copy).orElse(null);
        this.usedCount = other.optionalUsedCount().map(IntegerFilter::copy).orElse(null);
        this.validFrom = other.optionalValidFrom().map(InstantFilter::copy).orElse(null);
        this.validTo = other.optionalValidTo().map(InstantFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.serviceId = other.optionalServiceId().map(LongFilter::copy).orElse(null);
        this.cityId = other.optionalCityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CouponCriteria copy() {
        return new CouponCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DiscountTypeFilter getDiscountType() {
        return discountType;
    }

    public Optional<DiscountTypeFilter> optionalDiscountType() {
        return Optional.ofNullable(discountType);
    }

    public DiscountTypeFilter discountType() {
        if (discountType == null) {
            setDiscountType(new DiscountTypeFilter());
        }
        return discountType;
    }

    public void setDiscountType(DiscountTypeFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountValue() {
        return discountValue;
    }

    public Optional<BigDecimalFilter> optionalDiscountValue() {
        return Optional.ofNullable(discountValue);
    }

    public BigDecimalFilter discountValue() {
        if (discountValue == null) {
            setDiscountValue(new BigDecimalFilter());
        }
        return discountValue;
    }

    public void setDiscountValue(BigDecimalFilter discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimalFilter getMaxDiscount() {
        return maxDiscount;
    }

    public Optional<BigDecimalFilter> optionalMaxDiscount() {
        return Optional.ofNullable(maxDiscount);
    }

    public BigDecimalFilter maxDiscount() {
        if (maxDiscount == null) {
            setMaxDiscount(new BigDecimalFilter());
        }
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimalFilter maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimalFilter getMinOrderValue() {
        return minOrderValue;
    }

    public Optional<BigDecimalFilter> optionalMinOrderValue() {
        return Optional.ofNullable(minOrderValue);
    }

    public BigDecimalFilter minOrderValue() {
        if (minOrderValue == null) {
            setMinOrderValue(new BigDecimalFilter());
        }
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimalFilter minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public BooleanFilter getFirstBookingOnly() {
        return firstBookingOnly;
    }

    public Optional<BooleanFilter> optionalFirstBookingOnly() {
        return Optional.ofNullable(firstBookingOnly);
    }

    public BooleanFilter firstBookingOnly() {
        if (firstBookingOnly == null) {
            setFirstBookingOnly(new BooleanFilter());
        }
        return firstBookingOnly;
    }

    public void setFirstBookingOnly(BooleanFilter firstBookingOnly) {
        this.firstBookingOnly = firstBookingOnly;
    }

    public IntegerFilter getTotalUsageLimit() {
        return totalUsageLimit;
    }

    public Optional<IntegerFilter> optionalTotalUsageLimit() {
        return Optional.ofNullable(totalUsageLimit);
    }

    public IntegerFilter totalUsageLimit() {
        if (totalUsageLimit == null) {
            setTotalUsageLimit(new IntegerFilter());
        }
        return totalUsageLimit;
    }

    public void setTotalUsageLimit(IntegerFilter totalUsageLimit) {
        this.totalUsageLimit = totalUsageLimit;
    }

    public IntegerFilter getPerUserLimit() {
        return perUserLimit;
    }

    public Optional<IntegerFilter> optionalPerUserLimit() {
        return Optional.ofNullable(perUserLimit);
    }

    public IntegerFilter perUserLimit() {
        if (perUserLimit == null) {
            setPerUserLimit(new IntegerFilter());
        }
        return perUserLimit;
    }

    public void setPerUserLimit(IntegerFilter perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public IntegerFilter getUsedCount() {
        return usedCount;
    }

    public Optional<IntegerFilter> optionalUsedCount() {
        return Optional.ofNullable(usedCount);
    }

    public IntegerFilter usedCount() {
        if (usedCount == null) {
            setUsedCount(new IntegerFilter());
        }
        return usedCount;
    }

    public void setUsedCount(IntegerFilter usedCount) {
        this.usedCount = usedCount;
    }

    public InstantFilter getValidFrom() {
        return validFrom;
    }

    public Optional<InstantFilter> optionalValidFrom() {
        return Optional.ofNullable(validFrom);
    }

    public InstantFilter validFrom() {
        if (validFrom == null) {
            setValidFrom(new InstantFilter());
        }
        return validFrom;
    }

    public void setValidFrom(InstantFilter validFrom) {
        this.validFrom = validFrom;
    }

    public InstantFilter getValidTo() {
        return validTo;
    }

    public Optional<InstantFilter> optionalValidTo() {
        return Optional.ofNullable(validTo);
    }

    public InstantFilter validTo() {
        if (validTo == null) {
            setValidTo(new InstantFilter());
        }
        return validTo;
    }

    public void setValidTo(InstantFilter validTo) {
        this.validTo = validTo;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getServiceId() {
        return serviceId;
    }

    public Optional<LongFilter> optionalServiceId() {
        return Optional.ofNullable(serviceId);
    }

    public LongFilter serviceId() {
        if (serviceId == null) {
            setServiceId(new LongFilter());
        }
        return serviceId;
    }

    public void setServiceId(LongFilter serviceId) {
        this.serviceId = serviceId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public Optional<LongFilter> optionalCityId() {
        return Optional.ofNullable(cityId);
    }

    public LongFilter cityId() {
        if (cityId == null) {
            setCityId(new LongFilter());
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
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
        final CouponCriteria that = (CouponCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountValue, that.discountValue) &&
            Objects.equals(maxDiscount, that.maxDiscount) &&
            Objects.equals(minOrderValue, that.minOrderValue) &&
            Objects.equals(firstBookingOnly, that.firstBookingOnly) &&
            Objects.equals(totalUsageLimit, that.totalUsageLimit) &&
            Objects.equals(perUserLimit, that.perUserLimit) &&
            Objects.equals(usedCount, that.usedCount) &&
            Objects.equals(validFrom, that.validFrom) &&
            Objects.equals(validTo, that.validTo) &&
            Objects.equals(active, that.active) &&
            Objects.equals(serviceId, that.serviceId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            description,
            discountType,
            discountValue,
            maxDiscount,
            minOrderValue,
            firstBookingOnly,
            totalUsageLimit,
            perUserLimit,
            usedCount,
            validFrom,
            validTo,
            active,
            serviceId,
            cityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CouponCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountValue().map(f -> "discountValue=" + f + ", ").orElse("") +
            optionalMaxDiscount().map(f -> "maxDiscount=" + f + ", ").orElse("") +
            optionalMinOrderValue().map(f -> "minOrderValue=" + f + ", ").orElse("") +
            optionalFirstBookingOnly().map(f -> "firstBookingOnly=" + f + ", ").orElse("") +
            optionalTotalUsageLimit().map(f -> "totalUsageLimit=" + f + ", ").orElse("") +
            optionalPerUserLimit().map(f -> "perUserLimit=" + f + ", ").orElse("") +
            optionalUsedCount().map(f -> "usedCount=" + f + ", ").orElse("") +
            optionalValidFrom().map(f -> "validFrom=" + f + ", ").orElse("") +
            optionalValidTo().map(f -> "validTo=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalServiceId().map(f -> "serviceId=" + f + ", ").orElse("") +
            optionalCityId().map(f -> "cityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
