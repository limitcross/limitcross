package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalSkillTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalSkill.class);
        ProfessionalSkill professionalSkill1 = getProfessionalSkillSample1();
        ProfessionalSkill professionalSkill2 = new ProfessionalSkill();
        assertThat(professionalSkill1).isNotEqualTo(professionalSkill2);

        professionalSkill2.setId(professionalSkill1.getId());
        assertThat(professionalSkill1).isEqualTo(professionalSkill2);

        professionalSkill2 = getProfessionalSkillSample2();
        assertThat(professionalSkill1).isNotEqualTo(professionalSkill2);
    }

    @Test
    void serviceTest() {
        ProfessionalSkill professionalSkill = getProfessionalSkillRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        professionalSkill.setService(facilityServiceBack);
        assertThat(professionalSkill.getService()).isEqualTo(facilityServiceBack);

        professionalSkill.service(null);
        assertThat(professionalSkill.getService()).isNull();
    }

    @Test
    void professionalTest() {
        ProfessionalSkill professionalSkill = getProfessionalSkillRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalSkill.setProfessional(professionalBack);
        assertThat(professionalSkill.getProfessional()).isEqualTo(professionalBack);

        professionalSkill.professional(null);
        assertThat(professionalSkill.getProfessional()).isNull();
    }
}
