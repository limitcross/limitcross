package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.FacilityService} entity.
 */
@Schema(description = "Replaces facility_service. 'code' keeps the old string ids like hm-ac.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityServiceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String code;

    @NotNull
    @Size(max = 120)
    private String title;

    @NotNull
    @Size(max = 140)
    private String slug;

    @Size(max = 10)
    private String emoji;

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    @Size(max = 500)
    private String description;

    @Lob
    private String highlights;

    @NotNull
    @Min(value = 1)
    private Integer durationMinutes;

    @Min(value = 0)
    private Integer warrantyDays;

    @Size(max = 10)
    private String sacCode;

    @DecimalMin(value = "0")
    private BigDecimal gstPercent;

    private Gender genderSpecific;

    private Boolean requiresVisitCharge;

    private Boolean popular;

    @NotNull
    private Boolean active;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double avgRating;

    @Min(value = 0)
    private Integer reviewsCount;

    private Integer sortOrder;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private ServiceCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getWarrantyDays() {
        return warrantyDays;
    }

    public void setWarrantyDays(Integer warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public String getSacCode() {
        return sacCode;
    }

    public void setSacCode(String sacCode) {
        this.sacCode = sacCode;
    }

    public BigDecimal getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(BigDecimal gstPercent) {
        this.gstPercent = gstPercent;
    }

    public Gender getGenderSpecific() {
        return genderSpecific;
    }

    public void setGenderSpecific(Gender genderSpecific) {
        this.genderSpecific = genderSpecific;
    }

    public Boolean getRequiresVisitCharge() {
        return requiresVisitCharge;
    }

    public void setRequiresVisitCharge(Boolean requiresVisitCharge) {
        this.requiresVisitCharge = requiresVisitCharge;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacilityServiceDTO)) {
            return false;
        }

        FacilityServiceDTO facilityServiceDTO = (FacilityServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facilityServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityServiceDTO{" +
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
            ", category=" + getCategory() +
            "}";
    }
}
