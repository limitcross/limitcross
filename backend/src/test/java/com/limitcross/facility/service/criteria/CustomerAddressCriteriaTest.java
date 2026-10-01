package com.limitcross.facility.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CustomerAddressCriteriaTest {

    @Test
    void newCustomerAddressCriteriaHasAllFiltersNullTest() {
        var customerAddressCriteria = new CustomerAddressCriteria();
        assertThat(customerAddressCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void customerAddressCriteriaFluentMethodsCreatesFiltersTest() {
        var customerAddressCriteria = new CustomerAddressCriteria();

        setAllFilters(customerAddressCriteria);

        assertThat(customerAddressCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void customerAddressCriteriaCopyCreatesNullFilterTest() {
        var customerAddressCriteria = new CustomerAddressCriteria();
        var copy = customerAddressCriteria.copy();

        assertThat(customerAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(customerAddressCriteria)
        );
    }

    @Test
    void customerAddressCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var customerAddressCriteria = new CustomerAddressCriteria();
        setAllFilters(customerAddressCriteria);

        var copy = customerAddressCriteria.copy();

        assertThat(customerAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(customerAddressCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var customerAddressCriteria = new CustomerAddressCriteria();

        assertThat(customerAddressCriteria).hasToString("CustomerAddressCriteria{}");
    }

    private static void setAllFilters(CustomerAddressCriteria customerAddressCriteria) {
        customerAddressCriteria.id();
        customerAddressCriteria.label();
        customerAddressCriteria.contactName();
        customerAddressCriteria.contactPhone();
        customerAddressCriteria.line1();
        customerAddressCriteria.line2();
        customerAddressCriteria.landmark();
        customerAddressCriteria.pincode();
        customerAddressCriteria.latitude();
        customerAddressCriteria.longitude();
        customerAddressCriteria.defaultAddress();
        customerAddressCriteria.deletedAt();
        customerAddressCriteria.createdAt();
        customerAddressCriteria.updatedAt();
        customerAddressCriteria.customerId();
        customerAddressCriteria.cityId();
        customerAddressCriteria.distinct();
    }

    private static Condition<CustomerAddressCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLabel()) &&
                condition.apply(criteria.getContactName()) &&
                condition.apply(criteria.getContactPhone()) &&
                condition.apply(criteria.getLine1()) &&
                condition.apply(criteria.getLine2()) &&
                condition.apply(criteria.getLandmark()) &&
                condition.apply(criteria.getPincode()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getDefaultAddress()) &&
                condition.apply(criteria.getDeletedAt()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getCityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CustomerAddressCriteria> copyFiltersAre(
        CustomerAddressCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLabel(), copy.getLabel()) &&
                condition.apply(criteria.getContactName(), copy.getContactName()) &&
                condition.apply(criteria.getContactPhone(), copy.getContactPhone()) &&
                condition.apply(criteria.getLine1(), copy.getLine1()) &&
                condition.apply(criteria.getLine2(), copy.getLine2()) &&
                condition.apply(criteria.getLandmark(), copy.getLandmark()) &&
                condition.apply(criteria.getPincode(), copy.getPincode()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getDefaultAddress(), copy.getDefaultAddress()) &&
                condition.apply(criteria.getDeletedAt(), copy.getDeletedAt()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getCityId(), copy.getCityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
