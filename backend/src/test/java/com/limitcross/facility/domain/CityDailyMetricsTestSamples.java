package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CityDailyMetricsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CityDailyMetrics getCityDailyMetricsSample1() {
        return new CityDailyMetrics().id(1L).bookingsCreated(1).bookingsCompleted(1).bookingsCancelled(1).avgAssignmentSec(1);
    }

    public static CityDailyMetrics getCityDailyMetricsSample2() {
        return new CityDailyMetrics().id(2L).bookingsCreated(2).bookingsCompleted(2).bookingsCancelled(2).avgAssignmentSec(2);
    }

    public static CityDailyMetrics getCityDailyMetricsRandomSampleGenerator() {
        return new CityDailyMetrics()
            .id(longCount.incrementAndGet())
            .bookingsCreated(intCount.incrementAndGet())
            .bookingsCompleted(intCount.incrementAndGet())
            .bookingsCancelled(intCount.incrementAndGet())
            .avgAssignmentSec(intCount.incrementAndGet());
    }
}
