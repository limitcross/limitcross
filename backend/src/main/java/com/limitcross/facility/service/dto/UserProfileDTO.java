package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.Gender;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    private Gender gender;

    private LocalDate dateOfBirth;

    @NotNull
    private Boolean phoneVerified;

    @NotNull
    private Boolean emailVerified;

    @Size(max = 10)
    private String preferredLanguage;

    @Size(max = 20)
    private String referralCode;

    private Boolean marketingOptIn;

    private Instant lastLoginAt;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private UserDTO user;

    private UserDTO referredBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Boolean getMarketingOptIn() {
        return marketingOptIn;
    }

    public void setMarketingOptIn(Boolean marketingOptIn) {
        this.marketingOptIn = marketingOptIn;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
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

    public UserDTO getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(UserDTO referredBy) {
        this.referredBy = referredBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", phoneVerified='" + getPhoneVerified() + "'" +
            ", emailVerified='" + getEmailVerified() + "'" +
            ", preferredLanguage='" + getPreferredLanguage() + "'" +
            ", referralCode='" + getReferralCode() + "'" +
            ", marketingOptIn='" + getMarketingOptIn() + "'" +
            ", lastLoginAt='" + getLastLoginAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", user=" + getUser() +
            ", referredBy=" + getReferredBy() +
            "}";
    }
}
