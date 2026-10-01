package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FraudFlagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FraudFlag getFraudFlagSample1() {
        return new FraudFlag().id(1L).entityId("entityId1").ruleCode("ruleCode1").riskScore(1);
    }

    public static FraudFlag getFraudFlagSample2() {
        return new FraudFlag().id(2L).entityId("entityId2").ruleCode("ruleCode2").riskScore(2);
    }

    public static FraudFlag getFraudFlagRandomSampleGenerator() {
        return new FraudFlag()
            .id(longCount.incrementAndGet())
            .entityId(UUID.randomUUID().toString())
            .ruleCode(UUID.randomUUID().toString())
            .riskScore(intCount.incrementAndGet());
    }
}
