package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingRescheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingRescheduleDTO.class);
        BookingRescheduleDTO bookingRescheduleDTO1 = new BookingRescheduleDTO();
        bookingRescheduleDTO1.setId(1L);
        BookingRescheduleDTO bookingRescheduleDTO2 = new BookingRescheduleDTO();
        assertThat(bookingRescheduleDTO1).isNotEqualTo(bookingRescheduleDTO2);
        bookingRescheduleDTO2.setId(bookingRescheduleDTO1.getId());
        assertThat(bookingRescheduleDTO1).isEqualTo(bookingRescheduleDTO2);
        bookingRescheduleDTO2.setId(2L);
        assertThat(bookingRescheduleDTO1).isNotEqualTo(bookingRescheduleDTO2);
        bookingRescheduleDTO1.setId(null);
        assertThat(bookingRescheduleDTO1).isNotEqualTo(bookingRescheduleDTO2);
    }
}
