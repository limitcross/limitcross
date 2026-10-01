package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalTierRepository;
import com.limitcross.facility.service.ProfessionalTierService;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalTier}.
 */
@RestController
@RequestMapping("/api/professional-tiers")
public class ProfessionalTierResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTierResource.class);

    private static final String ENTITY_NAME = "professionalTier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalTierService professionalTierService;

    private final ProfessionalTierRepository professionalTierRepository;

    public ProfessionalTierResource(
        ProfessionalTierService professionalTierService,
        ProfessionalTierRepository professionalTierRepository
    ) {
        this.professionalTierService = professionalTierService;
        this.professionalTierRepository = professionalTierRepository;
    }

    /**
     * {@code POST  /professional-tiers} : Create a new professionalTier.
     *
     * @param professionalTierDTO the professionalTierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalTierDTO, or with status {@code 400 (Bad Request)} if the professionalTier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalTierDTO> createProfessionalTier(@Valid @RequestBody ProfessionalTierDTO professionalTierDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalTier : {}", professionalTierDTO);
        if (professionalTierDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalTier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalTierDTO = professionalTierService.save(professionalTierDTO);
        return ResponseEntity.created(new URI("/api/professional-tiers/" + professionalTierDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalTierDTO.getId().toString()))
            .body(professionalTierDTO);
    }

    /**
     * {@code PUT  /professional-tiers/:id} : Updates an existing professionalTier.
     *
     * @param id the id of the professionalTierDTO to save.
     * @param professionalTierDTO the professionalTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTierDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalTierDTO> updateProfessionalTier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalTierDTO professionalTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalTier : {}, {}", id, professionalTierDTO);
        if (professionalTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalTierDTO = professionalTierService.update(professionalTierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTierDTO.getId().toString()))
            .body(professionalTierDTO);
    }

    /**
     * {@code PATCH  /professional-tiers/:id} : Partial updates given fields of an existing professionalTier, field will ignore if it is null
     *
     * @param id the id of the professionalTierDTO to save.
     * @param professionalTierDTO the professionalTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTierDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalTierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalTierDTO> partialUpdateProfessionalTier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalTierDTO professionalTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalTier partially : {}, {}", id, professionalTierDTO);
        if (professionalTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalTierDTO> result = professionalTierService.partialUpdate(professionalTierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-tiers} : get all the professionalTiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalTiers in body.
     */
    @GetMapping("")
    public List<ProfessionalTierDTO> getAllProfessionalTiers() {
        LOG.debug("REST request to get all ProfessionalTiers");
        return professionalTierService.findAll();
    }

    /**
     * {@code GET  /professional-tiers/:id} : get the "id" professionalTier.
     *
     * @param id the id of the professionalTierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalTierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalTierDTO> getProfessionalTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalTier : {}", id);
        Optional<ProfessionalTierDTO> professionalTierDTO = professionalTierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalTierDTO);
    }

    /**
     * {@code DELETE  /professional-tiers/:id} : delete the "id" professionalTier.
     *
     * @param id the id of the professionalTierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalTier : {}", id);
        professionalTierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
