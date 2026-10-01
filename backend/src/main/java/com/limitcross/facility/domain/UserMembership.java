package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.MembershipStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A UserMembership.
 */
@Entity
@Table(name = "user_membership")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMembership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @NotNull
    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MembershipStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private MembershipPlan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserMembership id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartsAt() {
        return this.startsAt;
    }

    public UserMembership startsAt(Instant startsAt) {
        this.setStartsAt(startsAt);
        return this;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return this.endsAt;
    }

    public UserMembership endsAt(Instant endsAt) {
        this.setEndsAt(endsAt);
        return this;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public MembershipStatus getStatus() {
        return this.status;
    }

    public UserMembership status(MembershipStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserMembership user(User user) {
        this.setUser(user);
        return this;
    }

    public MembershipPlan getPlan() {
        return this.plan;
    }

    public void setPlan(MembershipPlan membershipPlan) {
        this.plan = membershipPlan;
    }

    public UserMembership plan(MembershipPlan membershipPlan) {
        this.setPlan(membershipPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMembership)) {
            return false;
        }
        return getId() != null && getId().equals(((UserMembership) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMembership{" +
            "id=" + getId() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
