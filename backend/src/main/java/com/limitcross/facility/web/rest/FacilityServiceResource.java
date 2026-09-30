package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.repository.FacilityServiceRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/services")
public class FacilityServiceResource {

    private final FacilityServiceRepository serviceRepository;

    public FacilityServiceResource(FacilityServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public List<ServiceResponse> getServices(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search
    ) {
        String normalizedCategory = category == null ? "" : category.trim().toLowerCase();
        String normalizedSearch = search == null ? "" : search.trim().toLowerCase();
        return serviceRepository.findAll().stream()
            .filter(service -> normalizedCategory.isEmpty() || "all".equals(normalizedCategory) || service.getCategory().equalsIgnoreCase(normalizedCategory))
            .filter(service -> normalizedSearch.isEmpty() || (service.getTitle() + " " + service.getDescription()).toLowerCase().contains(normalizedSearch))
            .map(FacilityServiceResource::toResponse)
            .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse getService(@PathVariable String id) {
        return serviceRepository.findById(id).map(FacilityServiceResource::toResponse).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
    }

    private static ServiceResponse toResponse(FacilityService service) {
        List<String> highlights = Arrays.stream(service.getHighlights().split("\\|"))
            .filter(value -> !value.isBlank())
            .collect(Collectors.toList());
        return new ServiceResponse(
            service.getId(), service.getTitle(), service.getEmoji(), service.getCategory(), service.getDescription(),
            service.getPriceRange(), service.getStartingPrice(), service.getRating(), service.getReviewsCount(),
            service.getDuration(), highlights, service.getPopular()
        );
    }

    public record ServiceResponse(
        String id, String title, String emoji, String category, String description, String priceRange,
        Integer startingPrice, Double rating, Integer reviewsCount, String duration, List<String> highlights, Boolean isPopular
    ) {}
}