package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.ActorType;
import com.limitcross.facility.domain.enumeration.BookingPaymentStatus;
import com.limitcross.facility.domain.enumeration.BookingSource;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import com.limitcross.facility.domain.enumeration.PaymentMode;
import com.limitcross.facility.repository.BookingRepository;
import com.limitcross.facility.repository.CityRepository;
import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
@Transactional
public class CustomerBookingResource {

    private static final DateTimeFormatter LEGACY_TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private final BookingRepository bookingRepository;
    private final FacilityServiceRepository serviceRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    public CustomerBookingResource(
        BookingRepository bookingRepository,
        FacilityServiceRepository serviceRepository,
        CityRepository cityRepository,
        UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<BookingResponse> getBookings() {
        String login = currentLogin();
        return bookingRepository.findAllByCustomer_LoginOrderByScheduledStartDesc(login).stream()
            .map(CustomerBookingResource::toResponse)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        String login = currentLogin();
        FacilityService service = serviceRepository.findWithPackagesByCode(request.serviceId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
        ServicePackage servicePackage = service.getServicePackages().stream()
            .filter(candidate -> Boolean.TRUE.equals(candidate.getActive()))
            .min((left, right) -> left.getBasePrice().compareTo(right.getBasePrice()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Service has no active package"));
        City city = request.cityId() == null
            ? cityRepository.findFirstByActiveTrueOrderByIdAsc().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.CONFLICT, "No active city is configured"))
            : cityRepository.findById(request.cityId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
        User customer = userRepository.findOneByLogin(login).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Instant start = LocalDateTime.of(request.date(), parseStartTime(request.timeSlot())).atZone(ZoneId.systemDefault()).toInstant();
        BigDecimal serviceFee = BigDecimal.valueOf(49);
        BigDecimal total = servicePackage.getBasePrice().add(serviceFee);
        Booking booking = new Booking();
        booking.setBookingNo("BK" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase(Locale.ROOT));
        booking.setPublicId(UUID.randomUUID());
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setCity(city);
        booking.setServiceTitle(service.getTitle());
        booking.setAddressSnapshot(request.address().trim());
        booking.storeLegacyTimeSlot(request.timeSlot().trim());
        booking.setScheduledStart(start);
        booking.setScheduledEnd(start.plusSeconds(servicePackage.getDurationMinutes() * 60L));
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setPaymentStatus(BookingPaymentStatus.PENDING);
        booking.setPaymentMode(PaymentMode.ONLINE);
        booking.setCurrency("INR");
        booking.setSubtotal(servicePackage.getBasePrice());
        booking.setServiceFee(serviceFee);
        booking.setTotalAmount(total);
        booking.setSource(BookingSource.APP);
        booking.setCreatedAt(Instant.now());
        booking.setUpdatedAt(Instant.now());
        booking.setRescheduleCount(0);
        return toResponse(bookingRepository.save(booking));
    }

    @PatchMapping("/{id}")
    public BookingResponse reschedule(@PathVariable UUID id, @Valid @RequestBody RescheduleRequest request) {
        Booking booking = findOwned(id);
        ensureOpen(booking);
        Instant start = LocalDateTime.of(request.date(), parseStartTime(request.timeSlot())).atZone(ZoneId.systemDefault()).toInstant();
        int durationMinutes = booking.getService().getDurationMinutes();
        booking.setScheduledStart(start);
        booking.setScheduledEnd(start.plusSeconds(durationMinutes * 60L));
        booking.storeLegacyTimeSlot(request.timeSlot().trim());
        booking.setRescheduleCount(booking.getRescheduleCount() == null ? 1 : booking.getRescheduleCount() + 1);
        booking.setUpdatedAt(Instant.now());
        return toResponse(bookingRepository.save(booking));
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable UUID id) {
        Booking booking = findOwned(id);
        ensureOpen(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledBy(ActorType.CUSTOMER);
        booking.setCancelledAt(Instant.now());
        booking.setUpdatedAt(Instant.now());
        return toResponse(bookingRepository.save(booking));
    }

    private Booking findOwned(UUID publicId) {
        return bookingRepository.findOneByPublicIdAndCustomer_Login(publicId, currentLogin()).orElseThrow(() ->
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

    private static LocalTime parseStartTime(String timeSlot) {
        String start = timeSlot.split("\\s*-\\s*", 2)[0].trim();
        for (DateTimeFormatter formatter : List.of(LEGACY_TIME_FORMAT, DateTimeFormatter.ISO_LOCAL_TIME)) {
            try {
                return LocalTime.parse(start, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next supported legacy representation.
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time slot must begin with a time such as 9:00 AM or 09:00");
    }

    private static BookingResponse toResponse(Booking booking) {
        FacilityService service = booking.getService();
        String timeSlot = booking.legacyTimeSlotValue();
        if (timeSlot == null || timeSlot.isBlank()) {
            LocalTime start = booking.getScheduledStart().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime end = booking.getScheduledEnd().atZone(ZoneId.systemDefault()).toLocalTime();
            timeSlot = LEGACY_TIME_FORMAT.format(start) + " - " + LEGACY_TIME_FORMAT.format(end);
        }
        String assignedProfessional = booking.getProfessional() == null ? "Not assigned yet" : booking.getProfessional().getDisplayName();
        Double rating = booking.getProfessional() == null || booking.getProfessional().getAvgRating() == null
            ? 0D
            : booking.getProfessional().getAvgRating();
        return new BookingResponse(
            booking.getPublicId().toString(),
            service.getCode(),
            booking.getServiceTitle(),
            service.getEmoji(),
            booking.getSubtotal().intValue(),
            booking.getServiceFee().intValue(),
            booking.getTotalAmount().intValue(),
            booking.getScheduledStart().atZone(ZoneId.systemDefault()).toLocalDate(),
            timeSlot,
            booking.getAddressSnapshot(),
            booking.getStatus().name().toLowerCase(Locale.ROOT),
            assignedProfessional,
            rating
        );
    }

    public record CreateBookingRequest(
        @NotBlank String serviceId,
        @NotNull @Future LocalDate date,
        @NotBlank String timeSlot,
        @NotBlank String address,
        Long cityId
    ) {}

    public record RescheduleRequest(@NotNull @Future LocalDate date, @NotBlank String timeSlot) {}

    public record BookingResponse(
        String id,
        String serviceId,
        String serviceTitle,
        String emoji,
        Integer price,
        Integer serviceFee,
        Integer totalPrice,
        LocalDate date,
        String timeSlot,
        String address,
        String status,
        String assignedProName,
        Double proRating
    ) {}
}