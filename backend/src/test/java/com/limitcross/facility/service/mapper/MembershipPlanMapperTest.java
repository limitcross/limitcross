package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.MembershipPlanAsserts.*;
import static com.limitcross.facility.domain.MembershipPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipPlanMapperTest {

    private MembershipPlanMapper membershipPlanMapper;

    @BeforeEach
    void setUp() {
        membershipPlanMapper = new MembershipPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMembershipPlanSample1();
        var actual = membershipPlanMapper.toEntity(membershipPlanMapper.toDto(expected));
        assertMembershipPlanAllPropertiesEquals(expected, actual);
    }
}
