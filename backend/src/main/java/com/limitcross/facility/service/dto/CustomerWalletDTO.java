package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CustomerWallet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerWalletDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal balance;

    private Instant updatedAt;

    @NotNull
    private UserDTO user;

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
        if (!(o instanceof CustomerWalletDTO)) {
            return false;
        }

        CustomerWalletDTO customerWalletDTO = (CustomerWalletDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerWalletDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerWalletDTO{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
