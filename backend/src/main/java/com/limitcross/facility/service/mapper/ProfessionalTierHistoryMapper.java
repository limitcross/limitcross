package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.domain.ProfessionalTierHistory;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import com.limitcross.facility.service.dto.ProfessionalTierHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalTierHistory} and its DTO {@link ProfessionalTierHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalTierHistoryMapper extends EntityMapper<ProfessionalTierHistoryDTO, ProfessionalTierHistory> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "tier", source = "tier", qualifiedByName = "professionalTierName")
    ProfessionalTierHistoryDTO toDto(ProfessionalTierHistory s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("professionalTierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProfessionalTierDTO toDtoProfessionalTierName(ProfessionalTier professionalTier);
}
