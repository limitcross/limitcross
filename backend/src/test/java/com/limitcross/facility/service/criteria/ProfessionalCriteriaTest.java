package com.limitcross.facility.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProfessionalCriteriaTest {

    @Test
    void newProfessionalCriteriaHasAllFiltersNullTest() {
        var professionalCriteria = new ProfessionalCriteria();
        assertThat(professionalCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void professionalCriteriaFluentMethodsCreatesFiltersTest() {
        var professionalCriteria = new ProfessionalCriteria();

        setAllFilters(professionalCriteria);

        assertThat(professionalCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void professionalCriteriaCopyCreatesNullFilterTest() {
        var professionalCriteria = new ProfessionalCriteria();
        var copy = professionalCriteria.copy();

        assertThat(professionalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(professionalCriteria)
        );
    }

    @Test
    void professionalCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var professionalCriteria = new ProfessionalCriteria();
        setAllFilters(professionalCriteria);

        var copy = professionalCriteria.copy();

        assertThat(professionalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(professionalCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var professionalCriteria = new ProfessionalCriteria();

        assertThat(professionalCriteria).hasToString("ProfessionalCriteria{}");
    }

    private static void setAllFilters(ProfessionalCriteria professionalCriteria) {
        professionalCriteria.id();
        professionalCriteria.displayName();
        professionalCriteria.photoUrl();
        professionalCriteria.gender();
        professionalCriteria.onboardingStatus();
        professionalCriteria.online();
        professionalCriteria.avgRating();
        professionalCriteria.jobsCompleted();
        professionalCriteria.cancellationRate();
        professionalCriteria.lastLat();
        professionalCriteria.lastLng();
        professionalCriteria.lastLocationAt();
        professionalCriteria.joinedAt();
        professionalCriteria.cashInHand();
        professionalCriteria.cashLimit();
        professionalCriteria.maxDailyJobs();
        professionalCriteria.languages();
        professionalCriteria.bankAccountEnc();
        professionalCriteria.ifscCode();
        professionalCriteria.deletedAt();
        professionalCriteria.createdAt();
        professionalCriteria.updatedAt();
        professionalCriteria.userId();
        professionalCriteria.kycDocumentId();
        professionalCriteria.skillId();
        professionalCriteria.availabilityId();
        professionalCriteria.timeOffId();
        professionalCriteria.homeCityId();
        professionalCriteria.tierId();
        professionalCriteria.zoneId();
        professionalCriteria.professionalWalletId();
        professionalCriteria.distinct();
    }

    private static Condition<ProfessionalCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDisplayName()) &&
                condition.apply(criteria.getPhotoUrl()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getOnboardingStatus()) &&
                condition.apply(criteria.getOnline()) &&
                condition.apply(criteria.getAvgRating()) &&
                condition.apply(criteria.getJobsCompleted()) &&
                condition.apply(criteria.getCancellationRate()) &&
                condition.apply(criteria.getLastLat()) &&
                condition.apply(criteria.getLastLng()) &&
                condition.apply(criteria.getLastLocationAt()) &&
                condition.apply(criteria.getJoinedAt()) &&
                condition.apply(criteria.getCashInHand()) &&
                condition.apply(criteria.getCashLimit()) &&
                condition.apply(criteria.getMaxDailyJobs()) &&
                condition.apply(criteria.getLanguages()) &&
                condition.apply(criteria.getBankAccountEnc()) &&
                condition.apply(criteria.getIfscCode()) &&
                condition.apply(criteria.getDeletedAt()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getKycDocumentId()) &&
                condition.apply(criteria.getSkillId()) &&
                condition.apply(criteria.getAvailabilityId()) &&
                condition.apply(criteria.getTimeOffId()) &&
                condition.apply(criteria.getHomeCityId()) &&
                condition.apply(criteria.getTierId()) &&
                condition.apply(criteria.getZoneId()) &&
                condition.apply(criteria.getProfessionalWalletId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProfessionalCriteria> copyFiltersAre(
        ProfessionalCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDisplayName(), copy.getDisplayName()) &&
                condition.apply(criteria.getPhotoUrl(), copy.getPhotoUrl()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getOnboardingStatus(), copy.getOnboardingStatus()) &&
                condition.apply(criteria.getOnline(), copy.getOnline()) &&
                condition.apply(criteria.getAvgRating(), copy.getAvgRating()) &&
                condition.apply(criteria.getJobsCompleted(), copy.getJobsCompleted()) &&
                condition.apply(criteria.getCancellationRate(), copy.getCancellationRate()) &&
                condition.apply(criteria.getLastLat(), copy.getLastLat()) &&
                condition.apply(criteria.getLastLng(), copy.getLastLng()) &&
                condition.apply(criteria.getLastLocationAt(), copy.getLastLocationAt()) &&
                condition.apply(criteria.getJoinedAt(), copy.getJoinedAt()) &&
                condition.apply(criteria.getCashInHand(), copy.getCashInHand()) &&
                condition.apply(criteria.getCashLimit(), copy.getCashLimit()) &&
                condition.apply(criteria.getMaxDailyJobs(), copy.getMaxDailyJobs()) &&
                condition.apply(criteria.getLanguages(), copy.getLanguages()) &&
                condition.apply(criteria.getBankAccountEnc(), copy.getBankAccountEnc()) &&
                condition.apply(criteria.getIfscCode(), copy.getIfscCode()) &&
                condition.apply(criteria.getDeletedAt(), copy.getDeletedAt()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getKycDocumentId(), copy.getKycDocumentId()) &&
                condition.apply(criteria.getSkillId(), copy.getSkillId()) &&
                condition.apply(criteria.getAvailabilityId(), copy.getAvailabilityId()) &&
                condition.apply(criteria.getTimeOffId(), copy.getTimeOffId()) &&
                condition.apply(criteria.getHomeCityId(), copy.getHomeCityId()) &&
                condition.apply(criteria.getTierId(), copy.getTierId()) &&
                condition.apply(criteria.getZoneId(), copy.getZoneId()) &&
                condition.apply(criteria.getProfessionalWalletId(), copy.getProfessionalWalletId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
