package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ChatThreadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatThread getChatThreadSample1() {
        return new ChatThread().id(1L);
    }

    public static ChatThread getChatThreadSample2() {
        return new ChatThread().id(2L);
    }

    public static ChatThread getChatThreadRandomSampleGenerator() {
        return new ChatThread().id(longCount.incrementAndGet());
    }
}
