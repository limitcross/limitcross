package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CityPackagePriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CityPackagePrice getCityPackagePriceSample1() {
        return new CityPackagePrice().id(1L);
    }

    public static CityPackagePrice getCityPackagePriceSample2() {
        return new CityPackagePrice().id(2L);
    }

    public static CityPackagePrice getCityPackagePriceRandomSampleGenerator() {
        return new CityPackagePrice().id(longCount.incrementAndGet());
    }
}
