package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.ChatThread;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.ChatThreadDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatThread} and its DTO {@link ChatThreadDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatThreadMapper extends EntityMapper<ChatThreadDTO, ChatThread> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userLogin")
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ChatThreadDTO toDto(ChatThread s);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
