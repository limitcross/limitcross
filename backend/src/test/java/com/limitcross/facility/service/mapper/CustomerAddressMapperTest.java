package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CustomerAddressAsserts.*;
import static com.limitcross.facility.domain.CustomerAddressTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerAddressMapperTest {

    private CustomerAddressMapper customerAddressMapper;

    @BeforeEach
    void setUp() {
        customerAddressMapper = new CustomerAddressMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerAddressSample1();
        var actual = customerAddressMapper.toEntity(customerAddressMapper.toDto(expected));
        assertCustomerAddressAllPropertiesEquals(expected, actual);
    }
}
