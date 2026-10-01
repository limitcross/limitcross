package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityPackagePriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityPackagePriceDTO.class);
        CityPackagePriceDTO cityPackagePriceDTO1 = new CityPackagePriceDTO();
        cityPackagePriceDTO1.setId(1L);
        CityPackagePriceDTO cityPackagePriceDTO2 = new CityPackagePriceDTO();
        assertThat(cityPackagePriceDTO1).isNotEqualTo(cityPackagePriceDTO2);
        cityPackagePriceDTO2.setId(cityPackagePriceDTO1.getId());
        assertThat(cityPackagePriceDTO1).isEqualTo(cityPackagePriceDTO2);
        cityPackagePriceDTO2.setId(2L);
        assertThat(cityPackagePriceDTO1).isNotEqualTo(cityPackagePriceDTO2);
        cityPackagePriceDTO1.setId(null);
        assertThat(cityPackagePriceDTO1).isNotEqualTo(cityPackagePriceDTO2);
    }
}
