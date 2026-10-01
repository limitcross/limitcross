package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTimeOffDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTimeOffDTO.class);
        ProfessionalTimeOffDTO professionalTimeOffDTO1 = new ProfessionalTimeOffDTO();
        professionalTimeOffDTO1.setId(1L);
        ProfessionalTimeOffDTO professionalTimeOffDTO2 = new ProfessionalTimeOffDTO();
        assertThat(professionalTimeOffDTO1).isNotEqualTo(professionalTimeOffDTO2);
        professionalTimeOffDTO2.setId(professionalTimeOffDTO1.getId());
        assertThat(professionalTimeOffDTO1).isEqualTo(professionalTimeOffDTO2);
        professionalTimeOffDTO2.setId(2L);
        assertThat(professionalTimeOffDTO1).isNotEqualTo(professionalTimeOffDTO2);
        professionalTimeOffDTO1.setId(null);
        assertThat(professionalTimeOffDTO1).isNotEqualTo(professionalTimeOffDTO2);
    }
}
