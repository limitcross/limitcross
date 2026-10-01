package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingMediaDTO.class);
        BookingMediaDTO bookingMediaDTO1 = new BookingMediaDTO();
        bookingMediaDTO1.setId(1L);
        BookingMediaDTO bookingMediaDTO2 = new BookingMediaDTO();
        assertThat(bookingMediaDTO1).isNotEqualTo(bookingMediaDTO2);
        bookingMediaDTO2.setId(bookingMediaDTO1.getId());
        assertThat(bookingMediaDTO1).isEqualTo(bookingMediaDTO2);
        bookingMediaDTO2.setId(2L);
        assertThat(bookingMediaDTO1).isNotEqualTo(bookingMediaDTO2);
        bookingMediaDTO1.setId(null);
        assertThat(bookingMediaDTO1).isNotEqualTo(bookingMediaDTO2);
    }
}
