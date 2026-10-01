package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingQuoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingQuoteDTO.class);
        BookingQuoteDTO bookingQuoteDTO1 = new BookingQuoteDTO();
        bookingQuoteDTO1.setId(1L);
        BookingQuoteDTO bookingQuoteDTO2 = new BookingQuoteDTO();
        assertThat(bookingQuoteDTO1).isNotEqualTo(bookingQuoteDTO2);
        bookingQuoteDTO2.setId(bookingQuoteDTO1.getId());
        assertThat(bookingQuoteDTO1).isEqualTo(bookingQuoteDTO2);
        bookingQuoteDTO2.setId(2L);
        assertThat(bookingQuoteDTO1).isNotEqualTo(bookingQuoteDTO2);
        bookingQuoteDTO1.setId(null);
        assertThat(bookingQuoteDTO1).isNotEqualTo(bookingQuoteDTO2);
    }
}
