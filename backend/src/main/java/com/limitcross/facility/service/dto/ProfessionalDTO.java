package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.Gender;
import com.limitcross.facility.domain.enumeration.OnboardingStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Professional} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 120)
    private String displayName;

    @Size(max = 255)
    private String photoUrl;

    private Gender gender;

    @NotNull
    private OnboardingStatus onboardingStatus;

    @NotNull
    private Boolean online;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double avgRating;

    @Min(value = 0)
    private Integer jobsCompleted;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Double cancellationRate;

    private Double lastLat;

    private Double lastLng;

    private Instant lastLocationAt;

    private Instant joinedAt;

    private BigDecimal cashInHand;

    private BigDecimal cashLimit;

    @Min(value = 1)
    private Integer maxDailyJobs;

    @Size(max = 200)
    private String languages;

    @Size(max = 255)
    private String bankAccountEnc;

    @Size(max = 11)
    private String ifscCode;

    private Instant deletedAt;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private UserDTO user;

    @NotNull
    private CityDTO homeCity;

    private ProfessionalTierDTO tier;

    private Set<ServiceZoneDTO> zones = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public OnboardingStatus getOnboardingStatus() {
        return onboardingStatus;
    }

    public void setOnboardingStatus(OnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getJobsCompleted() {
        return jobsCompleted;
    }

    public void setJobsCompleted(Integer jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public Double getCancellationRate() {
        return cancellationRate;
    }

    public void setCancellationRate(Double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public Double getLastLat() {
        return lastLat;
    }

    public void setLastLat(Double lastLat) {
        this.lastLat = lastLat;
    }

    public Double getLastLng() {
        return lastLng;
    }

    public void setLastLng(Double lastLng) {
        this.lastLng = lastLng;
    }

    public Instant getLastLocationAt() {
        return lastLocationAt;
    }

    public void setLastLocationAt(Instant lastLocationAt) {
        this.lastLocationAt = lastLocationAt;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public BigDecimal getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(BigDecimal cashInHand) {
        this.cashInHand = cashInHand;
    }

    public BigDecimal getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(BigDecimal cashLimit) {
        this.cashLimit = cashLimit;
    }

    public Integer getMaxDailyJobs() {
        return maxDailyJobs;
    }

    public void setMaxDailyJobs(Integer maxDailyJobs) {
        this.maxDailyJobs = maxDailyJobs;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getBankAccountEnc() {
        return bankAccountEnc;
    }

    public void setBankAccountEnc(String bankAccountEnc) {
        this.bankAccountEnc = bankAccountEnc;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CityDTO getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(CityDTO homeCity) {
        this.homeCity = homeCity;
    }

    public ProfessionalTierDTO getTier() {
        return tier;
    }

    public void setTier(ProfessionalTierDTO tier) {
        this.tier = tier;
    }

    public Set<ServiceZoneDTO> getZones() {
        return zones;
    }

    public void setZones(Set<ServiceZoneDTO> zones) {
        this.zones = zones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalDTO)) {
            return false;
        }

        ProfessionalDTO professionalDTO = (ProfessionalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalDTO{" +
            "id=" + getId() +
            ", displayName='" + getDisplayName() + "'" +
            ", photoUrl='" + getPhotoUrl() + "'" +
            ", gender='" + getGender() + "'" +
            ", onboardingStatus='" + getOnboardingStatus() + "'" +
            ", online='" + getOnline() + "'" +
            ", avgRating=" + getAvgRating() +
            ", jobsCompleted=" + getJobsCompleted() +
            ", cancellationRate=" + getCancellationRate() +
            ", lastLat=" + getLastLat() +
            ", lastLng=" + getLastLng() +
            ", lastLocationAt='" + getLastLocationAt() + "'" +
            ", joinedAt='" + getJoinedAt() + "'" +
            ", cashInHand=" + getCashInHand() +
            ", cashLimit=" + getCashLimit() +
            ", maxDailyJobs=" + getMaxDailyJobs() +
            ", languages='" + getLanguages() + "'" +
            ", bankAccountEnc='" + getBankAccountEnc() + "'" +
            ", ifscCode='" + getIfscCode() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", user=" + getUser() +
            ", homeCity=" + getHomeCity() +
            ", tier=" + getTier() +
            ", zones=" + getZones() +
            "}";
    }
}
