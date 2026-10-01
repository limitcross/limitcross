package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ProfessionalLocationLog;
import com.limitcross.facility.service.dto.ProfessionalLocationLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalLocationLog} and its DTO {@link ProfessionalLocationLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalLocationLogMapper extends EntityMapper<ProfessionalLocationLogDTO, ProfessionalLocationLog> {}
