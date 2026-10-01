package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.TicketCategory;
import com.limitcross.facility.domain.enumeration.TicketPriority;
import com.limitcross.facility.domain.enumeration.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SupportTicket.
 */
@Entity
@Table(name = "support_ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "ticket_no", length = 20, nullable = false, unique = true)
    private String ticketNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TicketCategory category;

    @NotNull
    @Size(max = 200)
    @Column(name = "subject", length = 200, nullable = false)
    private String subject;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TicketPriority priority;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedTo;

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
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupportTicket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNo() {
        return this.ticketNo;
    }

    public SupportTicket ticketNo(String ticketNo) {
        this.setTicketNo(ticketNo);
        return this;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public TicketCategory getCategory() {
        return this.category;
    }

    public SupportTicket category(TicketCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public String getSubject() {
        return this.subject;
    }

    public SupportTicket subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public SupportTicket description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public SupportTicket status(TicketStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return this.priority;
    }

    public SupportTicket priority(TicketPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SupportTicket createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return this.resolvedAt;
    }

    public SupportTicket resolvedAt(Instant resolvedAt) {
        this.setResolvedAt(resolvedAt);
        return this;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SupportTicket user(User user) {
        this.setUser(user);
        return this;
    }

    public User getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(User user) {
        this.assignedTo = user;
    }

    public SupportTicket assignedTo(User user) {
        this.setAssignedTo(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public SupportTicket booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupportTicket)) {
            return false;
        }
        return getId() != null && getId().equals(((SupportTicket) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportTicket{" +
            "id=" + getId() +
            ", ticketNo='" + getTicketNo() + "'" +
            ", category='" + getCategory() + "'" +
            ", subject='" + getSubject() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            "}";
    }
}
