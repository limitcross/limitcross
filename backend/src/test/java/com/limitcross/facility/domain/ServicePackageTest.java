package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicePackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicePackage.class);
        ServicePackage servicePackage1 = getServicePackageSample1();
        ServicePackage servicePackage2 = new ServicePackage();
        assertThat(servicePackage1).isNotEqualTo(servicePackage2);

        servicePackage2.setId(servicePackage1.getId());
        assertThat(servicePackage1).isEqualTo(servicePackage2);

        servicePackage2 = getServicePackageSample2();
        assertThat(servicePackage1).isNotEqualTo(servicePackage2);
    }

    @Test
    void serviceTest() {
        ServicePackage servicePackage = getServicePackageRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        servicePackage.setService(facilityServiceBack);
        assertThat(servicePackage.getService()).isEqualTo(facilityServiceBack);

        servicePackage.service(null);
        assertThat(servicePackage.getService()).isNull();
    }
}
