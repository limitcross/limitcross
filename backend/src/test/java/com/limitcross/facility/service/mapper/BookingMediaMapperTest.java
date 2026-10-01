package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingMediaAsserts.*;
import static com.limitcross.facility.domain.BookingMediaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingMediaMapperTest {

    private BookingMediaMapper bookingMediaMapper;

    @BeforeEach
    void setUp() {
        bookingMediaMapper = new BookingMediaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingMediaSample1();
        var actual = bookingMediaMapper.toEntity(bookingMediaMapper.toDto(expected));
        assertBookingMediaAllPropertiesEquals(expected, actual);
    }
}
