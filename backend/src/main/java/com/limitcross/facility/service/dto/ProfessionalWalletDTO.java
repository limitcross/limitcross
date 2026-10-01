package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalWallet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalWalletDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal balance;

    private Instant updatedAt;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalWalletDTO)) {
            return false;
        }

        ProfessionalWalletDTO professionalWalletDTO = (ProfessionalWalletDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalWalletDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalWalletDTO{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", professional=" + getProfessional() +
            "}";
    }
}
