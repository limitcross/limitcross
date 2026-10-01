package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServiceTranslationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceTranslationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTranslation.class);
        ServiceTranslation serviceTranslation1 = getServiceTranslationSample1();
        ServiceTranslation serviceTranslation2 = new ServiceTranslation();
        assertThat(serviceTranslation1).isNotEqualTo(serviceTranslation2);

        serviceTranslation2.setId(serviceTranslation1.getId());
        assertThat(serviceTranslation1).isEqualTo(serviceTranslation2);

        serviceTranslation2 = getServiceTranslationSample2();
        assertThat(serviceTranslation1).isNotEqualTo(serviceTranslation2);
    }

    @Test
    void serviceTest() {
        ServiceTranslation serviceTranslation = getServiceTranslationRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        serviceTranslation.setService(facilityServiceBack);
        assertThat(serviceTranslation.getService()).isEqualTo(facilityServiceBack);

        serviceTranslation.service(null);
        assertThat(serviceTranslation.getService()).isNull();
    }
}
