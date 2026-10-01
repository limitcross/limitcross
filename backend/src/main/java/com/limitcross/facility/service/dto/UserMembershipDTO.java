package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.MembershipStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.UserMembership} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMembershipDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startsAt;

    @NotNull
    private Instant endsAt;

    @NotNull
    private MembershipStatus status;

    @NotNull
    private UserDTO user;

    @NotNull
    private MembershipPlanDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public MembershipPlanDTO getPlan() {
        return plan;
    }

    public void setPlan(MembershipPlanDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMembershipDTO)) {
            return false;
        }

        UserMembershipDTO userMembershipDTO = (UserMembershipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userMembershipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMembershipDTO{" +
            "id=" + getId() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", user=" + getUser() +
            ", plan=" + getPlan() +
            "}";
    }
}
