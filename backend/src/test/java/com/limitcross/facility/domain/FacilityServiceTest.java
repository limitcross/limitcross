package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServiceAddonTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;
import static com.limitcross.facility.domain.ServiceTranslationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FacilityServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityService.class);
        FacilityService facilityService1 = getFacilityServiceSample1();
        FacilityService facilityService2 = new FacilityService();
        assertThat(facilityService1).isNotEqualTo(facilityService2);

        facilityService2.setId(facilityService1.getId());
        assertThat(facilityService1).isEqualTo(facilityService2);

        facilityService2 = getFacilityServiceSample2();
        assertThat(facilityService1).isNotEqualTo(facilityService2);
    }

    @Test
    void servicePackageTest() {
        FacilityService facilityService = getFacilityServiceRandomSampleGenerator();
        ServicePackage servicePackageBack = getServicePackageRandomSampleGenerator();

        facilityService.addServicePackage(servicePackageBack);
        assertThat(facilityService.getServicePackages()).containsOnly(servicePackageBack);
        assertThat(servicePackageBack.getService()).isEqualTo(facilityService);

        facilityService.removeServicePackage(servicePackageBack);
        assertThat(facilityService.getServicePackages()).doesNotContain(servicePackageBack);
        assertThat(servicePackageBack.getService()).isNull();

        facilityService.servicePackages(new HashSet<>(Set.of(servicePackageBack)));
        assertThat(facilityService.getServicePackages()).containsOnly(servicePackageBack);
        assertThat(servicePackageBack.getService()).isEqualTo(facilityService);

        facilityService.setServicePackages(new HashSet<>());
        assertThat(facilityService.getServicePackages()).doesNotContain(servicePackageBack);
        assertThat(servicePackageBack.getService()).isNull();
    }

    @Test
    void addonTest() {
        FacilityService facilityService = getFacilityServiceRandomSampleGenerator();
        ServiceAddon serviceAddonBack = getServiceAddonRandomSampleGenerator();

        facilityService.addAddon(serviceAddonBack);
        assertThat(facilityService.getAddons()).containsOnly(serviceAddonBack);
        assertThat(serviceAddonBack.getService()).isEqualTo(facilityService);

        facilityService.removeAddon(serviceAddonBack);
        assertThat(facilityService.getAddons()).doesNotContain(serviceAddonBack);
        assertThat(serviceAddonBack.getService()).isNull();

        facilityService.addons(new HashSet<>(Set.of(serviceAddonBack)));
        assertThat(facilityService.getAddons()).containsOnly(serviceAddonBack);
        assertThat(serviceAddonBack.getService()).isEqualTo(facilityService);

        facilityService.setAddons(new HashSet<>());
        assertThat(facilityService.getAddons()).doesNotContain(serviceAddonBack);
        assertThat(serviceAddonBack.getService()).isNull();
    }

    @Test
    void translationTest() {
        FacilityService facilityService = getFacilityServiceRandomSampleGenerator();
        ServiceTranslation serviceTranslationBack = getServiceTranslationRandomSampleGenerator();

        facilityService.addTranslation(serviceTranslationBack);
        assertThat(facilityService.getTranslations()).containsOnly(serviceTranslationBack);
        assertThat(serviceTranslationBack.getService()).isEqualTo(facilityService);

        facilityService.removeTranslation(serviceTranslationBack);
        assertThat(facilityService.getTranslations()).doesNotContain(serviceTranslationBack);
        assertThat(serviceTranslationBack.getService()).isNull();

        facilityService.translations(new HashSet<>(Set.of(serviceTranslationBack)));
        assertThat(facilityService.getTranslations()).containsOnly(serviceTranslationBack);
        assertThat(serviceTranslationBack.getService()).isEqualTo(facilityService);

        facilityService.setTranslations(new HashSet<>());
        assertThat(facilityService.getTranslations()).doesNotContain(serviceTranslationBack);
        assertThat(serviceTranslationBack.getService()).isNull();
    }

    @Test
    void categoryTest() {
        FacilityService facilityService = getFacilityServiceRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        facilityService.setCategory(serviceCategoryBack);
        assertThat(facilityService.getCategory()).isEqualTo(serviceCategoryBack);

        facilityService.category(null);
        assertThat(facilityService.getCategory()).isNull();
    }
}
