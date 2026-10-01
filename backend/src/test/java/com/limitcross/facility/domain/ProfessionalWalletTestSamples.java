package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalWalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessionalWallet getProfessionalWalletSample1() {
        return new ProfessionalWallet().id(1L);
    }

    public static ProfessionalWallet getProfessionalWalletSample2() {
        return new ProfessionalWallet().id(2L);
    }

    public static ProfessionalWallet getProfessionalWalletRandomSampleGenerator() {
        return new ProfessionalWallet().id(longCount.incrementAndGet());
    }
}
