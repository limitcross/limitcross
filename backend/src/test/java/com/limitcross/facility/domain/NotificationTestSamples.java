package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification().id(1L).notificationType("notificationType1").title("title1").body("body1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id(2L).notificationType("notificationType2").title("title2").body("body2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .notificationType(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .body(UUID.randomUUID().toString());
    }
}
