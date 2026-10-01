package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalTierHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTierHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant effectiveFrom;

    @Size(max = 120)
    private String reason;

    @NotNull
    private ProfessionalDTO professional;

    @NotNull
    private ProfessionalTierDTO tier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Instant effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    public ProfessionalTierDTO getTier() {
        return tier;
    }

    public void setTier(ProfessionalTierDTO tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTierHistoryDTO)) {
            return false;
        }

        ProfessionalTierHistoryDTO professionalTierHistoryDTO = (ProfessionalTierHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalTierHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTierHistoryDTO{" +
            "id=" + getId() +
            ", effectiveFrom='" + getEffectiveFrom() + "'" +
            ", reason='" + getReason() + "'" +
            ", professional=" + getProfessional() +
            ", tier=" + getTier() +
            "}";
    }
}
