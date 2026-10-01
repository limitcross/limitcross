package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.NotificationChannel;
import com.limitcross.facility.domain.enumeration.NotificationStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.Notification} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationChannel
     */
    public static class NotificationChannelFilter extends Filter<NotificationChannel> {

        public NotificationChannelFilter() {}

        public NotificationChannelFilter(NotificationChannelFilter filter) {
            super(filter);
        }

        @Override
        public NotificationChannelFilter copy() {
            return new NotificationChannelFilter(this);
        }
    }

    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {}

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private NotificationChannelFilter channel;

    private StringFilter notificationType;

    private StringFilter title;

    private StringFilter body;

    private NotificationStatusFilter status;

    private InstantFilter createdAt;

    private InstantFilter sentAt;

    private InstantFilter readAt;

    private LongFilter userId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.channel = other.optionalChannel().map(NotificationChannelFilter::copy).orElse(null);
        this.notificationType = other.optionalNotificationType().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.body = other.optionalBody().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(NotificationStatusFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.sentAt = other.optionalSentAt().map(InstantFilter::copy).orElse(null);
        this.readAt = other.optionalReadAt().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public NotificationChannelFilter getChannel() {
        return channel;
    }

    public Optional<NotificationChannelFilter> optionalChannel() {
        return Optional.ofNullable(channel);
    }

    public NotificationChannelFilter channel() {
        if (channel == null) {
            setChannel(new NotificationChannelFilter());
        }
        return channel;
    }

    public void setChannel(NotificationChannelFilter channel) {
        this.channel = channel;
    }

    public StringFilter getNotificationType() {
        return notificationType;
    }

    public Optional<StringFilter> optionalNotificationType() {
        return Optional.ofNullable(notificationType);
    }

    public StringFilter notificationType() {
        if (notificationType == null) {
            setNotificationType(new StringFilter());
        }
        return notificationType;
    }

    public void setNotificationType(StringFilter notificationType) {
        this.notificationType = notificationType;
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

    public StringFilter getBody() {
        return body;
    }

    public Optional<StringFilter> optionalBody() {
        return Optional.ofNullable(body);
    }

    public StringFilter body() {
        if (body == null) {
            setBody(new StringFilter());
        }
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public NotificationStatusFilter getStatus() {
        return status;
    }

    public Optional<NotificationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public NotificationStatusFilter status() {
        if (status == null) {
            setStatus(new NotificationStatusFilter());
        }
        return status;
    }

    public void setStatus(NotificationStatusFilter status) {
        this.status = status;
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

    public InstantFilter getSentAt() {
        return sentAt;
    }

    public Optional<InstantFilter> optionalSentAt() {
        return Optional.ofNullable(sentAt);
    }

    public InstantFilter sentAt() {
        if (sentAt == null) {
            setSentAt(new InstantFilter());
        }
        return sentAt;
    }

    public void setSentAt(InstantFilter sentAt) {
        this.sentAt = sentAt;
    }

    public InstantFilter getReadAt() {
        return readAt;
    }

    public Optional<InstantFilter> optionalReadAt() {
        return Optional.ofNullable(readAt);
    }

    public InstantFilter readAt() {
        if (readAt == null) {
            setReadAt(new InstantFilter());
        }
        return readAt;
    }

    public void setReadAt(InstantFilter readAt) {
        this.readAt = readAt;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(notificationType, that.notificationType) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(readAt, that.readAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channel, notificationType, title, body, status, createdAt, sentAt, readAt, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalChannel().map(f -> "channel=" + f + ", ").orElse("") +
            optionalNotificationType().map(f -> "notificationType=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalBody().map(f -> "body=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalSentAt().map(f -> "sentAt=" + f + ", ").orElse("") +
            optionalReadAt().map(f -> "readAt=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
