package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.SupportTicketAsserts.*;
import static com.limitcross.facility.domain.SupportTicketTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupportTicketMapperTest {

    private SupportTicketMapper supportTicketMapper;

    @BeforeEach
    void setUp() {
        supportTicketMapper = new SupportTicketMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupportTicketSample1();
        var actual = supportTicketMapper.toEntity(supportTicketMapper.toDto(expected));
        assertSupportTicketAllPropertiesEquals(expected, actual);
    }
}
