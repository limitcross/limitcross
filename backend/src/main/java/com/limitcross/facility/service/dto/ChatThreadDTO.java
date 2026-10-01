package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.ChatThreadStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ChatThread} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatThreadDTO implements Serializable {

    private Long id;

    @NotNull
    private ChatThreadStatus status;

    private Instant createdAt;

    @NotNull
    private BookingDTO booking;

    @NotNull
    private UserDTO customer;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatThreadStatus getStatus() {
        return status;
    }

    public void setStatus(ChatThreadStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatThreadDTO)) {
            return false;
        }

        ChatThreadDTO chatThreadDTO = (ChatThreadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatThreadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatThreadDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", booking=" + getBooking() +
            ", customer=" + getCustomer() +
            ", professional=" + getProfessional() +
            "}";
    }
}
