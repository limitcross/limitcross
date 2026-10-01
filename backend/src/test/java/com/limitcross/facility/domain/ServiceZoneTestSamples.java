package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceZoneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceZone getServiceZoneSample1() {
        return new ServiceZone().id(1L).name("name1").pincode("pincode1");
    }

    public static ServiceZone getServiceZoneSample2() {
        return new ServiceZone().id(2L).name("name2").pincode("pincode2");
    }

    public static ServiceZone getServiceZoneRandomSampleGenerator() {
        return new ServiceZone().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).pincode(UUID.randomUUID().toString());
    }
}
