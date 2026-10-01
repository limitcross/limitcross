package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServiceAddonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceAddonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceAddon.class);
        ServiceAddon serviceAddon1 = getServiceAddonSample1();
        ServiceAddon serviceAddon2 = new ServiceAddon();
        assertThat(serviceAddon1).isNotEqualTo(serviceAddon2);

        serviceAddon2.setId(serviceAddon1.getId());
        assertThat(serviceAddon1).isEqualTo(serviceAddon2);

        serviceAddon2 = getServiceAddonSample2();
        assertThat(serviceAddon1).isNotEqualTo(serviceAddon2);
    }

    @Test
    void serviceTest() {
        ServiceAddon serviceAddon = getServiceAddonRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        serviceAddon.setService(facilityServiceBack);
        assertThat(serviceAddon.getService()).isEqualTo(facilityServiceBack);

        serviceAddon.service(null);
        assertThat(serviceAddon.getService()).isNull();
    }
}
