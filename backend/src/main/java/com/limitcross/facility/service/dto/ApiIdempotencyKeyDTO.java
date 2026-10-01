package com.limitcross.facility.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ApiIdempotencyKey} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiIdempotencyKeyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String idemKey;

    @NotNull
    @Size(max = 64)
    private String requestHash;

    private Integer responseCode;

    @Lob
    private String responseBody;

    private Instant createdAt;

    @NotNull
    private Instant expiresAt;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdemKey() {
        return idemKey;
    }

    public void setIdemKey(String idemKey) {
        this.idemKey = idemKey;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiIdempotencyKeyDTO)) {
            return false;
        }

        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO = (ApiIdempotencyKeyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, apiIdempotencyKeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiIdempotencyKeyDTO{" +
            "id=" + getId() +
            ", idemKey='" + getIdemKey() + "'" +
            ", requestHash='" + getRequestHash() + "'" +
            ", responseCode=" + getResponseCode() +
            ", responseBody='" + getResponseBody() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
