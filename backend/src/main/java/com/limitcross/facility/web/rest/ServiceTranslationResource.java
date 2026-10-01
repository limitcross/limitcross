package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ServiceTranslationRepository;
import com.limitcross.facility.service.ServiceTranslationService;
import com.limitcross.facility.service.dto.ServiceTranslationDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ServiceTranslation}.
 */
@RestController
@RequestMapping("/api/service-translations")
public class ServiceTranslationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTranslationResource.class);

    private static final String ENTITY_NAME = "serviceTranslation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceTranslationService serviceTranslationService;

    private final ServiceTranslationRepository serviceTranslationRepository;

    public ServiceTranslationResource(
        ServiceTranslationService serviceTranslationService,
        ServiceTranslationRepository serviceTranslationRepository
    ) {
        this.serviceTranslationService = serviceTranslationService;
        this.serviceTranslationRepository = serviceTranslationRepository;
    }

    /**
     * {@code POST  /service-translations} : Create a new serviceTranslation.
     *
     * @param serviceTranslationDTO the serviceTranslationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceTranslationDTO, or with status {@code 400 (Bad Request)} if the serviceTranslation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceTranslationDTO> createServiceTranslation(@Valid @RequestBody ServiceTranslationDTO serviceTranslationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServiceTranslation : {}", serviceTranslationDTO);
        if (serviceTranslationDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceTranslation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceTranslationDTO = serviceTranslationService.save(serviceTranslationDTO);
        return ResponseEntity.created(new URI("/api/service-translations/" + serviceTranslationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceTranslationDTO.getId().toString()))
            .body(serviceTranslationDTO);
    }

    /**
     * {@code PUT  /service-translations/:id} : Updates an existing serviceTranslation.
     *
     * @param id the id of the serviceTranslationDTO to save.
     * @param serviceTranslationDTO the serviceTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the serviceTranslationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceTranslationDTO> updateServiceTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceTranslationDTO serviceTranslationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceTranslation : {}, {}", id, serviceTranslationDTO);
        if (serviceTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceTranslationDTO = serviceTranslationService.update(serviceTranslationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceTranslationDTO.getId().toString()))
            .body(serviceTranslationDTO);
    }

    /**
     * {@code PATCH  /service-translations/:id} : Partial updates given fields of an existing serviceTranslation, field will ignore if it is null
     *
     * @param id the id of the serviceTranslationDTO to save.
     * @param serviceTranslationDTO the serviceTranslationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceTranslationDTO,
     * or with status {@code 400 (Bad Request)} if the serviceTranslationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceTranslationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceTranslationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceTranslationDTO> partialUpdateServiceTranslation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceTranslationDTO serviceTranslationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceTranslation partially : {}, {}", id, serviceTranslationDTO);
        if (serviceTranslationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceTranslationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceTranslationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceTranslationDTO> result = serviceTranslationService.partialUpdate(serviceTranslationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceTranslationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-translations} : get all the serviceTranslations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceTranslations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceTranslationDTO>> getAllServiceTranslations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ServiceTranslations");
        Page<ServiceTranslationDTO> page;
        if (eagerload) {
            page = serviceTranslationService.findAllWithEagerRelationships(pageable);
        } else {
            page = serviceTranslationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-translations/:id} : get the "id" serviceTranslation.
     *
     * @param id the id of the serviceTranslationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceTranslationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceTranslationDTO> getServiceTranslation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceTranslation : {}", id);
        Optional<ServiceTranslationDTO> serviceTranslationDTO = serviceTranslationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceTranslationDTO);
    }

    /**
     * {@code DELETE  /service-translations/:id} : delete the "id" serviceTranslation.
     *
     * @param id the id of the serviceTranslationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceTranslation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceTranslation : {}", id);
        serviceTranslationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
