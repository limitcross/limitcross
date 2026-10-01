package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingAssignment;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.service.dto.BookingAssignmentDTO;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingAssignment} and its DTO {@link BookingAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingAssignmentMapper extends EntityMapper<BookingAssignmentDTO, BookingAssignment> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    BookingAssignmentDTO toDto(BookingAssignment s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
