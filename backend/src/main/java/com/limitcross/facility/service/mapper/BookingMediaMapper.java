package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingMedia;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingMediaDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingMedia} and its DTO {@link BookingMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMediaMapper extends EntityMapper<BookingMediaDTO, BookingMedia> {
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "userLogin")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingMediaDTO toDto(BookingMedia s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
