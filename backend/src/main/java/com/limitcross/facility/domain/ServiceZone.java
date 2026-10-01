package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceZone.
 */
@Entity
@Table(name = "service_zone")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceZone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(max = 10)
    @Column(name = "pincode", length = 10, nullable = false)
    private String pincode;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "zones")
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Set<Professional> professionals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceZone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServiceZone name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return this.pincode;
    }

    public ServiceZone pincode(String pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ServiceZone active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ServiceZone city(City city) {
        this.setCity(city);
        return this;
    }

    public Set<Professional> getProfessionals() {
        return this.professionals;
    }

    public void setProfessionals(Set<Professional> professionals) {
        if (this.professionals != null) {
            this.professionals.forEach(i -> i.removeZone(this));
        }
        if (professionals != null) {
            professionals.forEach(i -> i.addZone(this));
        }
        this.professionals = professionals;
    }

    public ServiceZone professionals(Set<Professional> professionals) {
        this.setProfessionals(professionals);
        return this;
    }

    public ServiceZone addProfessional(Professional professional) {
        this.professionals.add(professional);
        professional.getZones().add(this);
        return this;
    }

    public ServiceZone removeProfessional(Professional professional) {
        this.professionals.remove(professional);
        professional.getZones().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceZone)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceZone) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceZone{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
