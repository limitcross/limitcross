package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserMembershipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserMembership getUserMembershipSample1() {
        return new UserMembership().id(1L);
    }

    public static UserMembership getUserMembershipSample2() {
        return new UserMembership().id(2L);
    }

    public static UserMembership getUserMembershipRandomSampleGenerator() {
        return new UserMembership().id(longCount.incrementAndGet());
    }
}
