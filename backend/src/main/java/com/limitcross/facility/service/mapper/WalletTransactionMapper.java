package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.WalletTransaction;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WalletTransaction} and its DTO {@link WalletTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface WalletTransactionMapper extends EntityMapper<WalletTransactionDTO, WalletTransaction> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    WalletTransactionDTO toDto(WalletTransaction s);

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
