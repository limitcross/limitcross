package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceLine getInvoiceLineSample1() {
        return new InvoiceLine().id(1L).description("description1").sacCode("sacCode1");
    }

    public static InvoiceLine getInvoiceLineSample2() {
        return new InvoiceLine().id(2L).description("description2").sacCode("sacCode2");
    }

    public static InvoiceLine getInvoiceLineRandomSampleGenerator() {
        return new InvoiceLine()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .sacCode(UUID.randomUUID().toString());
    }
}
