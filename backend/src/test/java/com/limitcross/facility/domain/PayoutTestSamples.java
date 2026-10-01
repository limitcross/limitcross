package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PayoutTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payout getPayoutSample1() {
        return new Payout().id(1L).bankReference("bankReference1");
    }

    public static Payout getPayoutSample2() {
        return new Payout().id(2L).bankReference("bankReference2");
    }

    public static Payout getPayoutRandomSampleGenerator() {
        return new Payout().id(longCount.incrementAndGet()).bankReference(UUID.randomUUID().toString());
    }
}
