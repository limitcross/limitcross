package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReferralRewardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReferralReward getReferralRewardSample1() {
        return new ReferralReward().id(1L);
    }

    public static ReferralReward getReferralRewardSample2() {
        return new ReferralReward().id(2L);
    }

    public static ReferralReward getReferralRewardRandomSampleGenerator() {
        return new ReferralReward().id(longCount.incrementAndGet());
    }
}
