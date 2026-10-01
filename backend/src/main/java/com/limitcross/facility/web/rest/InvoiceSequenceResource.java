package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.InvoiceSequenceRepository;
import com.limitcross.facility.service.InvoiceSequenceService;
import com.limitcross.facility.service.dto.InvoiceSequenceDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.InvoiceSequence}.
 */
@RestController
@RequestMapping("/api/invoice-sequences")
public class InvoiceSequenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSequenceResource.class);

    private static final String ENTITY_NAME = "invoiceSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceSequenceService invoiceSequenceService;

    private final InvoiceSequenceRepository invoiceSequenceRepository;

    public InvoiceSequenceResource(InvoiceSequenceService invoiceSequenceService, InvoiceSequenceRepository invoiceSequenceRepository) {
        this.invoiceSequenceService = invoiceSequenceService;
        this.invoiceSequenceRepository = invoiceSequenceRepository;
    }

    /**
     * {@code POST  /invoice-sequences} : Create a new invoiceSequence.
     *
     * @param invoiceSequenceDTO the invoiceSequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceSequenceDTO, or with status {@code 400 (Bad Request)} if the invoiceSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceSequenceDTO> createInvoiceSequence(@Valid @RequestBody InvoiceSequenceDTO invoiceSequenceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceSequence : {}", invoiceSequenceDTO);
        if (invoiceSequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceSequenceDTO = invoiceSequenceService.save(invoiceSequenceDTO);
        return ResponseEntity.created(new URI("/api/invoice-sequences/" + invoiceSequenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, invoiceSequenceDTO.getId().toString()))
            .body(invoiceSequenceDTO);
    }

    /**
     * {@code PUT  /invoice-sequences/:id} : Updates an existing invoiceSequence.
     *
     * @param id the id of the invoiceSequenceDTO to save.
     * @param invoiceSequenceDTO the invoiceSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceSequenceDTO> updateInvoiceSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceSequenceDTO invoiceSequenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceSequence : {}, {}", id, invoiceSequenceDTO);
        if (invoiceSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceSequenceDTO = invoiceSequenceService.update(invoiceSequenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceSequenceDTO.getId().toString()))
            .body(invoiceSequenceDTO);
    }

    /**
     * {@code PATCH  /invoice-sequences/:id} : Partial updates given fields of an existing invoiceSequence, field will ignore if it is null
     *
     * @param id the id of the invoiceSequenceDTO to save.
     * @param invoiceSequenceDTO the invoiceSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSequenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceSequenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceSequenceDTO> partialUpdateInvoiceSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceSequenceDTO invoiceSequenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceSequence partially : {}, {}", id, invoiceSequenceDTO);
        if (invoiceSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceSequenceDTO> result = invoiceSequenceService.partialUpdate(invoiceSequenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceSequenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-sequences} : get all the invoiceSequences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceSequences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceSequenceDTO>> getAllInvoiceSequences(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of InvoiceSequences");
        Page<InvoiceSequenceDTO> page = invoiceSequenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-sequences/:id} : get the "id" invoiceSequence.
     *
     * @param id the id of the invoiceSequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceSequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceSequenceDTO> getInvoiceSequence(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceSequence : {}", id);
        Optional<InvoiceSequenceDTO> invoiceSequenceDTO = invoiceSequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceSequenceDTO);
    }

    /**
     * {@code DELETE  /invoice-sequences/:id} : delete the "id" invoiceSequence.
     *
     * @param id the id of the invoiceSequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceSequence(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceSequence : {}", id);
        invoiceSequenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
