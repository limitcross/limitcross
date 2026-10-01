package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ChatMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatMessageDTO implements Serializable {

    private Long id;

    @Size(max = 2000)
    private String body;

    @Size(max = 255)
    private String mediaUrl;

    private Boolean flagged;

    private Instant createdAt;

    private Instant readAt;

    @NotNull
    private UserDTO sender;

    @NotNull
    private ChatThreadDTO thread;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public ChatThreadDTO getThread() {
        return thread;
    }

    public void setThread(ChatThreadDTO thread) {
        this.thread = thread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessageDTO)) {
            return false;
        }

        ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatMessageDTO{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", flagged='" + getFlagged() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            ", sender=" + getSender() +
            ", thread=" + getThread() +
            "}";
    }
}
