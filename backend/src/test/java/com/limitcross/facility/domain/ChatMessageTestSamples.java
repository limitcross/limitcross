package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChatMessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatMessage getChatMessageSample1() {
        return new ChatMessage().id(1L).body("body1").mediaUrl("mediaUrl1");
    }

    public static ChatMessage getChatMessageSample2() {
        return new ChatMessage().id(2L).body("body2").mediaUrl("mediaUrl2");
    }

    public static ChatMessage getChatMessageRandomSampleGenerator() {
        return new ChatMessage().id(longCount.incrementAndGet()).body(UUID.randomUUID().toString()).mediaUrl(UUID.randomUUID().toString());
    }
}
