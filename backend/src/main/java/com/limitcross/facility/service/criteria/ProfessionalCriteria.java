package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.Gender;
import com.limitcross.facility.domain.enumeration.OnboardingStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Professional} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.ProfessionalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /professionals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering OnboardingStatus
     */
    public static class OnboardingStatusFilter extends Filter<OnboardingStatus> {

        public OnboardingStatusFilter() {}

        public OnboardingStatusFilter(OnboardingStatusFilter filter) {
            super(filter);
        }

        @Override
        public OnboardingStatusFilter copy() {
            return new OnboardingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter displayName;

    private StringFilter photoUrl;

    private GenderFilter gender;

    private OnboardingStatusFilter onboardingStatus;

    private BooleanFilter online;

    private DoubleFilter avgRating;

    private IntegerFilter jobsCompleted;

    private DoubleFilter cancellationRate;

    private DoubleFilter lastLat;

    private DoubleFilter lastLng;

    private InstantFilter lastLocationAt;

    private InstantFilter joinedAt;

    private BigDecimalFilter cashInHand;

    private BigDecimalFilter cashLimit;

    private IntegerFilter maxDailyJobs;

    private StringFilter languages;

    private StringFilter bankAccountEnc;

    private StringFilter ifscCode;

    private InstantFilter deletedAt;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter userId;

    private LongFilter kycDocumentId;

    private LongFilter skillId;

    private LongFilter availabilityId;

    private LongFilter timeOffId;

    private LongFilter homeCityId;

    private LongFilter tierId;

    private LongFilter zoneId;

    private LongFilter professionalWalletId;

    private Boolean distinct;

    public ProfessionalCriteria() {}

    public ProfessionalCriteria(ProfessionalCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.displayName = other.optionalDisplayName().map(StringFilter::copy).orElse(null);
        this.photoUrl = other.optionalPhotoUrl().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.onboardingStatus = other.optionalOnboardingStatus().map(OnboardingStatusFilter::copy).orElse(null);
        this.online = other.optionalOnline().map(BooleanFilter::copy).orElse(null);
        this.avgRating = other.optionalAvgRating().map(DoubleFilter::copy).orElse(null);
        this.jobsCompleted = other.optionalJobsCompleted().map(IntegerFilter::copy).orElse(null);
        this.cancellationRate = other.optionalCancellationRate().map(DoubleFilter::copy).orElse(null);
        this.lastLat = other.optionalLastLat().map(DoubleFilter::copy).orElse(null);
        this.lastLng = other.optionalLastLng().map(DoubleFilter::copy).orElse(null);
        this.lastLocationAt = other.optionalLastLocationAt().map(InstantFilter::copy).orElse(null);
        this.joinedAt = other.optionalJoinedAt().map(InstantFilter::copy).orElse(null);
        this.cashInHand = other.optionalCashInHand().map(BigDecimalFilter::copy).orElse(null);
        this.cashLimit = other.optionalCashLimit().map(BigDecimalFilter::copy).orElse(null);
        this.maxDailyJobs = other.optionalMaxDailyJobs().map(IntegerFilter::copy).orElse(null);
        this.languages = other.optionalLanguages().map(StringFilter::copy).orElse(null);
        this.bankAccountEnc = other.optionalBankAccountEnc().map(StringFilter::copy).orElse(null);
        this.ifscCode = other.optionalIfscCode().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(InstantFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.kycDocumentId = other.optionalKycDocumentId().map(LongFilter::copy).orElse(null);
        this.skillId = other.optionalSkillId().map(LongFilter::copy).orElse(null);
        this.availabilityId = other.optionalAvailabilityId().map(LongFilter::copy).orElse(null);
        this.timeOffId = other.optionalTimeOffId().map(LongFilter::copy).orElse(null);
        this.homeCityId = other.optionalHomeCityId().map(LongFilter::copy).orElse(null);
        this.tierId = other.optionalTierId().map(LongFilter::copy).orElse(null);
        this.zoneId = other.optionalZoneId().map(LongFilter::copy).orElse(null);
        this.professionalWalletId = other.optionalProfessionalWalletId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProfessionalCriteria copy() {
        return new ProfessionalCriteria(this);
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public Optional<StringFilter> optionalDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public StringFilter displayName() {
        if (displayName == null) {
            setDisplayName(new StringFilter());
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getPhotoUrl() {
        return photoUrl;
    }

    public Optional<StringFilter> optionalPhotoUrl() {
        return Optional.ofNullable(photoUrl);
    }

    public StringFilter photoUrl() {
        if (photoUrl == null) {
            setPhotoUrl(new StringFilter());
        }
        return photoUrl;
    }

    public void setPhotoUrl(StringFilter photoUrl) {
        this.photoUrl = photoUrl;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public OnboardingStatusFilter getOnboardingStatus() {
        return onboardingStatus;
    }

    public Optional<OnboardingStatusFilter> optionalOnboardingStatus() {
        return Optional.ofNullable(onboardingStatus);
    }

    public OnboardingStatusFilter onboardingStatus() {
        if (onboardingStatus == null) {
            setOnboardingStatus(new OnboardingStatusFilter());
        }
        return onboardingStatus;
    }

    public void setOnboardingStatus(OnboardingStatusFilter onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }

    public BooleanFilter getOnline() {
        return online;
    }

    public Optional<BooleanFilter> optionalOnline() {
        return Optional.ofNullable(online);
    }

    public BooleanFilter online() {
        if (online == null) {
            setOnline(new BooleanFilter());
        }
        return online;
    }

    public void setOnline(BooleanFilter online) {
        this.online = online;
    }

    public DoubleFilter getAvgRating() {
        return avgRating;
    }

    public Optional<DoubleFilter> optionalAvgRating() {
        return Optional.ofNullable(avgRating);
    }

    public DoubleFilter avgRating() {
        if (avgRating == null) {
            setAvgRating(new DoubleFilter());
        }
        return avgRating;
    }

    public void setAvgRating(DoubleFilter avgRating) {
        this.avgRating = avgRating;
    }

    public IntegerFilter getJobsCompleted() {
        return jobsCompleted;
    }

    public Optional<IntegerFilter> optionalJobsCompleted() {
        return Optional.ofNullable(jobsCompleted);
    }

    public IntegerFilter jobsCompleted() {
        if (jobsCompleted == null) {
            setJobsCompleted(new IntegerFilter());
        }
        return jobsCompleted;
    }

    public void setJobsCompleted(IntegerFilter jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public DoubleFilter getCancellationRate() {
        return cancellationRate;
    }

    public Optional<DoubleFilter> optionalCancellationRate() {
        return Optional.ofNullable(cancellationRate);
    }

    public DoubleFilter cancellationRate() {
        if (cancellationRate == null) {
            setCancellationRate(new DoubleFilter());
        }
        return cancellationRate;
    }

    public void setCancellationRate(DoubleFilter cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public DoubleFilter getLastLat() {
        return lastLat;
    }

    public Optional<DoubleFilter> optionalLastLat() {
        return Optional.ofNullable(lastLat);
    }

    public DoubleFilter lastLat() {
        if (lastLat == null) {
            setLastLat(new DoubleFilter());
        }
        return lastLat;
    }

    public void setLastLat(DoubleFilter lastLat) {
        this.lastLat = lastLat;
    }

    public DoubleFilter getLastLng() {
        return lastLng;
    }

    public Optional<DoubleFilter> optionalLastLng() {
        return Optional.ofNullable(lastLng);
    }

    public DoubleFilter lastLng() {
        if (lastLng == null) {
            setLastLng(new DoubleFilter());
        }
        return lastLng;
    }

    public void setLastLng(DoubleFilter lastLng) {
        this.lastLng = lastLng;
    }

    public InstantFilter getLastLocationAt() {
        return lastLocationAt;
    }

    public Optional<InstantFilter> optionalLastLocationAt() {
        return Optional.ofNullable(lastLocationAt);
    }

    public InstantFilter lastLocationAt() {
        if (lastLocationAt == null) {
            setLastLocationAt(new InstantFilter());
        }
        return lastLocationAt;
    }

    public void setLastLocationAt(InstantFilter lastLocationAt) {
        this.lastLocationAt = lastLocationAt;
    }

    public InstantFilter getJoinedAt() {
        return joinedAt;
    }

    public Optional<InstantFilter> optionalJoinedAt() {
        return Optional.ofNullable(joinedAt);
    }

    public InstantFilter joinedAt() {
        if (joinedAt == null) {
            setJoinedAt(new InstantFilter());
        }
        return joinedAt;
    }

    public void setJoinedAt(InstantFilter joinedAt) {
        this.joinedAt = joinedAt;
    }

    public BigDecimalFilter getCashInHand() {
        return cashInHand;
    }

    public Optional<BigDecimalFilter> optionalCashInHand() {
        return Optional.ofNullable(cashInHand);
    }

    public BigDecimalFilter cashInHand() {
        if (cashInHand == null) {
            setCashInHand(new BigDecimalFilter());
        }
        return cashInHand;
    }

    public void setCashInHand(BigDecimalFilter cashInHand) {
        this.cashInHand = cashInHand;
    }

    public BigDecimalFilter getCashLimit() {
        return cashLimit;
    }

    public Optional<BigDecimalFilter> optionalCashLimit() {
        return Optional.ofNullable(cashLimit);
    }

    public BigDecimalFilter cashLimit() {
        if (cashLimit == null) {
            setCashLimit(new BigDecimalFilter());
        }
        return cashLimit;
    }

    public void setCashLimit(BigDecimalFilter cashLimit) {
        this.cashLimit = cashLimit;
    }

    public IntegerFilter getMaxDailyJobs() {
        return maxDailyJobs;
    }

    public Optional<IntegerFilter> optionalMaxDailyJobs() {
        return Optional.ofNullable(maxDailyJobs);
    }

    public IntegerFilter maxDailyJobs() {
        if (maxDailyJobs == null) {
            setMaxDailyJobs(new IntegerFilter());
        }
        return maxDailyJobs;
    }

    public void setMaxDailyJobs(IntegerFilter maxDailyJobs) {
        this.maxDailyJobs = maxDailyJobs;
    }

    public StringFilter getLanguages() {
        return languages;
    }

    public Optional<StringFilter> optionalLanguages() {
        return Optional.ofNullable(languages);
    }

    public StringFilter languages() {
        if (languages == null) {
            setLanguages(new StringFilter());
        }
        return languages;
    }

    public void setLanguages(StringFilter languages) {
        this.languages = languages;
    }

    public StringFilter getBankAccountEnc() {
        return bankAccountEnc;
    }

    public Optional<StringFilter> optionalBankAccountEnc() {
        return Optional.ofNullable(bankAccountEnc);
    }

    public StringFilter bankAccountEnc() {
        if (bankAccountEnc == null) {
            setBankAccountEnc(new StringFilter());
        }
        return bankAccountEnc;
    }

    public void setBankAccountEnc(StringFilter bankAccountEnc) {
        this.bankAccountEnc = bankAccountEnc;
    }

    public StringFilter getIfscCode() {
        return ifscCode;
    }

    public Optional<StringFilter> optionalIfscCode() {
        return Optional.ofNullable(ifscCode);
    }

    public StringFilter ifscCode() {
        if (ifscCode == null) {
            setIfscCode(new StringFilter());
        }
        return ifscCode;
    }

    public void setIfscCode(StringFilter ifscCode) {
        this.ifscCode = ifscCode;
    }

    public InstantFilter getDeletedAt() {
        return deletedAt;
    }

    public Optional<InstantFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public InstantFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new InstantFilter());
        }
        return deletedAt;
    }

    public void setDeletedAt(InstantFilter deletedAt) {
        this.deletedAt = deletedAt;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getKycDocumentId() {
        return kycDocumentId;
    }

    public Optional<LongFilter> optionalKycDocumentId() {
        return Optional.ofNullable(kycDocumentId);
    }

    public LongFilter kycDocumentId() {
        if (kycDocumentId == null) {
            setKycDocumentId(new LongFilter());
        }
        return kycDocumentId;
    }

    public void setKycDocumentId(LongFilter kycDocumentId) {
        this.kycDocumentId = kycDocumentId;
    }

    public LongFilter getSkillId() {
        return skillId;
    }

    public Optional<LongFilter> optionalSkillId() {
        return Optional.ofNullable(skillId);
    }

    public LongFilter skillId() {
        if (skillId == null) {
            setSkillId(new LongFilter());
        }
        return skillId;
    }

    public void setSkillId(LongFilter skillId) {
        this.skillId = skillId;
    }

    public LongFilter getAvailabilityId() {
        return availabilityId;
    }

    public Optional<LongFilter> optionalAvailabilityId() {
        return Optional.ofNullable(availabilityId);
    }

    public LongFilter availabilityId() {
        if (availabilityId == null) {
            setAvailabilityId(new LongFilter());
        }
        return availabilityId;
    }

    public void setAvailabilityId(LongFilter availabilityId) {
        this.availabilityId = availabilityId;
    }

    public LongFilter getTimeOffId() {
        return timeOffId;
    }

    public Optional<LongFilter> optionalTimeOffId() {
        return Optional.ofNullable(timeOffId);
    }

    public LongFilter timeOffId() {
        if (timeOffId == null) {
            setTimeOffId(new LongFilter());
        }
        return timeOffId;
    }

    public void setTimeOffId(LongFilter timeOffId) {
        this.timeOffId = timeOffId;
    }

    public LongFilter getHomeCityId() {
        return homeCityId;
    }

    public Optional<LongFilter> optionalHomeCityId() {
        return Optional.ofNullable(homeCityId);
    }

    public LongFilter homeCityId() {
        if (homeCityId == null) {
            setHomeCityId(new LongFilter());
        }
        return homeCityId;
    }

    public void setHomeCityId(LongFilter homeCityId) {
        this.homeCityId = homeCityId;
    }

    public LongFilter getTierId() {
        return tierId;
    }

    public Optional<LongFilter> optionalTierId() {
        return Optional.ofNullable(tierId);
    }

    public LongFilter tierId() {
        if (tierId == null) {
            setTierId(new LongFilter());
        }
        return tierId;
    }

    public void setTierId(LongFilter tierId) {
        this.tierId = tierId;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public Optional<LongFilter> optionalZoneId() {
        return Optional.ofNullable(zoneId);
    }

    public LongFilter zoneId() {
        if (zoneId == null) {
            setZoneId(new LongFilter());
        }
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    public LongFilter getProfessionalWalletId() {
        return professionalWalletId;
    }

    public Optional<LongFilter> optionalProfessionalWalletId() {
        return Optional.ofNullable(professionalWalletId);
    }

    public LongFilter professionalWalletId() {
        if (professionalWalletId == null) {
            setProfessionalWalletId(new LongFilter());
        }
        return professionalWalletId;
    }

    public void setProfessionalWalletId(LongFilter professionalWalletId) {
        this.professionalWalletId = professionalWalletId;
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
        final ProfessionalCriteria that = (ProfessionalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(onboardingStatus, that.onboardingStatus) &&
            Objects.equals(online, that.online) &&
            Objects.equals(avgRating, that.avgRating) &&
            Objects.equals(jobsCompleted, that.jobsCompleted) &&
            Objects.equals(cancellationRate, that.cancellationRate) &&
            Objects.equals(lastLat, that.lastLat) &&
            Objects.equals(lastLng, that.lastLng) &&
            Objects.equals(lastLocationAt, that.lastLocationAt) &&
            Objects.equals(joinedAt, that.joinedAt) &&
            Objects.equals(cashInHand, that.cashInHand) &&
            Objects.equals(cashLimit, that.cashLimit) &&
            Objects.equals(maxDailyJobs, that.maxDailyJobs) &&
            Objects.equals(languages, that.languages) &&
            Objects.equals(bankAccountEnc, that.bankAccountEnc) &&
            Objects.equals(ifscCode, that.ifscCode) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(kycDocumentId, that.kycDocumentId) &&
            Objects.equals(skillId, that.skillId) &&
            Objects.equals(availabilityId, that.availabilityId) &&
            Objects.equals(timeOffId, that.timeOffId) &&
            Objects.equals(homeCityId, that.homeCityId) &&
            Objects.equals(tierId, that.tierId) &&
            Objects.equals(zoneId, that.zoneId) &&
            Objects.equals(professionalWalletId, that.professionalWalletId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            displayName,
            photoUrl,
            gender,
            onboardingStatus,
            online,
            avgRating,
            jobsCompleted,
            cancellationRate,
            lastLat,
            lastLng,
            lastLocationAt,
            joinedAt,
            cashInHand,
            cashLimit,
            maxDailyJobs,
            languages,
            bankAccountEnc,
            ifscCode,
            deletedAt,
            createdAt,
            updatedAt,
            userId,
            kycDocumentId,
            skillId,
            availabilityId,
            timeOffId,
            homeCityId,
            tierId,
            zoneId,
            professionalWalletId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDisplayName().map(f -> "displayName=" + f + ", ").orElse("") +
            optionalPhotoUrl().map(f -> "photoUrl=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalOnboardingStatus().map(f -> "onboardingStatus=" + f + ", ").orElse("") +
            optionalOnline().map(f -> "online=" + f + ", ").orElse("") +
            optionalAvgRating().map(f -> "avgRating=" + f + ", ").orElse("") +
            optionalJobsCompleted().map(f -> "jobsCompleted=" + f + ", ").orElse("") +
            optionalCancellationRate().map(f -> "cancellationRate=" + f + ", ").orElse("") +
            optionalLastLat().map(f -> "lastLat=" + f + ", ").orElse("") +
            optionalLastLng().map(f -> "lastLng=" + f + ", ").orElse("") +
            optionalLastLocationAt().map(f -> "lastLocationAt=" + f + ", ").orElse("") +
            optionalJoinedAt().map(f -> "joinedAt=" + f + ", ").orElse("") +
            optionalCashInHand().map(f -> "cashInHand=" + f + ", ").orElse("") +
            optionalCashLimit().map(f -> "cashLimit=" + f + ", ").orElse("") +
            optionalMaxDailyJobs().map(f -> "maxDailyJobs=" + f + ", ").orElse("") +
            optionalLanguages().map(f -> "languages=" + f + ", ").orElse("") +
            optionalBankAccountEnc().map(f -> "bankAccountEnc=" + f + ", ").orElse("") +
            optionalIfscCode().map(f -> "ifscCode=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalKycDocumentId().map(f -> "kycDocumentId=" + f + ", ").orElse("") +
            optionalSkillId().map(f -> "skillId=" + f + ", ").orElse("") +
            optionalAvailabilityId().map(f -> "availabilityId=" + f + ", ").orElse("") +
            optionalTimeOffId().map(f -> "timeOffId=" + f + ", ").orElse("") +
            optionalHomeCityId().map(f -> "homeCityId=" + f + ", ").orElse("") +
            optionalTierId().map(f -> "tierId=" + f + ", ").orElse("") +
            optionalZoneId().map(f -> "zoneId=" + f + ", ").orElse("") +
            optionalProfessionalWalletId().map(f -> "professionalWalletId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
