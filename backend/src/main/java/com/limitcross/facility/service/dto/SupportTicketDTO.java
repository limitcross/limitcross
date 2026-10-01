package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.TicketCategory;
import com.limitcross.facility.domain.enumeration.TicketPriority;
import com.limitcross.facility.domain.enumeration.TicketStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.SupportTicket} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportTicketDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String ticketNo;

    @NotNull
    private TicketCategory category;

    @NotNull
    @Size(max = 200)
    private String subject;

    @Size(max = 2000)
    private String description;

    @NotNull
    private TicketStatus status;

    @NotNull
    private TicketPriority priority;

    private Instant createdAt;

    private Instant resolvedAt;

    @NotNull
    private UserDTO user;

    private UserDTO assignedTo;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDTO assignedTo) {
        this.assignedTo = assignedTo;
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
        if (!(o instanceof SupportTicketDTO)) {
            return false;
        }

        SupportTicketDTO supportTicketDTO = (SupportTicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supportTicketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportTicketDTO{" +
            "id=" + getId() +
            ", ticketNo='" + getTicketNo() + "'" +
            ", category='" + getCategory() + "'" +
            ", subject='" + getSubject() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            ", user=" + getUser() +
            ", assignedTo=" + getAssignedTo() +
            ", booking=" + getBooking() +
            "}";
    }
}
