package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Payout;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.service.dto.PayoutDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payout} and its DTO {@link PayoutDTO}.
 */
@Mapper(componentModel = "spring")
public interface PayoutMapper extends EntityMapper<PayoutDTO, Payout> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    PayoutDTO toDto(Payout s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
