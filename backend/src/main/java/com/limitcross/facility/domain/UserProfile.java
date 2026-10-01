package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "phone_verified", nullable = false)
    private Boolean phoneVerified;

    @NotNull
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @Size(max = 10)
    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage;

    @Size(max = 20)
    @Column(name = "referral_code", length = 20, unique = true)
    private String referralCode;

    @Column(name = "marketing_opt_in")
    private Boolean marketingOptIn;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User referredBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return this.gender;
    }

    public UserProfile gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public UserProfile dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getPhoneVerified() {
        return this.phoneVerified;
    }

    public UserProfile phoneVerified(Boolean phoneVerified) {
        this.setPhoneVerified(phoneVerified);
        return this;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Boolean getEmailVerified() {
        return this.emailVerified;
    }

    public UserProfile emailVerified(Boolean emailVerified) {
        this.setEmailVerified(emailVerified);
        return this;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    public UserProfile preferredLanguage(String preferredLanguage) {
        this.setPreferredLanguage(preferredLanguage);
        return this;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getReferralCode() {
        return this.referralCode;
    }

    public UserProfile referralCode(String referralCode) {
        this.setReferralCode(referralCode);
        return this;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Boolean getMarketingOptIn() {
        return this.marketingOptIn;
    }

    public UserProfile marketingOptIn(Boolean marketingOptIn) {
        this.setMarketingOptIn(marketingOptIn);
        return this;
    }

    public void setMarketingOptIn(Boolean marketingOptIn) {
        this.marketingOptIn = marketingOptIn;
    }

    public Instant getLastLoginAt() {
        return this.lastLoginAt;
    }

    public UserProfile lastLoginAt(Instant lastLoginAt) {
        this.setLastLoginAt(lastLoginAt);
        return this;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserProfile createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserProfile updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile user(User user) {
        this.setUser(user);
        return this;
    }

    public User getReferredBy() {
        return this.referredBy;
    }

    public void setReferredBy(User user) {
        this.referredBy = user;
    }

    public UserProfile referredBy(User user) {
        this.setReferredBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
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
            "}";
    }
}
