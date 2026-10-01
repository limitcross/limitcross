package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.SavedAppCredential;
import com.limitcross.facility.repository.SavedAppCredentialRepository;
import com.limitcross.facility.security.SecurityUtils;
import com.limitcross.facility.service.CredentialEncryptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/app-credentials")
public class SavedAppCredentialResource {

    private final SavedAppCredentialRepository repository;
    private final CredentialEncryptionService encryptionService;

    public SavedAppCredentialResource(SavedAppCredentialRepository repository, CredentialEncryptionService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/{appName}")
    public CredentialResponse get(@PathVariable @Size(max = 120) String appName) {
        SavedAppCredential credential = repository.findOneByOwnerUidAndAppName(currentUid(), appName.trim()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Saved credential not found"));
        return toResponse(credential);
    }

    @PutMapping("/{appName}")
    @Transactional
    public CredentialResponse save(
        @PathVariable @Size(max = 120) String appName,
        @Valid @RequestBody SaveCredentialRequest request
    ) {
        String normalizedName = appName.trim();
        SavedAppCredential credential = repository.findOneByOwnerUidAndAppName(currentUid(), normalizedName)
            .orElseGet(SavedAppCredential::new);
        credential.setOwnerUid(currentUid());
        credential.setAppName(normalizedName);
        credential.setEmail(request.email().trim().toLowerCase());
        credential.setAppPasswordCiphertext(encryptionService.encrypt(request.appPassword()));
        return toResponse(repository.save(credential));
    }

    @DeleteMapping("/{appName}")
    @Transactional
    public void delete(@PathVariable @Size(max = 120) String appName) {
        repository.deleteByOwnerUidAndAppName(currentUid(), appName.trim());
    }

    private static String currentUid() {
        return SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
    }

    private CredentialResponse toResponse(SavedAppCredential credential) {
        return new CredentialResponse(
            credential.getEmail(),
            credential.getAppName(),
            encryptionService.decrypt(credential.getAppPasswordCiphertext()),
            credential.getUpdatedAt()
        );
    }

    public record SaveCredentialRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 2048) String appPassword
    ) {}

    public record CredentialResponse(String email, String appName, String appPassword, Instant updatedAt) {}
}