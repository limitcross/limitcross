package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.LoyaltyLedgerRepository;
import com.limitcross.facility.service.LoyaltyLedgerService;
import com.limitcross.facility.service.dto.LoyaltyLedgerDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.LoyaltyLedger}.
 */
@RestController
@RequestMapping("/api/loyalty-ledgers")
public class LoyaltyLedgerResource {

    private static final Logger LOG = LoggerFactory.getLogger(LoyaltyLedgerResource.class);

    private static final String ENTITY_NAME = "loyaltyLedger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoyaltyLedgerService loyaltyLedgerService;

    private final LoyaltyLedgerRepository loyaltyLedgerRepository;

    public LoyaltyLedgerResource(LoyaltyLedgerService loyaltyLedgerService, LoyaltyLedgerRepository loyaltyLedgerRepository) {
        this.loyaltyLedgerService = loyaltyLedgerService;
        this.loyaltyLedgerRepository = loyaltyLedgerRepository;
    }

    /**
     * {@code POST  /loyalty-ledgers} : Create a new loyaltyLedger.
     *
     * @param loyaltyLedgerDTO the loyaltyLedgerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loyaltyLedgerDTO, or with status {@code 400 (Bad Request)} if the loyaltyLedger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LoyaltyLedgerDTO> createLoyaltyLedger(@Valid @RequestBody LoyaltyLedgerDTO loyaltyLedgerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LoyaltyLedger : {}", loyaltyLedgerDTO);
        if (loyaltyLedgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new loyaltyLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        loyaltyLedgerDTO = loyaltyLedgerService.save(loyaltyLedgerDTO);
        return ResponseEntity.created(new URI("/api/loyalty-ledgers/" + loyaltyLedgerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, loyaltyLedgerDTO.getId().toString()))
            .body(loyaltyLedgerDTO);
    }

    /**
     * {@code PUT  /loyalty-ledgers/:id} : Updates an existing loyaltyLedger.
     *
     * @param id the id of the loyaltyLedgerDTO to save.
     * @param loyaltyLedgerDTO the loyaltyLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loyaltyLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the loyaltyLedgerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loyaltyLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyLedgerDTO> updateLoyaltyLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoyaltyLedgerDTO loyaltyLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LoyaltyLedger : {}, {}", id, loyaltyLedgerDTO);
        if (loyaltyLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loyaltyLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loyaltyLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        loyaltyLedgerDTO = loyaltyLedgerService.update(loyaltyLedgerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loyaltyLedgerDTO.getId().toString()))
            .body(loyaltyLedgerDTO);
    }

    /**
     * {@code PATCH  /loyalty-ledgers/:id} : Partial updates given fields of an existing loyaltyLedger, field will ignore if it is null
     *
     * @param id the id of the loyaltyLedgerDTO to save.
     * @param loyaltyLedgerDTO the loyaltyLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loyaltyLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the loyaltyLedgerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loyaltyLedgerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loyaltyLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoyaltyLedgerDTO> partialUpdateLoyaltyLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoyaltyLedgerDTO loyaltyLedgerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LoyaltyLedger partially : {}, {}", id, loyaltyLedgerDTO);
        if (loyaltyLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loyaltyLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loyaltyLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoyaltyLedgerDTO> result = loyaltyLedgerService.partialUpdate(loyaltyLedgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loyaltyLedgerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loyalty-ledgers} : get all the loyaltyLedgers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loyaltyLedgers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LoyaltyLedgerDTO>> getAllLoyaltyLedgers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of LoyaltyLedgers");
        Page<LoyaltyLedgerDTO> page;
        if (eagerload) {
            page = loyaltyLedgerService.findAllWithEagerRelationships(pageable);
        } else {
            page = loyaltyLedgerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loyalty-ledgers/:id} : get the "id" loyaltyLedger.
     *
     * @param id the id of the loyaltyLedgerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loyaltyLedgerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyLedgerDTO> getLoyaltyLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LoyaltyLedger : {}", id);
        Optional<LoyaltyLedgerDTO> loyaltyLedgerDTO = loyaltyLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loyaltyLedgerDTO);
    }

    /**
     * {@code DELETE  /loyalty-ledgers/:id} : delete the "id" loyaltyLedger.
     *
     * @param id the id of the loyaltyLedgerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoyaltyLedger(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LoyaltyLedger : {}", id);
        loyaltyLedgerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
