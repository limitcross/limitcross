package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupportTicketTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SupportTicket getSupportTicketSample1() {
        return new SupportTicket().id(1L).ticketNo("ticketNo1").subject("subject1").description("description1");
    }

    public static SupportTicket getSupportTicketSample2() {
        return new SupportTicket().id(2L).ticketNo("ticketNo2").subject("subject2").description("description2");
    }

    public static SupportTicket getSupportTicketRandomSampleGenerator() {
        return new SupportTicket()
            .id(longCount.incrementAndGet())
            .ticketNo(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
