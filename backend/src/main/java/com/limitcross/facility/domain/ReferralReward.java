package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.ReferralStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A ReferralReward.
 */
@Entity
@Table(name = "referral_reward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "reward_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal rewardAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User referrer;

    @ManyToOne(optional = false)
    @NotNull
    private User referee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "items",
            "statusHistories",
            "assignments",
            "media",
            "payments",
            "quotes",
            "reschedules",
            "customer",
            "service",
            "city",
            "address",
            "professional",
            "coupon",
            "subscription",
            "slotCapacity",
            "couponRedemption",
            "review",
            "chatThread",
        },
        allowSetters = true
    )
    private Booking triggerBooking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReferralReward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRewardAmount() {
        return this.rewardAmount;
    }

    public ReferralReward rewardAmount(BigDecimal rewardAmount) {
        this.setRewardAmount(rewardAmount);
        return this;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferralStatus getStatus() {
        return this.status;
    }

    public ReferralReward status(ReferralStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ReferralReward createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getReferrer() {
        return this.referrer;
    }

    public void setReferrer(User user) {
        this.referrer = user;
    }

    public ReferralReward referrer(User user) {
        this.setReferrer(user);
        return this;
    }

    public User getReferee() {
        return this.referee;
    }

    public void setReferee(User user) {
        this.referee = user;
    }

    public ReferralReward referee(User user) {
        this.setReferee(user);
        return this;
    }

    public Booking getTriggerBooking() {
        return this.triggerBooking;
    }

    public void setTriggerBooking(Booking booking) {
        this.triggerBooking = booking;
    }

    public ReferralReward triggerBooking(Booking booking) {
        this.setTriggerBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralReward)) {
            return false;
        }
        return getId() != null && getId().equals(((ReferralReward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralReward{" +
            "id=" + getId() +
            ", rewardAmount=" + getRewardAmount() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
