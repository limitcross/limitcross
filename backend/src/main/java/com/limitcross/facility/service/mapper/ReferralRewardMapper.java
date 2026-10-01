package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.ReferralReward;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.ReferralRewardDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReferralReward} and its DTO {@link ReferralRewardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReferralRewardMapper extends EntityMapper<ReferralRewardDTO, ReferralReward> {
    @Mapping(target = "referrer", source = "referrer", qualifiedByName = "userLogin")
    @Mapping(target = "referee", source = "referee", qualifiedByName = "userLogin")
    @Mapping(target = "triggerBooking", source = "triggerBooking", qualifiedByName = "bookingBookingNo")
    ReferralRewardDTO toDto(ReferralReward s);

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
