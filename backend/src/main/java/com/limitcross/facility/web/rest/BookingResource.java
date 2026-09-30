package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.BookingStatus;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.repository.BookingRepository;
import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
public class BookingResource {

    private final BookingRepository bookingRepository;
    private final FacilityServiceRepository serviceRepository;

    public BookingResource(BookingRepository bookingRepository, FacilityServiceRepository serviceRepository) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public List<BookingResponse> getBookings() {
        return bookingRepository.findAllByOwnerLoginOrderByDateDesc(currentLogin()).stream().map(BookingResource::toResponse).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        FacilityService service = serviceRepository.findById(request.serviceId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
        Booking booking = new Booking();
        booking.setOwnerLogin(currentLogin());
        booking.setServiceId(service.getId());
        booking.setServiceTitle(service.getTitle());
        booking.setEmoji(service.getEmoji());
        booking.setPrice(service.getStartingPrice());
        booking.setDate(request.date());
        booking.setTimeSlot(request.timeSlot());
        booking.setAddress(request.address().trim());
        return toResponse(bookingRepository.save(booking));
    }

    @PatchMapping("/{id}")
    public BookingResponse reschedule(@PathVariable String id, @Valid @RequestBody RescheduleRequest request) {
        Booking booking = findOwned(id);
        ensureOpen(booking);
        booking.setDate(request.date());
        booking.setTimeSlot(request.timeSlot());
        booking.touch();
        return toResponse(bookingRepository.save(booking));
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable String id) {
        Booking booking = findOwned(id);
        ensureOpen(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.touch();
        return toResponse(bookingRepository.save(booking));
    }

    private Booking findOwned(String id) {
        return bookingRepository.findOneByIdAndOwnerLogin(id, currentLogin()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    private static void ensureOpen(Booking booking) {
        if (booking.getStatus() != BookingStatus.REQUESTED && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This booking can no longer be changed");
        }
    }

    private static String currentLogin() {
        return SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
    }

    private static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
            booking.getId(), booking.getServiceId(), booking.getServiceTitle(), booking.getEmoji(), booking.getPrice(),
            booking.getServiceFee(), booking.getTotalPrice(), booking.getDate(), booking.getTimeSlot(), booking.getAddress(),
            booking.getStatus().name().toLowerCase(), booking.getAssignedProName(), booking.getProRating()
        );
    }

    public record CreateBookingRequest(
        @NotBlank String serviceId, @NotNull @Future LocalDate date, @NotBlank String timeSlot, @NotBlank String address
    ) {}

    public record RescheduleRequest(@NotNull @Future LocalDate date, @NotBlank String timeSlot) {}

    public record BookingResponse(
        String id, String serviceId, String serviceTitle, String emoji, Integer price, Integer serviceFee, Integer totalPrice,
        LocalDate date, String timeSlot, String address, String status, String assignedProName, Double proRating
    ) {}
}