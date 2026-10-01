package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.Payment;
import com.limitcross.facility.domain.Refund;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.dto.PaymentDTO;
import com.limitcross.facility.service.dto.RefundDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Refund} and its DTO {@link RefundDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefundMapper extends EntityMapper<RefundDTO, Refund> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingNo")
    RefundDTO toDto(Refund s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("bookingBookingNo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookingNo", source = "bookingNo")
    BookingDTO toDtoBookingBookingNo(Booking booking);
}
