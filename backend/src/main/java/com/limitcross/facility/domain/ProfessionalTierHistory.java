package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProfessionalTierHistory.
 */
@Entity
@Table(name = "professional_tier_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTierHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "effective_from", nullable = false)
    private Instant effectiveFrom;

    @Size(max = 120)
    @Column(name = "reason", length = 120)
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    @ManyToOne(optional = false)
    @NotNull
    private ProfessionalTier tier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalTierHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public ProfessionalTierHistory effectiveFrom(Instant effectiveFrom) {
        this.setEffectiveFrom(effectiveFrom);
        return this;
    }

    public void setEffectiveFrom(Instant effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getReason() {
        return this.reason;
    }

    public ProfessionalTierHistory reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalTierHistory professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public ProfessionalTier getTier() {
        return this.tier;
    }

    public void setTier(ProfessionalTier professionalTier) {
        this.tier = professionalTier;
    }

    public ProfessionalTierHistory tier(ProfessionalTier professionalTier) {
        this.setTier(professionalTier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTierHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalTierHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTierHistory{" +
            "id=" + getId() +
            ", effectiveFrom='" + getEffectiveFrom() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
