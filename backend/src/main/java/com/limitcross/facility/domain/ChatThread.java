package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.ChatThreadStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ChatThread.
 */
@Entity
@Table(name = "chat_thread")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatThread implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChatThreadStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

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
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Booking booking;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thread")
    @JsonIgnoreProperties(value = { "sender", "thread" }, allowSetters = true)
    private Set<ChatMessage> messages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatThread id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatThreadStatus getStatus() {
        return this.status;
    }

    public ChatThread status(ChatThreadStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ChatThreadStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ChatThread createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ChatThread booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    public Set<ChatMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<ChatMessage> chatMessages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setThread(null));
        }
        if (chatMessages != null) {
            chatMessages.forEach(i -> i.setThread(this));
        }
        this.messages = chatMessages;
    }

    public ChatThread messages(Set<ChatMessage> chatMessages) {
        this.setMessages(chatMessages);
        return this;
    }

    public ChatThread addMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
        chatMessage.setThread(this);
        return this;
    }

    public ChatThread removeMessage(ChatMessage chatMessage) {
        this.messages.remove(chatMessage);
        chatMessage.setThread(null);
        return this;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public ChatThread customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ChatThread professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatThread)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatThread) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatThread{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
