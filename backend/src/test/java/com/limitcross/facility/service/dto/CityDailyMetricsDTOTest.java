package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityDailyMetricsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityDailyMetricsDTO.class);
        CityDailyMetricsDTO cityDailyMetricsDTO1 = new CityDailyMetricsDTO();
        cityDailyMetricsDTO1.setId(1L);
        CityDailyMetricsDTO cityDailyMetricsDTO2 = new CityDailyMetricsDTO();
        assertThat(cityDailyMetricsDTO1).isNotEqualTo(cityDailyMetricsDTO2);
        cityDailyMetricsDTO2.setId(cityDailyMetricsDTO1.getId());
        assertThat(cityDailyMetricsDTO1).isEqualTo(cityDailyMetricsDTO2);
        cityDailyMetricsDTO2.setId(2L);
        assertThat(cityDailyMetricsDTO1).isNotEqualTo(cityDailyMetricsDTO2);
        cityDailyMetricsDTO1.setId(null);
        assertThat(cityDailyMetricsDTO1).isNotEqualTo(cityDailyMetricsDTO2);
    }
}
