package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingStatusHistoryAsserts.*;
import static com.limitcross.facility.domain.BookingStatusHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingStatusHistoryMapperTest {

    private BookingStatusHistoryMapper bookingStatusHistoryMapper;

    @BeforeEach
    void setUp() {
        bookingStatusHistoryMapper = new BookingStatusHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingStatusHistorySample1();
        var actual = bookingStatusHistoryMapper.toEntity(bookingStatusHistoryMapper.toDto(expected));
        assertBookingStatusHistoryAllPropertiesEquals(expected, actual);
    }
}
