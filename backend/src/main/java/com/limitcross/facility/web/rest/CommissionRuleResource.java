package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CommissionRuleRepository;
import com.limitcross.facility.service.CommissionRuleService;
import com.limitcross.facility.service.dto.CommissionRuleDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CommissionRule}.
 */
@RestController
@RequestMapping("/api/commission-rules")
public class CommissionRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommissionRuleResource.class);

    private static final String ENTITY_NAME = "commissionRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommissionRuleService commissionRuleService;

    private final CommissionRuleRepository commissionRuleRepository;

    public CommissionRuleResource(CommissionRuleService commissionRuleService, CommissionRuleRepository commissionRuleRepository) {
        this.commissionRuleService = commissionRuleService;
        this.commissionRuleRepository = commissionRuleRepository;
    }

    /**
     * {@code POST  /commission-rules} : Create a new commissionRule.
     *
     * @param commissionRuleDTO the commissionRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commissionRuleDTO, or with status {@code 400 (Bad Request)} if the commissionRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommissionRuleDTO> createCommissionRule(@Valid @RequestBody CommissionRuleDTO commissionRuleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CommissionRule : {}", commissionRuleDTO);
        if (commissionRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new commissionRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commissionRuleDTO = commissionRuleService.save(commissionRuleDTO);
        return ResponseEntity.created(new URI("/api/commission-rules/" + commissionRuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, commissionRuleDTO.getId().toString()))
            .body(commissionRuleDTO);
    }

    /**
     * {@code PUT  /commission-rules/:id} : Updates an existing commissionRule.
     *
     * @param id the id of the commissionRuleDTO to save.
     * @param commissionRuleDTO the commissionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the commissionRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommissionRuleDTO> updateCommissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommissionRuleDTO commissionRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommissionRule : {}, {}", id, commissionRuleDTO);
        if (commissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commissionRuleDTO = commissionRuleService.update(commissionRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commissionRuleDTO.getId().toString()))
            .body(commissionRuleDTO);
    }

    /**
     * {@code PATCH  /commission-rules/:id} : Partial updates given fields of an existing commissionRule, field will ignore if it is null
     *
     * @param id the id of the commissionRuleDTO to save.
     * @param commissionRuleDTO the commissionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commissionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the commissionRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commissionRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commissionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommissionRuleDTO> partialUpdateCommissionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommissionRuleDTO commissionRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommissionRule partially : {}, {}", id, commissionRuleDTO);
        if (commissionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commissionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commissionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommissionRuleDTO> result = commissionRuleService.partialUpdate(commissionRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commissionRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commission-rules} : get all the commissionRules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commissionRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommissionRuleDTO>> getAllCommissionRules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CommissionRules");
        Page<CommissionRuleDTO> page;
        if (eagerload) {
            page = commissionRuleService.findAllWithEagerRelationships(pageable);
        } else {
            page = commissionRuleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commission-rules/:id} : get the "id" commissionRule.
     *
     * @param id the id of the commissionRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commissionRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommissionRuleDTO> getCommissionRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommissionRule : {}", id);
        Optional<CommissionRuleDTO> commissionRuleDTO = commissionRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commissionRuleDTO);
    }

    /**
     * {@code DELETE  /commission-rules/:id} : delete the "id" commissionRule.
     *
     * @param id the id of the commissionRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommissionRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommissionRule : {}", id);
        commissionRuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
