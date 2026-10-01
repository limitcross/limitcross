package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.OutboxEventRepository;
import com.limitcross.facility.service.OutboxEventService;
import com.limitcross.facility.service.dto.OutboxEventDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.OutboxEvent}.
 */
@RestController
@RequestMapping("/api/outbox-events")
public class OutboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(OutboxEventResource.class);

    private static final String ENTITY_NAME = "outboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutboxEventService outboxEventService;

    private final OutboxEventRepository outboxEventRepository;

    public OutboxEventResource(OutboxEventService outboxEventService, OutboxEventRepository outboxEventRepository) {
        this.outboxEventService = outboxEventService;
        this.outboxEventRepository = outboxEventRepository;
    }

    /**
     * {@code POST  /outbox-events} : Create a new outboxEvent.
     *
     * @param outboxEventDTO the outboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outboxEventDTO, or with status {@code 400 (Bad Request)} if the outboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OutboxEventDTO> createOutboxEvent(@Valid @RequestBody OutboxEventDTO outboxEventDTO) throws URISyntaxException {
        LOG.debug("REST request to save OutboxEvent : {}", outboxEventDTO);
        if (outboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new outboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        outboxEventDTO = outboxEventService.save(outboxEventDTO);
        return ResponseEntity.created(new URI("/api/outbox-events/" + outboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, outboxEventDTO.getId().toString()))
            .body(outboxEventDTO);
    }

    /**
     * {@code PUT  /outbox-events/:id} : Updates an existing outboxEvent.
     *
     * @param id the id of the outboxEventDTO to save.
     * @param outboxEventDTO the outboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the outboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OutboxEventDTO> updateOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OutboxEventDTO outboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OutboxEvent : {}, {}", id, outboxEventDTO);
        if (outboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        outboxEventDTO = outboxEventService.update(outboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outboxEventDTO.getId().toString()))
            .body(outboxEventDTO);
    }

    /**
     * {@code PATCH  /outbox-events/:id} : Partial updates given fields of an existing outboxEvent, field will ignore if it is null
     *
     * @param id the id of the outboxEventDTO to save.
     * @param outboxEventDTO the outboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the outboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutboxEventDTO> partialUpdateOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OutboxEventDTO outboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OutboxEvent partially : {}, {}", id, outboxEventDTO);
        if (outboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutboxEventDTO> result = outboxEventService.partialUpdate(outboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /outbox-events} : get all the outboxEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outboxEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OutboxEventDTO>> getAllOutboxEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of OutboxEvents");
        Page<OutboxEventDTO> page = outboxEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /outbox-events/:id} : get the "id" outboxEvent.
     *
     * @param id the id of the outboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OutboxEventDTO> getOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OutboxEvent : {}", id);
        Optional<OutboxEventDTO> outboxEventDTO = outboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outboxEventDTO);
    }

    /**
     * {@code DELETE  /outbox-events/:id} : delete the "id" outboxEvent.
     *
     * @param id the id of the outboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OutboxEvent : {}", id);
        outboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
