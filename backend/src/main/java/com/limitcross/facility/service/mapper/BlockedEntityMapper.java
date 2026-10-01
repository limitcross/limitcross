package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.BlockedEntity;
import com.limitcross.facility.service.dto.BlockedEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BlockedEntity} and its DTO {@link BlockedEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface BlockedEntityMapper extends EntityMapper<BlockedEntityDTO, BlockedEntity> {}
