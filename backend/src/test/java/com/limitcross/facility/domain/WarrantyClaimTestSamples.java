package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WarrantyClaimTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WarrantyClaim getWarrantyClaimSample1() {
        return new WarrantyClaim().id(1L).issue("issue1");
    }

    public static WarrantyClaim getWarrantyClaimSample2() {
        return new WarrantyClaim().id(2L).issue("issue2");
    }

    public static WarrantyClaim getWarrantyClaimRandomSampleGenerator() {
        return new WarrantyClaim().id(longCount.incrementAndGet()).issue(UUID.randomUUID().toString());
    }
}
