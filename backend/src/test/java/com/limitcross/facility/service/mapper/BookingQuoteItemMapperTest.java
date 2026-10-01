package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingQuoteItemAsserts.*;
import static com.limitcross.facility.domain.BookingQuoteItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingQuoteItemMapperTest {

    private BookingQuoteItemMapper bookingQuoteItemMapper;

    @BeforeEach
    void setUp() {
        bookingQuoteItemMapper = new BookingQuoteItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingQuoteItemSample1();
        var actual = bookingQuoteItemMapper.toEntity(bookingQuoteItemMapper.toDto(expected));
        assertBookingQuoteItemAllPropertiesEquals(expected, actual);
    }
}
