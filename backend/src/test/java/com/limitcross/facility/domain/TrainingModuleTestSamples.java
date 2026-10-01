package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrainingModuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrainingModule getTrainingModuleSample1() {
        return new TrainingModule().id(1L).title("title1").contentUrl("contentUrl1").passScore(1);
    }

    public static TrainingModule getTrainingModuleSample2() {
        return new TrainingModule().id(2L).title("title2").contentUrl("contentUrl2").passScore(2);
    }

    public static TrainingModule getTrainingModuleRandomSampleGenerator() {
        return new TrainingModule()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .contentUrl(UUID.randomUUID().toString())
            .passScore(intCount.incrementAndGet());
    }
}
