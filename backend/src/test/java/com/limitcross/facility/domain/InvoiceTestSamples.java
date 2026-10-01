package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        return new Invoice()
            .id(1L)
            .invoiceNo("invoiceNo1")
            .customerGstin("customerGstin1")
            .sellerGstin("sellerGstin1")
            .placeOfSupply("placeOfSupply1")
            .pdfUrl("pdfUrl1");
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice()
            .id(2L)
            .invoiceNo("invoiceNo2")
            .customerGstin("customerGstin2")
            .sellerGstin("sellerGstin2")
            .placeOfSupply("placeOfSupply2")
            .pdfUrl("pdfUrl2");
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice()
            .id(longCount.incrementAndGet())
            .invoiceNo(UUID.randomUUID().toString())
            .customerGstin(UUID.randomUUID().toString())
            .sellerGstin(UUID.randomUUID().toString())
            .placeOfSupply(UUID.randomUUID().toString())
            .pdfUrl(UUID.randomUUID().toString());
    }
}
