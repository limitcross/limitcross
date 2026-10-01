package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Professional getProfessionalSample1() {
        return new Professional()
            .id(1L)
            .displayName("displayName1")
            .photoUrl("photoUrl1")
            .jobsCompleted(1)
            .maxDailyJobs(1)
            .languages("languages1")
            .bankAccountEnc("bankAccountEnc1")
            .ifscCode("ifscCode1");
    }

    public static Professional getProfessionalSample2() {
        return new Professional()
            .id(2L)
            .displayName("displayName2")
            .photoUrl("photoUrl2")
            .jobsCompleted(2)
            .maxDailyJobs(2)
            .languages("languages2")
            .bankAccountEnc("bankAccountEnc2")
            .ifscCode("ifscCode2");
    }

    public static Professional getProfessionalRandomSampleGenerator() {
        return new Professional()
            .id(longCount.incrementAndGet())
            .displayName(UUID.randomUUID().toString())
            .photoUrl(UUID.randomUUID().toString())
            .jobsCompleted(intCount.incrementAndGet())
            .maxDailyJobs(intCount.incrementAndGet())
            .languages(UUID.randomUUID().toString())
            .bankAccountEnc(UUID.randomUUID().toString())
            .ifscCode(UUID.randomUUID().toString());
    }
}
