package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @NotNull
    @Size(max = 80)
    @Column(name = "state_name", length = 80, nullable = false)
    private String stateName;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;

    @NotNull
    @Size(max = 40)
    @Column(name = "timezone", length = 40, nullable = false)
    private String timezone;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @JsonIgnoreProperties(value = { "city", "professionals" }, allowSetters = true)
    private Set<ServiceZone> zones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public City id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public City name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateName() {
        return this.stateName;
    }

    public City stateName(String stateName) {
        this.setStateName(stateName);
        return this;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public City countryCode(String countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public City timezone(String timezone) {
        this.setTimezone(timezone);
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getActive() {
        return this.active;
    }

    public City active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public City createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ServiceZone> getZones() {
        return this.zones;
    }

    public void setZones(Set<ServiceZone> serviceZones) {
        if (this.zones != null) {
            this.zones.forEach(i -> i.setCity(null));
        }
        if (serviceZones != null) {
            serviceZones.forEach(i -> i.setCity(this));
        }
        this.zones = serviceZones;
    }

    public City zones(Set<ServiceZone> serviceZones) {
        this.setZones(serviceZones);
        return this;
    }

    public City addZone(ServiceZone serviceZone) {
        this.zones.add(serviceZone);
        serviceZone.setCity(this);
        return this;
    }

    public City removeZone(ServiceZone serviceZone) {
        this.zones.remove(serviceZone);
        serviceZone.setCity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return getId() != null && getId().equals(((City) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", stateName='" + getStateName() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", timezone='" + getTimezone() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
