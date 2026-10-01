package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.GatewayProvider;
import com.limitcross.facility.domain.enumeration.WebhookStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.PaymentWebhookEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentWebhookEventDTO implements Serializable {

    private Long id;

    @NotNull
    private GatewayProvider gateway;

    @NotNull
    @Size(max = 100)
    private String eventId;

    @NotNull
    @Size(max = 60)
    private String eventType;

    @NotNull
    private Boolean signatureOk;

    @Lob
    private String payload;

    @NotNull
    private WebhookStatus status;

    private Instant receivedAt;

    private Instant processedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GatewayProvider getGateway() {
        return gateway;
    }

    public void setGateway(GatewayProvider gateway) {
        this.gateway = gateway;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getSignatureOk() {
        return signatureOk;
    }

    public void setSignatureOk(Boolean signatureOk) {
        this.signatureOk = signatureOk;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public WebhookStatus getStatus() {
        return status;
    }

    public void setStatus(WebhookStatus status) {
        this.status = status;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentWebhookEventDTO)) {
            return false;
        }

        PaymentWebhookEventDTO paymentWebhookEventDTO = (PaymentWebhookEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentWebhookEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentWebhookEventDTO{" +
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
