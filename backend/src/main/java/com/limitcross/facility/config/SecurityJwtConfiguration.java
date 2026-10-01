package com.limitcross.facility.config;

import static com.limitcross.facility.security.SecurityUtils.JWT_ALGORITHM;

import com.nimbusds.jose.JWSAlgorithm;
import com.limitcross.facility.management.SecurityMetersService;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;

@Configuration
public class SecurityJwtConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${facility.firebase.project-id:login-ui-test-2026}")
    private String firebaseProjectId;

    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        NimbusJwtDecoder jhipsterDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        String firebaseIssuer = "https://securetoken.google.com/" + firebaseProjectId;
        NimbusJwtDecoder firebaseDecoder = NimbusJwtDecoder
            .withJwkSetUri("https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com")
            .build();
        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> jwt.getAudience().contains(firebaseProjectId)
            ? OAuth2TokenValidatorResult.success()
            : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid Firebase token audience", null));
        firebaseDecoder.setJwtValidator(
            new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefaultWithIssuer(firebaseIssuer), audienceValidator)
        );
        return token -> {
            try {
                return isFirebaseToken(token) ? firebaseDecoder.decode(token) : jhipsterDecoder.decode(token);
            } catch (Exception e) {
                String message = e.getMessage() == null ? "" : e.getMessage();
                if (message.contains("Invalid signature")) {
                    metersService.trackTokenInvalidSignature();
                } else if (message.contains("Jwt expired at")) {
                    metersService.trackTokenExpired();
                } else if (message.contains("Invalid JWT serialization") || message.contains("Malformed token") || message.contains("Invalid unsecured/JWS/JWE")) {
                    metersService.trackTokenMalformed();
                } else {
                    LOG.error("JWT validation failed: {}", message);
                }
                throw e;
            }
        };
    }

    private static boolean isFirebaseToken(String token) {
        try {
            return JWSAlgorithm.RS256.equals(SignedJWT.parse(token).getHeader().getAlgorithm());
        } catch (ParseException e) {
            return false;
        }
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
