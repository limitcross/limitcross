package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.ConsentPurpose;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A UserConsent.
 */
@Entity
@Table(name = "user_consent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConsent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false)
    private ConsentPurpose purpose;

    @NotNull
    @Size(max = 10)
    @Column(name = "policy_version", length = 10, nullable = false)
    private String policyVersion;

    @NotNull
    @Column(name = "granted", nullable = false)
    private Boolean granted;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserConsent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsentPurpose getPurpose() {
        return this.purpose;
    }

    public UserConsent purpose(ConsentPurpose purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public void setPurpose(ConsentPurpose purpose) {
        this.purpose = purpose;
    }

    public String getPolicyVersion() {
        return this.policyVersion;
    }

    public UserConsent policyVersion(String policyVersion) {
        this.setPolicyVersion(policyVersion);
        return this;
    }

    public void setPolicyVersion(String policyVersion) {
        this.policyVersion = policyVersion;
    }

    public Boolean getGranted() {
        return this.granted;
    }

    public UserConsent granted(Boolean granted) {
        this.setGranted(granted);
        return this;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public UserConsent ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserConsent createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserConsent user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConsent)) {
            return false;
        }
        return getId() != null && getId().equals(((UserConsent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConsent{" +
            "id=" + getId() +
            ", purpose='" + getPurpose() + "'" +
            ", policyVersion='" + getPolicyVersion() + "'" +
            ", granted='" + getGranted() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
