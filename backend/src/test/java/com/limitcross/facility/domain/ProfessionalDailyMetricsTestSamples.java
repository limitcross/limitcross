package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalDailyMetricsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalDailyMetrics getProfessionalDailyMetricsSample1() {
        return new ProfessionalDailyMetrics().id(1L).jobsOffered(1).jobsAccepted(1).jobsCompleted(1).jobsCancelled(1).onlineMinutes(1);
    }

    public static ProfessionalDailyMetrics getProfessionalDailyMetricsSample2() {
        return new ProfessionalDailyMetrics().id(2L).jobsOffered(2).jobsAccepted(2).jobsCompleted(2).jobsCancelled(2).onlineMinutes(2);
    }

    public static ProfessionalDailyMetrics getProfessionalDailyMetricsRandomSampleGenerator() {
        return new ProfessionalDailyMetrics()
            .id(longCount.incrementAndGet())
            .jobsOffered(intCount.incrementAndGet())
            .jobsAccepted(intCount.incrementAndGet())
            .jobsCompleted(intCount.incrementAndGet())
            .jobsCancelled(intCount.incrementAndGet())
            .onlineMinutes(intCount.incrementAndGet());
    }
}
