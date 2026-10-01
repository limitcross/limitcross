package com.limitcross.facility.web.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/services")
public class ServiceCatalogResource {

    private static final String SELECT_SERVICES = """
         SELECT service.id AS code, service.title, service.emoji, service.category,
               service.description, service.price_range, service.starting_price,
             service.rating, service.reviews_count, service.duration,
             service.highlights, service.is_popular
        FROM facility_service service
        """;

    private final JdbcTemplate jdbcTemplate;

    public ServiceCatalogResource(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<ServiceResponse> getServices(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search
    ) {
        String normalizedCategory = category == null ? "" : category.trim();
        String normalizedSearch = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        return jdbcTemplate.query(SELECT_SERVICES, ServiceResponseRowMapper.INSTANCE).stream()
            .filter(service -> normalizedCategory.isEmpty() || "all".equalsIgnoreCase(normalizedCategory) ||
                service.category().equalsIgnoreCase(normalizedCategory))
            .filter(service -> normalizedSearch.isEmpty() ||
                (service.title() + " " + service.description()).toLowerCase(Locale.ROOT).contains(normalizedSearch))
            .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse getService(@PathVariable String id) {
        return jdbcTemplate.query(SELECT_SERVICES + " AND service.code = ?", ServiceResponseRowMapper.INSTANCE, id).stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
    }

    private record ServiceResponse(
        String id,
        String title,
        String emoji,
        String category,
        String description,
        String priceRange,
        Integer startingPrice,
        Double rating,
        Integer reviewsCount,
        String duration,
        List<String> highlights,
        Boolean isPopular
    ) {}

    private enum ServiceResponseRowMapper implements RowMapper<ServiceResponse> {
        INSTANCE;

        @Override
        public ServiceResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            String highlights = resultSet.getString("highlights");
            List<String> highlightList = highlights == null
                ? List.of()
                : Arrays.stream(highlights.split("\\|"))
                    .filter(value -> !value.isBlank())
                    .toList();
            return new ServiceResponse(
                resultSet.getString("code"),
                resultSet.getString("title"),
                resultSet.getString("emoji"),
                resultSet.getString("category"),
                resultSet.getString("description"),
                resultSet.getString("price_range"),
                resultSet.getInt("starting_price"),
                resultSet.getDouble("rating"),
                resultSet.getInt("reviews_count"),
                resultSet.getString("duration"),
                highlightList,
                resultSet.getBoolean("is_popular")
            );
        }
    }
}