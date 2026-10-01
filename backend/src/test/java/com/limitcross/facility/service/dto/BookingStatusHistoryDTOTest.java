package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingStatusHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingStatusHistoryDTO.class);
        BookingStatusHistoryDTO bookingStatusHistoryDTO1 = new BookingStatusHistoryDTO();
        bookingStatusHistoryDTO1.setId(1L);
        BookingStatusHistoryDTO bookingStatusHistoryDTO2 = new BookingStatusHistoryDTO();
        assertThat(bookingStatusHistoryDTO1).isNotEqualTo(bookingStatusHistoryDTO2);
        bookingStatusHistoryDTO2.setId(bookingStatusHistoryDTO1.getId());
        assertThat(bookingStatusHistoryDTO1).isEqualTo(bookingStatusHistoryDTO2);
        bookingStatusHistoryDTO2.setId(2L);
        assertThat(bookingStatusHistoryDTO1).isNotEqualTo(bookingStatusHistoryDTO2);
        bookingStatusHistoryDTO1.setId(null);
        assertThat(bookingStatusHistoryDTO1).isNotEqualTo(bookingStatusHistoryDTO2);
    }
}
