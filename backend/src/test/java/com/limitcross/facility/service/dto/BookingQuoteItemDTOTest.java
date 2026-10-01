package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingQuoteItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingQuoteItemDTO.class);
        BookingQuoteItemDTO bookingQuoteItemDTO1 = new BookingQuoteItemDTO();
        bookingQuoteItemDTO1.setId(1L);
        BookingQuoteItemDTO bookingQuoteItemDTO2 = new BookingQuoteItemDTO();
        assertThat(bookingQuoteItemDTO1).isNotEqualTo(bookingQuoteItemDTO2);
        bookingQuoteItemDTO2.setId(bookingQuoteItemDTO1.getId());
        assertThat(bookingQuoteItemDTO1).isEqualTo(bookingQuoteItemDTO2);
        bookingQuoteItemDTO2.setId(2L);
        assertThat(bookingQuoteItemDTO1).isNotEqualTo(bookingQuoteItemDTO2);
        bookingQuoteItemDTO1.setId(null);
        assertThat(bookingQuoteItemDTO1).isNotEqualTo(bookingQuoteItemDTO2);
    }
}
