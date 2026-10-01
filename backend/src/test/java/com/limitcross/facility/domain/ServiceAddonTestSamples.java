package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceAddonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ServiceAddon getServiceAddonSample1() {
        return new ServiceAddon().id(1L).name("name1").durationMinutes(1);
    }

    public static ServiceAddon getServiceAddonSample2() {
        return new ServiceAddon().id(2L).name("name2").durationMinutes(2);
    }

    public static ServiceAddon getServiceAddonRandomSampleGenerator() {
        return new ServiceAddon()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .durationMinutes(intCount.incrementAndGet());
    }
}
