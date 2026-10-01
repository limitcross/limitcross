package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.SlotHoldRepository;
import com.limitcross.facility.service.SlotHoldService;
import com.limitcross.facility.service.dto.SlotHoldDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.SlotHold}.
 */
@RestController
@RequestMapping("/api/slot-holds")
public class SlotHoldResource {

    private static final Logger LOG = LoggerFactory.getLogger(SlotHoldResource.class);

    private static final String ENTITY_NAME = "slotHold";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlotHoldService slotHoldService;

    private final SlotHoldRepository slotHoldRepository;

    public SlotHoldResource(SlotHoldService slotHoldService, SlotHoldRepository slotHoldRepository) {
        this.slotHoldService = slotHoldService;
        this.slotHoldRepository = slotHoldRepository;
    }

    /**
     * {@code POST  /slot-holds} : Create a new slotHold.
     *
     * @param slotHoldDTO the slotHoldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slotHoldDTO, or with status {@code 400 (Bad Request)} if the slotHold has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SlotHoldDTO> createSlotHold(@Valid @RequestBody SlotHoldDTO slotHoldDTO) throws URISyntaxException {
        LOG.debug("REST request to save SlotHold : {}", slotHoldDTO);
        if (slotHoldDTO.getId() != null) {
            throw new BadRequestAlertException("A new slotHold cannot already have an ID", ENTITY_NAME, "idexists");
        }
        slotHoldDTO = slotHoldService.save(slotHoldDTO);
        return ResponseEntity.created(new URI("/api/slot-holds/" + slotHoldDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, slotHoldDTO.getId().toString()))
            .body(slotHoldDTO);
    }

    /**
     * {@code PUT  /slot-holds/:id} : Updates an existing slotHold.
     *
     * @param id the id of the slotHoldDTO to save.
     * @param slotHoldDTO the slotHoldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotHoldDTO,
     * or with status {@code 400 (Bad Request)} if the slotHoldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slotHoldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SlotHoldDTO> updateSlotHold(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SlotHoldDTO slotHoldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SlotHold : {}, {}", id, slotHoldDTO);
        if (slotHoldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotHoldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotHoldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        slotHoldDTO = slotHoldService.update(slotHoldDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotHoldDTO.getId().toString()))
            .body(slotHoldDTO);
    }

    /**
     * {@code PATCH  /slot-holds/:id} : Partial updates given fields of an existing slotHold, field will ignore if it is null
     *
     * @param id the id of the slotHoldDTO to save.
     * @param slotHoldDTO the slotHoldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotHoldDTO,
     * or with status {@code 400 (Bad Request)} if the slotHoldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the slotHoldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the slotHoldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SlotHoldDTO> partialUpdateSlotHold(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SlotHoldDTO slotHoldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SlotHold partially : {}, {}", id, slotHoldDTO);
        if (slotHoldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotHoldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotHoldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SlotHoldDTO> result = slotHoldService.partialUpdate(slotHoldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotHoldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /slot-holds} : get all the slotHolds.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slotHolds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SlotHoldDTO>> getAllSlotHolds(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SlotHolds");
        Page<SlotHoldDTO> page;
        if (eagerload) {
            page = slotHoldService.findAllWithEagerRelationships(pageable);
        } else {
            page = slotHoldService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /slot-holds/:id} : get the "id" slotHold.
     *
     * @param id the id of the slotHoldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slotHoldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SlotHoldDTO> getSlotHold(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SlotHold : {}", id);
        Optional<SlotHoldDTO> slotHoldDTO = slotHoldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slotHoldDTO);
    }

    /**
     * {@code DELETE  /slot-holds/:id} : delete the "id" slotHold.
     *
     * @param id the id of the slotHoldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlotHold(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SlotHold : {}", id);
        slotHoldService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
