package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAddressDTO.class);
        CustomerAddressDTO customerAddressDTO1 = new CustomerAddressDTO();
        customerAddressDTO1.setId(1L);
        CustomerAddressDTO customerAddressDTO2 = new CustomerAddressDTO();
        assertThat(customerAddressDTO1).isNotEqualTo(customerAddressDTO2);
        customerAddressDTO2.setId(customerAddressDTO1.getId());
        assertThat(customerAddressDTO1).isEqualTo(customerAddressDTO2);
        customerAddressDTO2.setId(2L);
        assertThat(customerAddressDTO1).isNotEqualTo(customerAddressDTO2);
        customerAddressDTO1.setId(null);
        assertThat(customerAddressDTO1).isNotEqualTo(customerAddressDTO2);
    }
}
