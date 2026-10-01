package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BannerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Banner getBannerSample1() {
        return new Banner().id(1L).title("title1").imageUrl("imageUrl1").deepLink("deepLink1").sortOrder(1);
    }

    public static Banner getBannerSample2() {
        return new Banner().id(2L).title("title2").imageUrl("imageUrl2").deepLink("deepLink2").sortOrder(2);
    }

    public static Banner getBannerRandomSampleGenerator() {
        return new Banner()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .deepLink(UUID.randomUUID().toString())
            .sortOrder(intCount.incrementAndGet());
    }
}
