package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalRepository;
import com.limitcross.facility.service.ProfessionalQueryService;
import com.limitcross.facility.service.ProfessionalService;
import com.limitcross.facility.service.criteria.ProfessionalCriteria;
import com.limitcross.facility.service.dto.ProfessionalDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.Professional}.
 */
@RestController
@RequestMapping("/api/professionals")
public class ProfessionalResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalResource.class);

    private static final String ENTITY_NAME = "professional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalService professionalService;

    private final ProfessionalRepository professionalRepository;

    private final ProfessionalQueryService professionalQueryService;

    public ProfessionalResource(
        ProfessionalService professionalService,
        ProfessionalRepository professionalRepository,
        ProfessionalQueryService professionalQueryService
    ) {
        this.professionalService = professionalService;
        this.professionalRepository = professionalRepository;
        this.professionalQueryService = professionalQueryService;
    }

    /**
     * {@code POST  /professionals} : Create a new professional.
     *
     * @param professionalDTO the professionalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalDTO, or with status {@code 400 (Bad Request)} if the professional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalDTO> createProfessional(@Valid @RequestBody ProfessionalDTO professionalDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Professional : {}", professionalDTO);
        if (professionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new professional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalDTO = professionalService.save(professionalDTO);
        return ResponseEntity.created(new URI("/api/professionals/" + professionalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalDTO.getId().toString()))
            .body(professionalDTO);
    }

    /**
     * {@code PUT  /professionals/:id} : Updates an existing professional.
     *
     * @param id the id of the professionalDTO to save.
     * @param professionalDTO the professionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalDTO,
     * or with status {@code 400 (Bad Request)} if the professionalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalDTO> updateProfessional(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalDTO professionalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Professional : {}, {}", id, professionalDTO);
        if (professionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalDTO = professionalService.update(professionalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalDTO.getId().toString()))
            .body(professionalDTO);
    }

    /**
     * {@code PATCH  /professionals/:id} : Partial updates given fields of an existing professional, field will ignore if it is null
     *
     * @param id the id of the professionalDTO to save.
     * @param professionalDTO the professionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalDTO,
     * or with status {@code 400 (Bad Request)} if the professionalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalDTO> partialUpdateProfessional(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalDTO professionalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Professional partially : {}, {}", id, professionalDTO);
        if (professionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalDTO> result = professionalService.partialUpdate(professionalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professionals} : get all the professionals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalDTO>> getAllProfessionals(
        ProfessionalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Professionals by criteria: {}", criteria);

        Page<ProfessionalDTO> page = professionalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professionals/count} : count all the professionals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProfessionals(ProfessionalCriteria criteria) {
        LOG.debug("REST request to count Professionals by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /professionals/:id} : get the "id" professional.
     *
     * @param id the id of the professionalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalDTO> getProfessional(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Professional : {}", id);
        Optional<ProfessionalDTO> professionalDTO = professionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalDTO);
    }

    /**
     * {@code DELETE  /professionals/:id} : delete the "id" professional.
     *
     * @param id the id of the professionalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessional(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Professional : {}", id);
        professionalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
