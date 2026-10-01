package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CustomerWalletTxnAsserts.*;
import static com.limitcross.facility.domain.CustomerWalletTxnTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerWalletTxnMapperTest {

    private CustomerWalletTxnMapper customerWalletTxnMapper;

    @BeforeEach
    void setUp() {
        customerWalletTxnMapper = new CustomerWalletTxnMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerWalletTxnSample1();
        var actual = customerWalletTxnMapper.toEntity(customerWalletTxnMapper.toDto(expected));
        assertCustomerWalletTxnAllPropertiesEquals(expected, actual);
    }
}
