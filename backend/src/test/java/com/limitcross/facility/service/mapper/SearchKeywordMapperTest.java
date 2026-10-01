package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.SearchKeywordAsserts.*;
import static com.limitcross.facility.domain.SearchKeywordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchKeywordMapperTest {

    private SearchKeywordMapper searchKeywordMapper;

    @BeforeEach
    void setUp() {
        searchKeywordMapper = new SearchKeywordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSearchKeywordSample1();
        var actual = searchKeywordMapper.toEntity(searchKeywordMapper.toDto(expected));
        assertSearchKeywordAllPropertiesEquals(expected, actual);
    }
}
