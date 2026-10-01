package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ServiceZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceZoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceZone.class);
        ServiceZone serviceZone1 = getServiceZoneSample1();
        ServiceZone serviceZone2 = new ServiceZone();
        assertThat(serviceZone1).isNotEqualTo(serviceZone2);

        serviceZone2.setId(serviceZone1.getId());
        assertThat(serviceZone1).isEqualTo(serviceZone2);

        serviceZone2 = getServiceZoneSample2();
        assertThat(serviceZone1).isNotEqualTo(serviceZone2);
    }

    @Test
    void cityTest() {
        ServiceZone serviceZone = getServiceZoneRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        serviceZone.setCity(cityBack);
        assertThat(serviceZone.getCity()).isEqualTo(cityBack);

        serviceZone.city(null);
        assertThat(serviceZone.getCity()).isNull();
    }

    @Test
    void professionalTest() {
        ServiceZone serviceZone = getServiceZoneRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        serviceZone.addProfessional(professionalBack);
        assertThat(serviceZone.getProfessionals()).containsOnly(professionalBack);
        assertThat(professionalBack.getZones()).containsOnly(serviceZone);

        serviceZone.removeProfessional(professionalBack);
        assertThat(serviceZone.getProfessionals()).doesNotContain(professionalBack);
        assertThat(professionalBack.getZones()).doesNotContain(serviceZone);

        serviceZone.professionals(new HashSet<>(Set.of(professionalBack)));
        assertThat(serviceZone.getProfessionals()).containsOnly(professionalBack);
        assertThat(professionalBack.getZones()).containsOnly(serviceZone);

        serviceZone.setProfessionals(new HashSet<>());
        assertThat(serviceZone.getProfessionals()).doesNotContain(professionalBack);
        assertThat(professionalBack.getZones()).doesNotContain(serviceZone);
    }
}
