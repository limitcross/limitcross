package com.limitcross.facility.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Review} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.ReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rating;

    private StringFilter reviewText;

    private StringFilter tags;

    private BooleanFilter visible;

    private InstantFilter createdAt;

    private LongFilter bookingId;

    private LongFilter customerId;

    private LongFilter professionalId;

    private LongFilter serviceId;

    private Boolean distinct;

    public ReviewCriteria() {}

    public ReviewCriteria(ReviewCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.rating = other.optionalRating().map(IntegerFilter::copy).orElse(null);
        this.reviewText = other.optionalReviewText().map(StringFilter::copy).orElse(null);
        this.tags = other.optionalTags().map(StringFilter::copy).orElse(null);
        this.visible = other.optionalVisible().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.bookingId = other.optionalBookingId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.professionalId = other.optionalProfessionalId().map(LongFilter::copy).orElse(null);
        this.serviceId = other.optionalServiceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReviewCriteria copy() {
        return new ReviewCriteria(this);
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

    public IntegerFilter getRating() {
        return rating;
    }

    public Optional<IntegerFilter> optionalRating() {
        return Optional.ofNullable(rating);
    }

    public IntegerFilter rating() {
        if (rating == null) {
            setRating(new IntegerFilter());
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public StringFilter getReviewText() {
        return reviewText;
    }

    public Optional<StringFilter> optionalReviewText() {
        return Optional.ofNullable(reviewText);
    }

    public StringFilter reviewText() {
        if (reviewText == null) {
            setReviewText(new StringFilter());
        }
        return reviewText;
    }

    public void setReviewText(StringFilter reviewText) {
        this.reviewText = reviewText;
    }

    public StringFilter getTags() {
        return tags;
    }

    public Optional<StringFilter> optionalTags() {
        return Optional.ofNullable(tags);
    }

    public StringFilter tags() {
        if (tags == null) {
            setTags(new StringFilter());
        }
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }

    public BooleanFilter getVisible() {
        return visible;
    }

    public Optional<BooleanFilter> optionalVisible() {
        return Optional.ofNullable(visible);
    }

    public BooleanFilter visible() {
        if (visible == null) {
            setVisible(new BooleanFilter());
        }
        return visible;
    }

    public void setVisible(BooleanFilter visible) {
        this.visible = visible;
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

    public LongFilter getBookingId() {
        return bookingId;
    }

    public Optional<LongFilter> optionalBookingId() {
        return Optional.ofNullable(bookingId);
    }

    public LongFilter bookingId() {
        if (bookingId == null) {
            setBookingId(new LongFilter());
        }
        return bookingId;
    }

    public void setBookingId(LongFilter bookingId) {
        this.bookingId = bookingId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getProfessionalId() {
        return professionalId;
    }

    public Optional<LongFilter> optionalProfessionalId() {
        return Optional.ofNullable(professionalId);
    }

    public LongFilter professionalId() {
        if (professionalId == null) {
            setProfessionalId(new LongFilter());
        }
        return professionalId;
    }

    public void setProfessionalId(LongFilter professionalId) {
        this.professionalId = professionalId;
    }

    public LongFilter getServiceId() {
        return serviceId;
    }

    public Optional<LongFilter> optionalServiceId() {
        return Optional.ofNullable(serviceId);
    }

    public LongFilter serviceId() {
        if (serviceId == null) {
            setServiceId(new LongFilter());
        }
        return serviceId;
    }

    public void setServiceId(LongFilter serviceId) {
        this.serviceId = serviceId;
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
        final ReviewCriteria that = (ReviewCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(reviewText, that.reviewText) &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(visible, that.visible) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(professionalId, that.professionalId) &&
            Objects.equals(serviceId, that.serviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, reviewText, tags, visible, createdAt, bookingId, customerId, professionalId, serviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRating().map(f -> "rating=" + f + ", ").orElse("") +
            optionalReviewText().map(f -> "reviewText=" + f + ", ").orElse("") +
            optionalTags().map(f -> "tags=" + f + ", ").orElse("") +
            optionalVisible().map(f -> "visible=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalBookingId().map(f -> "bookingId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalProfessionalId().map(f -> "professionalId=" + f + ", ").orElse("") +
            optionalServiceId().map(f -> "serviceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
