package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalSkillTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProfessionalSkill getProfessionalSkillSample1() {
        return new ProfessionalSkill().id(1L).skillLevel(1);
    }

    public static ProfessionalSkill getProfessionalSkillSample2() {
        return new ProfessionalSkill().id(2L).skillLevel(2);
    }

    public static ProfessionalSkill getProfessionalSkillRandomSampleGenerator() {
        return new ProfessionalSkill().id(longCount.incrementAndGet()).skillLevel(intCount.incrementAndGet());
    }
}
