package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerWalletTxnTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerWalletTxn getCustomerWalletTxnSample1() {
        return new CustomerWalletTxn().id(1L).note("note1");
    }

    public static CustomerWalletTxn getCustomerWalletTxnSample2() {
        return new CustomerWalletTxn().id(2L).note("note2");
    }

    public static CustomerWalletTxn getCustomerWalletTxnRandomSampleGenerator() {
        return new CustomerWalletTxn().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString());
    }
}
