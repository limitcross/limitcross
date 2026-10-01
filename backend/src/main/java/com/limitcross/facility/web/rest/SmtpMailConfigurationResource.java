package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.SmtpMailConfiguration;
import com.limitcross.facility.service.SmtpMailConfigurationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/smtp-mail-configuration")
public class SmtpMailConfigurationResource {

    private final SmtpMailConfigurationService service;

    public SmtpMailConfigurationResource(SmtpMailConfigurationService service) {
        this.service = service;
    }

    @GetMapping
    public ConfigurationResponse get() {
        return service.findConfiguration().map(SmtpMailConfigurationResource::toResponse).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "SMTP configuration not found"));
    }

    @PutMapping
    public ConfigurationResponse save(@Valid @RequestBody ConfigurationRequest request) {
        try {
            SmtpMailConfiguration saved = service.save(new SmtpMailConfigurationService.ConfigurationRequest(
                request.host(), request.port(), request.username(), request.password(), request.fromAddress(),
                request.authEnabled(), request.startTlsEnabled(), request.sslEnabled(), request.enabled()
            ));
            return toResponse(saved);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PostMapping("/test")
    public TestResponse test(@Valid @RequestBody TestRequest request) {
        try {
            service.sendTestEmail(request.to());
            return new TestResponse("Test email sent.");
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    private static ConfigurationResponse toResponse(SmtpMailConfiguration configuration) {
        return new ConfigurationResponse(
            configuration.getHost(), configuration.getPort(), configuration.getUsername(), configuration.getFromAddress(),
            configuration.isAuthEnabled(), configuration.isStartTlsEnabled(), configuration.isSslEnabled(),
            configuration.isEnabled(), configuration.getPasswordCiphertext() != null, configuration.getUpdatedAt()
        );
    }

    public record ConfigurationRequest(
        @NotBlank @Size(max = 255) String host,
        @Min(1) @Max(65535) int port,
        @Size(max = 255) String username,
        @Size(max = 2048) String password,
        @NotBlank @Email @Size(max = 254) String fromAddress,
        boolean authEnabled,
        boolean startTlsEnabled,
        boolean sslEnabled,
        boolean enabled
    ) {}

    public record TestRequest(@NotBlank @Email @Size(max = 254) String to) {}

    public record ConfigurationResponse(
        String host,
        int port,
        String username,
        String fromAddress,
        boolean authEnabled,
        boolean startTlsEnabled,
        boolean sslEnabled,
        boolean enabled,
        boolean passwordConfigured,
        Instant updatedAt
    ) {}

    public record TestResponse(String message) {}
}