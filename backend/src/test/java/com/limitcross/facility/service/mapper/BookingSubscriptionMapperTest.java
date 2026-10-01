package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BookingSubscriptionAsserts.*;
import static com.limitcross.facility.domain.BookingSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingSubscriptionMapperTest {

    private BookingSubscriptionMapper bookingSubscriptionMapper;

    @BeforeEach
    void setUp() {
        bookingSubscriptionMapper = new BookingSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookingSubscriptionSample1();
        var actual = bookingSubscriptionMapper.toEntity(bookingSubscriptionMapper.toDto(expected));
        assertBookingSubscriptionAllPropertiesEquals(expected, actual);
    }
}
