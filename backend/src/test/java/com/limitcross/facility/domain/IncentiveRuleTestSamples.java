package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IncentiveRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IncentiveRule getIncentiveRuleSample1() {
        return new IncentiveRule().id(1L).name("name1");
    }

    public static IncentiveRule getIncentiveRuleSample2() {
        return new IncentiveRule().id(2L).name("name2");
    }

    public static IncentiveRule getIncentiveRuleRandomSampleGenerator() {
        return new IncentiveRule().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
