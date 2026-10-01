package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ApiIdempotencyKeyRepository;
import com.limitcross.facility.service.ApiIdempotencyKeyService;
import com.limitcross.facility.service.dto.ApiIdempotencyKeyDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ApiIdempotencyKey}.
 */
@RestController
@RequestMapping("/api/api-idempotency-keys")
public class ApiIdempotencyKeyResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApiIdempotencyKeyResource.class);

    private static final String ENTITY_NAME = "apiIdempotencyKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiIdempotencyKeyService apiIdempotencyKeyService;

    private final ApiIdempotencyKeyRepository apiIdempotencyKeyRepository;

    public ApiIdempotencyKeyResource(
        ApiIdempotencyKeyService apiIdempotencyKeyService,
        ApiIdempotencyKeyRepository apiIdempotencyKeyRepository
    ) {
        this.apiIdempotencyKeyService = apiIdempotencyKeyService;
        this.apiIdempotencyKeyRepository = apiIdempotencyKeyRepository;
    }

    /**
     * {@code POST  /api-idempotency-keys} : Create a new apiIdempotencyKey.
     *
     * @param apiIdempotencyKeyDTO the apiIdempotencyKeyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiIdempotencyKeyDTO, or with status {@code 400 (Bad Request)} if the apiIdempotencyKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApiIdempotencyKeyDTO> createApiIdempotencyKey(@Valid @RequestBody ApiIdempotencyKeyDTO apiIdempotencyKeyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ApiIdempotencyKey : {}", apiIdempotencyKeyDTO);
        if (apiIdempotencyKeyDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiIdempotencyKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        apiIdempotencyKeyDTO = apiIdempotencyKeyService.save(apiIdempotencyKeyDTO);
        return ResponseEntity.created(new URI("/api/api-idempotency-keys/" + apiIdempotencyKeyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, apiIdempotencyKeyDTO.getId().toString()))
            .body(apiIdempotencyKeyDTO);
    }

    /**
     * {@code PUT  /api-idempotency-keys/:id} : Updates an existing apiIdempotencyKey.
     *
     * @param id the id of the apiIdempotencyKeyDTO to save.
     * @param apiIdempotencyKeyDTO the apiIdempotencyKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiIdempotencyKeyDTO,
     * or with status {@code 400 (Bad Request)} if the apiIdempotencyKeyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiIdempotencyKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiIdempotencyKeyDTO> updateApiIdempotencyKey(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApiIdempotencyKeyDTO apiIdempotencyKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApiIdempotencyKey : {}, {}", id, apiIdempotencyKeyDTO);
        if (apiIdempotencyKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiIdempotencyKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiIdempotencyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        apiIdempotencyKeyDTO = apiIdempotencyKeyService.update(apiIdempotencyKeyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apiIdempotencyKeyDTO.getId().toString()))
            .body(apiIdempotencyKeyDTO);
    }

    /**
     * {@code PATCH  /api-idempotency-keys/:id} : Partial updates given fields of an existing apiIdempotencyKey, field will ignore if it is null
     *
     * @param id the id of the apiIdempotencyKeyDTO to save.
     * @param apiIdempotencyKeyDTO the apiIdempotencyKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiIdempotencyKeyDTO,
     * or with status {@code 400 (Bad Request)} if the apiIdempotencyKeyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the apiIdempotencyKeyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the apiIdempotencyKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApiIdempotencyKeyDTO> partialUpdateApiIdempotencyKey(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApiIdempotencyKeyDTO apiIdempotencyKeyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApiIdempotencyKey partially : {}, {}", id, apiIdempotencyKeyDTO);
        if (apiIdempotencyKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiIdempotencyKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiIdempotencyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApiIdempotencyKeyDTO> result = apiIdempotencyKeyService.partialUpdate(apiIdempotencyKeyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apiIdempotencyKeyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /api-idempotency-keys} : get all the apiIdempotencyKeys.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiIdempotencyKeys in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApiIdempotencyKeyDTO>> getAllApiIdempotencyKeys(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ApiIdempotencyKeys");
        Page<ApiIdempotencyKeyDTO> page;
        if (eagerload) {
            page = apiIdempotencyKeyService.findAllWithEagerRelationships(pageable);
        } else {
            page = apiIdempotencyKeyService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /api-idempotency-keys/:id} : get the "id" apiIdempotencyKey.
     *
     * @param id the id of the apiIdempotencyKeyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiIdempotencyKeyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiIdempotencyKeyDTO> getApiIdempotencyKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApiIdempotencyKey : {}", id);
        Optional<ApiIdempotencyKeyDTO> apiIdempotencyKeyDTO = apiIdempotencyKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiIdempotencyKeyDTO);
    }

    /**
     * {@code DELETE  /api-idempotency-keys/:id} : delete the "id" apiIdempotencyKey.
     *
     * @param id the id of the apiIdempotencyKeyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiIdempotencyKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApiIdempotencyKey : {}", id);
        apiIdempotencyKeyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
