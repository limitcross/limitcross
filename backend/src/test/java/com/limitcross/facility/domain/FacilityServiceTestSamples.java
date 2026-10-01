package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FacilityServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FacilityService getFacilityServiceSample1() {
        return new FacilityService()
            .id(1L)
            .code("code1")
            .title("title1")
            .slug("slug1")
            .emoji("emoji1")
            .imageUrl("imageUrl1")
            .description("description1")
            .durationMinutes(1)
            .warrantyDays(1)
            .sacCode("sacCode1")
            .reviewsCount(1)
            .sortOrder(1);
    }

    public static FacilityService getFacilityServiceSample2() {
        return new FacilityService()
            .id(2L)
            .code("code2")
            .title("title2")
            .slug("slug2")
            .emoji("emoji2")
            .imageUrl("imageUrl2")
            .description("description2")
            .durationMinutes(2)
            .warrantyDays(2)
            .sacCode("sacCode2")
            .reviewsCount(2)
            .sortOrder(2);
    }

    public static FacilityService getFacilityServiceRandomSampleGenerator() {
        return new FacilityService()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .emoji(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .durationMinutes(intCount.incrementAndGet())
            .warrantyDays(intCount.incrementAndGet())
            .sacCode(UUID.randomUUID().toString())
            .reviewsCount(intCount.incrementAndGet())
            .sortOrder(intCount.incrementAndGet());
    }
}
