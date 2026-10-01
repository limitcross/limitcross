package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingItem;
import com.limitcross.facility.domain.ServiceAddon;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.BookingItemDTO;
import com.limitcross.facility.service.dto.ServiceAddonDTO;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingItem} and its DTO {@link BookingItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingItemMapper extends EntityMapper<BookingItemDTO, BookingItem> {
    @Mapping(target = "servicePackage", source = "servicePackage", qualifiedByName = "servicePackageName")
    @Mapping(target = "addon", source = "addon", qualifiedByName = "serviceAddonName")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingItemDTO toDto(BookingItem s);

    @Named("servicePackageName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServicePackageDTO toDtoServicePackageName(ServicePackage servicePackage);

    @Named("serviceAddonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceAddonDTO toDtoServiceAddonName(ServiceAddon serviceAddon);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
