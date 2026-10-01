package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CustomerWalletAsserts.*;
import static com.limitcross.facility.domain.CustomerWalletTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerWalletMapperTest {

    private CustomerWalletMapper customerWalletMapper;

    @BeforeEach
    void setUp() {
        customerWalletMapper = new CustomerWalletMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerWalletSample1();
        var actual = customerWalletMapper.toEntity(customerWalletMapper.toDto(expected));
        assertCustomerWalletAllPropertiesEquals(expected, actual);
    }
}
