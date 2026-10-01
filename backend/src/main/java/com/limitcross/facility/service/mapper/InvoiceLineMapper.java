package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Invoice;
import com.limitcross.facility.domain.InvoiceLine;
import com.limitcross.facility.service.dto.InvoiceDTO;
import com.limitcross.facility.service.dto.InvoiceLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceLine} and its DTO {@link InvoiceLineDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceLineMapper extends EntityMapper<InvoiceLineDTO, InvoiceLine> {
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceInvoiceNo")
    InvoiceLineDTO toDto(InvoiceLine s);

    @Named("invoiceInvoiceNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceNo", source = "invoiceNo")
    InvoiceDTO toDtoInvoiceInvoiceNo(Invoice invoice);
}
