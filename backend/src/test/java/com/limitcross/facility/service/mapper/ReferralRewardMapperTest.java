package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ReferralRewardAsserts.*;
import static com.limitcross.facility.domain.ReferralRewardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReferralRewardMapperTest {

    private ReferralRewardMapper referralRewardMapper;

    @BeforeEach
    void setUp() {
        referralRewardMapper = new ReferralRewardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReferralRewardSample1();
        var actual = referralRewardMapper.toEntity(referralRewardMapper.toDto(expected));
        assertReferralRewardAllPropertiesEquals(expected, actual);
    }
}
