package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingQuoteAsserts.*;
import static com.limitcross.facility.domain.BookingQuoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingQuoteMapperTest {

    private BookingQuoteMapper bookingQuoteMapper;

    @BeforeEach
    void setUp() {
        bookingQuoteMapper = new BookingQuoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingQuoteSample1();
        var actual = bookingQuoteMapper.toEntity(bookingQuoteMapper.toDto(expected));
        assertBookingQuoteAllPropertiesEquals(expected, actual);
    }
}
