package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.AppConfig;
import com.limitcross.facility.service.dto.AppConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppConfig} and its DTO {@link AppConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppConfigMapper extends EntityMapper<AppConfigDTO, AppConfig> {}
