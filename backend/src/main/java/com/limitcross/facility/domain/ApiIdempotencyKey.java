package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ApiIdempotencyKey.
 */
@Entity
@Table(name = "api_idempotency_key")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiIdempotencyKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "idem_key", length = 64, nullable = false)
    private String idemKey;

    @NotNull
    @Size(max = 64)
    @Column(name = "request_hash", length = 64, nullable = false)
    private String requestHash;

    @Column(name = "response_code")
    private Integer responseCode;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApiIdempotencyKey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdemKey() {
        return this.idemKey;
    }

    public ApiIdempotencyKey idemKey(String idemKey) {
        this.setIdemKey(idemKey);
        return this;
    }

    public void setIdemKey(String idemKey) {
        this.idemKey = idemKey;
    }

    public String getRequestHash() {
        return this.requestHash;
    }

    public ApiIdempotencyKey requestHash(String requestHash) {
        this.setRequestHash(requestHash);
        return this;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }

    public ApiIdempotencyKey responseCode(Integer responseCode) {
        this.setResponseCode(responseCode);
        return this;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public ApiIdempotencyKey responseBody(String responseBody) {
        this.setResponseBody(responseBody);
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ApiIdempotencyKey createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public ApiIdempotencyKey expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApiIdempotencyKey user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiIdempotencyKey)) {
            return false;
        }
        return getId() != null && getId().equals(((ApiIdempotencyKey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiIdempotencyKey{" +
            "id=" + getId() +
            ", idemKey='" + getIdemKey() + "'" +
            ", requestHash='" + getRequestHash() + "'" +
            ", responseCode=" + getResponseCode() +
            ", responseBody='" + getResponseBody() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            "}";
    }
}
