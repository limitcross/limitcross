package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CommissionRuleAsserts.*;
import static com.limitcross.facility.domain.CommissionRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommissionRuleMapperTest {

    private CommissionRuleMapper commissionRuleMapper;

    @BeforeEach
    void setUp() {
        commissionRuleMapper = new CommissionRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommissionRuleSample1();
        var actual = commissionRuleMapper.toEntity(commissionRuleMapper.toDto(expected));
        assertCommissionRuleAllPropertiesEquals(expected, actual);
    }
}
