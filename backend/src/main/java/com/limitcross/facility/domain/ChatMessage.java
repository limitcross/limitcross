package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ChatMessage.
 */
@Entity
@Table(name = "chat_message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 2000)
    @Column(name = "body", length = 2000)
    private String body;

    @Size(max = 255)
    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    @Column(name = "flagged")
    private Boolean flagged;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "read_at")
    private Instant readAt;

    @ManyToOne(optional = false)
    @NotNull
    private User sender;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "booking", "messages", "customer", "professional" }, allowSetters = true)
    private ChatThread thread;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return this.body;
    }

    public ChatMessage body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public ChatMessage mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Boolean getFlagged() {
        return this.flagged;
    }

    public ChatMessage flagged(Boolean flagged) {
        this.setFlagged(flagged);
        return this;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ChatMessage createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return this.readAt;
    }

    public ChatMessage readAt(Instant readAt) {
        this.setReadAt(readAt);
        return this;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    public ChatMessage sender(User user) {
        this.setSender(user);
        return this;
    }

    public ChatThread getThread() {
        return this.thread;
    }

    public void setThread(ChatThread chatThread) {
        this.thread = chatThread;
    }

    public ChatMessage thread(ChatThread chatThread) {
        this.setThread(chatThread);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessage{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", flagged='" + getFlagged() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            "}";
    }
}
