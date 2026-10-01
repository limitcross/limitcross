package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalWallet;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalWalletDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalWallet} and its DTO {@link ProfessionalWalletDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalWalletMapper extends EntityMapper<ProfessionalWalletDTO, ProfessionalWallet> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalWalletDTO toDto(ProfessionalWallet s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
