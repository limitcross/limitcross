package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTimeOffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessionalTimeOff getProfessionalTimeOffSample1() {
        return new ProfessionalTimeOff().id(1L);
    }

    public static ProfessionalTimeOff getProfessionalTimeOffSample2() {
        return new ProfessionalTimeOff().id(2L);
    }

    public static ProfessionalTimeOff getProfessionalTimeOffRandomSampleGenerator() {
        return new ProfessionalTimeOff().id(longCount.incrementAndGet());
    }
}
