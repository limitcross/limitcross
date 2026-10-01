package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.DevicePlatform;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DeviceToken.
 */
@Entity
@Table(name = "device_token")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private DevicePlatform platform;

    @NotNull
    @Size(max = 300)
    @Column(name = "token", length = 300, nullable = false, unique = true)
    private String token;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeviceToken id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DevicePlatform getPlatform() {
        return this.platform;
    }

    public DeviceToken platform(DevicePlatform platform) {
        this.setPlatform(platform);
        return this;
    }

    public void setPlatform(DevicePlatform platform) {
        this.platform = platform;
    }

    public String getToken() {
        return this.token;
    }

    public DeviceToken token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return this.active;
    }

    public DeviceToken active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public DeviceToken createdAt(Instant createdAt) {
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

    public DeviceToken user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceToken)) {
            return false;
        }
        return getId() != null && getId().equals(((DeviceToken) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceToken{" +
            "id=" + getId() +
            ", platform='" + getPlatform() + "'" +
            ", token='" + getToken() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
