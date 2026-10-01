package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.DevicePlatform;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.DeviceToken} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceTokenDTO implements Serializable {

    private Long id;

    @NotNull
    private DevicePlatform platform;

    @NotNull
    @Size(max = 300)
    private String token;

    @NotNull
    private Boolean active;

    private Instant createdAt;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DevicePlatform getPlatform() {
        return platform;
    }

    public void setPlatform(DevicePlatform platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(o instanceof DeviceTokenDTO)) {
            return false;
        }

        DeviceTokenDTO deviceTokenDTO = (DeviceTokenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceTokenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceTokenDTO{" +
            "id=" + getId() +
            ", platform='" + getPlatform() + "'" +
            ", token='" + getToken() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
