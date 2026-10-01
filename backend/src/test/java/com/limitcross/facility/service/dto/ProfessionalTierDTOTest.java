package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTierDTO.class);
        ProfessionalTierDTO professionalTierDTO1 = new ProfessionalTierDTO();
        professionalTierDTO1.setId(1L);
        ProfessionalTierDTO professionalTierDTO2 = new ProfessionalTierDTO();
        assertThat(professionalTierDTO1).isNotEqualTo(professionalTierDTO2);
        professionalTierDTO2.setId(professionalTierDTO1.getId());
        assertThat(professionalTierDTO1).isEqualTo(professionalTierDTO2);
        professionalTierDTO2.setId(2L);
        assertThat(professionalTierDTO1).isNotEqualTo(professionalTierDTO2);
        professionalTierDTO1.setId(null);
        assertThat(professionalTierDTO1).isNotEqualTo(professionalTierDTO2);
    }
}
