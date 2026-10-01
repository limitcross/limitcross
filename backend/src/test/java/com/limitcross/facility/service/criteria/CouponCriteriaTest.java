package com.limitcross.facility.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CouponCriteriaTest {

    @Test
    void newCouponCriteriaHasAllFiltersNullTest() {
        var couponCriteria = new CouponCriteria();
        assertThat(couponCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void couponCriteriaFluentMethodsCreatesFiltersTest() {
        var couponCriteria = new CouponCriteria();

        setAllFilters(couponCriteria);

        assertThat(couponCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void couponCriteriaCopyCreatesNullFilterTest() {
        var couponCriteria = new CouponCriteria();
        var copy = couponCriteria.copy();

        assertThat(couponCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(couponCriteria)
        );
    }

    @Test
    void couponCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var couponCriteria = new CouponCriteria();
        setAllFilters(couponCriteria);

        var copy = couponCriteria.copy();

        assertThat(couponCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(couponCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var couponCriteria = new CouponCriteria();

        assertThat(couponCriteria).hasToString("CouponCriteria{}");
    }

    private static void setAllFilters(CouponCriteria couponCriteria) {
        couponCriteria.id();
        couponCriteria.code();
        couponCriteria.description();
        couponCriteria.discountType();
        couponCriteria.discountValue();
        couponCriteria.maxDiscount();
        couponCriteria.minOrderValue();
        couponCriteria.firstBookingOnly();
        couponCriteria.totalUsageLimit();
        couponCriteria.perUserLimit();
        couponCriteria.usedCount();
        couponCriteria.validFrom();
        couponCriteria.validTo();
        couponCriteria.active();
        couponCriteria.serviceId();
        couponCriteria.cityId();
        couponCriteria.distinct();
    }

    private static Condition<CouponCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue()) &&
                condition.apply(criteria.getMaxDiscount()) &&
                condition.apply(criteria.getMinOrderValue()) &&
                condition.apply(criteria.getFirstBookingOnly()) &&
                condition.apply(criteria.getTotalUsageLimit()) &&
                condition.apply(criteria.getPerUserLimit()) &&
                condition.apply(criteria.getUsedCount()) &&
                condition.apply(criteria.getValidFrom()) &&
                condition.apply(criteria.getValidTo()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getServiceId()) &&
                condition.apply(criteria.getCityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CouponCriteria> copyFiltersAre(CouponCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountValue(), copy.getDiscountValue()) &&
                condition.apply(criteria.getMaxDiscount(), copy.getMaxDiscount()) &&
                condition.apply(criteria.getMinOrderValue(), copy.getMinOrderValue()) &&
                condition.apply(criteria.getFirstBookingOnly(), copy.getFirstBookingOnly()) &&
                condition.apply(criteria.getTotalUsageLimit(), copy.getTotalUsageLimit()) &&
                condition.apply(criteria.getPerUserLimit(), copy.getPerUserLimit()) &&
                condition.apply(criteria.getUsedCount(), copy.getUsedCount()) &&
                condition.apply(criteria.getValidFrom(), copy.getValidFrom()) &&
                condition.apply(criteria.getValidTo(), copy.getValidTo()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getServiceId(), copy.getServiceId()) &&
                condition.apply(criteria.getCityId(), copy.getCityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
