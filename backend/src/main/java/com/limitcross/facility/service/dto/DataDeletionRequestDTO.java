package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.DeletionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.DataDeletionRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataDeletionRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private DeletionStatus status;

    private Instant requestedAt;

    private Instant completedAt;

    @Size(max = 255)
    private String note;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeletionStatus getStatus() {
        return status;
    }

    public void setStatus(DeletionStatus status) {
        this.status = status;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataDeletionRequestDTO)) {
            return false;
        }

        DataDeletionRequestDTO dataDeletionRequestDTO = (DataDeletionRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dataDeletionRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDeletionRequestDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", note='" + getNote() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
