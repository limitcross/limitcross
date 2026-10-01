package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SosAlertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SosAlertDTO.class);
        SosAlertDTO sosAlertDTO1 = new SosAlertDTO();
        sosAlertDTO1.setId(1L);
        SosAlertDTO sosAlertDTO2 = new SosAlertDTO();
        assertThat(sosAlertDTO1).isNotEqualTo(sosAlertDTO2);
        sosAlertDTO2.setId(sosAlertDTO1.getId());
        assertThat(sosAlertDTO1).isEqualTo(sosAlertDTO2);
        sosAlertDTO2.setId(2L);
        assertThat(sosAlertDTO1).isNotEqualTo(sosAlertDTO2);
        sosAlertDTO1.setId(null);
        assertThat(sosAlertDTO1).isNotEqualTo(sosAlertDTO2);
    }
}
