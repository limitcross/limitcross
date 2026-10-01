package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalDailyMetricsRepository;
import com.limitcross.facility.service.ProfessionalDailyMetricsService;
import com.limitcross.facility.service.dto.ProfessionalDailyMetricsDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalDailyMetrics}.
 */
@RestController
@RequestMapping("/api/professional-daily-metrics")
public class ProfessionalDailyMetricsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalDailyMetricsResource.class);

    private static final String ENTITY_NAME = "professionalDailyMetrics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalDailyMetricsService professionalDailyMetricsService;

    private final ProfessionalDailyMetricsRepository professionalDailyMetricsRepository;

    public ProfessionalDailyMetricsResource(
        ProfessionalDailyMetricsService professionalDailyMetricsService,
        ProfessionalDailyMetricsRepository professionalDailyMetricsRepository
    ) {
        this.professionalDailyMetricsService = professionalDailyMetricsService;
        this.professionalDailyMetricsRepository = professionalDailyMetricsRepository;
    }

    /**
     * {@code POST  /professional-daily-metrics} : Create a new professionalDailyMetrics.
     *
     * @param professionalDailyMetricsDTO the professionalDailyMetricsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalDailyMetricsDTO, or with status {@code 400 (Bad Request)} if the professionalDailyMetrics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalDailyMetricsDTO> createProfessionalDailyMetrics(
        @Valid @RequestBody ProfessionalDailyMetricsDTO professionalDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalDailyMetrics : {}", professionalDailyMetricsDTO);
        if (professionalDailyMetricsDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalDailyMetrics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalDailyMetricsDTO = professionalDailyMetricsService.save(professionalDailyMetricsDTO);
        return ResponseEntity.created(new URI("/api/professional-daily-metrics/" + professionalDailyMetricsDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalDailyMetricsDTO.getId().toString())
            )
            .body(professionalDailyMetricsDTO);
    }

    /**
     * {@code PUT  /professional-daily-metrics/:id} : Updates an existing professionalDailyMetrics.
     *
     * @param id the id of the professionalDailyMetricsDTO to save.
     * @param professionalDailyMetricsDTO the professionalDailyMetricsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalDailyMetricsDTO,
     * or with status {@code 400 (Bad Request)} if the professionalDailyMetricsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalDailyMetricsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalDailyMetricsDTO> updateProfessionalDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalDailyMetricsDTO professionalDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalDailyMetrics : {}, {}", id, professionalDailyMetricsDTO);
        if (professionalDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalDailyMetricsDTO = professionalDailyMetricsService.update(professionalDailyMetricsDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalDailyMetricsDTO.getId().toString())
            )
            .body(professionalDailyMetricsDTO);
    }

    /**
     * {@code PATCH  /professional-daily-metrics/:id} : Partial updates given fields of an existing professionalDailyMetrics, field will ignore if it is null
     *
     * @param id the id of the professionalDailyMetricsDTO to save.
     * @param professionalDailyMetricsDTO the professionalDailyMetricsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalDailyMetricsDTO,
     * or with status {@code 400 (Bad Request)} if the professionalDailyMetricsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalDailyMetricsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalDailyMetricsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalDailyMetricsDTO> partialUpdateProfessionalDailyMetrics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalDailyMetricsDTO professionalDailyMetricsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalDailyMetrics partially : {}, {}", id, professionalDailyMetricsDTO);
        if (professionalDailyMetricsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDailyMetricsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalDailyMetricsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalDailyMetricsDTO> result = professionalDailyMetricsService.partialUpdate(professionalDailyMetricsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalDailyMetricsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-daily-metrics} : get all the professionalDailyMetrics.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalDailyMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalDailyMetricsDTO>> getAllProfessionalDailyMetrics(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalDailyMetrics");
        Page<ProfessionalDailyMetricsDTO> page;
        if (eagerload) {
            page = professionalDailyMetricsService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalDailyMetricsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-daily-metrics/:id} : get the "id" professionalDailyMetrics.
     *
     * @param id the id of the professionalDailyMetricsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalDailyMetricsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalDailyMetricsDTO> getProfessionalDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalDailyMetrics : {}", id);
        Optional<ProfessionalDailyMetricsDTO> professionalDailyMetricsDTO = professionalDailyMetricsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalDailyMetricsDTO);
    }

    /**
     * {@code DELETE  /professional-daily-metrics/:id} : delete the "id" professionalDailyMetrics.
     *
     * @param id the id of the professionalDailyMetricsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalDailyMetrics(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalDailyMetrics : {}", id);
        professionalDailyMetricsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
