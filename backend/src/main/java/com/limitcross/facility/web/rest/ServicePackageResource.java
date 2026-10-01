package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ServicePackageRepository;
import com.limitcross.facility.service.ServicePackageService;
import com.limitcross.facility.service.dto.ServicePackageDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ServicePackage}.
 */
@RestController
@RequestMapping("/api/service-packages")
public class ServicePackageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServicePackageResource.class);

    private static final String ENTITY_NAME = "servicePackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicePackageService servicePackageService;

    private final ServicePackageRepository servicePackageRepository;

    public ServicePackageResource(ServicePackageService servicePackageService, ServicePackageRepository servicePackageRepository) {
        this.servicePackageService = servicePackageService;
        this.servicePackageRepository = servicePackageRepository;
    }

    /**
     * {@code POST  /service-packages} : Create a new servicePackage.
     *
     * @param servicePackageDTO the servicePackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicePackageDTO, or with status {@code 400 (Bad Request)} if the servicePackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServicePackageDTO> createServicePackage(@Valid @RequestBody ServicePackageDTO servicePackageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServicePackage : {}", servicePackageDTO);
        if (servicePackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicePackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        servicePackageDTO = servicePackageService.save(servicePackageDTO);
        return ResponseEntity.created(new URI("/api/service-packages/" + servicePackageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, servicePackageDTO.getId().toString()))
            .body(servicePackageDTO);
    }

    /**
     * {@code PUT  /service-packages/:id} : Updates an existing servicePackage.
     *
     * @param id the id of the servicePackageDTO to save.
     * @param servicePackageDTO the servicePackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicePackageDTO> updateServicePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicePackageDTO servicePackageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServicePackage : {}, {}", id, servicePackageDTO);
        if (servicePackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        servicePackageDTO = servicePackageService.update(servicePackageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, servicePackageDTO.getId().toString()))
            .body(servicePackageDTO);
    }

    /**
     * {@code PATCH  /service-packages/:id} : Partial updates given fields of an existing servicePackage, field will ignore if it is null
     *
     * @param id the id of the servicePackageDTO to save.
     * @param servicePackageDTO the servicePackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicePackageDTO,
     * or with status {@code 400 (Bad Request)} if the servicePackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicePackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicePackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicePackageDTO> partialUpdateServicePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicePackageDTO servicePackageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServicePackage partially : {}, {}", id, servicePackageDTO);
        if (servicePackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicePackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicePackageDTO> result = servicePackageService.partialUpdate(servicePackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, servicePackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-packages} : get all the servicePackages.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicePackages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServicePackageDTO>> getAllServicePackages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ServicePackages");
        Page<ServicePackageDTO> page;
        if (eagerload) {
            page = servicePackageService.findAllWithEagerRelationships(pageable);
        } else {
            page = servicePackageService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-packages/:id} : get the "id" servicePackage.
     *
     * @param id the id of the servicePackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicePackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicePackageDTO> getServicePackage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServicePackage : {}", id);
        Optional<ServicePackageDTO> servicePackageDTO = servicePackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicePackageDTO);
    }

    /**
     * {@code DELETE  /service-packages/:id} : delete the "id" servicePackage.
     *
     * @param id the id of the servicePackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicePackage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServicePackage : {}", id);
        servicePackageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
