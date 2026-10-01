package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.OutboxEventAsserts.*;
import static com.limitcross.facility.domain.OutboxEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutboxEventMapperTest {

    private OutboxEventMapper outboxEventMapper;

    @BeforeEach
    void setUp() {
        outboxEventMapper = new OutboxEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOutboxEventSample1();
        var actual = outboxEventMapper.toEntity(outboxEventMapper.toDto(expected));
        assertOutboxEventAllPropertiesEquals(expected, actual);
    }
}
