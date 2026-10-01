package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DataDeletionRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DataDeletionRequest getDataDeletionRequestSample1() {
        return new DataDeletionRequest().id(1L).note("note1");
    }

    public static DataDeletionRequest getDataDeletionRequestSample2() {
        return new DataDeletionRequest().id(2L).note("note2");
    }

    public static DataDeletionRequest getDataDeletionRequestRandomSampleGenerator() {
        return new DataDeletionRequest().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString());
    }
}
