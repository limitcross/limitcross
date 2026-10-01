package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CallSessionRepository;
import com.limitcross.facility.service.CallSessionService;
import com.limitcross.facility.service.dto.CallSessionDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CallSession}.
 */
@RestController
@RequestMapping("/api/call-sessions")
public class CallSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CallSessionResource.class);

    private static final String ENTITY_NAME = "callSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CallSessionService callSessionService;

    private final CallSessionRepository callSessionRepository;

    public CallSessionResource(CallSessionService callSessionService, CallSessionRepository callSessionRepository) {
        this.callSessionService = callSessionService;
        this.callSessionRepository = callSessionRepository;
    }

    /**
     * {@code POST  /call-sessions} : Create a new callSession.
     *
     * @param callSessionDTO the callSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new callSessionDTO, or with status {@code 400 (Bad Request)} if the callSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CallSessionDTO> createCallSession(@Valid @RequestBody CallSessionDTO callSessionDTO) throws URISyntaxException {
        LOG.debug("REST request to save CallSession : {}", callSessionDTO);
        if (callSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new callSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        callSessionDTO = callSessionService.save(callSessionDTO);
        return ResponseEntity.created(new URI("/api/call-sessions/" + callSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, callSessionDTO.getId().toString()))
            .body(callSessionDTO);
    }

    /**
     * {@code PUT  /call-sessions/:id} : Updates an existing callSession.
     *
     * @param id the id of the callSessionDTO to save.
     * @param callSessionDTO the callSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated callSessionDTO,
     * or with status {@code 400 (Bad Request)} if the callSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the callSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CallSessionDTO> updateCallSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CallSessionDTO callSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CallSession : {}, {}", id, callSessionDTO);
        if (callSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, callSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!callSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        callSessionDTO = callSessionService.update(callSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, callSessionDTO.getId().toString()))
            .body(callSessionDTO);
    }

    /**
     * {@code PATCH  /call-sessions/:id} : Partial updates given fields of an existing callSession, field will ignore if it is null
     *
     * @param id the id of the callSessionDTO to save.
     * @param callSessionDTO the callSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated callSessionDTO,
     * or with status {@code 400 (Bad Request)} if the callSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the callSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the callSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CallSessionDTO> partialUpdateCallSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CallSessionDTO callSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CallSession partially : {}, {}", id, callSessionDTO);
        if (callSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, callSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!callSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CallSessionDTO> result = callSessionService.partialUpdate(callSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, callSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /call-sessions} : get all the callSessions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of callSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CallSessionDTO>> getAllCallSessions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CallSessions");
        Page<CallSessionDTO> page;
        if (eagerload) {
            page = callSessionService.findAllWithEagerRelationships(pageable);
        } else {
            page = callSessionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /call-sessions/:id} : get the "id" callSession.
     *
     * @param id the id of the callSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the callSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CallSessionDTO> getCallSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CallSession : {}", id);
        Optional<CallSessionDTO> callSessionDTO = callSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(callSessionDTO);
    }

    /**
     * {@code DELETE  /call-sessions/:id} : delete the "id" callSession.
     *
     * @param id the id of the callSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCallSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CallSession : {}", id);
        callSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
