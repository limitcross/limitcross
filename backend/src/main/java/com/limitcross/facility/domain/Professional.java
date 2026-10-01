package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.Gender;
import com.limitcross.facility.domain.enumeration.OnboardingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Professional.
 */
@Entity
@Table(name = "professional")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(name = "display_name", length = 120, nullable = false)
    private String displayName;

    @Size(max = 255)
    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_status", nullable = false)
    private OnboardingStatus onboardingStatus;

    @NotNull
    @Column(name = "online", nullable = false)
    private Boolean online;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "avg_rating")
    private Double avgRating;

    @Min(value = 0)
    @Column(name = "jobs_completed")
    private Integer jobsCompleted;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "cancellation_rate")
    private Double cancellationRate;

    @Column(name = "last_lat")
    private Double lastLat;

    @Column(name = "last_lng")
    private Double lastLng;

    @Column(name = "last_location_at")
    private Instant lastLocationAt;

    @Column(name = "joined_at")
    private Instant joinedAt;

    @Column(name = "cash_in_hand", precision = 21, scale = 2)
    private BigDecimal cashInHand;

    @Column(name = "cash_limit", precision = 21, scale = 2)
    private BigDecimal cashLimit;

    @Min(value = 1)
    @Column(name = "max_daily_jobs")
    private Integer maxDailyJobs;

    @Size(max = 200)
    @Column(name = "languages", length = 200)
    private String languages;

    @Size(max = 255)
    @Column(name = "bank_account_enc", length = 255)
    private String bankAccountEnc;

    @Size(max = 11)
    @Column(name = "ifsc_code", length = 11)
    private String ifscCode;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professional")
    @JsonIgnoreProperties(value = { "reviewer", "professional" }, allowSetters = true)
    private Set<ProfessionalKycDocument> kycDocuments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professional")
    @JsonIgnoreProperties(value = { "service", "professional" }, allowSetters = true)
    private Set<ProfessionalSkill> skills = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professional")
    @JsonIgnoreProperties(value = { "professional" }, allowSetters = true)
    private Set<ProfessionalAvailability> availabilities = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professional")
    @JsonIgnoreProperties(value = { "booking", "professional" }, allowSetters = true)
    private Set<ProfessionalTimeOff> timeOffs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City homeCity;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProfessionalTier tier;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_professional__zone",
        joinColumns = @JoinColumn(name = "professional_id"),
        inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    @JsonIgnoreProperties(value = { "city", "professionals" }, allowSetters = true)
    private Set<ServiceZone> zones = new HashSet<>();

    @JsonIgnoreProperties(value = { "professional" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "professional")
    private ProfessionalWallet professionalWallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professional id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Professional displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public Professional photoUrl(String photoUrl) {
        this.setPhotoUrl(photoUrl);
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Professional gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public OnboardingStatus getOnboardingStatus() {
        return this.onboardingStatus;
    }

    public Professional onboardingStatus(OnboardingStatus onboardingStatus) {
        this.setOnboardingStatus(onboardingStatus);
        return this;
    }

    public void setOnboardingStatus(OnboardingStatus onboardingStatus) {
        this.onboardingStatus = onboardingStatus;
    }

    public Boolean getOnline() {
        return this.online;
    }

    public Professional online(Boolean online) {
        this.setOnline(online);
        return this;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Double getAvgRating() {
        return this.avgRating;
    }

    public Professional avgRating(Double avgRating) {
        this.setAvgRating(avgRating);
        return this;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getJobsCompleted() {
        return this.jobsCompleted;
    }

    public Professional jobsCompleted(Integer jobsCompleted) {
        this.setJobsCompleted(jobsCompleted);
        return this;
    }

    public void setJobsCompleted(Integer jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public Double getCancellationRate() {
        return this.cancellationRate;
    }

    public Professional cancellationRate(Double cancellationRate) {
        this.setCancellationRate(cancellationRate);
        return this;
    }

    public void setCancellationRate(Double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public Double getLastLat() {
        return this.lastLat;
    }

    public Professional lastLat(Double lastLat) {
        this.setLastLat(lastLat);
        return this;
    }

    public void setLastLat(Double lastLat) {
        this.lastLat = lastLat;
    }

    public Double getLastLng() {
        return this.lastLng;
    }

    public Professional lastLng(Double lastLng) {
        this.setLastLng(lastLng);
        return this;
    }

    public void setLastLng(Double lastLng) {
        this.lastLng = lastLng;
    }

    public Instant getLastLocationAt() {
        return this.lastLocationAt;
    }

    public Professional lastLocationAt(Instant lastLocationAt) {
        this.setLastLocationAt(lastLocationAt);
        return this;
    }

    public void setLastLocationAt(Instant lastLocationAt) {
        this.lastLocationAt = lastLocationAt;
    }

    public Instant getJoinedAt() {
        return this.joinedAt;
    }

    public Professional joinedAt(Instant joinedAt) {
        this.setJoinedAt(joinedAt);
        return this;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public BigDecimal getCashInHand() {
        return this.cashInHand;
    }

    public Professional cashInHand(BigDecimal cashInHand) {
        this.setCashInHand(cashInHand);
        return this;
    }

    public void setCashInHand(BigDecimal cashInHand) {
        this.cashInHand = cashInHand;
    }

    public BigDecimal getCashLimit() {
        return this.cashLimit;
    }

    public Professional cashLimit(BigDecimal cashLimit) {
        this.setCashLimit(cashLimit);
        return this;
    }

    public void setCashLimit(BigDecimal cashLimit) {
        this.cashLimit = cashLimit;
    }

    public Integer getMaxDailyJobs() {
        return this.maxDailyJobs;
    }

    public Professional maxDailyJobs(Integer maxDailyJobs) {
        this.setMaxDailyJobs(maxDailyJobs);
        return this;
    }

    public void setMaxDailyJobs(Integer maxDailyJobs) {
        this.maxDailyJobs = maxDailyJobs;
    }

    public String getLanguages() {
        return this.languages;
    }

    public Professional languages(String languages) {
        this.setLanguages(languages);
        return this;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getBankAccountEnc() {
        return this.bankAccountEnc;
    }

    public Professional bankAccountEnc(String bankAccountEnc) {
        this.setBankAccountEnc(bankAccountEnc);
        return this;
    }

    public void setBankAccountEnc(String bankAccountEnc) {
        this.bankAccountEnc = bankAccountEnc;
    }

    public String getIfscCode() {
        return this.ifscCode;
    }

    public Professional ifscCode(String ifscCode) {
        this.setIfscCode(ifscCode);
        return this;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public Professional deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Professional createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Professional updatedAt(Instant updatedAt) {
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

    public Professional user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<ProfessionalKycDocument> getKycDocuments() {
        return this.kycDocuments;
    }

    public void setKycDocuments(Set<ProfessionalKycDocument> professionalKycDocuments) {
        if (this.kycDocuments != null) {
            this.kycDocuments.forEach(i -> i.setProfessional(null));
        }
        if (professionalKycDocuments != null) {
            professionalKycDocuments.forEach(i -> i.setProfessional(this));
        }
        this.kycDocuments = professionalKycDocuments;
    }

    public Professional kycDocuments(Set<ProfessionalKycDocument> professionalKycDocuments) {
        this.setKycDocuments(professionalKycDocuments);
        return this;
    }

    public Professional addKycDocument(ProfessionalKycDocument professionalKycDocument) {
        this.kycDocuments.add(professionalKycDocument);
        professionalKycDocument.setProfessional(this);
        return this;
    }

    public Professional removeKycDocument(ProfessionalKycDocument professionalKycDocument) {
        this.kycDocuments.remove(professionalKycDocument);
        professionalKycDocument.setProfessional(null);
        return this;
    }

    public Set<ProfessionalSkill> getSkills() {
        return this.skills;
    }

    public void setSkills(Set<ProfessionalSkill> professionalSkills) {
        if (this.skills != null) {
            this.skills.forEach(i -> i.setProfessional(null));
        }
        if (professionalSkills != null) {
            professionalSkills.forEach(i -> i.setProfessional(this));
        }
        this.skills = professionalSkills;
    }

    public Professional skills(Set<ProfessionalSkill> professionalSkills) {
        this.setSkills(professionalSkills);
        return this;
    }

    public Professional addSkill(ProfessionalSkill professionalSkill) {
        this.skills.add(professionalSkill);
        professionalSkill.setProfessional(this);
        return this;
    }

    public Professional removeSkill(ProfessionalSkill professionalSkill) {
        this.skills.remove(professionalSkill);
        professionalSkill.setProfessional(null);
        return this;
    }

    public Set<ProfessionalAvailability> getAvailabilities() {
        return this.availabilities;
    }

    public void setAvailabilities(Set<ProfessionalAvailability> professionalAvailabilities) {
        if (this.availabilities != null) {
            this.availabilities.forEach(i -> i.setProfessional(null));
        }
        if (professionalAvailabilities != null) {
            professionalAvailabilities.forEach(i -> i.setProfessional(this));
        }
        this.availabilities = professionalAvailabilities;
    }

    public Professional availabilities(Set<ProfessionalAvailability> professionalAvailabilities) {
        this.setAvailabilities(professionalAvailabilities);
        return this;
    }

    public Professional addAvailability(ProfessionalAvailability professionalAvailability) {
        this.availabilities.add(professionalAvailability);
        professionalAvailability.setProfessional(this);
        return this;
    }

    public Professional removeAvailability(ProfessionalAvailability professionalAvailability) {
        this.availabilities.remove(professionalAvailability);
        professionalAvailability.setProfessional(null);
        return this;
    }

    public Set<ProfessionalTimeOff> getTimeOffs() {
        return this.timeOffs;
    }

    public void setTimeOffs(Set<ProfessionalTimeOff> professionalTimeOffs) {
        if (this.timeOffs != null) {
            this.timeOffs.forEach(i -> i.setProfessional(null));
        }
        if (professionalTimeOffs != null) {
            professionalTimeOffs.forEach(i -> i.setProfessional(this));
        }
        this.timeOffs = professionalTimeOffs;
    }

    public Professional timeOffs(Set<ProfessionalTimeOff> professionalTimeOffs) {
        this.setTimeOffs(professionalTimeOffs);
        return this;
    }

    public Professional addTimeOff(ProfessionalTimeOff professionalTimeOff) {
        this.timeOffs.add(professionalTimeOff);
        professionalTimeOff.setProfessional(this);
        return this;
    }

    public Professional removeTimeOff(ProfessionalTimeOff professionalTimeOff) {
        this.timeOffs.remove(professionalTimeOff);
        professionalTimeOff.setProfessional(null);
        return this;
    }

    public City getHomeCity() {
        return this.homeCity;
    }

    public void setHomeCity(City city) {
        this.homeCity = city;
    }

    public Professional homeCity(City city) {
        this.setHomeCity(city);
        return this;
    }

    public ProfessionalTier getTier() {
        return this.tier;
    }

    public void setTier(ProfessionalTier professionalTier) {
        this.tier = professionalTier;
    }

    public Professional tier(ProfessionalTier professionalTier) {
        this.setTier(professionalTier);
        return this;
    }

    public Set<ServiceZone> getZones() {
        return this.zones;
    }

    public void setZones(Set<ServiceZone> serviceZones) {
        this.zones = serviceZones;
    }

    public Professional zones(Set<ServiceZone> serviceZones) {
        this.setZones(serviceZones);
        return this;
    }

    public Professional addZone(ServiceZone serviceZone) {
        this.zones.add(serviceZone);
        return this;
    }

    public Professional removeZone(ServiceZone serviceZone) {
        this.zones.remove(serviceZone);
        return this;
    }

    public ProfessionalWallet getProfessionalWallet() {
        return this.professionalWallet;
    }

    public void setProfessionalWallet(ProfessionalWallet professionalWallet) {
        if (this.professionalWallet != null) {
            this.professionalWallet.setProfessional(null);
        }
        if (professionalWallet != null) {
            professionalWallet.setProfessional(this);
        }
        this.professionalWallet = professionalWallet;
    }

    public Professional professionalWallet(ProfessionalWallet professionalWallet) {
        this.setProfessionalWallet(professionalWallet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professional)) {
            return false;
        }
        return getId() != null && getId().equals(((Professional) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professional{" +
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
            "}";
    }
}
