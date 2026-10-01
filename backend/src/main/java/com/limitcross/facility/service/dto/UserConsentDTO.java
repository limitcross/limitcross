package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.ConsentPurpose;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.UserConsent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConsentDTO implements Serializable {

    private Long id;

    @NotNull
    private ConsentPurpose purpose;

    @NotNull
    @Size(max = 10)
    private String policyVersion;

    @NotNull
    private Boolean granted;

    @Size(max = 45)
    private String ipAddress;

    private Instant createdAt;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsentPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(ConsentPurpose purpose) {
        this.purpose = purpose;
    }

    public String getPolicyVersion() {
        return policyVersion;
    }

    public void setPolicyVersion(String policyVersion) {
        this.policyVersion = policyVersion;
    }

    public Boolean getGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
        if (!(o instanceof UserConsentDTO)) {
            return false;
        }

        UserConsentDTO userConsentDTO = (UserConsentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userConsentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConsentDTO{" +
            "id=" + getId() +
            ", purpose='" + getPurpose() + "'" +
            ", policyVersion='" + getPolicyVersion() + "'" +
            ", granted='" + getGranted() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
