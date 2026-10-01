package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityPackagePriceTestSamples.*;
import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityPackagePriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityPackagePrice.class);
        CityPackagePrice cityPackagePrice1 = getCityPackagePriceSample1();
        CityPackagePrice cityPackagePrice2 = new CityPackagePrice();
        assertThat(cityPackagePrice1).isNotEqualTo(cityPackagePrice2);

        cityPackagePrice2.setId(cityPackagePrice1.getId());
        assertThat(cityPackagePrice1).isEqualTo(cityPackagePrice2);

        cityPackagePrice2 = getCityPackagePriceSample2();
        assertThat(cityPackagePrice1).isNotEqualTo(cityPackagePrice2);
    }

    @Test
    void servicePackageTest() {
        CityPackagePrice cityPackagePrice = getCityPackagePriceRandomSampleGenerator();
        ServicePackage servicePackageBack = getServicePackageRandomSampleGenerator();

        cityPackagePrice.setServicePackage(servicePackageBack);
        assertThat(cityPackagePrice.getServicePackage()).isEqualTo(servicePackageBack);

        cityPackagePrice.servicePackage(null);
        assertThat(cityPackagePrice.getServicePackage()).isNull();
    }

    @Test
    void cityTest() {
        CityPackagePrice cityPackagePrice = getCityPackagePriceRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        cityPackagePrice.setCity(cityBack);
        assertThat(cityPackagePrice.getCity()).isEqualTo(cityBack);

        cityPackagePrice.city(null);
        assertThat(cityPackagePrice.getCity()).isNull();
    }
}
