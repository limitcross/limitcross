package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ServiceZoneRepository;
import com.limitcross.facility.service.ServiceZoneService;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ServiceZone}.
 */
@RestController
@RequestMapping("/api/service-zones")
public class ServiceZoneResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceZoneResource.class);

    private static final String ENTITY_NAME = "serviceZone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceZoneService serviceZoneService;

    private final ServiceZoneRepository serviceZoneRepository;

    public ServiceZoneResource(ServiceZoneService serviceZoneService, ServiceZoneRepository serviceZoneRepository) {
        this.serviceZoneService = serviceZoneService;
        this.serviceZoneRepository = serviceZoneRepository;
    }

    /**
     * {@code POST  /service-zones} : Create a new serviceZone.
     *
     * @param serviceZoneDTO the serviceZoneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceZoneDTO, or with status {@code 400 (Bad Request)} if the serviceZone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceZoneDTO> createServiceZone(@Valid @RequestBody ServiceZoneDTO serviceZoneDTO) throws URISyntaxException {
        LOG.debug("REST request to save ServiceZone : {}", serviceZoneDTO);
        if (serviceZoneDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceZone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceZoneDTO = serviceZoneService.save(serviceZoneDTO);
        return ResponseEntity.created(new URI("/api/service-zones/" + serviceZoneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceZoneDTO.getId().toString()))
            .body(serviceZoneDTO);
    }

    /**
     * {@code PUT  /service-zones/:id} : Updates an existing serviceZone.
     *
     * @param id the id of the serviceZoneDTO to save.
     * @param serviceZoneDTO the serviceZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceZoneDTO,
     * or with status {@code 400 (Bad Request)} if the serviceZoneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceZoneDTO> updateServiceZone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceZoneDTO serviceZoneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceZone : {}, {}", id, serviceZoneDTO);
        if (serviceZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceZoneDTO = serviceZoneService.update(serviceZoneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceZoneDTO.getId().toString()))
            .body(serviceZoneDTO);
    }

    /**
     * {@code PATCH  /service-zones/:id} : Partial updates given fields of an existing serviceZone, field will ignore if it is null
     *
     * @param id the id of the serviceZoneDTO to save.
     * @param serviceZoneDTO the serviceZoneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceZoneDTO,
     * or with status {@code 400 (Bad Request)} if the serviceZoneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceZoneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceZoneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceZoneDTO> partialUpdateServiceZone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceZoneDTO serviceZoneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceZone partially : {}, {}", id, serviceZoneDTO);
        if (serviceZoneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceZoneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceZoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceZoneDTO> result = serviceZoneService.partialUpdate(serviceZoneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceZoneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-zones} : get all the serviceZones.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceZones in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceZoneDTO>> getAllServiceZones(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ServiceZones");
        Page<ServiceZoneDTO> page;
        if (eagerload) {
            page = serviceZoneService.findAllWithEagerRelationships(pageable);
        } else {
            page = serviceZoneService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-zones/:id} : get the "id" serviceZone.
     *
     * @param id the id of the serviceZoneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceZoneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceZoneDTO> getServiceZone(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceZone : {}", id);
        Optional<ServiceZoneDTO> serviceZoneDTO = serviceZoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceZoneDTO);
    }

    /**
     * {@code DELETE  /service-zones/:id} : delete the "id" serviceZone.
     *
     * @param id the id of the serviceZoneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceZone(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceZone : {}", id);
        serviceZoneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
