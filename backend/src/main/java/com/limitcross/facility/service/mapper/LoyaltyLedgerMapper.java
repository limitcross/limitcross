package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.LoyaltyLedger;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.LoyaltyLedgerDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoyaltyLedger} and its DTO {@link LoyaltyLedgerDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoyaltyLedgerMapper extends EntityMapper<LoyaltyLedgerDTO, LoyaltyLedger> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    LoyaltyLedgerDTO toDto(LoyaltyLedger s);

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
