package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalDailyMetricsTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalDailyMetricsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDailyMetrics.class);
        ProfessionalDailyMetrics professionalDailyMetrics1 = getProfessionalDailyMetricsSample1();
        ProfessionalDailyMetrics professionalDailyMetrics2 = new ProfessionalDailyMetrics();
        assertThat(professionalDailyMetrics1).isNotEqualTo(professionalDailyMetrics2);

        professionalDailyMetrics2.setId(professionalDailyMetrics1.getId());
        assertThat(professionalDailyMetrics1).isEqualTo(professionalDailyMetrics2);

        professionalDailyMetrics2 = getProfessionalDailyMetricsSample2();
        assertThat(professionalDailyMetrics1).isNotEqualTo(professionalDailyMetrics2);
    }

    @Test
    void professionalTest() {
        ProfessionalDailyMetrics professionalDailyMetrics = getProfessionalDailyMetricsRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalDailyMetrics.setProfessional(professionalBack);
        assertThat(professionalDailyMetrics.getProfessional()).isEqualTo(professionalBack);

        professionalDailyMetrics.professional(null);
        assertThat(professionalDailyMetrics.getProfessional()).isNull();
    }
}
