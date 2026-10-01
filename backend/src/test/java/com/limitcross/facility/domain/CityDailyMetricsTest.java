package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityDailyMetricsTestSamples.*;
import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityDailyMetricsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityDailyMetrics.class);
        CityDailyMetrics cityDailyMetrics1 = getCityDailyMetricsSample1();
        CityDailyMetrics cityDailyMetrics2 = new CityDailyMetrics();
        assertThat(cityDailyMetrics1).isNotEqualTo(cityDailyMetrics2);

        cityDailyMetrics2.setId(cityDailyMetrics1.getId());
        assertThat(cityDailyMetrics1).isEqualTo(cityDailyMetrics2);

        cityDailyMetrics2 = getCityDailyMetricsSample2();
        assertThat(cityDailyMetrics1).isNotEqualTo(cityDailyMetrics2);
    }

    @Test
    void cityTest() {
        CityDailyMetrics cityDailyMetrics = getCityDailyMetricsRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        cityDailyMetrics.setCity(cityBack);
        assertThat(cityDailyMetrics.getCity()).isEqualTo(cityBack);

        cityDailyMetrics.city(null);
        assertThat(cityDailyMetrics.getCity()).isNull();
    }

    @Test
    void categoryTest() {
        CityDailyMetrics cityDailyMetrics = getCityDailyMetricsRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        cityDailyMetrics.setCategory(serviceCategoryBack);
        assertThat(cityDailyMetrics.getCategory()).isEqualTo(serviceCategoryBack);

        cityDailyMetrics.category(null);
        assertThat(cityDailyMetrics.getCategory()).isNull();
    }
}
