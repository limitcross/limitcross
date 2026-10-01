package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.SupportTicket;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.SupportTicketDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupportTicket} and its DTO {@link SupportTicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupportTicketMapper extends EntityMapper<SupportTicketDTO, SupportTicket> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "userLogin")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    SupportTicketDTO toDto(SupportTicket s);

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
