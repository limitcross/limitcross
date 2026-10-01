package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalAvailabilityRepository;
import com.limitcross.facility.service.ProfessionalAvailabilityService;
import com.limitcross.facility.service.dto.ProfessionalAvailabilityDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalAvailability}.
 */
@RestController
@RequestMapping("/api/professional-availabilities")
public class ProfessionalAvailabilityResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalAvailabilityResource.class);

    private static final String ENTITY_NAME = "professionalAvailability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalAvailabilityService professionalAvailabilityService;

    private final ProfessionalAvailabilityRepository professionalAvailabilityRepository;

    public ProfessionalAvailabilityResource(
        ProfessionalAvailabilityService professionalAvailabilityService,
        ProfessionalAvailabilityRepository professionalAvailabilityRepository
    ) {
        this.professionalAvailabilityService = professionalAvailabilityService;
        this.professionalAvailabilityRepository = professionalAvailabilityRepository;
    }

    /**
     * {@code POST  /professional-availabilities} : Create a new professionalAvailability.
     *
     * @param professionalAvailabilityDTO the professionalAvailabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalAvailabilityDTO, or with status {@code 400 (Bad Request)} if the professionalAvailability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalAvailabilityDTO> createProfessionalAvailability(
        @Valid @RequestBody ProfessionalAvailabilityDTO professionalAvailabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalAvailability : {}", professionalAvailabilityDTO);
        if (professionalAvailabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalAvailabilityDTO = professionalAvailabilityService.save(professionalAvailabilityDTO);
        return ResponseEntity.created(new URI("/api/professional-availabilities/" + professionalAvailabilityDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalAvailabilityDTO.getId().toString())
            )
            .body(professionalAvailabilityDTO);
    }

    /**
     * {@code PUT  /professional-availabilities/:id} : Updates an existing professionalAvailability.
     *
     * @param id the id of the professionalAvailabilityDTO to save.
     * @param professionalAvailabilityDTO the professionalAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the professionalAvailabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalAvailabilityDTO> updateProfessionalAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalAvailabilityDTO professionalAvailabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalAvailability : {}, {}", id, professionalAvailabilityDTO);
        if (professionalAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalAvailabilityDTO = professionalAvailabilityService.update(professionalAvailabilityDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalAvailabilityDTO.getId().toString())
            )
            .body(professionalAvailabilityDTO);
    }

    /**
     * {@code PATCH  /professional-availabilities/:id} : Partial updates given fields of an existing professionalAvailability, field will ignore if it is null
     *
     * @param id the id of the professionalAvailabilityDTO to save.
     * @param professionalAvailabilityDTO the professionalAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the professionalAvailabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalAvailabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalAvailabilityDTO> partialUpdateProfessionalAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalAvailabilityDTO professionalAvailabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalAvailability partially : {}, {}", id, professionalAvailabilityDTO);
        if (professionalAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalAvailabilityDTO> result = professionalAvailabilityService.partialUpdate(professionalAvailabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalAvailabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-availabilities} : get all the professionalAvailabilities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalAvailabilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalAvailabilityDTO>> getAllProfessionalAvailabilities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalAvailabilities");
        Page<ProfessionalAvailabilityDTO> page;
        if (eagerload) {
            page = professionalAvailabilityService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalAvailabilityService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-availabilities/:id} : get the "id" professionalAvailability.
     *
     * @param id the id of the professionalAvailabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalAvailabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalAvailabilityDTO> getProfessionalAvailability(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalAvailability : {}", id);
        Optional<ProfessionalAvailabilityDTO> professionalAvailabilityDTO = professionalAvailabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalAvailabilityDTO);
    }

    /**
     * {@code DELETE  /professional-availabilities/:id} : delete the "id" professionalAvailability.
     *
     * @param id the id of the professionalAvailabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalAvailability(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalAvailability : {}", id);
        professionalAvailabilityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
