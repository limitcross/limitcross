package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.FacilityService} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.FacilityServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facility-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityServiceCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter title;

    private StringFilter slug;

    private StringFilter emoji;

    private StringFilter imageUrl;

    private StringFilter description;

    private IntegerFilter durationMinutes;

    private IntegerFilter warrantyDays;

    private StringFilter sacCode;

    private BigDecimalFilter gstPercent;

    private GenderFilter genderSpecific;

    private BooleanFilter requiresVisitCharge;

    private BooleanFilter popular;

    private BooleanFilter active;

    private DoubleFilter avgRating;

    private IntegerFilter reviewsCount;

    private IntegerFilter sortOrder;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter servicePackageId;

    private LongFilter addonId;

    private LongFilter translationId;

    private LongFilter categoryId;

    private Boolean distinct;

    public FacilityServiceCriteria() {}

    public FacilityServiceCriteria(FacilityServiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.emoji = other.optionalEmoji().map(StringFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.durationMinutes = other.optionalDurationMinutes().map(IntegerFilter::copy).orElse(null);
        this.warrantyDays = other.optionalWarrantyDays().map(IntegerFilter::copy).orElse(null);
        this.sacCode = other.optionalSacCode().map(StringFilter::copy).orElse(null);
        this.gstPercent = other.optionalGstPercent().map(BigDecimalFilter::copy).orElse(null);
        this.genderSpecific = other.optionalGenderSpecific().map(GenderFilter::copy).orElse(null);
        this.requiresVisitCharge = other.optionalRequiresVisitCharge().map(BooleanFilter::copy).orElse(null);
        this.popular = other.optionalPopular().map(BooleanFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.avgRating = other.optionalAvgRating().map(DoubleFilter::copy).orElse(null);
        this.reviewsCount = other.optionalReviewsCount().map(IntegerFilter::copy).orElse(null);
        this.sortOrder = other.optionalSortOrder().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.servicePackageId = other.optionalServicePackageId().map(LongFilter::copy).orElse(null);
        this.addonId = other.optionalAddonId().map(LongFilter::copy).orElse(null);
        this.translationId = other.optionalTranslationId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacilityServiceCriteria copy() {
        return new FacilityServiceCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getEmoji() {
        return emoji;
    }

    public Optional<StringFilter> optionalEmoji() {
        return Optional.ofNullable(emoji);
    }

    public StringFilter emoji() {
        if (emoji == null) {
            setEmoji(new StringFilter());
        }
        return emoji;
    }

    public void setEmoji(StringFilter emoji) {
        this.emoji = emoji;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getDurationMinutes() {
        return durationMinutes;
    }

    public Optional<IntegerFilter> optionalDurationMinutes() {
        return Optional.ofNullable(durationMinutes);
    }

    public IntegerFilter durationMinutes() {
        if (durationMinutes == null) {
            setDurationMinutes(new IntegerFilter());
        }
        return durationMinutes;
    }

    public void setDurationMinutes(IntegerFilter durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public IntegerFilter getWarrantyDays() {
        return warrantyDays;
    }

    public Optional<IntegerFilter> optionalWarrantyDays() {
        return Optional.ofNullable(warrantyDays);
    }

    public IntegerFilter warrantyDays() {
        if (warrantyDays == null) {
            setWarrantyDays(new IntegerFilter());
        }
        return warrantyDays;
    }

    public void setWarrantyDays(IntegerFilter warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public StringFilter getSacCode() {
        return sacCode;
    }

    public Optional<StringFilter> optionalSacCode() {
        return Optional.ofNullable(sacCode);
    }

    public StringFilter sacCode() {
        if (sacCode == null) {
            setSacCode(new StringFilter());
        }
        return sacCode;
    }

    public void setSacCode(StringFilter sacCode) {
        this.sacCode = sacCode;
    }

    public BigDecimalFilter getGstPercent() {
        return gstPercent;
    }

    public Optional<BigDecimalFilter> optionalGstPercent() {
        return Optional.ofNullable(gstPercent);
    }

    public BigDecimalFilter gstPercent() {
        if (gstPercent == null) {
            setGstPercent(new BigDecimalFilter());
        }
        return gstPercent;
    }

    public void setGstPercent(BigDecimalFilter gstPercent) {
        this.gstPercent = gstPercent;
    }

    public GenderFilter getGenderSpecific() {
        return genderSpecific;
    }

    public Optional<GenderFilter> optionalGenderSpecific() {
        return Optional.ofNullable(genderSpecific);
    }

    public GenderFilter genderSpecific() {
        if (genderSpecific == null) {
            setGenderSpecific(new GenderFilter());
        }
        return genderSpecific;
    }

    public void setGenderSpecific(GenderFilter genderSpecific) {
        this.genderSpecific = genderSpecific;
    }

    public BooleanFilter getRequiresVisitCharge() {
        return requiresVisitCharge;
    }

    public Optional<BooleanFilter> optionalRequiresVisitCharge() {
        return Optional.ofNullable(requiresVisitCharge);
    }

    public BooleanFilter requiresVisitCharge() {
        if (requiresVisitCharge == null) {
            setRequiresVisitCharge(new BooleanFilter());
        }
        return requiresVisitCharge;
    }

    public void setRequiresVisitCharge(BooleanFilter requiresVisitCharge) {
        this.requiresVisitCharge = requiresVisitCharge;
    }

    public BooleanFilter getPopular() {
        return popular;
    }

    public Optional<BooleanFilter> optionalPopular() {
        return Optional.ofNullable(popular);
    }

    public BooleanFilter popular() {
        if (popular == null) {
            setPopular(new BooleanFilter());
        }
        return popular;
    }

    public void setPopular(BooleanFilter popular) {
        this.popular = popular;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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

    public IntegerFilter getReviewsCount() {
        return reviewsCount;
    }

    public Optional<IntegerFilter> optionalReviewsCount() {
        return Optional.ofNullable(reviewsCount);
    }

    public IntegerFilter reviewsCount() {
        if (reviewsCount == null) {
            setReviewsCount(new IntegerFilter());
        }
        return reviewsCount;
    }

    public void setReviewsCount(IntegerFilter reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public Optional<IntegerFilter> optionalSortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            setSortOrder(new IntegerFilter());
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
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

    public LongFilter getServicePackageId() {
        return servicePackageId;
    }

    public Optional<LongFilter> optionalServicePackageId() {
        return Optional.ofNullable(servicePackageId);
    }

    public LongFilter servicePackageId() {
        if (servicePackageId == null) {
            setServicePackageId(new LongFilter());
        }
        return servicePackageId;
    }

    public void setServicePackageId(LongFilter servicePackageId) {
        this.servicePackageId = servicePackageId;
    }

    public LongFilter getAddonId() {
        return addonId;
    }

    public Optional<LongFilter> optionalAddonId() {
        return Optional.ofNullable(addonId);
    }

    public LongFilter addonId() {
        if (addonId == null) {
            setAddonId(new LongFilter());
        }
        return addonId;
    }

    public void setAddonId(LongFilter addonId) {
        this.addonId = addonId;
    }

    public LongFilter getTranslationId() {
        return translationId;
    }

    public Optional<LongFilter> optionalTranslationId() {
        return Optional.ofNullable(translationId);
    }

    public LongFilter translationId() {
        if (translationId == null) {
            setTranslationId(new LongFilter());
        }
        return translationId;
    }

    public void setTranslationId(LongFilter translationId) {
        this.translationId = translationId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final FacilityServiceCriteria that = (FacilityServiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(title, that.title) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(emoji, that.emoji) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(description, that.description) &&
            Objects.equals(durationMinutes, that.durationMinutes) &&
            Objects.equals(warrantyDays, that.warrantyDays) &&
            Objects.equals(sacCode, that.sacCode) &&
            Objects.equals(gstPercent, that.gstPercent) &&
            Objects.equals(genderSpecific, that.genderSpecific) &&
            Objects.equals(requiresVisitCharge, that.requiresVisitCharge) &&
            Objects.equals(popular, that.popular) &&
            Objects.equals(active, that.active) &&
            Objects.equals(avgRating, that.avgRating) &&
            Objects.equals(reviewsCount, that.reviewsCount) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(servicePackageId, that.servicePackageId) &&
            Objects.equals(addonId, that.addonId) &&
            Objects.equals(translationId, that.translationId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            title,
            slug,
            emoji,
            imageUrl,
            description,
            durationMinutes,
            warrantyDays,
            sacCode,
            gstPercent,
            genderSpecific,
            requiresVisitCharge,
            popular,
            active,
            avgRating,
            reviewsCount,
            sortOrder,
            createdAt,
            updatedAt,
            servicePackageId,
            addonId,
            translationId,
            categoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityServiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalEmoji().map(f -> "emoji=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDurationMinutes().map(f -> "durationMinutes=" + f + ", ").orElse("") +
            optionalWarrantyDays().map(f -> "warrantyDays=" + f + ", ").orElse("") +
            optionalSacCode().map(f -> "sacCode=" + f + ", ").orElse("") +
            optionalGstPercent().map(f -> "gstPercent=" + f + ", ").orElse("") +
            optionalGenderSpecific().map(f -> "genderSpecific=" + f + ", ").orElse("") +
            optionalRequiresVisitCharge().map(f -> "requiresVisitCharge=" + f + ", ").orElse("") +
            optionalPopular().map(f -> "popular=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalAvgRating().map(f -> "avgRating=" + f + ", ").orElse("") +
            optionalReviewsCount().map(f -> "reviewsCount=" + f + ", ").orElse("") +
            optionalSortOrder().map(f -> "sortOrder=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalServicePackageId().map(f -> "servicePackageId=" + f + ", ").orElse("") +
            optionalAddonId().map(f -> "addonId=" + f + ", ").orElse("") +
            optionalTranslationId().map(f -> "translationId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
