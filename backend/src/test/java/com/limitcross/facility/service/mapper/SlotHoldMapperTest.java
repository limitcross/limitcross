package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.SlotHoldAsserts.*;
import static com.limitcross.facility.domain.SlotHoldTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotHoldMapperTest {

    private SlotHoldMapper slotHoldMapper;

    @BeforeEach
    void setUp() {
        slotHoldMapper = new SlotHoldMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSlotHoldSample1();
        var actual = slotHoldMapper.toEntity(slotHoldMapper.toDto(expected));
        assertSlotHoldAllPropertiesEquals(expected, actual);
    }
}
