package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FeatureFlagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FeatureFlag getFeatureFlagSample1() {
        return new FeatureFlag().id(1L).flagKey("flagKey1").rolloutPercent(1).cityIds("cityIds1");
    }

    public static FeatureFlag getFeatureFlagSample2() {
        return new FeatureFlag().id(2L).flagKey("flagKey2").rolloutPercent(2).cityIds("cityIds2");
    }

    public static FeatureFlag getFeatureFlagRandomSampleGenerator() {
        return new FeatureFlag()
            .id(longCount.incrementAndGet())
            .flagKey(UUID.randomUUID().toString())
            .rolloutPercent(intCount.incrementAndGet())
            .cityIds(UUID.randomUUID().toString());
    }
}
