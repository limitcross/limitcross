package com.limitcross.facility.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CredentialEncryptionService {

    private static final String PREFIX = "v1:";
    private static final int NONCE_LENGTH = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final byte[] encryptionKey;

    public CredentialEncryptionService(@Value("${facility.credentials.encryption-key:}") String configuredKey) {
        this.encryptionKey = configuredKey.isBlank() ? null : deriveKey(configuredKey);
    }

    public String encrypt(String value) {
        if (encryptionKey == null) {
            throw new IllegalStateException("Set FACILITY_CREDENTIALS_ENCRYPTION_KEY before saving app credentials.");
        }
        try {
            byte[] nonce = new byte[NONCE_LENGTH];
            SECURE_RANDOM.nextBytes(nonce);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"), new GCMParameterSpec(128, nonce));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            byte[] payload = new byte[nonce.length + encrypted.length];
            System.arraycopy(nonce, 0, payload, 0, nonce.length);
            System.arraycopy(encrypted, 0, payload, nonce.length, encrypted.length);
            return PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to encrypt app credential.", e);
        }
    }

    public String decrypt(String value) {
        if (encryptionKey == null) {
            throw new IllegalStateException("Set FACILITY_CREDENTIALS_ENCRYPTION_KEY before reading app credentials.");
        }
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalStateException("Saved app credential has an unsupported encryption format.");
        }
        try {
            byte[] payload = Base64.getDecoder().decode(value.substring(PREFIX.length()));
            if (payload.length <= NONCE_LENGTH) throw new IllegalStateException("Saved app credential is invalid.");
            byte[] nonce = new byte[NONCE_LENGTH];
            System.arraycopy(payload, 0, nonce, 0, nonce.length);
            byte[] encrypted = new byte[payload.length - nonce.length];
            System.arraycopy(payload, nonce.length, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"), new GCMParameterSpec(128, nonce));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException("Unable to decrypt app credential.", e);
        }
    }

    private static byte[] deriveKey(String configuredKey) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(configuredKey.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to initialize app credential encryption.", e);
        }
    }
}