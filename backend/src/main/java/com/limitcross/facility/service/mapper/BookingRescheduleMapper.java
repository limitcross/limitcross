package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingReschedule;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingRescheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingReschedule} and its DTO {@link BookingRescheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingRescheduleMapper extends EntityMapper<BookingRescheduleDTO, BookingReschedule> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingRescheduleDTO toDto(BookingReschedule s);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
