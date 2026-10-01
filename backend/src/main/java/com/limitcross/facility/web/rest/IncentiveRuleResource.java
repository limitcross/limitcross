package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.IncentiveRuleRepository;
import com.limitcross.facility.service.IncentiveRuleService;
import com.limitcross.facility.service.dto.IncentiveRuleDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.IncentiveRule}.
 */
@RestController
@RequestMapping("/api/incentive-rules")
public class IncentiveRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncentiveRuleResource.class);

    private static final String ENTITY_NAME = "incentiveRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncentiveRuleService incentiveRuleService;

    private final IncentiveRuleRepository incentiveRuleRepository;

    public IncentiveRuleResource(IncentiveRuleService incentiveRuleService, IncentiveRuleRepository incentiveRuleRepository) {
        this.incentiveRuleService = incentiveRuleService;
        this.incentiveRuleRepository = incentiveRuleRepository;
    }

    /**
     * {@code POST  /incentive-rules} : Create a new incentiveRule.
     *
     * @param incentiveRuleDTO the incentiveRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incentiveRuleDTO, or with status {@code 400 (Bad Request)} if the incentiveRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncentiveRuleDTO> createIncentiveRule(@Valid @RequestBody IncentiveRuleDTO incentiveRuleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IncentiveRule : {}", incentiveRuleDTO);
        if (incentiveRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new incentiveRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        incentiveRuleDTO = incentiveRuleService.save(incentiveRuleDTO);
        return ResponseEntity.created(new URI("/api/incentive-rules/" + incentiveRuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, incentiveRuleDTO.getId().toString()))
            .body(incentiveRuleDTO);
    }

    /**
     * {@code PUT  /incentive-rules/:id} : Updates an existing incentiveRule.
     *
     * @param id the id of the incentiveRuleDTO to save.
     * @param incentiveRuleDTO the incentiveRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incentiveRuleDTO,
     * or with status {@code 400 (Bad Request)} if the incentiveRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incentiveRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncentiveRuleDTO> updateIncentiveRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncentiveRuleDTO incentiveRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncentiveRule : {}, {}", id, incentiveRuleDTO);
        if (incentiveRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incentiveRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incentiveRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        incentiveRuleDTO = incentiveRuleService.update(incentiveRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, incentiveRuleDTO.getId().toString()))
            .body(incentiveRuleDTO);
    }

    /**
     * {@code PATCH  /incentive-rules/:id} : Partial updates given fields of an existing incentiveRule, field will ignore if it is null
     *
     * @param id the id of the incentiveRuleDTO to save.
     * @param incentiveRuleDTO the incentiveRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incentiveRuleDTO,
     * or with status {@code 400 (Bad Request)} if the incentiveRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the incentiveRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the incentiveRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncentiveRuleDTO> partialUpdateIncentiveRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncentiveRuleDTO incentiveRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncentiveRule partially : {}, {}", id, incentiveRuleDTO);
        if (incentiveRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incentiveRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incentiveRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncentiveRuleDTO> result = incentiveRuleService.partialUpdate(incentiveRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, incentiveRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /incentive-rules} : get all the incentiveRules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incentiveRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IncentiveRuleDTO>> getAllIncentiveRules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of IncentiveRules");
        Page<IncentiveRuleDTO> page;
        if (eagerload) {
            page = incentiveRuleService.findAllWithEagerRelationships(pageable);
        } else {
            page = incentiveRuleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incentive-rules/:id} : get the "id" incentiveRule.
     *
     * @param id the id of the incentiveRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incentiveRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncentiveRuleDTO> getIncentiveRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncentiveRule : {}", id);
        Optional<IncentiveRuleDTO> incentiveRuleDTO = incentiveRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incentiveRuleDTO);
    }

    /**
     * {@code DELETE  /incentive-rules/:id} : delete the "id" incentiveRule.
     *
     * @param id the id of the incentiveRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncentiveRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncentiveRule : {}", id);
        incentiveRuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
