package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingRescheduleAsserts.*;
import static com.limitcross.facility.domain.BookingRescheduleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingRescheduleMapperTest {

    private BookingRescheduleMapper bookingRescheduleMapper;

    @BeforeEach
    void setUp() {
        bookingRescheduleMapper = new BookingRescheduleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingRescheduleSample1();
        var actual = bookingRescheduleMapper.toEntity(bookingRescheduleMapper.toDto(expected));
        assertBookingRescheduleAllPropertiesEquals(expected, actual);
    }
}
