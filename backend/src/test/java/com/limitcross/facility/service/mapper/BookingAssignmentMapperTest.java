package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingAssignmentAsserts.*;
import static com.limitcross.facility.domain.BookingAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingAssignmentMapperTest {

    private BookingAssignmentMapper bookingAssignmentMapper;

    @BeforeEach
    void setUp() {
        bookingAssignmentMapper = new BookingAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingAssignmentSample1();
        var actual = bookingAssignmentMapper.toEntity(bookingAssignmentMapper.toDto(expected));
        assertBookingAssignmentAllPropertiesEquals(expected, actual);
    }
}
