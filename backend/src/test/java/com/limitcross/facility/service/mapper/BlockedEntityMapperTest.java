package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BlockedEntityAsserts.*;
import static com.limitcross.facility.domain.BlockedEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockedEntityMapperTest {

    private BlockedEntityMapper blockedEntityMapper;

    @BeforeEach
    void setUp() {
        blockedEntityMapper = new BlockedEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBlockedEntitySample1();
        var actual = blockedEntityMapper.toEntity(blockedEntityMapper.toDto(expected));
        assertBlockedEntityAllPropertiesEquals(expected, actual);
    }
}
