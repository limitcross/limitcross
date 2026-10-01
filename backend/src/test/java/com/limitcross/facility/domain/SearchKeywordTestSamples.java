package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SearchKeywordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SearchKeyword getSearchKeywordSample1() {
        return new SearchKeyword().id(1L).keyword("keyword1").weight(1);
    }

    public static SearchKeyword getSearchKeywordSample2() {
        return new SearchKeyword().id(2L).keyword("keyword2").weight(2);
    }

    public static SearchKeyword getSearchKeywordRandomSampleGenerator() {
        return new SearchKeyword().id(longCount.incrementAndGet()).keyword(UUID.randomUUID().toString()).weight(intCount.incrementAndGet());
    }
}
