package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CallSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CallSessionDTO.class);
        CallSessionDTO callSessionDTO1 = new CallSessionDTO();
        callSessionDTO1.setId(1L);
        CallSessionDTO callSessionDTO2 = new CallSessionDTO();
        assertThat(callSessionDTO1).isNotEqualTo(callSessionDTO2);
        callSessionDTO2.setId(callSessionDTO1.getId());
        assertThat(callSessionDTO1).isEqualTo(callSessionDTO2);
        callSessionDTO2.setId(2L);
        assertThat(callSessionDTO1).isNotEqualTo(callSessionDTO2);
        callSessionDTO1.setId(null);
        assertThat(callSessionDTO1).isNotEqualTo(callSessionDTO2);
    }
}
