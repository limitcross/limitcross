package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceTranslationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceTranslation getServiceTranslationSample1() {
        return new ServiceTranslation().id(1L).langKey("langKey1").title("title1").description("description1");
    }

    public static ServiceTranslation getServiceTranslationSample2() {
        return new ServiceTranslation().id(2L).langKey("langKey2").title("title2").description("description2");
    }

    public static ServiceTranslation getServiceTranslationRandomSampleGenerator() {
        return new ServiceTranslation()
            .id(longCount.incrementAndGet())
            .langKey(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
