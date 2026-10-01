package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.FraudFlagRepository;
import com.limitcross.facility.service.FraudFlagService;
import com.limitcross.facility.service.dto.FraudFlagDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.FraudFlag}.
 */
@RestController
@RequestMapping("/api/fraud-flags")
public class FraudFlagResource {

    private static final Logger LOG = LoggerFactory.getLogger(FraudFlagResource.class);

    private static final String ENTITY_NAME = "fraudFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraudFlagService fraudFlagService;

    private final FraudFlagRepository fraudFlagRepository;

    public FraudFlagResource(FraudFlagService fraudFlagService, FraudFlagRepository fraudFlagRepository) {
        this.fraudFlagService = fraudFlagService;
        this.fraudFlagRepository = fraudFlagRepository;
    }

    /**
     * {@code POST  /fraud-flags} : Create a new fraudFlag.
     *
     * @param fraudFlagDTO the fraudFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraudFlagDTO, or with status {@code 400 (Bad Request)} if the fraudFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FraudFlagDTO> createFraudFlag(@Valid @RequestBody FraudFlagDTO fraudFlagDTO) throws URISyntaxException {
        LOG.debug("REST request to save FraudFlag : {}", fraudFlagDTO);
        if (fraudFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new fraudFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fraudFlagDTO = fraudFlagService.save(fraudFlagDTO);
        return ResponseEntity.created(new URI("/api/fraud-flags/" + fraudFlagDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fraudFlagDTO.getId().toString()))
            .body(fraudFlagDTO);
    }

    /**
     * {@code PUT  /fraud-flags/:id} : Updates an existing fraudFlag.
     *
     * @param id the id of the fraudFlagDTO to save.
     * @param fraudFlagDTO the fraudFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudFlagDTO,
     * or with status {@code 400 (Bad Request)} if the fraudFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraudFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FraudFlagDTO> updateFraudFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FraudFlagDTO fraudFlagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FraudFlag : {}, {}", id, fraudFlagDTO);
        if (fraudFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fraudFlagDTO = fraudFlagService.update(fraudFlagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fraudFlagDTO.getId().toString()))
            .body(fraudFlagDTO);
    }

    /**
     * {@code PATCH  /fraud-flags/:id} : Partial updates given fields of an existing fraudFlag, field will ignore if it is null
     *
     * @param id the id of the fraudFlagDTO to save.
     * @param fraudFlagDTO the fraudFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudFlagDTO,
     * or with status {@code 400 (Bad Request)} if the fraudFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraudFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraudFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraudFlagDTO> partialUpdateFraudFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FraudFlagDTO fraudFlagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FraudFlag partially : {}, {}", id, fraudFlagDTO);
        if (fraudFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FraudFlagDTO> result = fraudFlagService.partialUpdate(fraudFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fraudFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fraud-flags} : get all the fraudFlags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fraudFlags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FraudFlagDTO>> getAllFraudFlags(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FraudFlags");
        Page<FraudFlagDTO> page = fraudFlagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fraud-flags/:id} : get the "id" fraudFlag.
     *
     * @param id the id of the fraudFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraudFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FraudFlagDTO> getFraudFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FraudFlag : {}", id);
        Optional<FraudFlagDTO> fraudFlagDTO = fraudFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraudFlagDTO);
    }

    /**
     * {@code DELETE  /fraud-flags/:id} : delete the "id" fraudFlag.
     *
     * @param id the id of the fraudFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudFlag(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FraudFlag : {}", id);
        fraudFlagService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
