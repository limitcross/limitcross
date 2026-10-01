package com.limitcross.facility.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityServiceCriteriaTest {

    @Test
    void newFacilityServiceCriteriaHasAllFiltersNullTest() {
        var facilityServiceCriteria = new FacilityServiceCriteria();
        assertThat(facilityServiceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityServiceCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityServiceCriteria = new FacilityServiceCriteria();

        setAllFilters(facilityServiceCriteria);

        assertThat(facilityServiceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityServiceCriteriaCopyCreatesNullFilterTest() {
        var facilityServiceCriteria = new FacilityServiceCriteria();
        var copy = facilityServiceCriteria.copy();

        assertThat(facilityServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityServiceCriteria)
        );
    }

    @Test
    void facilityServiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityServiceCriteria = new FacilityServiceCriteria();
        setAllFilters(facilityServiceCriteria);

        var copy = facilityServiceCriteria.copy();

        assertThat(facilityServiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityServiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityServiceCriteria = new FacilityServiceCriteria();

        assertThat(facilityServiceCriteria).hasToString("FacilityServiceCriteria{}");
    }

    private static void setAllFilters(FacilityServiceCriteria facilityServiceCriteria) {
        facilityServiceCriteria.id();
        facilityServiceCriteria.code();
        facilityServiceCriteria.title();
        facilityServiceCriteria.slug();
        facilityServiceCriteria.emoji();
        facilityServiceCriteria.imageUrl();
        facilityServiceCriteria.description();
        facilityServiceCriteria.durationMinutes();
        facilityServiceCriteria.warrantyDays();
        facilityServiceCriteria.sacCode();
        facilityServiceCriteria.gstPercent();
        facilityServiceCriteria.genderSpecific();
        facilityServiceCriteria.requiresVisitCharge();
        facilityServiceCriteria.popular();
        facilityServiceCriteria.active();
        facilityServiceCriteria.avgRating();
        facilityServiceCriteria.reviewsCount();
        facilityServiceCriteria.sortOrder();
        facilityServiceCriteria.createdAt();
        facilityServiceCriteria.updatedAt();
        facilityServiceCriteria.servicePackageId();
        facilityServiceCriteria.addonId();
        facilityServiceCriteria.translationId();
        facilityServiceCriteria.categoryId();
        facilityServiceCriteria.distinct();
    }

    private static Condition<FacilityServiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getEmoji()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDurationMinutes()) &&
                condition.apply(criteria.getWarrantyDays()) &&
                condition.apply(criteria.getSacCode()) &&
                condition.apply(criteria.getGstPercent()) &&
                condition.apply(criteria.getGenderSpecific()) &&
                condition.apply(criteria.getRequiresVisitCharge()) &&
                condition.apply(criteria.getPopular()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getAvgRating()) &&
                condition.apply(criteria.getReviewsCount()) &&
                condition.apply(criteria.getSortOrder()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getServicePackageId()) &&
                condition.apply(criteria.getAddonId()) &&
                condition.apply(criteria.getTranslationId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityServiceCriteria> copyFiltersAre(
        FacilityServiceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getEmoji(), copy.getEmoji()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDurationMinutes(), copy.getDurationMinutes()) &&
                condition.apply(criteria.getWarrantyDays(), copy.getWarrantyDays()) &&
                condition.apply(criteria.getSacCode(), copy.getSacCode()) &&
                condition.apply(criteria.getGstPercent(), copy.getGstPercent()) &&
                condition.apply(criteria.getGenderSpecific(), copy.getGenderSpecific()) &&
                condition.apply(criteria.getRequiresVisitCharge(), copy.getRequiresVisitCharge()) &&
                condition.apply(criteria.getPopular(), copy.getPopular()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getAvgRating(), copy.getAvgRating()) &&
                condition.apply(criteria.getReviewsCount(), copy.getReviewsCount()) &&
                condition.apply(criteria.getSortOrder(), copy.getSortOrder()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getServicePackageId(), copy.getServicePackageId()) &&
                condition.apply(criteria.getAddonId(), copy.getAddonId()) &&
                condition.apply(criteria.getTranslationId(), copy.getTranslationId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
