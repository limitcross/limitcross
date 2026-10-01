package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalTrainingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalTraining getProfessionalTrainingSample1() {
        return new ProfessionalTraining().id(1L).score(1);
    }

    public static ProfessionalTraining getProfessionalTrainingSample2() {
        return new ProfessionalTraining().id(2L).score(2);
    }

    public static ProfessionalTraining getProfessionalTrainingRandomSampleGenerator() {
        return new ProfessionalTraining().id(longCount.incrementAndGet()).score(intCount.incrementAndGet());
    }
}
