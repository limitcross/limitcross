package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalTimeOffRepository;
import com.limitcross.facility.service.ProfessionalTimeOffService;
import com.limitcross.facility.service.dto.ProfessionalTimeOffDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalTimeOff}.
 */
@RestController
@RequestMapping("/api/professional-time-offs")
public class ProfessionalTimeOffResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTimeOffResource.class);

    private static final String ENTITY_NAME = "professionalTimeOff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalTimeOffService professionalTimeOffService;

    private final ProfessionalTimeOffRepository professionalTimeOffRepository;

    public ProfessionalTimeOffResource(
        ProfessionalTimeOffService professionalTimeOffService,
        ProfessionalTimeOffRepository professionalTimeOffRepository
    ) {
        this.professionalTimeOffService = professionalTimeOffService;
        this.professionalTimeOffRepository = professionalTimeOffRepository;
    }

    /**
     * {@code POST  /professional-time-offs} : Create a new professionalTimeOff.
     *
     * @param professionalTimeOffDTO the professionalTimeOffDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalTimeOffDTO, or with status {@code 400 (Bad Request)} if the professionalTimeOff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalTimeOffDTO> createProfessionalTimeOff(
        @Valid @RequestBody ProfessionalTimeOffDTO professionalTimeOffDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalTimeOff : {}", professionalTimeOffDTO);
        if (professionalTimeOffDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalTimeOff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalTimeOffDTO = professionalTimeOffService.save(professionalTimeOffDTO);
        return ResponseEntity.created(new URI("/api/professional-time-offs/" + professionalTimeOffDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalTimeOffDTO.getId().toString()))
            .body(professionalTimeOffDTO);
    }

    /**
     * {@code PUT  /professional-time-offs/:id} : Updates an existing professionalTimeOff.
     *
     * @param id the id of the professionalTimeOffDTO to save.
     * @param professionalTimeOffDTO the professionalTimeOffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTimeOffDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTimeOffDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalTimeOffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalTimeOffDTO> updateProfessionalTimeOff(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalTimeOffDTO professionalTimeOffDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalTimeOff : {}, {}", id, professionalTimeOffDTO);
        if (professionalTimeOffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTimeOffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTimeOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalTimeOffDTO = professionalTimeOffService.update(professionalTimeOffDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTimeOffDTO.getId().toString()))
            .body(professionalTimeOffDTO);
    }

    /**
     * {@code PATCH  /professional-time-offs/:id} : Partial updates given fields of an existing professionalTimeOff, field will ignore if it is null
     *
     * @param id the id of the professionalTimeOffDTO to save.
     * @param professionalTimeOffDTO the professionalTimeOffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTimeOffDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTimeOffDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalTimeOffDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalTimeOffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalTimeOffDTO> partialUpdateProfessionalTimeOff(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalTimeOffDTO professionalTimeOffDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalTimeOff partially : {}, {}", id, professionalTimeOffDTO);
        if (professionalTimeOffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTimeOffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTimeOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalTimeOffDTO> result = professionalTimeOffService.partialUpdate(professionalTimeOffDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTimeOffDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-time-offs} : get all the professionalTimeOffs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalTimeOffs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalTimeOffDTO>> getAllProfessionalTimeOffs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalTimeOffs");
        Page<ProfessionalTimeOffDTO> page;
        if (eagerload) {
            page = professionalTimeOffService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalTimeOffService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-time-offs/:id} : get the "id" professionalTimeOff.
     *
     * @param id the id of the professionalTimeOffDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalTimeOffDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalTimeOffDTO> getProfessionalTimeOff(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalTimeOff : {}", id);
        Optional<ProfessionalTimeOffDTO> professionalTimeOffDTO = professionalTimeOffService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalTimeOffDTO);
    }

    /**
     * {@code DELETE  /professional-time-offs/:id} : delete the "id" professionalTimeOff.
     *
     * @param id the id of the professionalTimeOffDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalTimeOff(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalTimeOff : {}", id);
        professionalTimeOffService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
