package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OutboxEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutboxEventDTO.class);
        OutboxEventDTO outboxEventDTO1 = new OutboxEventDTO();
        outboxEventDTO1.setId(1L);
        OutboxEventDTO outboxEventDTO2 = new OutboxEventDTO();
        assertThat(outboxEventDTO1).isNotEqualTo(outboxEventDTO2);
        outboxEventDTO2.setId(outboxEventDTO1.getId());
        assertThat(outboxEventDTO1).isEqualTo(outboxEventDTO2);
        outboxEventDTO2.setId(2L);
        assertThat(outboxEventDTO1).isNotEqualTo(outboxEventDTO2);
        outboxEventDTO1.setId(null);
        assertThat(outboxEventDTO1).isNotEqualTo(outboxEventDTO2);
    }
}
