package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceSequenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceSequence getInvoiceSequenceSample1() {
        return new InvoiceSequence().id(1L).series("series1").fiscalYear("fiscalYear1").lastNumber(1L);
    }

    public static InvoiceSequence getInvoiceSequenceSample2() {
        return new InvoiceSequence().id(2L).series("series2").fiscalYear("fiscalYear2").lastNumber(2L);
    }

    public static InvoiceSequence getInvoiceSequenceRandomSampleGenerator() {
        return new InvoiceSequence()
            .id(longCount.incrementAndGet())
            .series(UUID.randomUUID().toString())
            .fiscalYear(UUID.randomUUID().toString())
            .lastNumber(longCount.incrementAndGet());
    }
}
