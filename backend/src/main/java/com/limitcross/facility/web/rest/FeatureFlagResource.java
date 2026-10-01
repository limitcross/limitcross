package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.FeatureFlagRepository;
import com.limitcross.facility.service.FeatureFlagService;
import com.limitcross.facility.service.dto.FeatureFlagDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.limitcross.facility.domain.FeatureFlag}.
 */
@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagResource.class);

    private static final String ENTITY_NAME = "featureFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureFlagService featureFlagService;

    private final FeatureFlagRepository featureFlagRepository;

    public FeatureFlagResource(FeatureFlagService featureFlagService, FeatureFlagRepository featureFlagRepository) {
        this.featureFlagService = featureFlagService;
        this.featureFlagRepository = featureFlagRepository;
    }

    /**
     * {@code POST  /feature-flags} : Create a new featureFlag.
     *
     * @param featureFlagDTO the featureFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new featureFlagDTO, or with status {@code 400 (Bad Request)} if the featureFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeatureFlagDTO> createFeatureFlag(@Valid @RequestBody FeatureFlagDTO featureFlagDTO) throws URISyntaxException {
        LOG.debug("REST request to save FeatureFlag : {}", featureFlagDTO);
        if (featureFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new featureFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        featureFlagDTO = featureFlagService.save(featureFlagDTO);
        return ResponseEntity.created(new URI("/api/feature-flags/" + featureFlagDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, featureFlagDTO.getId().toString()))
            .body(featureFlagDTO);
    }

    /**
     * {@code PUT  /feature-flags/:id} : Updates an existing featureFlag.
     *
     * @param id the id of the featureFlagDTO to save.
     * @param featureFlagDTO the featureFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureFlagDTO,
     * or with status {@code 400 (Bad Request)} if the featureFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the featureFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagDTO> updateFeatureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeatureFlagDTO featureFlagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FeatureFlag : {}, {}", id, featureFlagDTO);
        if (featureFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        featureFlagDTO = featureFlagService.update(featureFlagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, featureFlagDTO.getId().toString()))
            .body(featureFlagDTO);
    }

    /**
     * {@code PATCH  /feature-flags/:id} : Partial updates given fields of an existing featureFlag, field will ignore if it is null
     *
     * @param id the id of the featureFlagDTO to save.
     * @param featureFlagDTO the featureFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureFlagDTO,
     * or with status {@code 400 (Bad Request)} if the featureFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the featureFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the featureFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeatureFlagDTO> partialUpdateFeatureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeatureFlagDTO featureFlagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FeatureFlag partially : {}, {}", id, featureFlagDTO);
        if (featureFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeatureFlagDTO> result = featureFlagService.partialUpdate(featureFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, featureFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /feature-flags} : get all the featureFlags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of featureFlags in body.
     */
    @GetMapping("")
    public List<FeatureFlagDTO> getAllFeatureFlags() {
        LOG.debug("REST request to get all FeatureFlags");
        return featureFlagService.findAll();
    }

    /**
     * {@code GET  /feature-flags/:id} : get the "id" featureFlag.
     *
     * @param id the id of the featureFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the featureFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlagDTO> getFeatureFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FeatureFlag : {}", id);
        Optional<FeatureFlagDTO> featureFlagDTO = featureFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(featureFlagDTO);
    }

    /**
     * {@code DELETE  /feature-flags/:id} : delete the "id" featureFlag.
     *
     * @param id the id of the featureFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FeatureFlag : {}", id);
        featureFlagService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
