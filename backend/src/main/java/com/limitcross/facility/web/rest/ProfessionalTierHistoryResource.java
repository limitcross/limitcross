package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalTierHistoryRepository;
import com.limitcross.facility.service.ProfessionalTierHistoryService;
import com.limitcross.facility.service.dto.ProfessionalTierHistoryDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalTierHistory}.
 */
@RestController
@RequestMapping("/api/professional-tier-histories")
public class ProfessionalTierHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTierHistoryResource.class);

    private static final String ENTITY_NAME = "professionalTierHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalTierHistoryService professionalTierHistoryService;

    private final ProfessionalTierHistoryRepository professionalTierHistoryRepository;

    public ProfessionalTierHistoryResource(
        ProfessionalTierHistoryService professionalTierHistoryService,
        ProfessionalTierHistoryRepository professionalTierHistoryRepository
    ) {
        this.professionalTierHistoryService = professionalTierHistoryService;
        this.professionalTierHistoryRepository = professionalTierHistoryRepository;
    }

    /**
     * {@code POST  /professional-tier-histories} : Create a new professionalTierHistory.
     *
     * @param professionalTierHistoryDTO the professionalTierHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalTierHistoryDTO, or with status {@code 400 (Bad Request)} if the professionalTierHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalTierHistoryDTO> createProfessionalTierHistory(
        @Valid @RequestBody ProfessionalTierHistoryDTO professionalTierHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalTierHistory : {}", professionalTierHistoryDTO);
        if (professionalTierHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalTierHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalTierHistoryDTO = professionalTierHistoryService.save(professionalTierHistoryDTO);
        return ResponseEntity.created(new URI("/api/professional-tier-histories/" + professionalTierHistoryDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalTierHistoryDTO.getId().toString())
            )
            .body(professionalTierHistoryDTO);
    }

    /**
     * {@code PUT  /professional-tier-histories/:id} : Updates an existing professionalTierHistory.
     *
     * @param id the id of the professionalTierHistoryDTO to save.
     * @param professionalTierHistoryDTO the professionalTierHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTierHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTierHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalTierHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalTierHistoryDTO> updateProfessionalTierHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalTierHistoryDTO professionalTierHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalTierHistory : {}, {}", id, professionalTierHistoryDTO);
        if (professionalTierHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTierHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTierHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalTierHistoryDTO = professionalTierHistoryService.update(professionalTierHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTierHistoryDTO.getId().toString()))
            .body(professionalTierHistoryDTO);
    }

    /**
     * {@code PATCH  /professional-tier-histories/:id} : Partial updates given fields of an existing professionalTierHistory, field will ignore if it is null
     *
     * @param id the id of the professionalTierHistoryDTO to save.
     * @param professionalTierHistoryDTO the professionalTierHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTierHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTierHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalTierHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalTierHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalTierHistoryDTO> partialUpdateProfessionalTierHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalTierHistoryDTO professionalTierHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalTierHistory partially : {}, {}", id, professionalTierHistoryDTO);
        if (professionalTierHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTierHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTierHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalTierHistoryDTO> result = professionalTierHistoryService.partialUpdate(professionalTierHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTierHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-tier-histories} : get all the professionalTierHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalTierHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalTierHistoryDTO>> getAllProfessionalTierHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalTierHistories");
        Page<ProfessionalTierHistoryDTO> page;
        if (eagerload) {
            page = professionalTierHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalTierHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-tier-histories/:id} : get the "id" professionalTierHistory.
     *
     * @param id the id of the professionalTierHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalTierHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalTierHistoryDTO> getProfessionalTierHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalTierHistory : {}", id);
        Optional<ProfessionalTierHistoryDTO> professionalTierHistoryDTO = professionalTierHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalTierHistoryDTO);
    }

    /**
     * {@code DELETE  /professional-tier-histories/:id} : delete the "id" professionalTierHistory.
     *
     * @param id the id of the professionalTierHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalTierHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalTierHistory : {}", id);
        professionalTierHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
