package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.SosAlert;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.SosAlertDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SosAlert} and its DTO {@link SosAlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface SosAlertMapper extends EntityMapper<SosAlertDTO, SosAlert> {
    @Mapping(target = "raisedBy", source = "raisedBy", qualifiedByName = "userLogin")
    @Mapping(target = "handledBy", source = "handledBy", qualifiedByName = "userLogin")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    SosAlertDTO toDto(SosAlert s);

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
