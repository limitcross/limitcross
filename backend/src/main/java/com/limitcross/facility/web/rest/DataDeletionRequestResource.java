package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.DataDeletionRequestRepository;
import com.limitcross.facility.service.DataDeletionRequestService;
import com.limitcross.facility.service.dto.DataDeletionRequestDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.DataDeletionRequest}.
 */
@RestController
@RequestMapping("/api/data-deletion-requests")
public class DataDeletionRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(DataDeletionRequestResource.class);

    private static final String ENTITY_NAME = "dataDeletionRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataDeletionRequestService dataDeletionRequestService;

    private final DataDeletionRequestRepository dataDeletionRequestRepository;

    public DataDeletionRequestResource(
        DataDeletionRequestService dataDeletionRequestService,
        DataDeletionRequestRepository dataDeletionRequestRepository
    ) {
        this.dataDeletionRequestService = dataDeletionRequestService;
        this.dataDeletionRequestRepository = dataDeletionRequestRepository;
    }

    /**
     * {@code POST  /data-deletion-requests} : Create a new dataDeletionRequest.
     *
     * @param dataDeletionRequestDTO the dataDeletionRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataDeletionRequestDTO, or with status {@code 400 (Bad Request)} if the dataDeletionRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DataDeletionRequestDTO> createDataDeletionRequest(
        @Valid @RequestBody DataDeletionRequestDTO dataDeletionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save DataDeletionRequest : {}", dataDeletionRequestDTO);
        if (dataDeletionRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataDeletionRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dataDeletionRequestDTO = dataDeletionRequestService.save(dataDeletionRequestDTO);
        return ResponseEntity.created(new URI("/api/data-deletion-requests/" + dataDeletionRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dataDeletionRequestDTO.getId().toString()))
            .body(dataDeletionRequestDTO);
    }

    /**
     * {@code PUT  /data-deletion-requests/:id} : Updates an existing dataDeletionRequest.
     *
     * @param id the id of the dataDeletionRequestDTO to save.
     * @param dataDeletionRequestDTO the dataDeletionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDeletionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the dataDeletionRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataDeletionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataDeletionRequestDTO> updateDataDeletionRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataDeletionRequestDTO dataDeletionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DataDeletionRequest : {}, {}", id, dataDeletionRequestDTO);
        if (dataDeletionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataDeletionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataDeletionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dataDeletionRequestDTO = dataDeletionRequestService.update(dataDeletionRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataDeletionRequestDTO.getId().toString()))
            .body(dataDeletionRequestDTO);
    }

    /**
     * {@code PATCH  /data-deletion-requests/:id} : Partial updates given fields of an existing dataDeletionRequest, field will ignore if it is null
     *
     * @param id the id of the dataDeletionRequestDTO to save.
     * @param dataDeletionRequestDTO the dataDeletionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataDeletionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the dataDeletionRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dataDeletionRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataDeletionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataDeletionRequestDTO> partialUpdateDataDeletionRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataDeletionRequestDTO dataDeletionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DataDeletionRequest partially : {}, {}", id, dataDeletionRequestDTO);
        if (dataDeletionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataDeletionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataDeletionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataDeletionRequestDTO> result = dataDeletionRequestService.partialUpdate(dataDeletionRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dataDeletionRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /data-deletion-requests} : get all the dataDeletionRequests.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataDeletionRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DataDeletionRequestDTO>> getAllDataDeletionRequests(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of DataDeletionRequests");
        Page<DataDeletionRequestDTO> page;
        if (eagerload) {
            page = dataDeletionRequestService.findAllWithEagerRelationships(pageable);
        } else {
            page = dataDeletionRequestService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /data-deletion-requests/:id} : get the "id" dataDeletionRequest.
     *
     * @param id the id of the dataDeletionRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataDeletionRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataDeletionRequestDTO> getDataDeletionRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DataDeletionRequest : {}", id);
        Optional<DataDeletionRequestDTO> dataDeletionRequestDTO = dataDeletionRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataDeletionRequestDTO);
    }

    /**
     * {@code DELETE  /data-deletion-requests/:id} : delete the "id" dataDeletionRequest.
     *
     * @param id the id of the dataDeletionRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataDeletionRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DataDeletionRequest : {}", id);
        dataDeletionRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
