package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.CallStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CallSession.
 */
@Entity
@Table(name = "call_session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CallSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "virtual_number", length = 20, nullable = false)
    private String virtualNumber;

    @Size(max = 80)
    @Column(name = "provider_call_id", length = 80)
    private String providerCallId;

    @Min(value = 0)
    @Column(name = "duration_sec")
    private Integer durationSec;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CallStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User caller;

    @ManyToOne(optional = false)
    @NotNull
    private User callee;

    @ManyToOne(optional = false)
    @NotNull
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
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CallSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVirtualNumber() {
        return this.virtualNumber;
    }

    public CallSession virtualNumber(String virtualNumber) {
        this.setVirtualNumber(virtualNumber);
        return this;
    }

    public void setVirtualNumber(String virtualNumber) {
        this.virtualNumber = virtualNumber;
    }

    public String getProviderCallId() {
        return this.providerCallId;
    }

    public CallSession providerCallId(String providerCallId) {
        this.setProviderCallId(providerCallId);
        return this;
    }

    public void setProviderCallId(String providerCallId) {
        this.providerCallId = providerCallId;
    }

    public Integer getDurationSec() {
        return this.durationSec;
    }

    public CallSession durationSec(Integer durationSec) {
        this.setDurationSec(durationSec);
        return this;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }

    public CallStatus getStatus() {
        return this.status;
    }

    public CallSession status(CallStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CallSession createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getCaller() {
        return this.caller;
    }

    public void setCaller(User user) {
        this.caller = user;
    }

    public CallSession caller(User user) {
        this.setCaller(user);
        return this;
    }

    public User getCallee() {
        return this.callee;
    }

    public void setCallee(User user) {
        this.callee = user;
    }

    public CallSession callee(User user) {
        this.setCallee(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public CallSession booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallSession)) {
            return false;
        }
        return getId() != null && getId().equals(((CallSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CallSession{" +
            "id=" + getId() +
            ", virtualNumber='" + getVirtualNumber() + "'" +
            ", providerCallId='" + getProviderCallId() + "'" +
            ", durationSec=" + getDurationSec() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
