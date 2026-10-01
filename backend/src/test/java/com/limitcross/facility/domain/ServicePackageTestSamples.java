package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServicePackageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ServicePackage getServicePackageSample1() {
        return new ServicePackage().id(1L).name("name1").description("description1").durationMinutes(1).sortOrder(1);
    }

    public static ServicePackage getServicePackageSample2() {
        return new ServicePackage().id(2L).name("name2").description("description2").durationMinutes(2).sortOrder(2);
    }

    public static ServicePackage getServicePackageRandomSampleGenerator() {
        return new ServicePackage()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .durationMinutes(intCount.incrementAndGet())
            .sortOrder(intCount.incrementAndGet());
    }
}
