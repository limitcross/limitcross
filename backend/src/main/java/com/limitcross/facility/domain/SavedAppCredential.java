package com.limitcross.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "saved_app_credential",
    uniqueConstraints = @UniqueConstraint(name = "uk_saved_credential_owner_app", columnNames = { "owner_uid", "app_name" })
)
public class SavedAppCredential {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "owner_uid", nullable = false, length = 128)
    private String ownerUid;

    @Column(name = "app_name", nullable = false, length = 120)
    private String appName;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(name = "app_password_ciphertext", nullable = false, length = 4096)
    private String appPasswordCiphertext;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void initialize() {
        Instant now = Instant.now();
        if (id == null) id = UUID.randomUUID().toString();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void touch() {
        updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getOwnerUid() { return ownerUid; }
    public void setOwnerUid(String ownerUid) { this.ownerUid = ownerUid; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAppPasswordCiphertext() { return appPasswordCiphertext; }
    public void setAppPasswordCiphertext(String appPasswordCiphertext) { this.appPasswordCiphertext = appPasswordCiphertext; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}