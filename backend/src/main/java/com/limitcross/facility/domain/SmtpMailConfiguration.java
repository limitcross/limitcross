package com.limitcross.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "smtp_mail_configuration")
public class SmtpMailConfiguration {

	@Id
	private Long id;

	@Column(nullable = false, length = 255)
	private String host;

	@Column(nullable = false)
	private int port;

	@Column(length = 255)
	private String username;

	@Column(name = "password_ciphertext", length = 4096)
	private String passwordCiphertext;

	@Column(name = "from_address", nullable = false, length = 254)
	private String fromAddress;

	@Column(name = "auth_enabled", nullable = false)
	private boolean authEnabled;

	@Column(name = "starttls_enabled", nullable = false)
	private boolean startTlsEnabled;

	@Column(name = "ssl_enabled", nullable = false)
	private boolean sslEnabled;

	@Column(nullable = false)
	private boolean enabled;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@PrePersist
	@PreUpdate
	void updateTimestamp() {
		updatedAt = Instant.now();
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getHost() { return host; }
	public void setHost(String host) { this.host = host; }
	public int getPort() { return port; }
	public void setPort(int port) { this.port = port; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPasswordCiphertext() { return passwordCiphertext; }
	public void setPasswordCiphertext(String passwordCiphertext) { this.passwordCiphertext = passwordCiphertext; }
	public String getFromAddress() { return fromAddress; }
	public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
	public boolean isAuthEnabled() { return authEnabled; }
	public void setAuthEnabled(boolean authEnabled) { this.authEnabled = authEnabled; }
	public boolean isStartTlsEnabled() { return startTlsEnabled; }
	public void setStartTlsEnabled(boolean startTlsEnabled) { this.startTlsEnabled = startTlsEnabled; }
	public boolean isSslEnabled() { return sslEnabled; }
	public void setSslEnabled(boolean sslEnabled) { this.sslEnabled = sslEnabled; }
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public Instant getUpdatedAt() { return updatedAt; }
}
