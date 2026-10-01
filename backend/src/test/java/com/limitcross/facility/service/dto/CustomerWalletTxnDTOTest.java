package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWalletTxnDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWalletTxnDTO.class);
        CustomerWalletTxnDTO customerWalletTxnDTO1 = new CustomerWalletTxnDTO();
        customerWalletTxnDTO1.setId(1L);
        CustomerWalletTxnDTO customerWalletTxnDTO2 = new CustomerWalletTxnDTO();
        assertThat(customerWalletTxnDTO1).isNotEqualTo(customerWalletTxnDTO2);
        customerWalletTxnDTO2.setId(customerWalletTxnDTO1.getId());
        assertThat(customerWalletTxnDTO1).isEqualTo(customerWalletTxnDTO2);
        customerWalletTxnDTO2.setId(2L);
        assertThat(customerWalletTxnDTO1).isNotEqualTo(customerWalletTxnDTO2);
        customerWalletTxnDTO1.setId(null);
        assertThat(customerWalletTxnDTO1).isNotEqualTo(customerWalletTxnDTO2);
    }
}
