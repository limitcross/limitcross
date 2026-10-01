package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingStatusHistory;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingStatusHistory} and its DTO {@link BookingStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingStatusHistoryMapper extends EntityMapper<BookingStatusHistoryDTO, BookingStatusHistory> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingStatusHistoryDTO toDto(BookingStatusHistory s);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
