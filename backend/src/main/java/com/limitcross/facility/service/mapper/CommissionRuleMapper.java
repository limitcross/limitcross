package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CommissionRule;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CommissionRuleDTO;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommissionRule} and its DTO {@link CommissionRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommissionRuleMapper extends EntityMapper<CommissionRuleDTO, CommissionRule> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryName")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    @Mapping(target = "tier", source = "tier", qualifiedByName = "professionalTierName")
    CommissionRuleDTO toDto(CommissionRule s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);

    @Named("professionalTierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProfessionalTierDTO toDtoProfessionalTierName(ProfessionalTier professionalTier);
}
