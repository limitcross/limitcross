package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CancellationPolicyTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CancellationPolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CancellationPolicy.class);
        CancellationPolicy cancellationPolicy1 = getCancellationPolicySample1();
        CancellationPolicy cancellationPolicy2 = new CancellationPolicy();
        assertThat(cancellationPolicy1).isNotEqualTo(cancellationPolicy2);

        cancellationPolicy2.setId(cancellationPolicy1.getId());
        assertThat(cancellationPolicy1).isEqualTo(cancellationPolicy2);

        cancellationPolicy2 = getCancellationPolicySample2();
        assertThat(cancellationPolicy1).isNotEqualTo(cancellationPolicy2);
    }

    @Test
    void categoryTest() {
        CancellationPolicy cancellationPolicy = getCancellationPolicyRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        cancellationPolicy.setCategory(serviceCategoryBack);
        assertThat(cancellationPolicy.getCategory()).isEqualTo(serviceCategoryBack);

        cancellationPolicy.category(null);
        assertThat(cancellationPolicy.getCategory()).isNull();
    }

    @Test
    void serviceTest() {
        CancellationPolicy cancellationPolicy = getCancellationPolicyRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        cancellationPolicy.setService(facilityServiceBack);
        assertThat(cancellationPolicy.getService()).isEqualTo(facilityServiceBack);

        cancellationPolicy.service(null);
        assertThat(cancellationPolicy.getService()).isNull();
    }
}
