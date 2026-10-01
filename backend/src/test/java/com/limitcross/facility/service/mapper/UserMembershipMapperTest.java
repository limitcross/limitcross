package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.UserMembershipAsserts.*;
import static com.limitcross.facility.domain.UserMembershipTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMembershipMapperTest {

    private UserMembershipMapper userMembershipMapper;

    @BeforeEach
    void setUp() {
        userMembershipMapper = new UserMembershipMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserMembershipSample1();
        var actual = userMembershipMapper.toEntity(userMembershipMapper.toDto(expected));
        assertUserMembershipAllPropertiesEquals(expected, actual);
    }
}
