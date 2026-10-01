package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.TicketCategory;
import com.limitcross.facility.domain.enumeration.TicketPriority;
import com.limitcross.facility.domain.enumeration.TicketStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.SupportTicket} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.SupportTicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /support-tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportTicketCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TicketCategory
     */
    public static class TicketCategoryFilter extends Filter<TicketCategory> {

        public TicketCategoryFilter() {}

        public TicketCategoryFilter(TicketCategoryFilter filter) {
            super(filter);
        }

        @Override
        public TicketCategoryFilter copy() {
            return new TicketCategoryFilter(this);
        }
    }

    /**
     * Class for filtering TicketStatus
     */
    public static class TicketStatusFilter extends Filter<TicketStatus> {

        public TicketStatusFilter() {}

        public TicketStatusFilter(TicketStatusFilter filter) {
            super(filter);
        }

        @Override
        public TicketStatusFilter copy() {
            return new TicketStatusFilter(this);
        }
    }

    /**
     * Class for filtering TicketPriority
     */
    public static class TicketPriorityFilter extends Filter<TicketPriority> {

        public TicketPriorityFilter() {}

        public TicketPriorityFilter(TicketPriorityFilter filter) {
            super(filter);
        }

        @Override
        public TicketPriorityFilter copy() {
            return new TicketPriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ticketNo;

    private TicketCategoryFilter category;

    private StringFilter subject;

    private StringFilter description;

    private TicketStatusFilter status;

    private TicketPriorityFilter priority;

    private InstantFilter createdAt;

    private InstantFilter resolvedAt;

    private LongFilter userId;

    private LongFilter assignedToId;

    private LongFilter bookingId;

    private Boolean distinct;

    public SupportTicketCriteria() {}

    public SupportTicketCriteria(SupportTicketCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ticketNo = other.optionalTicketNo().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(TicketCategoryFilter::copy).orElse(null);
        this.subject = other.optionalSubject().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TicketStatusFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(TicketPriorityFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.resolvedAt = other.optionalResolvedAt().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.assignedToId = other.optionalAssignedToId().map(LongFilter::copy).orElse(null);
        this.bookingId = other.optionalBookingId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupportTicketCriteria copy() {
        return new SupportTicketCriteria(this);
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

    public StringFilter getTicketNo() {
        return ticketNo;
    }

    public Optional<StringFilter> optionalTicketNo() {
        return Optional.ofNullable(ticketNo);
    }

    public StringFilter ticketNo() {
        if (ticketNo == null) {
            setTicketNo(new StringFilter());
        }
        return ticketNo;
    }

    public void setTicketNo(StringFilter ticketNo) {
        this.ticketNo = ticketNo;
    }

    public TicketCategoryFilter getCategory() {
        return category;
    }

    public Optional<TicketCategoryFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public TicketCategoryFilter category() {
        if (category == null) {
            setCategory(new TicketCategoryFilter());
        }
        return category;
    }

    public void setCategory(TicketCategoryFilter category) {
        this.category = category;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public Optional<StringFilter> optionalSubject() {
        return Optional.ofNullable(subject);
    }

    public StringFilter subject() {
        if (subject == null) {
            setSubject(new StringFilter());
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
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

    public TicketStatusFilter getStatus() {
        return status;
    }

    public Optional<TicketStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TicketStatusFilter status() {
        if (status == null) {
            setStatus(new TicketStatusFilter());
        }
        return status;
    }

    public void setStatus(TicketStatusFilter status) {
        this.status = status;
    }

    public TicketPriorityFilter getPriority() {
        return priority;
    }

    public Optional<TicketPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public TicketPriorityFilter priority() {
        if (priority == null) {
            setPriority(new TicketPriorityFilter());
        }
        return priority;
    }

    public void setPriority(TicketPriorityFilter priority) {
        this.priority = priority;
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

    public InstantFilter getResolvedAt() {
        return resolvedAt;
    }

    public Optional<InstantFilter> optionalResolvedAt() {
        return Optional.ofNullable(resolvedAt);
    }

    public InstantFilter resolvedAt() {
        if (resolvedAt == null) {
            setResolvedAt(new InstantFilter());
        }
        return resolvedAt;
    }

    public void setResolvedAt(InstantFilter resolvedAt) {
        this.resolvedAt = resolvedAt;
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

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public Optional<LongFilter> optionalAssignedToId() {
        return Optional.ofNullable(assignedToId);
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            setAssignedToId(new LongFilter());
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
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
        final SupportTicketCriteria that = (SupportTicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ticketNo, that.ticketNo) &&
            Objects.equals(category, that.category) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(resolvedAt, that.resolvedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ticketNo,
            category,
            subject,
            description,
            status,
            priority,
            createdAt,
            resolvedAt,
            userId,
            assignedToId,
            bookingId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportTicketCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTicketNo().map(f -> "ticketNo=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalSubject().map(f -> "subject=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalResolvedAt().map(f -> "resolvedAt=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalAssignedToId().map(f -> "assignedToId=" + f + ", ").orElse("") +
            optionalBookingId().map(f -> "bookingId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
