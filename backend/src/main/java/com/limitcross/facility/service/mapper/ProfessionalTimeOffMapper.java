package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTimeOff;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalTimeOffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalTimeOff} and its DTO {@link ProfessionalTimeOffDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalTimeOffMapper extends EntityMapper<ProfessionalTimeOffDTO, ProfessionalTimeOff> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    ProfessionalTimeOffDTO toDto(ProfessionalTimeOff s);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);
}
