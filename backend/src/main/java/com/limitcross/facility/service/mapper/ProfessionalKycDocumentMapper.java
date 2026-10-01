package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalKycDocument;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalKycDocumentDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalKycDocument} and its DTO {@link ProfessionalKycDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalKycDocumentMapper extends EntityMapper<ProfessionalKycDocumentDTO, ProfessionalKycDocument> {
    @Mapping(target = "reviewer", source = "reviewer", qualifiedByName = "userLogin")
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalKycDocumentDTO toDto(ProfessionalKycDocument s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
