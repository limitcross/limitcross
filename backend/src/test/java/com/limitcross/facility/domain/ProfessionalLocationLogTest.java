package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalLocationLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalLocationLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalLocationLog.class);
        ProfessionalLocationLog professionalLocationLog1 = getProfessionalLocationLogSample1();
        ProfessionalLocationLog professionalLocationLog2 = new ProfessionalLocationLog();
        assertThat(professionalLocationLog1).isNotEqualTo(professionalLocationLog2);

        professionalLocationLog2.setId(professionalLocationLog1.getId());
        assertThat(professionalLocationLog1).isEqualTo(professionalLocationLog2);

        professionalLocationLog2 = getProfessionalLocationLogSample2();
        assertThat(professionalLocationLog1).isNotEqualTo(professionalLocationLog2);
    }
}
