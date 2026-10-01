package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalDailyMetricsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDailyMetricsDTO.class);
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO1 = new ProfessionalDailyMetricsDTO();
        professionalDailyMetricsDTO1.setId(1L);
        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO2 = new ProfessionalDailyMetricsDTO();
        assertThat(professionalDailyMetricsDTO1).isNotEqualTo(professionalDailyMetricsDTO2);
        professionalDailyMetricsDTO2.setId(professionalDailyMetricsDTO1.getId());
        assertThat(professionalDailyMetricsDTO1).isEqualTo(professionalDailyMetricsDTO2);
        professionalDailyMetricsDTO2.setId(2L);
        assertThat(professionalDailyMetricsDTO1).isNotEqualTo(professionalDailyMetricsDTO2);
        professionalDailyMetricsDTO1.setId(null);
        assertThat(professionalDailyMetricsDTO1).isNotEqualTo(professionalDailyMetricsDTO2);
    }
}
