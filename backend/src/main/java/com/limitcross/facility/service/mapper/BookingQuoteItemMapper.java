package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.BookingQuote;
import com.limitcross.facility.domain.BookingQuoteItem;
import com.limitcross.facility.service.dto.BookingQuoteDTO;
import com.limitcross.facility.service.dto.BookingQuoteItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingQuoteItem} and its DTO {@link BookingQuoteItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingQuoteItemMapper extends EntityMapper<BookingQuoteItemDTO, BookingQuoteItem> {
    @Mapping(target = "quote", source = "quote", qualifiedByName = "bookingQuoteId")
    BookingQuoteItemDTO toDto(BookingQuoteItem s);

    @Named("bookingQuoteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingQuoteDTO toDtoBookingQuoteId(BookingQuote bookingQuote);
}
