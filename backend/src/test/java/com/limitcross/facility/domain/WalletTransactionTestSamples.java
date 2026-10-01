package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WalletTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WalletTransaction getWalletTransactionSample1() {
        return new WalletTransaction().id(1L).description("description1");
    }

    public static WalletTransaction getWalletTransactionSample2() {
        return new WalletTransaction().id(2L).description("description2");
    }

    public static WalletTransaction getWalletTransactionRandomSampleGenerator() {
        return new WalletTransaction().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
