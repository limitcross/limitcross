package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfessionalKycDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfessionalKycDocument getProfessionalKycDocumentSample1() {
        return new ProfessionalKycDocument().id(1L).docNumberEnc("docNumberEnc1").fileUrl("fileUrl1").rejectReason("rejectReason1");
    }

    public static ProfessionalKycDocument getProfessionalKycDocumentSample2() {
        return new ProfessionalKycDocument().id(2L).docNumberEnc("docNumberEnc2").fileUrl("fileUrl2").rejectReason("rejectReason2");
    }

    public static ProfessionalKycDocument getProfessionalKycDocumentRandomSampleGenerator() {
        return new ProfessionalKycDocument()
            .id(longCount.incrementAndGet())
            .docNumberEnc(UUID.randomUUID().toString())
            .fileUrl(UUID.randomUUID().toString())
            .rejectReason(UUID.randomUUID().toString());
    }
}
