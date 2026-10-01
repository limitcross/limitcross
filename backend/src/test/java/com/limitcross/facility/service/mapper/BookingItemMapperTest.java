package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingItemAsserts.*;
import static com.limitcross.facility.domain.BookingItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingItemMapperTest {

    private BookingItemMapper bookingItemMapper;

    @BeforeEach
    void setUp() {
        bookingItemMapper = new BookingItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingItemSample1();
        var actual = bookingItemMapper.toEntity(bookingItemMapper.toDto(expected));
        assertBookingItemAllPropertiesEquals(expected, actual);
    }
}
