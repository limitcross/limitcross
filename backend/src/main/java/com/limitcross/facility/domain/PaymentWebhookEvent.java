package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.WebhookStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PaymentWebhookEvent.
 */
@Entity
@Table(name = "payment_webhook_event")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentWebhookEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gateway", nullable = false)
    private GatewayProvider gateway;

    @NotNull
    @Size(max = 100)
    @Column(name = "event_id", length = 100, nullable = false)
    private String eventId;

    @NotNull
    @Size(max = 60)
    @Column(name = "event_type", length = 60, nullable = false)
    private String eventType;

    @NotNull
    @Column(name = "signature_ok", nullable = false)
    private Boolean signatureOk;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WebhookStatus status;

    @Column(name = "received_at")
    private Instant receivedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentWebhookEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GatewayProvider getGateway() {
        return this.gateway;
    }

    public PaymentWebhookEvent gateway(GatewayProvider gateway) {
        this.setGateway(gateway);
        return this;
    }

    public void setGateway(GatewayProvider gateway) {
        this.gateway = gateway;
    }

    public String getEventId() {
        return this.eventId;
    }

    public PaymentWebhookEvent eventId(String eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public PaymentWebhookEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getSignatureOk() {
        return this.signatureOk;
    }

    public PaymentWebhookEvent signatureOk(Boolean signatureOk) {
        this.setSignatureOk(signatureOk);
        return this;
    }

    public void setSignatureOk(Boolean signatureOk) {
        this.signatureOk = signatureOk;
    }

    public String getPayload() {
        return this.payload;
    }

    public PaymentWebhookEvent payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public WebhookStatus getStatus() {
        return this.status;
    }

    public PaymentWebhookEvent status(WebhookStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WebhookStatus status) {
        this.status = status;
    }

    public Instant getReceivedAt() {
        return this.receivedAt;
    }

    public PaymentWebhookEvent receivedAt(Instant receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public PaymentWebhookEvent processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentWebhookEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentWebhookEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentWebhookEvent{" +
            "id=" + getId() +
            ", gateway='" + getGateway() + "'" +
            ", eventId='" + getEventId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", signatureOk='" + getSignatureOk() + "'" +
            ", payload='" + getPayload() + "'" +
            ", status='" + getStatus() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            "}";
    }
}
