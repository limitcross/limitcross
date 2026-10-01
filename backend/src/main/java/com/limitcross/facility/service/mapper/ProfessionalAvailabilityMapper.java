package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalAvailability;
import com.limitcross.facility.service.dto.ProfessionalAvailabilityDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalAvailability} and its DTO {@link ProfessionalAvailabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalAvailabilityMapper extends EntityMapper<ProfessionalAvailabilityDTO, ProfessionalAvailability> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalAvailabilityDTO toDto(ProfessionalAvailability s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
