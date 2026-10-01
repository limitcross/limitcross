package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.SlotCapacityAsserts.*;
import static com.limitcross.facility.domain.SlotCapacityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotCapacityMapperTest {

    private SlotCapacityMapper slotCapacityMapper;

    @BeforeEach
    void setUp() {
        slotCapacityMapper = new SlotCapacityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSlotCapacitySample1();
        var actual = slotCapacityMapper.toEntity(slotCapacityMapper.toDto(expected));
        assertSlotCapacityAllPropertiesEquals(expected, actual);
    }
}
