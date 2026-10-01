package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.UserConsentAsserts.*;
import static com.limitcross.facility.domain.UserConsentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserConsentMapperTest {

    private UserConsentMapper userConsentMapper;

    @BeforeEach
    void setUp() {
        userConsentMapper = new UserConsentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserConsentSample1();
        var actual = userConsentMapper.toEntity(userConsentMapper.toDto(expected));
        assertUserConsentAllPropertiesEquals(expected, actual);
    }
}
