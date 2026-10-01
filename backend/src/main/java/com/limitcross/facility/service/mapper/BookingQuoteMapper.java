package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingQuote;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingQuoteDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingQuote} and its DTO {@link BookingQuoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingQuoteMapper extends EntityMapper<BookingQuoteDTO, BookingQuote> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingQuoteDTO toDto(BookingQuote s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
