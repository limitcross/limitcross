package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.WarrantyClaimRepository;
import com.limitcross.facility.service.WarrantyClaimService;
import com.limitcross.facility.service.dto.WarrantyClaimDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.WarrantyClaim}.
 */
@RestController
@RequestMapping("/api/warranty-claims")
public class WarrantyClaimResource {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyClaimResource.class);

    private static final String ENTITY_NAME = "warrantyClaim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarrantyClaimService warrantyClaimService;

    private final WarrantyClaimRepository warrantyClaimRepository;

    public WarrantyClaimResource(WarrantyClaimService warrantyClaimService, WarrantyClaimRepository warrantyClaimRepository) {
        this.warrantyClaimService = warrantyClaimService;
        this.warrantyClaimRepository = warrantyClaimRepository;
    }

    /**
     * {@code POST  /warranty-claims} : Create a new warrantyClaim.
     *
     * @param warrantyClaimDTO the warrantyClaimDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warrantyClaimDTO, or with status {@code 400 (Bad Request)} if the warrantyClaim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WarrantyClaimDTO> createWarrantyClaim(@Valid @RequestBody WarrantyClaimDTO warrantyClaimDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WarrantyClaim : {}", warrantyClaimDTO);
        if (warrantyClaimDTO.getId() != null) {
            throw new BadRequestAlertException("A new warrantyClaim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        warrantyClaimDTO = warrantyClaimService.save(warrantyClaimDTO);
        return ResponseEntity.created(new URI("/api/warranty-claims/" + warrantyClaimDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, warrantyClaimDTO.getId().toString()))
            .body(warrantyClaimDTO);
    }

    /**
     * {@code PUT  /warranty-claims/:id} : Updates an existing warrantyClaim.
     *
     * @param id the id of the warrantyClaimDTO to save.
     * @param warrantyClaimDTO the warrantyClaimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyClaimDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyClaimDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warrantyClaimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarrantyClaimDTO> updateWarrantyClaim(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WarrantyClaimDTO warrantyClaimDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WarrantyClaim : {}, {}", id, warrantyClaimDTO);
        if (warrantyClaimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyClaimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warrantyClaimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        warrantyClaimDTO = warrantyClaimService.update(warrantyClaimDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, warrantyClaimDTO.getId().toString()))
            .body(warrantyClaimDTO);
    }

    /**
     * {@code PATCH  /warranty-claims/:id} : Partial updates given fields of an existing warrantyClaim, field will ignore if it is null
     *
     * @param id the id of the warrantyClaimDTO to save.
     * @param warrantyClaimDTO the warrantyClaimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyClaimDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyClaimDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warrantyClaimDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warrantyClaimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WarrantyClaimDTO> partialUpdateWarrantyClaim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WarrantyClaimDTO warrantyClaimDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WarrantyClaim partially : {}, {}", id, warrantyClaimDTO);
        if (warrantyClaimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyClaimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warrantyClaimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WarrantyClaimDTO> result = warrantyClaimService.partialUpdate(warrantyClaimDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, warrantyClaimDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /warranty-claims} : get all the warrantyClaims.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warrantyClaims in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WarrantyClaimDTO>> getAllWarrantyClaims(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of WarrantyClaims");
        Page<WarrantyClaimDTO> page;
        if (eagerload) {
            page = warrantyClaimService.findAllWithEagerRelationships(pageable);
        } else {
            page = warrantyClaimService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warranty-claims/:id} : get the "id" warrantyClaim.
     *
     * @param id the id of the warrantyClaimDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warrantyClaimDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarrantyClaimDTO> getWarrantyClaim(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WarrantyClaim : {}", id);
        Optional<WarrantyClaimDTO> warrantyClaimDTO = warrantyClaimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warrantyClaimDTO);
    }

    /**
     * {@code DELETE  /warranty-claims/:id} : delete the "id" warrantyClaim.
     *
     * @param id the id of the warrantyClaimDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarrantyClaim(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WarrantyClaim : {}", id);
        warrantyClaimService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
