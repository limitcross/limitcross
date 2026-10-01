package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.WarrantyClaim;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.UserDTO;
import com.limitcross.facility.service.dto.WarrantyClaimDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WarrantyClaim} and its DTO {@link WarrantyClaimDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarrantyClaimMapper extends EntityMapper<WarrantyClaimDTO, WarrantyClaim> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userLogin")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    @Mapping(target = "redoBooking", source = "redoBooking", qualifiedByName = "bookingBookingNo")
    WarrantyClaimDTO toDto(WarrantyClaim s);

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
