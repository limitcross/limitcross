package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoyaltyLedgerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LoyaltyLedger getLoyaltyLedgerSample1() {
        return new LoyaltyLedger().id(1L).points(1);
    }

    public static LoyaltyLedger getLoyaltyLedgerSample2() {
        return new LoyaltyLedger().id(2L).points(2);
    }

    public static LoyaltyLedger getLoyaltyLedgerRandomSampleGenerator() {
        return new LoyaltyLedger().id(longCount.incrementAndGet()).points(intCount.incrementAndGet());
    }
}
