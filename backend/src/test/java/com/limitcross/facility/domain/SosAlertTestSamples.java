package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SosAlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SosAlert getSosAlertSample1() {
        return new SosAlert().id(1L);
    }

    public static SosAlert getSosAlertSample2() {
        return new SosAlert().id(2L);
    }

    public static SosAlert getSosAlertRandomSampleGenerator() {
        return new SosAlert().id(longCount.incrementAndGet());
    }
}
