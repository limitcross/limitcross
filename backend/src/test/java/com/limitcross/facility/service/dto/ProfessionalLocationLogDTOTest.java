package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalLocationLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalLocationLogDTO.class);
        ProfessionalLocationLogDTO professionalLocationLogDTO1 = new ProfessionalLocationLogDTO();
        professionalLocationLogDTO1.setId(1L);
        ProfessionalLocationLogDTO professionalLocationLogDTO2 = new ProfessionalLocationLogDTO();
        assertThat(professionalLocationLogDTO1).isNotEqualTo(professionalLocationLogDTO2);
        professionalLocationLogDTO2.setId(professionalLocationLogDTO1.getId());
        assertThat(professionalLocationLogDTO1).isEqualTo(professionalLocationLogDTO2);
        professionalLocationLogDTO2.setId(2L);
        assertThat(professionalLocationLogDTO1).isNotEqualTo(professionalLocationLogDTO2);
        professionalLocationLogDTO1.setId(null);
        assertThat(professionalLocationLogDTO1).isNotEqualTo(professionalLocationLogDTO2);
    }
}
