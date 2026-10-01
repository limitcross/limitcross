package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalDailyMetrics;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalDailyMetricsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalDailyMetrics} and its DTO {@link ProfessionalDailyMetricsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalDailyMetricsMapper extends EntityMapper<ProfessionalDailyMetricsDTO, ProfessionalDailyMetrics> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalDailyMetricsDTO toDto(ProfessionalDailyMetrics s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
