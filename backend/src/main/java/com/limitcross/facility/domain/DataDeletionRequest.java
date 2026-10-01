package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.DeletionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DataDeletionRequest.
 */
@Entity
@Table(name = "data_deletion_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataDeletionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeletionStatus status;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataDeletionRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeletionStatus getStatus() {
        return this.status;
    }

    public DataDeletionRequest status(DeletionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeletionStatus status) {
        this.status = status;
    }

    public Instant getRequestedAt() {
        return this.requestedAt;
    }

    public DataDeletionRequest requestedAt(Instant requestedAt) {
        this.setRequestedAt(requestedAt);
        return this;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public DataDeletionRequest completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public String getNote() {
        return this.note;
    }

    public DataDeletionRequest note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DataDeletionRequest user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataDeletionRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((DataDeletionRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDeletionRequest{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestedAt='" + getRequestedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
