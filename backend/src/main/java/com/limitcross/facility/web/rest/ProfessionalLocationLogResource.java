package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalLocationLogRepository;
import com.limitcross.facility.service.ProfessionalLocationLogService;
import com.limitcross.facility.service.dto.ProfessionalLocationLogDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalLocationLog}.
 */
@RestController
@RequestMapping("/api/professional-location-logs")
public class ProfessionalLocationLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalLocationLogResource.class);

    private static final String ENTITY_NAME = "professionalLocationLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalLocationLogService professionalLocationLogService;

    private final ProfessionalLocationLogRepository professionalLocationLogRepository;

    public ProfessionalLocationLogResource(
        ProfessionalLocationLogService professionalLocationLogService,
        ProfessionalLocationLogRepository professionalLocationLogRepository
    ) {
        this.professionalLocationLogService = professionalLocationLogService;
        this.professionalLocationLogRepository = professionalLocationLogRepository;
    }

    /**
     * {@code POST  /professional-location-logs} : Create a new professionalLocationLog.
     *
     * @param professionalLocationLogDTO the professionalLocationLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalLocationLogDTO, or with status {@code 400 (Bad Request)} if the professionalLocationLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalLocationLogDTO> createProfessionalLocationLog(
        @Valid @RequestBody ProfessionalLocationLogDTO professionalLocationLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalLocationLog : {}", professionalLocationLogDTO);
        if (professionalLocationLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalLocationLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalLocationLogDTO = professionalLocationLogService.save(professionalLocationLogDTO);
        return ResponseEntity.created(new URI("/api/professional-location-logs/" + professionalLocationLogDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalLocationLogDTO.getId().toString())
            )
            .body(professionalLocationLogDTO);
    }

    /**
     * {@code PUT  /professional-location-logs/:id} : Updates an existing professionalLocationLog.
     *
     * @param id the id of the professionalLocationLogDTO to save.
     * @param professionalLocationLogDTO the professionalLocationLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalLocationLogDTO,
     * or with status {@code 400 (Bad Request)} if the professionalLocationLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalLocationLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalLocationLogDTO> updateProfessionalLocationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalLocationLogDTO professionalLocationLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalLocationLog : {}, {}", id, professionalLocationLogDTO);
        if (professionalLocationLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalLocationLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalLocationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalLocationLogDTO = professionalLocationLogService.update(professionalLocationLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalLocationLogDTO.getId().toString()))
            .body(professionalLocationLogDTO);
    }

    /**
     * {@code PATCH  /professional-location-logs/:id} : Partial updates given fields of an existing professionalLocationLog, field will ignore if it is null
     *
     * @param id the id of the professionalLocationLogDTO to save.
     * @param professionalLocationLogDTO the professionalLocationLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalLocationLogDTO,
     * or with status {@code 400 (Bad Request)} if the professionalLocationLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalLocationLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalLocationLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalLocationLogDTO> partialUpdateProfessionalLocationLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalLocationLogDTO professionalLocationLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalLocationLog partially : {}, {}", id, professionalLocationLogDTO);
        if (professionalLocationLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalLocationLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalLocationLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalLocationLogDTO> result = professionalLocationLogService.partialUpdate(professionalLocationLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalLocationLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-location-logs} : get all the professionalLocationLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalLocationLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalLocationLogDTO>> getAllProfessionalLocationLogs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProfessionalLocationLogs");
        Page<ProfessionalLocationLogDTO> page = professionalLocationLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-location-logs/:id} : get the "id" professionalLocationLog.
     *
     * @param id the id of the professionalLocationLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalLocationLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalLocationLogDTO> getProfessionalLocationLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalLocationLog : {}", id);
        Optional<ProfessionalLocationLogDTO> professionalLocationLogDTO = professionalLocationLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalLocationLogDTO);
    }

    /**
     * {@code DELETE  /professional-location-logs/:id} : delete the "id" professionalLocationLog.
     *
     * @param id the id of the professionalLocationLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalLocationLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalLocationLog : {}", id);
        professionalLocationLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
