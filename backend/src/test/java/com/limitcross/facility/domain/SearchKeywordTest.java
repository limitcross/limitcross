package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.SearchKeywordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchKeywordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchKeyword.class);
        SearchKeyword searchKeyword1 = getSearchKeywordSample1();
        SearchKeyword searchKeyword2 = new SearchKeyword();
        assertThat(searchKeyword1).isNotEqualTo(searchKeyword2);

        searchKeyword2.setId(searchKeyword1.getId());
        assertThat(searchKeyword1).isEqualTo(searchKeyword2);

        searchKeyword2 = getSearchKeywordSample2();
        assertThat(searchKeyword1).isNotEqualTo(searchKeyword2);
    }

    @Test
    void serviceTest() {
        SearchKeyword searchKeyword = getSearchKeywordRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        searchKeyword.setService(facilityServiceBack);
        assertThat(searchKeyword.getService()).isEqualTo(facilityServiceBack);

        searchKeyword.service(null);
        assertThat(searchKeyword.getService()).isNull();
    }
}
