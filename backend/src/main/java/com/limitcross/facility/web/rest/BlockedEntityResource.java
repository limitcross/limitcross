package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BlockedEntityRepository;
import com.limitcross.facility.service.BlockedEntityService;
import com.limitcross.facility.service.dto.BlockedEntityDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BlockedEntity}.
 */
@RestController
@RequestMapping("/api/blocked-entities")
public class BlockedEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(BlockedEntityResource.class);

    private static final String ENTITY_NAME = "blockedEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockedEntityService blockedEntityService;

    private final BlockedEntityRepository blockedEntityRepository;

    public BlockedEntityResource(BlockedEntityService blockedEntityService, BlockedEntityRepository blockedEntityRepository) {
        this.blockedEntityService = blockedEntityService;
        this.blockedEntityRepository = blockedEntityRepository;
    }

    /**
     * {@code POST  /blocked-entities} : Create a new blockedEntity.
     *
     * @param blockedEntityDTO the blockedEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blockedEntityDTO, or with status {@code 400 (Bad Request)} if the blockedEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BlockedEntityDTO> createBlockedEntity(@Valid @RequestBody BlockedEntityDTO blockedEntityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BlockedEntity : {}", blockedEntityDTO);
        if (blockedEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new blockedEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        blockedEntityDTO = blockedEntityService.save(blockedEntityDTO);
        return ResponseEntity.created(new URI("/api/blocked-entities/" + blockedEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, blockedEntityDTO.getId().toString()))
            .body(blockedEntityDTO);
    }

    /**
     * {@code PUT  /blocked-entities/:id} : Updates an existing blockedEntity.
     *
     * @param id the id of the blockedEntityDTO to save.
     * @param blockedEntityDTO the blockedEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockedEntityDTO,
     * or with status {@code 400 (Bad Request)} if the blockedEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockedEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlockedEntityDTO> updateBlockedEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BlockedEntityDTO blockedEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BlockedEntity : {}, {}", id, blockedEntityDTO);
        if (blockedEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockedEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockedEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        blockedEntityDTO = blockedEntityService.update(blockedEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, blockedEntityDTO.getId().toString()))
            .body(blockedEntityDTO);
    }

    /**
     * {@code PATCH  /blocked-entities/:id} : Partial updates given fields of an existing blockedEntity, field will ignore if it is null
     *
     * @param id the id of the blockedEntityDTO to save.
     * @param blockedEntityDTO the blockedEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockedEntityDTO,
     * or with status {@code 400 (Bad Request)} if the blockedEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the blockedEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the blockedEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BlockedEntityDTO> partialUpdateBlockedEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BlockedEntityDTO blockedEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BlockedEntity partially : {}, {}", id, blockedEntityDTO);
        if (blockedEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockedEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockedEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlockedEntityDTO> result = blockedEntityService.partialUpdate(blockedEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, blockedEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /blocked-entities} : get all the blockedEntities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blockedEntities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BlockedEntityDTO>> getAllBlockedEntities(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of BlockedEntities");
        Page<BlockedEntityDTO> page = blockedEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /blocked-entities/:id} : get the "id" blockedEntity.
     *
     * @param id the id of the blockedEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blockedEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlockedEntityDTO> getBlockedEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BlockedEntity : {}", id);
        Optional<BlockedEntityDTO> blockedEntityDTO = blockedEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blockedEntityDTO);
    }

    /**
     * {@code DELETE  /blocked-entities/:id} : delete the "id" blockedEntity.
     *
     * @param id the id of the blockedEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlockedEntity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BlockedEntity : {}", id);
        blockedEntityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
