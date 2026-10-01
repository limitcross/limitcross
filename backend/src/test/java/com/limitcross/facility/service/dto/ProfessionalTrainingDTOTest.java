package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTrainingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTrainingDTO.class);
        ProfessionalTrainingDTO professionalTrainingDTO1 = new ProfessionalTrainingDTO();
        professionalTrainingDTO1.setId(1L);
        ProfessionalTrainingDTO professionalTrainingDTO2 = new ProfessionalTrainingDTO();
        assertThat(professionalTrainingDTO1).isNotEqualTo(professionalTrainingDTO2);
        professionalTrainingDTO2.setId(professionalTrainingDTO1.getId());
        assertThat(professionalTrainingDTO1).isEqualTo(professionalTrainingDTO2);
        professionalTrainingDTO2.setId(2L);
        assertThat(professionalTrainingDTO1).isNotEqualTo(professionalTrainingDTO2);
        professionalTrainingDTO1.setId(null);
        assertThat(professionalTrainingDTO1).isNotEqualTo(professionalTrainingDTO2);
    }
}
