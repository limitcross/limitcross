package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.CouponRedemption;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.CouponDTO;
import com.limitcross.facility.service.dto.CouponRedemptionDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CouponRedemption} and its DTO {@link CouponRedemptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouponRedemptionMapper extends EntityMapper<CouponRedemptionDTO, CouponRedemption> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "coupon", source = "coupon", qualifiedByName = "couponCode")
    CouponRedemptionDTO toDto(CouponRedemption s);

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

    @Named("couponCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CouponDTO toDtoCouponCode(Coupon coupon);
}
