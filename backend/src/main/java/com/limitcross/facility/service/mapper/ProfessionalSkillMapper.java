package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalSkill;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalSkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalSkill} and its DTO {@link ProfessionalSkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalSkillMapper extends EntityMapper<ProfessionalSkillDTO, ProfessionalSkill> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalSkillDTO toDto(ProfessionalSkill s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
