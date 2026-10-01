package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataDeletionRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataDeletionRequestDTO.class);
        DataDeletionRequestDTO dataDeletionRequestDTO1 = new DataDeletionRequestDTO();
        dataDeletionRequestDTO1.setId(1L);
        DataDeletionRequestDTO dataDeletionRequestDTO2 = new DataDeletionRequestDTO();
        assertThat(dataDeletionRequestDTO1).isNotEqualTo(dataDeletionRequestDTO2);
        dataDeletionRequestDTO2.setId(dataDeletionRequestDTO1.getId());
        assertThat(dataDeletionRequestDTO1).isEqualTo(dataDeletionRequestDTO2);
        dataDeletionRequestDTO2.setId(2L);
        assertThat(dataDeletionRequestDTO1).isNotEqualTo(dataDeletionRequestDTO2);
        dataDeletionRequestDTO1.setId(null);
        assertThat(dataDeletionRequestDTO1).isNotEqualTo(dataDeletionRequestDTO2);
    }
}
