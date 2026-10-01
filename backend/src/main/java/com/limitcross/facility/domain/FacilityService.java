package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Replaces facility_service. 'code' keeps the old string ids like hm-ac.
 */
@Entity
@Table(name = "facility_service")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 120)
    @Column(name = "title", length = 120, nullable = false)
    private String title;

    @NotNull
    @Size(max = 140)
    @Column(name = "slug", length = 140, nullable = false, unique = true)
    private String slug;

    @Size(max = 10)
    @Column(name = "emoji", length = 10)
    private String emoji;

    @Size(max = 255)
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @NotNull
    @Size(max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Lob
    @Column(name = "highlights")
    private String highlights;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Min(value = 0)
    @Column(name = "warranty_days")
    private Integer warrantyDays;

    @Size(max = 10)
    @Column(name = "sac_code", length = 10)
    private String sacCode;

    @DecimalMin(value = "0")
    @Column(name = "gst_percent", precision = 21, scale = 2)
    private BigDecimal gstPercent;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_specific")
    private Gender genderSpecific;

    @Column(name = "requires_visit_charge")
    private Boolean requiresVisitCharge;

    @Column(name = "popular")
    private Boolean popular;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "avg_rating")
    private Double avgRating;

    @Min(value = 0)
    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private Set<ServicePackage> servicePackages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private Set<ServiceAddon> addons = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private Set<ServiceTranslation> translations = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FacilityService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public FacilityService code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public FacilityService title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public FacilityService slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public FacilityService emoji(String emoji) {
        this.setEmoji(emoji);
        return this;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public FacilityService imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public FacilityService description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlights() {
        return this.highlights;
    }

    public FacilityService highlights(String highlights) {
        this.setHighlights(highlights);
        return this;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public Integer getDurationMinutes() {
        return this.durationMinutes;
    }

    public FacilityService durationMinutes(Integer durationMinutes) {
        this.setDurationMinutes(durationMinutes);
        return this;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getWarrantyDays() {
        return this.warrantyDays;
    }

    public FacilityService warrantyDays(Integer warrantyDays) {
        this.setWarrantyDays(warrantyDays);
        return this;
    }

    public void setWarrantyDays(Integer warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public String getSacCode() {
        return this.sacCode;
    }

    public FacilityService sacCode(String sacCode) {
        this.setSacCode(sacCode);
        return this;
    }

    public void setSacCode(String sacCode) {
        this.sacCode = sacCode;
    }

    public BigDecimal getGstPercent() {
        return this.gstPercent;
    }

    public FacilityService gstPercent(BigDecimal gstPercent) {
        this.setGstPercent(gstPercent);
        return this;
    }

    public void setGstPercent(BigDecimal gstPercent) {
        this.gstPercent = gstPercent;
    }

    public Gender getGenderSpecific() {
        return this.genderSpecific;
    }

    public FacilityService genderSpecific(Gender genderSpecific) {
        this.setGenderSpecific(genderSpecific);
        return this;
    }

    public void setGenderSpecific(Gender genderSpecific) {
        this.genderSpecific = genderSpecific;
    }

    public Boolean getRequiresVisitCharge() {
        return this.requiresVisitCharge;
    }

    public FacilityService requiresVisitCharge(Boolean requiresVisitCharge) {
        this.setRequiresVisitCharge(requiresVisitCharge);
        return this;
    }

    public void setRequiresVisitCharge(Boolean requiresVisitCharge) {
        this.requiresVisitCharge = requiresVisitCharge;
    }

    public Boolean getPopular() {
        return this.popular;
    }

    public FacilityService popular(Boolean popular) {
        this.setPopular(popular);
        return this;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Boolean getActive() {
        return this.active;
    }

    public FacilityService active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getAvgRating() {
        return this.avgRating;
    }

    public FacilityService avgRating(Double avgRating) {
        this.setAvgRating(avgRating);
        return this;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getReviewsCount() {
        return this.reviewsCount;
    }

    public FacilityService reviewsCount(Integer reviewsCount) {
        this.setReviewsCount(reviewsCount);
        return this;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public FacilityService sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public FacilityService createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public FacilityService updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ServicePackage> getServicePackages() {
        return this.servicePackages;
    }

    public void setServicePackages(Set<ServicePackage> servicePackages) {
        if (this.servicePackages != null) {
            this.servicePackages.forEach(i -> i.setService(null));
        }
        if (servicePackages != null) {
            servicePackages.forEach(i -> i.setService(this));
        }
        this.servicePackages = servicePackages;
    }

    public FacilityService servicePackages(Set<ServicePackage> servicePackages) {
        this.setServicePackages(servicePackages);
        return this;
    }

    public FacilityService addServicePackage(ServicePackage servicePackage) {
        this.servicePackages.add(servicePackage);
        servicePackage.setService(this);
        return this;
    }

    public FacilityService removeServicePackage(ServicePackage servicePackage) {
        this.servicePackages.remove(servicePackage);
        servicePackage.setService(null);
        return this;
    }

    public Set<ServiceAddon> getAddons() {
        return this.addons;
    }

    public void setAddons(Set<ServiceAddon> serviceAddons) {
        if (this.addons != null) {
            this.addons.forEach(i -> i.setService(null));
        }
        if (serviceAddons != null) {
            serviceAddons.forEach(i -> i.setService(this));
        }
        this.addons = serviceAddons;
    }

    public FacilityService addons(Set<ServiceAddon> serviceAddons) {
        this.setAddons(serviceAddons);
        return this;
    }

    public FacilityService addAddon(ServiceAddon serviceAddon) {
        this.addons.add(serviceAddon);
        serviceAddon.setService(this);
        return this;
    }

    public FacilityService removeAddon(ServiceAddon serviceAddon) {
        this.addons.remove(serviceAddon);
        serviceAddon.setService(null);
        return this;
    }

    public Set<ServiceTranslation> getTranslations() {
        return this.translations;
    }

    public void setTranslations(Set<ServiceTranslation> serviceTranslations) {
        if (this.translations != null) {
            this.translations.forEach(i -> i.setService(null));
        }
        if (serviceTranslations != null) {
            serviceTranslations.forEach(i -> i.setService(this));
        }
        this.translations = serviceTranslations;
    }

    public FacilityService translations(Set<ServiceTranslation> serviceTranslations) {
        this.setTranslations(serviceTranslations);
        return this;
    }

    public FacilityService addTranslation(ServiceTranslation serviceTranslation) {
        this.translations.add(serviceTranslation);
        serviceTranslation.setService(this);
        return this;
    }

    public FacilityService removeTranslation(ServiceTranslation serviceTranslation) {
        this.translations.remove(serviceTranslation);
        serviceTranslation.setService(null);
        return this;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public FacilityService category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilityService)) {
            return false;
        }
        return getId() != null && getId().equals(((FacilityService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityService{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", emoji='" + getEmoji() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", highlights='" + getHighlights() + "'" +
            ", durationMinutes=" + getDurationMinutes() +
            ", warrantyDays=" + getWarrantyDays() +
            ", sacCode='" + getSacCode() + "'" +
            ", gstPercent=" + getGstPercent() +
            ", genderSpecific='" + getGenderSpecific() + "'" +
            ", requiresVisitCharge='" + getRequiresVisitCharge() + "'" +
            ", popular='" + getPopular() + "'" +
            ", active='" + getActive() + "'" +
            ", avgRating=" + getAvgRating() +
            ", reviewsCount=" + getReviewsCount() +
            ", sortOrder=" + getSortOrder() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
