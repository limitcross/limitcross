package com.limitcross.facility.service;

import com.limitcross.facility.domain.SmtpMailConfiguration;
import com.limitcross.facility.repository.SmtpMailConfigurationRepository;
import java.util.Optional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmtpMailConfigurationService {

    private static final long CONFIGURATION_ID = 1L;

    private final SmtpMailConfigurationRepository repository;
    private final CredentialEncryptionService encryptionService;

    public SmtpMailConfigurationService(
        SmtpMailConfigurationRepository repository,
        CredentialEncryptionService encryptionService
    ) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    @Transactional(readOnly = true)
    public Optional<SmtpMailConfiguration> findEnabled() {
        return repository.findById(CONFIGURATION_ID).filter(SmtpMailConfiguration::isEnabled);
    }

    @Transactional(readOnly = true)
    public Optional<SmtpMailConfiguration> findConfiguration() {
        return repository.findById(CONFIGURATION_ID);
    }

    @Transactional
    public SmtpMailConfiguration save(ConfigurationRequest request) {
        SmtpMailConfiguration configuration = repository.findById(CONFIGURATION_ID).orElseGet(() -> {
            SmtpMailConfiguration created = new SmtpMailConfiguration();
            created.setId(CONFIGURATION_ID);
            return created;
        });
        configuration.setHost(request.host().trim());
        configuration.setPort(request.port());
        configuration.setUsername(request.username());
        configuration.setFromAddress(request.fromAddress().trim());
        configuration.setAuthEnabled(request.authEnabled());
        configuration.setStartTlsEnabled(request.startTlsEnabled());
        configuration.setSslEnabled(request.sslEnabled());
        configuration.setEnabled(request.enabled());
        if (request.password() != null && !request.password().isBlank()) {
            configuration.setPasswordCiphertext(encryptionService.encrypt(request.password()));
        }
        if (configuration.isAuthEnabled() && (configuration.getUsername() == null || configuration.getPasswordCiphertext() == null)) {
            throw new IllegalArgumentException("SMTP username and password are required when authentication is enabled.");
        }
        return repository.save(configuration);
    }

    public void sendTestEmail(String to) {
        SmtpMailConfiguration configuration = findEnabled().orElseThrow(() ->
            new IllegalStateException("Save and enable SMTP configuration before sending a test email."));
        JavaMailSenderImpl sender = createSender(configuration);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(configuration.getFromAddress());
        message.setTo(to);
        message.setSubject("SMTP configuration test");
        message.setText("SMTP email delivery is configured successfully.");
        sender.send(message);
    }

    public JavaMailSenderImpl createSender(SmtpMailConfiguration configuration) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(configuration.getHost());
        sender.setPort(configuration.getPort());
        sender.setUsername(configuration.getUsername());
        if (configuration.isAuthEnabled()) {
            sender.setPassword(encryptionService.decrypt(configuration.getPasswordCiphertext()));
        }
        sender.setDefaultEncoding("UTF-8");
        sender.getJavaMailProperties().put("mail.smtp.auth", configuration.isAuthEnabled());
        sender.getJavaMailProperties().put("mail.smtp.starttls.enable", configuration.isStartTlsEnabled());
        sender.getJavaMailProperties().put("mail.smtp.ssl.enable", configuration.isSslEnabled());
        sender.getJavaMailProperties().put("mail.smtp.connectiontimeout", "10000");
        sender.getJavaMailProperties().put("mail.smtp.timeout", "10000");
        sender.getJavaMailProperties().put("mail.smtp.writetimeout", "10000");
        return sender;
    }

    public record ConfigurationRequest(
        String host,
        int port,
        String username,
        String password,
        String fromAddress,
        boolean authEnabled,
        boolean startTlsEnabled,
        boolean sslEnabled,
        boolean enabled
    ) {}
}