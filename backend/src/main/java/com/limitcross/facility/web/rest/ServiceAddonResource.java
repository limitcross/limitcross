package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ServiceAddonRepository;
import com.limitcross.facility.service.ServiceAddonService;
import com.limitcross.facility.service.dto.ServiceAddonDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ServiceAddon}.
 */
@RestController
@RequestMapping("/api/service-addons")
public class ServiceAddonResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceAddonResource.class);

    private static final String ENTITY_NAME = "serviceAddon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceAddonService serviceAddonService;

    private final ServiceAddonRepository serviceAddonRepository;

    public ServiceAddonResource(ServiceAddonService serviceAddonService, ServiceAddonRepository serviceAddonRepository) {
        this.serviceAddonService = serviceAddonService;
        this.serviceAddonRepository = serviceAddonRepository;
    }

    /**
     * {@code POST  /service-addons} : Create a new serviceAddon.
     *
     * @param serviceAddonDTO the serviceAddonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceAddonDTO, or with status {@code 400 (Bad Request)} if the serviceAddon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceAddonDTO> createServiceAddon(@Valid @RequestBody ServiceAddonDTO serviceAddonDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServiceAddon : {}", serviceAddonDTO);
        if (serviceAddonDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceAddon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceAddonDTO = serviceAddonService.save(serviceAddonDTO);
        return ResponseEntity.created(new URI("/api/service-addons/" + serviceAddonDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceAddonDTO.getId().toString()))
            .body(serviceAddonDTO);
    }

    /**
     * {@code PUT  /service-addons/:id} : Updates an existing serviceAddon.
     *
     * @param id the id of the serviceAddonDTO to save.
     * @param serviceAddonDTO the serviceAddonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceAddonDTO,
     * or with status {@code 400 (Bad Request)} if the serviceAddonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceAddonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceAddonDTO> updateServiceAddon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceAddonDTO serviceAddonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceAddon : {}, {}", id, serviceAddonDTO);
        if (serviceAddonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceAddonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceAddonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceAddonDTO = serviceAddonService.update(serviceAddonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceAddonDTO.getId().toString()))
            .body(serviceAddonDTO);
    }

    /**
     * {@code PATCH  /service-addons/:id} : Partial updates given fields of an existing serviceAddon, field will ignore if it is null
     *
     * @param id the id of the serviceAddonDTO to save.
     * @param serviceAddonDTO the serviceAddonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceAddonDTO,
     * or with status {@code 400 (Bad Request)} if the serviceAddonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceAddonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceAddonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceAddonDTO> partialUpdateServiceAddon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceAddonDTO serviceAddonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceAddon partially : {}, {}", id, serviceAddonDTO);
        if (serviceAddonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceAddonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceAddonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceAddonDTO> result = serviceAddonService.partialUpdate(serviceAddonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceAddonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-addons} : get all the serviceAddons.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceAddons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceAddonDTO>> getAllServiceAddons(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ServiceAddons");
        Page<ServiceAddonDTO> page;
        if (eagerload) {
            page = serviceAddonService.findAllWithEagerRelationships(pageable);
        } else {
            page = serviceAddonService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-addons/:id} : get the "id" serviceAddon.
     *
     * @param id the id of the serviceAddonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceAddonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceAddonDTO> getServiceAddon(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceAddon : {}", id);
        Optional<ServiceAddonDTO> serviceAddonDTO = serviceAddonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceAddonDTO);
    }

    /**
     * {@code DELETE  /service-addons/:id} : delete the "id" serviceAddon.
     *
     * @param id the id of the serviceAddonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceAddon(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceAddon : {}", id);
        serviceAddonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
