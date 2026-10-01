package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.DataDeletionRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataDeletionRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataDeletionRequest.class);
        DataDeletionRequest dataDeletionRequest1 = getDataDeletionRequestSample1();
        DataDeletionRequest dataDeletionRequest2 = new DataDeletionRequest();
        assertThat(dataDeletionRequest1).isNotEqualTo(dataDeletionRequest2);

        dataDeletionRequest2.setId(dataDeletionRequest1.getId());
        assertThat(dataDeletionRequest1).isEqualTo(dataDeletionRequest2);

        dataDeletionRequest2 = getDataDeletionRequestSample2();
        assertThat(dataDeletionRequest1).isNotEqualTo(dataDeletionRequest2);
    }
}
