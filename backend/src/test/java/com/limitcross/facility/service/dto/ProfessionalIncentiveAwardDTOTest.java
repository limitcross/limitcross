package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalIncentiveAwardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalIncentiveAwardDTO.class);
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO1 = new ProfessionalIncentiveAwardDTO();
        professionalIncentiveAwardDTO1.setId(1L);
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO2 = new ProfessionalIncentiveAwardDTO();
        assertThat(professionalIncentiveAwardDTO1).isNotEqualTo(professionalIncentiveAwardDTO2);
        professionalIncentiveAwardDTO2.setId(professionalIncentiveAwardDTO1.getId());
        assertThat(professionalIncentiveAwardDTO1).isEqualTo(professionalIncentiveAwardDTO2);
        professionalIncentiveAwardDTO2.setId(2L);
        assertThat(professionalIncentiveAwardDTO1).isNotEqualTo(professionalIncentiveAwardDTO2);
        professionalIncentiveAwardDTO1.setId(null);
        assertThat(professionalIncentiveAwardDTO1).isNotEqualTo(professionalIncentiveAwardDTO2);
    }
}
