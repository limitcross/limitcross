package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ReferralRewardRepository;
import com.limitcross.facility.service.ReferralRewardService;
import com.limitcross.facility.service.dto.ReferralRewardDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ReferralReward}.
 */
@RestController
@RequestMapping("/api/referral-rewards")
public class ReferralRewardResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReferralRewardResource.class);

    private static final String ENTITY_NAME = "referralReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferralRewardService referralRewardService;

    private final ReferralRewardRepository referralRewardRepository;

    public ReferralRewardResource(ReferralRewardService referralRewardService, ReferralRewardRepository referralRewardRepository) {
        this.referralRewardService = referralRewardService;
        this.referralRewardRepository = referralRewardRepository;
    }

    /**
     * {@code POST  /referral-rewards} : Create a new referralReward.
     *
     * @param referralRewardDTO the referralRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referralRewardDTO, or with status {@code 400 (Bad Request)} if the referralReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferralRewardDTO> createReferralReward(@Valid @RequestBody ReferralRewardDTO referralRewardDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReferralReward : {}", referralRewardDTO);
        if (referralRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new referralReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referralRewardDTO = referralRewardService.save(referralRewardDTO);
        return ResponseEntity.created(new URI("/api/referral-rewards/" + referralRewardDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, referralRewardDTO.getId().toString()))
            .body(referralRewardDTO);
    }

    /**
     * {@code PUT  /referral-rewards/:id} : Updates an existing referralReward.
     *
     * @param id the id of the referralRewardDTO to save.
     * @param referralRewardDTO the referralRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralRewardDTO,
     * or with status {@code 400 (Bad Request)} if the referralRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referralRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferralRewardDTO> updateReferralReward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReferralRewardDTO referralRewardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReferralReward : {}, {}", id, referralRewardDTO);
        if (referralRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referralRewardDTO = referralRewardService.update(referralRewardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referralRewardDTO.getId().toString()))
            .body(referralRewardDTO);
    }

    /**
     * {@code PATCH  /referral-rewards/:id} : Partial updates given fields of an existing referralReward, field will ignore if it is null
     *
     * @param id the id of the referralRewardDTO to save.
     * @param referralRewardDTO the referralRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralRewardDTO,
     * or with status {@code 400 (Bad Request)} if the referralRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the referralRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the referralRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferralRewardDTO> partialUpdateReferralReward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReferralRewardDTO referralRewardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReferralReward partially : {}, {}", id, referralRewardDTO);
        if (referralRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referralRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referralRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferralRewardDTO> result = referralRewardService.partialUpdate(referralRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referralRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /referral-rewards} : get all the referralRewards.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referralRewards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReferralRewardDTO>> getAllReferralRewards(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ReferralRewards");
        Page<ReferralRewardDTO> page;
        if (eagerload) {
            page = referralRewardService.findAllWithEagerRelationships(pageable);
        } else {
            page = referralRewardService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referral-rewards/:id} : get the "id" referralReward.
     *
     * @param id the id of the referralRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referralRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferralRewardDTO> getReferralReward(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReferralReward : {}", id);
        Optional<ReferralRewardDTO> referralRewardDTO = referralRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referralRewardDTO);
    }

    /**
     * {@code DELETE  /referral-rewards/:id} : delete the "id" referralReward.
     *
     * @param id the id of the referralRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferralReward(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReferralReward : {}", id);
        referralRewardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
