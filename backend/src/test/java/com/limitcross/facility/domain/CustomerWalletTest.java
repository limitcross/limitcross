package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CustomerWalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWallet.class);
        CustomerWallet customerWallet1 = getCustomerWalletSample1();
        CustomerWallet customerWallet2 = new CustomerWallet();
        assertThat(customerWallet1).isNotEqualTo(customerWallet2);

        customerWallet2.setId(customerWallet1.getId());
        assertThat(customerWallet1).isEqualTo(customerWallet2);

        customerWallet2 = getCustomerWalletSample2();
        assertThat(customerWallet1).isNotEqualTo(customerWallet2);
    }
}
