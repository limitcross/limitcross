package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalSkillDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalSkillDTO.class);
        ProfessionalSkillDTO professionalSkillDTO1 = new ProfessionalSkillDTO();
        professionalSkillDTO1.setId(1L);
        ProfessionalSkillDTO professionalSkillDTO2 = new ProfessionalSkillDTO();
        assertThat(professionalSkillDTO1).isNotEqualTo(professionalSkillDTO2);
        professionalSkillDTO2.setId(professionalSkillDTO1.getId());
        assertThat(professionalSkillDTO1).isEqualTo(professionalSkillDTO2);
        professionalSkillDTO2.setId(2L);
        assertThat(professionalSkillDTO1).isNotEqualTo(professionalSkillDTO2);
        professionalSkillDTO1.setId(null);
        assertThat(professionalSkillDTO1).isNotEqualTo(professionalSkillDTO2);
    }
}
