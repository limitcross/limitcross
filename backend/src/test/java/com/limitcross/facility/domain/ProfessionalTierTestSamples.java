package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalTier getProfessionalTierSample1() {
        return new ProfessionalTier().id(1L).code("code1").name("name1").minJobs(1).dispatchPriority(1);
    }

    public static ProfessionalTier getProfessionalTierSample2() {
        return new ProfessionalTier().id(2L).code("code2").name("name2").minJobs(2).dispatchPriority(2);
    }

    public static ProfessionalTier getProfessionalTierRandomSampleGenerator() {
        return new ProfessionalTier()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .minJobs(intCount.incrementAndGet())
            .dispatchPriority(intCount.incrementAndGet());
    }
}
