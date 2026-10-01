package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalAvailabilityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalAvailability getProfessionalAvailabilitySample1() {
        return new ProfessionalAvailability().id(1L).dayOfWeek(1).startTime("startTime1").endTime("endTime1");
    }

    public static ProfessionalAvailability getProfessionalAvailabilitySample2() {
        return new ProfessionalAvailability().id(2L).dayOfWeek(2).startTime("startTime2").endTime("endTime2");
    }

    public static ProfessionalAvailability getProfessionalAvailabilityRandomSampleGenerator() {
        return new ProfessionalAvailability()
            .id(longCount.incrementAndGet())
            .dayOfWeek(intCount.incrementAndGet())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString());
    }
}
