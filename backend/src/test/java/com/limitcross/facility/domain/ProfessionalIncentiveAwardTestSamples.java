package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalIncentiveAwardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessionalIncentiveAward getProfessionalIncentiveAwardSample1() {
        return new ProfessionalIncentiveAward().id(1L);
    }

    public static ProfessionalIncentiveAward getProfessionalIncentiveAwardSample2() {
        return new ProfessionalIncentiveAward().id(2L);
    }

    public static ProfessionalIncentiveAward getProfessionalIncentiveAwardRandomSampleGenerator() {
        return new ProfessionalIncentiveAward().id(longCount.incrementAndGet());
    }
}
