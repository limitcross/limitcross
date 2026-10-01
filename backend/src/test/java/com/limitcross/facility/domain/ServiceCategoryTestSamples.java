package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ServiceCategory getServiceCategorySample1() {
        return new ServiceCategory().id(1L).code("code1").name("name1").icon("icon1").sortOrder(1);
    }

    public static ServiceCategory getServiceCategorySample2() {
        return new ServiceCategory().id(2L).code("code2").name("name2").icon("icon2").sortOrder(2);
    }

    public static ServiceCategory getServiceCategoryRandomSampleGenerator() {
        return new ServiceCategory()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .sortOrder(intCount.incrementAndGet());
    }
}
