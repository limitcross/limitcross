package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTierHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessionalTierHistory getProfessionalTierHistorySample1() {
        return new ProfessionalTierHistory().id(1L).reason("reason1");
    }

    public static ProfessionalTierHistory getProfessionalTierHistorySample2() {
        return new ProfessionalTierHistory().id(2L).reason("reason2");
    }

    public static ProfessionalTierHistory getProfessionalTierHistoryRandomSampleGenerator() {
        return new ProfessionalTierHistory().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}
