package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.IncentiveRuleAsserts.*;
import static com.limitcross.facility.domain.IncentiveRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncentiveRuleMapperTest {

    private IncentiveRuleMapper incentiveRuleMapper;

    @BeforeEach
    void setUp() {
        incentiveRuleMapper = new IncentiveRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIncentiveRuleSample1();
        var actual = incentiveRuleMapper.toEntity(incentiveRuleMapper.toDto(expected));
        assertIncentiveRuleAllPropertiesEquals(expected, actual);
    }
}
