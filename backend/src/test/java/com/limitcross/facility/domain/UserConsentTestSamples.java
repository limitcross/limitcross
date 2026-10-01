package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserConsentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserConsent getUserConsentSample1() {
        return new UserConsent().id(1L).policyVersion("policyVersion1").ipAddress("ipAddress1");
    }

    public static UserConsent getUserConsentSample2() {
        return new UserConsent().id(2L).policyVersion("policyVersion2").ipAddress("ipAddress2");
    }

    public static UserConsent getUserConsentRandomSampleGenerator() {
        return new UserConsent()
            .id(longCount.incrementAndGet())
            .policyVersion(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString());
    }
}
