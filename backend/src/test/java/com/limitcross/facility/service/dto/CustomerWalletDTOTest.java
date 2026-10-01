package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWalletDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWalletDTO.class);
        CustomerWalletDTO customerWalletDTO1 = new CustomerWalletDTO();
        customerWalletDTO1.setId(1L);
        CustomerWalletDTO customerWalletDTO2 = new CustomerWalletDTO();
        assertThat(customerWalletDTO1).isNotEqualTo(customerWalletDTO2);
        customerWalletDTO2.setId(customerWalletDTO1.getId());
        assertThat(customerWalletDTO1).isEqualTo(customerWalletDTO2);
        customerWalletDTO2.setId(2L);
        assertThat(customerWalletDTO1).isNotEqualTo(customerWalletDTO2);
        customerWalletDTO1.setId(null);
        assertThat(customerWalletDTO1).isNotEqualTo(customerWalletDTO2);
    }
}
