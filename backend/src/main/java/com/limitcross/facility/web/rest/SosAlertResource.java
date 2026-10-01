package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.SosAlertRepository;
import com.limitcross.facility.service.SosAlertService;
import com.limitcross.facility.service.dto.SosAlertDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.SosAlert}.
 */
@RestController
@RequestMapping("/api/sos-alerts")
public class SosAlertResource {

    private static final Logger LOG = LoggerFactory.getLogger(SosAlertResource.class);

    private static final String ENTITY_NAME = "sosAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SosAlertService sosAlertService;

    private final SosAlertRepository sosAlertRepository;

    public SosAlertResource(SosAlertService sosAlertService, SosAlertRepository sosAlertRepository) {
        this.sosAlertService = sosAlertService;
        this.sosAlertRepository = sosAlertRepository;
    }

    /**
     * {@code POST  /sos-alerts} : Create a new sosAlert.
     *
     * @param sosAlertDTO the sosAlertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sosAlertDTO, or with status {@code 400 (Bad Request)} if the sosAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SosAlertDTO> createSosAlert(@Valid @RequestBody SosAlertDTO sosAlertDTO) throws URISyntaxException {
        LOG.debug("REST request to save SosAlert : {}", sosAlertDTO);
        if (sosAlertDTO.getId() != null) {
            throw new BadRequestAlertException("A new sosAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sosAlertDTO = sosAlertService.save(sosAlertDTO);
        return ResponseEntity.created(new URI("/api/sos-alerts/" + sosAlertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sosAlertDTO.getId().toString()))
            .body(sosAlertDTO);
    }

    /**
     * {@code PUT  /sos-alerts/:id} : Updates an existing sosAlert.
     *
     * @param id the id of the sosAlertDTO to save.
     * @param sosAlertDTO the sosAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosAlertDTO,
     * or with status {@code 400 (Bad Request)} if the sosAlertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sosAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SosAlertDTO> updateSosAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SosAlertDTO sosAlertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SosAlert : {}, {}", id, sosAlertDTO);
        if (sosAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sosAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sosAlertDTO = sosAlertService.update(sosAlertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sosAlertDTO.getId().toString()))
            .body(sosAlertDTO);
    }

    /**
     * {@code PATCH  /sos-alerts/:id} : Partial updates given fields of an existing sosAlert, field will ignore if it is null
     *
     * @param id the id of the sosAlertDTO to save.
     * @param sosAlertDTO the sosAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sosAlertDTO,
     * or with status {@code 400 (Bad Request)} if the sosAlertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sosAlertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sosAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SosAlertDTO> partialUpdateSosAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SosAlertDTO sosAlertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SosAlert partially : {}, {}", id, sosAlertDTO);
        if (sosAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sosAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sosAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SosAlertDTO> result = sosAlertService.partialUpdate(sosAlertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sosAlertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sos-alerts} : get all the sosAlerts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sosAlerts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SosAlertDTO>> getAllSosAlerts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SosAlerts");
        Page<SosAlertDTO> page;
        if (eagerload) {
            page = sosAlertService.findAllWithEagerRelationships(pageable);
        } else {
            page = sosAlertService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sos-alerts/:id} : get the "id" sosAlert.
     *
     * @param id the id of the sosAlertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sosAlertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SosAlertDTO> getSosAlert(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SosAlert : {}", id);
        Optional<SosAlertDTO> sosAlertDTO = sosAlertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sosAlertDTO);
    }

    /**
     * {@code DELETE  /sos-alerts/:id} : delete the "id" sosAlert.
     *
     * @param id the id of the sosAlertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSosAlert(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SosAlert : {}", id);
        sosAlertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
