package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.TrainingModuleRepository;
import com.limitcross.facility.service.TrainingModuleService;
import com.limitcross.facility.service.dto.TrainingModuleDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.TrainingModule}.
 */
@RestController
@RequestMapping("/api/training-modules")
public class TrainingModuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingModuleResource.class);

    private static final String ENTITY_NAME = "trainingModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingModuleService trainingModuleService;

    private final TrainingModuleRepository trainingModuleRepository;

    public TrainingModuleResource(TrainingModuleService trainingModuleService, TrainingModuleRepository trainingModuleRepository) {
        this.trainingModuleService = trainingModuleService;
        this.trainingModuleRepository = trainingModuleRepository;
    }

    /**
     * {@code POST  /training-modules} : Create a new trainingModule.
     *
     * @param trainingModuleDTO the trainingModuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingModuleDTO, or with status {@code 400 (Bad Request)} if the trainingModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrainingModuleDTO> createTrainingModule(@Valid @RequestBody TrainingModuleDTO trainingModuleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrainingModule : {}", trainingModuleDTO);
        if (trainingModuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trainingModuleDTO = trainingModuleService.save(trainingModuleDTO);
        return ResponseEntity.created(new URI("/api/training-modules/" + trainingModuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trainingModuleDTO.getId().toString()))
            .body(trainingModuleDTO);
    }

    /**
     * {@code PUT  /training-modules/:id} : Updates an existing trainingModule.
     *
     * @param id the id of the trainingModuleDTO to save.
     * @param trainingModuleDTO the trainingModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingModuleDTO,
     * or with status {@code 400 (Bad Request)} if the trainingModuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingModuleDTO> updateTrainingModule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainingModuleDTO trainingModuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrainingModule : {}, {}", id, trainingModuleDTO);
        if (trainingModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trainingModuleDTO = trainingModuleService.update(trainingModuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingModuleDTO.getId().toString()))
            .body(trainingModuleDTO);
    }

    /**
     * {@code PATCH  /training-modules/:id} : Partial updates given fields of an existing trainingModule, field will ignore if it is null
     *
     * @param id the id of the trainingModuleDTO to save.
     * @param trainingModuleDTO the trainingModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingModuleDTO,
     * or with status {@code 400 (Bad Request)} if the trainingModuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainingModuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingModuleDTO> partialUpdateTrainingModule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainingModuleDTO trainingModuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrainingModule partially : {}, {}", id, trainingModuleDTO);
        if (trainingModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingModuleDTO> result = trainingModuleService.partialUpdate(trainingModuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingModuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /training-modules} : get all the trainingModules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingModules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrainingModuleDTO>> getAllTrainingModules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of TrainingModules");
        Page<TrainingModuleDTO> page;
        if (eagerload) {
            page = trainingModuleService.findAllWithEagerRelationships(pageable);
        } else {
            page = trainingModuleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /training-modules/:id} : get the "id" trainingModule.
     *
     * @param id the id of the trainingModuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingModuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingModuleDTO> getTrainingModule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrainingModule : {}", id);
        Optional<TrainingModuleDTO> trainingModuleDTO = trainingModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainingModuleDTO);
    }

    /**
     * {@code DELETE  /training-modules/:id} : delete the "id" trainingModule.
     *
     * @param id the id of the trainingModuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingModule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrainingModule : {}", id);
        trainingModuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
