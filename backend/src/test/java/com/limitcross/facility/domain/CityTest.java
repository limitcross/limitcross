package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.ServiceZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void zoneTest() {
        City city = getCityRandomSampleGenerator();
        ServiceZone serviceZoneBack = getServiceZoneRandomSampleGenerator();

        city.addZone(serviceZoneBack);
        assertThat(city.getZones()).containsOnly(serviceZoneBack);
        assertThat(serviceZoneBack.getCity()).isEqualTo(city);

        city.removeZone(serviceZoneBack);
        assertThat(city.getZones()).doesNotContain(serviceZoneBack);
        assertThat(serviceZoneBack.getCity()).isNull();

        city.zones(new HashSet<>(Set.of(serviceZoneBack)));
        assertThat(city.getZones()).containsOnly(serviceZoneBack);
        assertThat(serviceZoneBack.getCity()).isEqualTo(city);

        city.setZones(new HashSet<>());
        assertThat(city.getZones()).doesNotContain(serviceZoneBack);
        assertThat(serviceZoneBack.getCity()).isNull();
    }
}
