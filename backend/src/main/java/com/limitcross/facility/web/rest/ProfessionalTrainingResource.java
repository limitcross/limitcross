package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalTrainingRepository;
import com.limitcross.facility.service.ProfessionalTrainingService;
import com.limitcross.facility.service.dto.ProfessionalTrainingDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalTraining}.
 */
@RestController
@RequestMapping("/api/professional-trainings")
public class ProfessionalTrainingResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalTrainingResource.class);

    private static final String ENTITY_NAME = "professionalTraining";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalTrainingService professionalTrainingService;

    private final ProfessionalTrainingRepository professionalTrainingRepository;

    public ProfessionalTrainingResource(
        ProfessionalTrainingService professionalTrainingService,
        ProfessionalTrainingRepository professionalTrainingRepository
    ) {
        this.professionalTrainingService = professionalTrainingService;
        this.professionalTrainingRepository = professionalTrainingRepository;
    }

    /**
     * {@code POST  /professional-trainings} : Create a new professionalTraining.
     *
     * @param professionalTrainingDTO the professionalTrainingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalTrainingDTO, or with status {@code 400 (Bad Request)} if the professionalTraining has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalTrainingDTO> createProfessionalTraining(
        @Valid @RequestBody ProfessionalTrainingDTO professionalTrainingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalTraining : {}", professionalTrainingDTO);
        if (professionalTrainingDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalTraining cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalTrainingDTO = professionalTrainingService.save(professionalTrainingDTO);
        return ResponseEntity.created(new URI("/api/professional-trainings/" + professionalTrainingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalTrainingDTO.getId().toString()))
            .body(professionalTrainingDTO);
    }

    /**
     * {@code PUT  /professional-trainings/:id} : Updates an existing professionalTraining.
     *
     * @param id the id of the professionalTrainingDTO to save.
     * @param professionalTrainingDTO the professionalTrainingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTrainingDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTrainingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalTrainingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalTrainingDTO> updateProfessionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalTrainingDTO professionalTrainingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalTraining : {}, {}", id, professionalTrainingDTO);
        if (professionalTrainingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTrainingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalTrainingDTO = professionalTrainingService.update(professionalTrainingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTrainingDTO.getId().toString()))
            .body(professionalTrainingDTO);
    }

    /**
     * {@code PATCH  /professional-trainings/:id} : Partial updates given fields of an existing professionalTraining, field will ignore if it is null
     *
     * @param id the id of the professionalTrainingDTO to save.
     * @param professionalTrainingDTO the professionalTrainingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalTrainingDTO,
     * or with status {@code 400 (Bad Request)} if the professionalTrainingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalTrainingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalTrainingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalTrainingDTO> partialUpdateProfessionalTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalTrainingDTO professionalTrainingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalTraining partially : {}, {}", id, professionalTrainingDTO);
        if (professionalTrainingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalTrainingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalTrainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalTrainingDTO> result = professionalTrainingService.partialUpdate(professionalTrainingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalTrainingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-trainings} : get all the professionalTrainings.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalTrainings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalTrainingDTO>> getAllProfessionalTrainings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalTrainings");
        Page<ProfessionalTrainingDTO> page;
        if (eagerload) {
            page = professionalTrainingService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalTrainingService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-trainings/:id} : get the "id" professionalTraining.
     *
     * @param id the id of the professionalTrainingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalTrainingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalTrainingDTO> getProfessionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalTraining : {}", id);
        Optional<ProfessionalTrainingDTO> professionalTrainingDTO = professionalTrainingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalTrainingDTO);
    }

    /**
     * {@code DELETE  /professional-trainings/:id} : delete the "id" professionalTraining.
     *
     * @param id the id of the professionalTrainingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalTraining(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalTraining : {}", id);
        professionalTrainingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
