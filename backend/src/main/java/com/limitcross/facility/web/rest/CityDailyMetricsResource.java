package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CityDailyMetricsRepository;
import com.limitcross.facility.service.CityDailyMetricsService;
import com.limitcross.facility.service.dto.CityDailyMetricsDTO;
import com.limitcross.facility.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.limitcross.facility.domain.CityDailyMetrics}.
 */
@RestController
@RequestMapping("/api/city-daily-metrics")
public class CityDailyMetricsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CityDailyMetricsResource.class);

    private static final String ENTITY_NAME = "cityDailyMetrics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityDailyMetricsService cityDailyMetricsService;

    private final CityDailyMetricsRepository cityDailyMetricsRepository;

    public CityDailyMetricsResource(
        CityDailyMetricsService cityDailyMetricsService,
        CityDailyMetricsRepository cityDailyMetricsRepository
    ) {
        this.cityDailyMetricsService = cityDailyMetricsService;
        this.cityDailyMetricsRepository = cityDailyMetricsRepository;
    }

    /**
     * {@code POST  /city-daily-metrics} : Create a new cityDailyMetrics.
     *
     * @param cityDailyMetricsDTO the cityDailyMetricsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cityDailyMetricsDTO, or with status {@code 400 (Bad Request)} if the cityDailyMetrics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CityDailyMetricsDTO> createCityDailyMetrics(@Valid @RequestBody CityDailyMetricsDTO cityDailyMetricsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CityDailyMetrics : {}", cityDailyMetricsDTO);
        if (cityDailyMetricsDTO.getId() != null) {
            throw new BadRequestAlertException("A new cityDailyMetrics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cityDailyMetricsDTO = cityDailyMetricsService.save(cityDailyMetricsDTO);
        return ResponseEntity.created(new URI("/api/city-daily-metrics/" + cityDailyMetricsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cityDailyMetricsDTO.getId().toString()))
            .body(cityDailyMetricsDTO);
    }

    /**
     * {@code PUT  /city-daily-metrics/:id} : Updates an existing cityDailyMetrics.
     *
     * @param id the id of the cityDailyMetricsDTO to save.
     * @param cityDailyMetricsDTO the cityDailyMetricsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDailyMetricsDTO,
     * or with status {@code 400 (Bad Request)} if the cityDailyMetricsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cityDailyMetricsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityDailyMetricsDTO> updateCityDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CityDailyMetricsDTO cityDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CityDailyMetrics : {}, {}", id, cityDailyMetricsDTO);
        if (cityDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cityDailyMetricsDTO = cityDailyMetricsService.update(cityDailyMetricsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cityDailyMetricsDTO.getId().toString()))
            .body(cityDailyMetricsDTO);
    }

    /**
     * {@code PATCH  /city-daily-metrics/:id} : Partial updates given fields of an existing cityDailyMetrics, field will ignore if it is null
     *
     * @param id the id of the cityDailyMetricsDTO to save.
     * @param cityDailyMetricsDTO the cityDailyMetricsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDailyMetricsDTO,
     * or with status {@code 400 (Bad Request)} if the cityDailyMetricsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cityDailyMetricsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cityDailyMetricsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CityDailyMetricsDTO> partialUpdateCityDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CityDailyMetricsDTO cityDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CityDailyMetrics partially : {}, {}", id, cityDailyMetricsDTO);
        if (cityDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CityDailyMetricsDTO> result = cityDailyMetricsService.partialUpdate(cityDailyMetricsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cityDailyMetricsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /city-daily-metrics} : get all the cityDailyMetrics.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cityDailyMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CityDailyMetricsDTO>> getAllCityDailyMetrics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CityDailyMetrics");
        Page<CityDailyMetricsDTO> page;
        if (eagerload) {
            page = cityDailyMetricsService.findAllWithEagerRelationships(pageable);
        } else {
            page = cityDailyMetricsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /city-daily-metrics/:id} : get the "id" cityDailyMetrics.
     *
     * @param id the id of the cityDailyMetricsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cityDailyMetricsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityDailyMetricsDTO> getCityDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CityDailyMetrics : {}", id);
        Optional<CityDailyMetricsDTO> cityDailyMetricsDTO = cityDailyMetricsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cityDailyMetricsDTO);
    }

    /**
     * {@code DELETE  /city-daily-metrics/:id} : delete the "id" cityDailyMetrics.
     *
     * @param id the id of the cityDailyMetricsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CityDailyMetrics : {}", id);
        cityDailyMetricsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
