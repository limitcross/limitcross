package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.CustomerAddressTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAddress.class);
        CustomerAddress customerAddress1 = getCustomerAddressSample1();
        CustomerAddress customerAddress2 = new CustomerAddress();
        assertThat(customerAddress1).isNotEqualTo(customerAddress2);

        customerAddress2.setId(customerAddress1.getId());
        assertThat(customerAddress1).isEqualTo(customerAddress2);

        customerAddress2 = getCustomerAddressSample2();
        assertThat(customerAddress1).isNotEqualTo(customerAddress2);
    }

    @Test
    void cityTest() {
        CustomerAddress customerAddress = getCustomerAddressRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        customerAddress.setCity(cityBack);
        assertThat(customerAddress.getCity()).isEqualTo(cityBack);

        customerAddress.city(null);
        assertThat(customerAddress.getCity()).isNull();
    }
}
