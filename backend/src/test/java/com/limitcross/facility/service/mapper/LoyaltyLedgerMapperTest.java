package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.LoyaltyLedgerAsserts.*;
import static com.limitcross.facility.domain.LoyaltyLedgerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoyaltyLedgerMapperTest {

    private LoyaltyLedgerMapper loyaltyLedgerMapper;

    @BeforeEach
    void setUp() {
        loyaltyLedgerMapper = new LoyaltyLedgerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLoyaltyLedgerSample1();
        var actual = loyaltyLedgerMapper.toEntity(loyaltyLedgerMapper.toDto(expected));
        assertLoyaltyLedgerAllPropertiesEquals(expected, actual);
    }
}
