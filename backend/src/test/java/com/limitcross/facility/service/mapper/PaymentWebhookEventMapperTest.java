package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.PaymentWebhookEventAsserts.*;
import static com.limitcross.facility.domain.PaymentWebhookEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentWebhookEventMapperTest {

    private PaymentWebhookEventMapper paymentWebhookEventMapper;

    @BeforeEach
    void setUp() {
        paymentWebhookEventMapper = new PaymentWebhookEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentWebhookEventSample1();
        var actual = paymentWebhookEventMapper.toEntity(paymentWebhookEventMapper.toDto(expected));
        assertPaymentWebhookEventAllPropertiesEquals(expected, actual);
    }
}
