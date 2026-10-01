package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.domain.SlotHold;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.SlotCapacityDTO;
import com.limitcross.facility.service.dto.SlotHoldDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SlotHold} and its DTO {@link SlotHoldDTO}.
 */
@Mapper(componentModel = "spring")
public interface SlotHoldMapper extends EntityMapper<SlotHoldDTO, SlotHold> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "slotCapacity", source = "slotCapacity", qualifiedByName = "slotCapacityId")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    SlotHoldDTO toDto(SlotHold s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("slotCapacityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SlotCapacityDTO toDtoSlotCapacityId(SlotCapacity slotCapacity);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
