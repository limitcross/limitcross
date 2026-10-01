package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.CallStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CallSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CallSessionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String virtualNumber;

    @Size(max = 80)
    private String providerCallId;

    @Min(value = 0)
    private Integer durationSec;

    @NotNull
    private CallStatus status;

    private Instant createdAt;

    @NotNull
    private UserDTO caller;

    @NotNull
    private UserDTO callee;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVirtualNumber() {
        return virtualNumber;
    }

    public void setVirtualNumber(String virtualNumber) {
        this.virtualNumber = virtualNumber;
    }

    public String getProviderCallId() {
        return providerCallId;
    }

    public void setProviderCallId(String providerCallId) {
        this.providerCallId = providerCallId;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getCaller() {
        return caller;
    }

    public void setCaller(UserDTO caller) {
        this.caller = caller;
    }

    public UserDTO getCallee() {
        return callee;
    }

    public void setCallee(UserDTO callee) {
        this.callee = callee;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallSessionDTO)) {
            return false;
        }

        CallSessionDTO callSessionDTO = (CallSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, callSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CallSessionDTO{" +
            "id=" + getId() +
            ", virtualNumber='" + getVirtualNumber() + "'" +
            ", providerCallId='" + getProviderCallId() + "'" +
            ", durationSec=" + getDurationSec() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", caller=" + getCaller() +
            ", callee=" + getCallee() +
            ", booking=" + getBooking() +
            "}";
    }
}
