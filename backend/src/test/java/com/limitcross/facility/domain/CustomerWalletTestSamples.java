package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerWalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerWallet getCustomerWalletSample1() {
        return new CustomerWallet().id(1L);
    }

    public static CustomerWallet getCustomerWalletSample2() {
        return new CustomerWallet().id(2L);
    }

    public static CustomerWallet getCustomerWalletRandomSampleGenerator() {
        return new CustomerWallet().id(longCount.incrementAndGet());
    }
}
