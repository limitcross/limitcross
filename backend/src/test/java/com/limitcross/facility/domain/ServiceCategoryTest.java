package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceCategory.class);
        ServiceCategory serviceCategory1 = getServiceCategorySample1();
        ServiceCategory serviceCategory2 = new ServiceCategory();
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);

        serviceCategory2.setId(serviceCategory1.getId());
        assertThat(serviceCategory1).isEqualTo(serviceCategory2);

        serviceCategory2 = getServiceCategorySample2();
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
    }

    @Test
    void serviceTest() {
        ServiceCategory serviceCategory = getServiceCategoryRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        serviceCategory.addService(facilityServiceBack);
        assertThat(serviceCategory.getServices()).containsOnly(facilityServiceBack);
        assertThat(facilityServiceBack.getCategory()).isEqualTo(serviceCategory);

        serviceCategory.removeService(facilityServiceBack);
        assertThat(serviceCategory.getServices()).doesNotContain(facilityServiceBack);
        assertThat(facilityServiceBack.getCategory()).isNull();

        serviceCategory.services(new HashSet<>(Set.of(facilityServiceBack)));
        assertThat(serviceCategory.getServices()).containsOnly(facilityServiceBack);
        assertThat(facilityServiceBack.getCategory()).isEqualTo(serviceCategory);

        serviceCategory.setServices(new HashSet<>());
        assertThat(serviceCategory.getServices()).doesNotContain(facilityServiceBack);
        assertThat(facilityServiceBack.getCategory()).isNull();
    }

    @Test
    void parentTest() {
        ServiceCategory serviceCategory = getServiceCategoryRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        serviceCategory.setParent(serviceCategoryBack);
        assertThat(serviceCategory.getParent()).isEqualTo(serviceCategoryBack);

        serviceCategory.parent(null);
        assertThat(serviceCategory.getParent()).isNull();
    }
}
