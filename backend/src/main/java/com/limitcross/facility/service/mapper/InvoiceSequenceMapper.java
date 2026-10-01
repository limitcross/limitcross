package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.InvoiceSequence;
import com.limitcross.facility.service.dto.InvoiceSequenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceSequence} and its DTO {@link InvoiceSequenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceSequenceMapper extends EntityMapper<InvoiceSequenceDTO, InvoiceSequence> {}
