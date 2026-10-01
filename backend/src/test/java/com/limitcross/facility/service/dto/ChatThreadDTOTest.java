package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatThreadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatThreadDTO.class);
        ChatThreadDTO chatThreadDTO1 = new ChatThreadDTO();
        chatThreadDTO1.setId(1L);
        ChatThreadDTO chatThreadDTO2 = new ChatThreadDTO();
        assertThat(chatThreadDTO1).isNotEqualTo(chatThreadDTO2);
        chatThreadDTO2.setId(chatThreadDTO1.getId());
        assertThat(chatThreadDTO1).isEqualTo(chatThreadDTO2);
        chatThreadDTO2.setId(2L);
        assertThat(chatThreadDTO1).isNotEqualTo(chatThreadDTO2);
        chatThreadDTO1.setId(null);
        assertThat(chatThreadDTO1).isNotEqualTo(chatThreadDTO2);
    }
}
