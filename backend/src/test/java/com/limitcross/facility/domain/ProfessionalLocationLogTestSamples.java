package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalLocationLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalLocationLog getProfessionalLocationLogSample1() {
        return new ProfessionalLocationLog().id(1L).professionalId(1L).bookingRef("bookingRef1").batteryPct(1);
    }

    public static ProfessionalLocationLog getProfessionalLocationLogSample2() {
        return new ProfessionalLocationLog().id(2L).professionalId(2L).bookingRef("bookingRef2").batteryPct(2);
    }

    public static ProfessionalLocationLog getProfessionalLocationLogRandomSampleGenerator() {
        return new ProfessionalLocationLog()
            .id(longCount.incrementAndGet())
            .professionalId(longCount.incrementAndGet())
            .bookingRef(UUID.randomUUID().toString())
            .batteryPct(intCount.incrementAndGet());
    }
}
