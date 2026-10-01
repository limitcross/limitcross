package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalTier} and its DTO {@link ProfessionalTierDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalTierMapper extends EntityMapper<ProfessionalTierDTO, ProfessionalTier> {}
