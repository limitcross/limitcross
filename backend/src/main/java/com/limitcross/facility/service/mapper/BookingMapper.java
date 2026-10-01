package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingSubscription;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingSubscriptionDTO;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CouponDTO;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.SlotCapacityDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userLogin")
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    @Mapping(target = "address", source = "address", qualifiedByName = "customerAddressLine1")
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "coupon", source = "coupon", qualifiedByName = "couponCode")
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "bookingSubscriptionId")
    @Mapping(target = "slotCapacity", source = "slotCapacity", qualifiedByName = "slotCapacityId")
    BookingDTO toDto(Booking s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);

    @Named("customerAddressLine1")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "line1", source = "line1")
    CustomerAddressDTO toDtoCustomerAddressLine1(CustomerAddress customerAddress);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("couponCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CouponDTO toDtoCouponCode(Coupon coupon);

    @Named("bookingSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingSubscriptionDTO toDtoBookingSubscriptionId(BookingSubscription bookingSubscription);

    @Named("slotCapacityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SlotCapacityDTO toDtoSlotCapacityId(SlotCapacity slotCapacity);
}
