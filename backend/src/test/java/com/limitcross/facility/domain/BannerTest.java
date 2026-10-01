package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BannerTestSamples.*;
import static com.limitcross.facility.domain.CityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BannerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banner.class);
        Banner banner1 = getBannerSample1();
        Banner banner2 = new Banner();
        assertThat(banner1).isNotEqualTo(banner2);

        banner2.setId(banner1.getId());
        assertThat(banner1).isEqualTo(banner2);

        banner2 = getBannerSample2();
        assertThat(banner1).isNotEqualTo(banner2);
    }

    @Test
    void cityTest() {
        Banner banner = getBannerRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        banner.setCity(cityBack);
        assertThat(banner.getCity()).isEqualTo(cityBack);

        banner.city(null);
        assertThat(banner.getCity()).isNull();
    }
}
