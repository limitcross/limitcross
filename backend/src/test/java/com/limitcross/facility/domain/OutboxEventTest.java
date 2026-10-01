package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.OutboxEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutboxEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutboxEvent.class);
        OutboxEvent outboxEvent1 = getOutboxEventSample1();
        OutboxEvent outboxEvent2 = new OutboxEvent();
        assertThat(outboxEvent1).isNotEqualTo(outboxEvent2);

        outboxEvent2.setId(outboxEvent1.getId());
        assertThat(outboxEvent1).isEqualTo(outboxEvent2);

        outboxEvent2 = getOutboxEventSample2();
        assertThat(outboxEvent1).isNotEqualTo(outboxEvent2);
    }
}
