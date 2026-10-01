package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CommissionRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CommissionRule getCommissionRuleSample1() {
        return new CommissionRule().id(1L);
    }

    public static CommissionRule getCommissionRuleSample2() {
        return new CommissionRule().id(2L);
    }

    public static CommissionRule getCommissionRuleRandomSampleGenerator() {
        return new CommissionRule().id(longCount.incrementAndGet());
    }
}
