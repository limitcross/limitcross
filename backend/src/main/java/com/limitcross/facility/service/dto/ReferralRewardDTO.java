package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.ReferralStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ReferralReward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralRewardDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal rewardAmount;

    @NotNull
    private ReferralStatus status;

    private Instant createdAt;

    @NotNull
    private UserDTO referrer;

    @NotNull
    private UserDTO referee;

    private BookingDTO triggerBooking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferralStatus getStatus() {
        return status;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getReferrer() {
        return referrer;
    }

    public void setReferrer(UserDTO referrer) {
        this.referrer = referrer;
    }

    public UserDTO getReferee() {
        return referee;
    }

    public void setReferee(UserDTO referee) {
        this.referee = referee;
    }

    public BookingDTO getTriggerBooking() {
        return triggerBooking;
    }

    public void setTriggerBooking(BookingDTO triggerBooking) {
        this.triggerBooking = triggerBooking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralRewardDTO)) {
            return false;
        }

        ReferralRewardDTO referralRewardDTO = (ReferralRewardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, referralRewardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralRewardDTO{" +
            "id=" + getId() +
            ", rewardAmount=" + getRewardAmount() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", referrer=" + getReferrer() +
            ", referee=" + getReferee() +
            ", triggerBooking=" + getTriggerBooking() +
            "}";
    }
}
