package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingAssignmentDTO.class);
        BookingAssignmentDTO bookingAssignmentDTO1 = new BookingAssignmentDTO();
        bookingAssignmentDTO1.setId(1L);
        BookingAssignmentDTO bookingAssignmentDTO2 = new BookingAssignmentDTO();
        assertThat(bookingAssignmentDTO1).isNotEqualTo(bookingAssignmentDTO2);
        bookingAssignmentDTO2.setId(bookingAssignmentDTO1.getId());
        assertThat(bookingAssignmentDTO1).isEqualTo(bookingAssignmentDTO2);
        bookingAssignmentDTO2.setId(2L);
        assertThat(bookingAssignmentDTO1).isNotEqualTo(bookingAssignmentDTO2);
        bookingAssignmentDTO1.setId(null);
        assertThat(bookingAssignmentDTO1).isNotEqualTo(bookingAssignmentDTO2);
    }
}
