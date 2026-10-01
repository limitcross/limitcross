package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CancellationPolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CancellationPolicy getCancellationPolicySample1() {
        return new CancellationPolicy().id(1L).hoursBeforeStart(1);
    }

    public static CancellationPolicy getCancellationPolicySample2() {
        return new CancellationPolicy().id(2L).hoursBeforeStart(2);
    }

    public static CancellationPolicy getCancellationPolicyRandomSampleGenerator() {
        return new CancellationPolicy().id(longCount.incrementAndGet()).hoursBeforeStart(intCount.incrementAndGet());
    }
}
