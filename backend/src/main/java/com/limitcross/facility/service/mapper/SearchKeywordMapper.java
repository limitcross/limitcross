package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.SearchKeyword;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.SearchKeywordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SearchKeyword} and its DTO {@link SearchKeywordDTO}.
 */
@Mapper(componentModel = "spring")
public interface SearchKeywordMapper extends EntityMapper<SearchKeywordDTO, SearchKeyword> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    SearchKeywordDTO toDto(SearchKeyword s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}
