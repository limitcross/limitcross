package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingSubscriptionDTO.class);
        BookingSubscriptionDTO bookingSubscriptionDTO1 = new BookingSubscriptionDTO();
        bookingSubscriptionDTO1.setId(1L);
        BookingSubscriptionDTO bookingSubscriptionDTO2 = new BookingSubscriptionDTO();
        assertThat(bookingSubscriptionDTO1).isNotEqualTo(bookingSubscriptionDTO2);
        bookingSubscriptionDTO2.setId(bookingSubscriptionDTO1.getId());
        assertThat(bookingSubscriptionDTO1).isEqualTo(bookingSubscriptionDTO2);
        bookingSubscriptionDTO2.setId(2L);
        assertThat(bookingSubscriptionDTO1).isNotEqualTo(bookingSubscriptionDTO2);
        bookingSubscriptionDTO1.setId(null);
        assertThat(bookingSubscriptionDTO1).isNotEqualTo(bookingSubscriptionDTO2);
    }
}
