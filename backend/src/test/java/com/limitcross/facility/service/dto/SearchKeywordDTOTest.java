package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchKeywordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchKeywordDTO.class);
        SearchKeywordDTO searchKeywordDTO1 = new SearchKeywordDTO();
        searchKeywordDTO1.setId(1L);
        SearchKeywordDTO searchKeywordDTO2 = new SearchKeywordDTO();
        assertThat(searchKeywordDTO1).isNotEqualTo(searchKeywordDTO2);
        searchKeywordDTO2.setId(searchKeywordDTO1.getId());
        assertThat(searchKeywordDTO1).isEqualTo(searchKeywordDTO2);
        searchKeywordDTO2.setId(2L);
        assertThat(searchKeywordDTO1).isNotEqualTo(searchKeywordDTO2);
        searchKeywordDTO1.setId(null);
        assertThat(searchKeywordDTO1).isNotEqualTo(searchKeywordDTO2);
    }
}
