package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.NotificationChannel;
import com.limitcross.facility.domain.enumeration.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @NotNull
    @Size(max = 40)
    @Column(name = "notification_type", length = 40, nullable = false)
    private String notificationType;

    @Size(max = 150)
    @Column(name = "title", length = 150)
    private String title;

    @NotNull
    @Size(max = 500)
    @Column(name = "body", length = 500, nullable = false)
    private String body;

    @Lob
    @Column(name = "data")
    private String data;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "read_at")
    private Instant readAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationChannel getChannel() {
        return this.channel;
    }

    public Notification channel(NotificationChannel channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getNotificationType() {
        return this.notificationType;
    }

    public Notification notificationType(String notificationType) {
        this.setNotificationType(notificationType);
        return this;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return this.title;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public Notification body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getData() {
        return this.data;
    }

    public Notification data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public Notification status(NotificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Notification createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public Notification sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getReadAt() {
        return this.readAt;
    }

    public Notification readAt(Instant readAt) {
        this.setReadAt(readAt);
        return this;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", channel='" + getChannel() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", data='" + getData() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            "}";
    }
}
