package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.service.FacilityServiceQueryService;
import com.limitcross.facility.service.FacilityServiceService;
import com.limitcross.facility.service.criteria.FacilityServiceCriteria;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.FacilityService}.
 */
@RestController
@RequestMapping("/api/facility-services")
public class FacilityServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityServiceResource.class);

    private static final String ENTITY_NAME = "facilityService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilityServiceService facilityServiceService;

    private final FacilityServiceRepository facilityServiceRepository;

    private final FacilityServiceQueryService facilityServiceQueryService;

    public FacilityServiceResource(
        FacilityServiceService facilityServiceService,
        FacilityServiceRepository facilityServiceRepository,
        FacilityServiceQueryService facilityServiceQueryService
    ) {
        this.facilityServiceService = facilityServiceService;
        this.facilityServiceRepository = facilityServiceRepository;
        this.facilityServiceQueryService = facilityServiceQueryService;
    }

    /**
     * {@code POST  /facility-services} : Create a new facilityService.
     *
     * @param facilityServiceDTO the facilityServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityServiceDTO, or with status {@code 400 (Bad Request)} if the facilityService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityServiceDTO> createFacilityService(@Valid @RequestBody FacilityServiceDTO facilityServiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save FacilityService : {}", facilityServiceDTO);
        if (facilityServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new facilityService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityServiceDTO = facilityServiceService.save(facilityServiceDTO);
        return ResponseEntity.created(new URI("/api/facility-services/" + facilityServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, facilityServiceDTO.getId().toString()))
            .body(facilityServiceDTO);
    }

    /**
     * {@code PUT  /facility-services/:id} : Updates an existing facilityService.
     *
     * @param id the id of the facilityServiceDTO to save.
     * @param facilityServiceDTO the facilityServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityServiceDTO,
     * or with status {@code 400 (Bad Request)} if the facilityServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityServiceDTO> updateFacilityService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityServiceDTO facilityServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FacilityService : {}, {}", id, facilityServiceDTO);
        if (facilityServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityServiceDTO = facilityServiceService.update(facilityServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facilityServiceDTO.getId().toString()))
            .body(facilityServiceDTO);
    }

    /**
     * {@code PATCH  /facility-services/:id} : Partial updates given fields of an existing facilityService, field will ignore if it is null
     *
     * @param id the id of the facilityServiceDTO to save.
     * @param facilityServiceDTO the facilityServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityServiceDTO,
     * or with status {@code 400 (Bad Request)} if the facilityServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facilityServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityServiceDTO> partialUpdateFacilityService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityServiceDTO facilityServiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FacilityService partially : {}, {}", id, facilityServiceDTO);
        if (facilityServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityServiceDTO> result = facilityServiceService.partialUpdate(facilityServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facilityServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-services} : get all the facilityServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilityServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityServiceDTO>> getAllFacilityServices(
        FacilityServiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FacilityServices by criteria: {}", criteria);

        Page<FacilityServiceDTO> page = facilityServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-services/count} : count all the facilityServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilityServices(FacilityServiceCriteria criteria) {
        LOG.debug("REST request to count FacilityServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facility-services/:id} : get the "id" facilityService.
     *
     * @param id the id of the facilityServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityServiceDTO> getFacilityService(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FacilityService : {}", id);
        Optional<FacilityServiceDTO> facilityServiceDTO = facilityServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityServiceDTO);
    }

    /**
     * {@code DELETE  /facility-services/:id} : delete the "id" facilityService.
     *
     * @param id the id of the facilityServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacilityService(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FacilityService : {}", id);
        facilityServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
