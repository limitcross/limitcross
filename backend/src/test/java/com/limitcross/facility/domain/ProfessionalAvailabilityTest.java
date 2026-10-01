package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalAvailabilityTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalAvailabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalAvailability.class);
        ProfessionalAvailability professionalAvailability1 = getProfessionalAvailabilitySample1();
        ProfessionalAvailability professionalAvailability2 = new ProfessionalAvailability();
        assertThat(professionalAvailability1).isNotEqualTo(professionalAvailability2);

        professionalAvailability2.setId(professionalAvailability1.getId());
        assertThat(professionalAvailability1).isEqualTo(professionalAvailability2);

        professionalAvailability2 = getProfessionalAvailabilitySample2();
        assertThat(professionalAvailability1).isNotEqualTo(professionalAvailability2);
    }

    @Test
    void professionalTest() {
        ProfessionalAvailability professionalAvailability = getProfessionalAvailabilityRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalAvailability.setProfessional(professionalBack);
        assertThat(professionalAvailability.getProfessional()).isEqualTo(professionalBack);

        professionalAvailability.professional(null);
        assertThat(professionalAvailability.getProfessional()).isNull();
    }
}
