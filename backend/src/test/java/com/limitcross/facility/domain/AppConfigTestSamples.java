package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppConfig getAppConfigSample1() {
        return new AppConfig().id(1L).configKey("configKey1").description("description1").updatedBy("updatedBy1");
    }

    public static AppConfig getAppConfigSample2() {
        return new AppConfig().id(2L).configKey("configKey2").description("description2").updatedBy("updatedBy2");
    }

    public static AppConfig getAppConfigRandomSampleGenerator() {
        return new AppConfig()
            .id(longCount.incrementAndGet())
            .configKey(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
