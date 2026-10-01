package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalIncentiveAwardRepository;
import com.limitcross.facility.service.ProfessionalIncentiveAwardService;
import com.limitcross.facility.service.dto.ProfessionalIncentiveAwardDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalIncentiveAward}.
 */
@RestController
@RequestMapping("/api/professional-incentive-awards")
public class ProfessionalIncentiveAwardResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalIncentiveAwardResource.class);

    private static final String ENTITY_NAME = "professionalIncentiveAward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalIncentiveAwardService professionalIncentiveAwardService;

    private final ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepository;

    public ProfessionalIncentiveAwardResource(
        ProfessionalIncentiveAwardService professionalIncentiveAwardService,
        ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepository
    ) {
        this.professionalIncentiveAwardService = professionalIncentiveAwardService;
        this.professionalIncentiveAwardRepository = professionalIncentiveAwardRepository;
    }

    /**
     * {@code POST  /professional-incentive-awards} : Create a new professionalIncentiveAward.
     *
     * @param professionalIncentiveAwardDTO the professionalIncentiveAwardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalIncentiveAwardDTO, or with status {@code 400 (Bad Request)} if the professionalIncentiveAward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalIncentiveAwardDTO> createProfessionalIncentiveAward(
        @Valid @RequestBody ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalIncentiveAward : {}", professionalIncentiveAwardDTO);
        if (professionalIncentiveAwardDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalIncentiveAward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalIncentiveAwardDTO = professionalIncentiveAwardService.save(professionalIncentiveAwardDTO);
        return ResponseEntity.created(new URI("/api/professional-incentive-awards/" + professionalIncentiveAwardDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalIncentiveAwardDTO.getId().toString())
            )
            .body(professionalIncentiveAwardDTO);
    }

    /**
     * {@code PUT  /professional-incentive-awards/:id} : Updates an existing professionalIncentiveAward.
     *
     * @param id the id of the professionalIncentiveAwardDTO to save.
     * @param professionalIncentiveAwardDTO the professionalIncentiveAwardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalIncentiveAwardDTO,
     * or with status {@code 400 (Bad Request)} if the professionalIncentiveAwardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalIncentiveAwardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalIncentiveAwardDTO> updateProfessionalIncentiveAward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalIncentiveAward : {}, {}", id, professionalIncentiveAwardDTO);
        if (professionalIncentiveAwardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalIncentiveAwardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalIncentiveAwardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalIncentiveAwardDTO = professionalIncentiveAwardService.update(professionalIncentiveAwardDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalIncentiveAwardDTO.getId().toString())
            )
            .body(professionalIncentiveAwardDTO);
    }

    /**
     * {@code PATCH  /professional-incentive-awards/:id} : Partial updates given fields of an existing professionalIncentiveAward, field will ignore if it is null
     *
     * @param id the id of the professionalIncentiveAwardDTO to save.
     * @param professionalIncentiveAwardDTO the professionalIncentiveAwardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalIncentiveAwardDTO,
     * or with status {@code 400 (Bad Request)} if the professionalIncentiveAwardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalIncentiveAwardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalIncentiveAwardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalIncentiveAwardDTO> partialUpdateProfessionalIncentiveAward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalIncentiveAward partially : {}, {}", id, professionalIncentiveAwardDTO);
        if (professionalIncentiveAwardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalIncentiveAwardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalIncentiveAwardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalIncentiveAwardDTO> result = professionalIncentiveAwardService.partialUpdate(professionalIncentiveAwardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalIncentiveAwardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-incentive-awards} : get all the professionalIncentiveAwards.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalIncentiveAwards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalIncentiveAwardDTO>> getAllProfessionalIncentiveAwards(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalIncentiveAwards");
        Page<ProfessionalIncentiveAwardDTO> page;
        if (eagerload) {
            page = professionalIncentiveAwardService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalIncentiveAwardService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-incentive-awards/:id} : get the "id" professionalIncentiveAward.
     *
     * @param id the id of the professionalIncentiveAwardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalIncentiveAwardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalIncentiveAwardDTO> getProfessionalIncentiveAward(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalIncentiveAward : {}", id);
        Optional<ProfessionalIncentiveAwardDTO> professionalIncentiveAwardDTO = professionalIncentiveAwardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalIncentiveAwardDTO);
    }

    /**
     * {@code DELETE  /professional-incentive-awards/:id} : delete the "id" professionalIncentiveAward.
     *
     * @param id the id of the professionalIncentiveAwardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalIncentiveAward(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalIncentiveAward : {}", id);
        professionalIncentiveAwardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
