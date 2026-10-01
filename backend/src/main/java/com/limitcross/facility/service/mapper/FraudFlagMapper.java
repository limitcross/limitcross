package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FraudFlag;
import com.limitcross.facility.service.dto.FraudFlagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FraudFlag} and its DTO {@link FraudFlagDTO}.
 */
@Mapper(componentModel = "spring")
public interface FraudFlagMapper extends EntityMapper<FraudFlagDTO, FraudFlag> {}
