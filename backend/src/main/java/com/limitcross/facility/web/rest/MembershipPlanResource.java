package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.MembershipPlanRepository;
import com.limitcross.facility.service.MembershipPlanService;
import com.limitcross.facility.service.dto.MembershipPlanDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.MembershipPlan}.
 */
@RestController
@RequestMapping("/api/membership-plans")
public class MembershipPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipPlanResource.class);

    private static final String ENTITY_NAME = "membershipPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipPlanService membershipPlanService;

    private final MembershipPlanRepository membershipPlanRepository;

    public MembershipPlanResource(MembershipPlanService membershipPlanService, MembershipPlanRepository membershipPlanRepository) {
        this.membershipPlanService = membershipPlanService;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    /**
     * {@code POST  /membership-plans} : Create a new membershipPlan.
     *
     * @param membershipPlanDTO the membershipPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipPlanDTO, or with status {@code 400 (Bad Request)} if the membershipPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MembershipPlanDTO> createMembershipPlan(@Valid @RequestBody MembershipPlanDTO membershipPlanDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MembershipPlan : {}", membershipPlanDTO);
        if (membershipPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        membershipPlanDTO = membershipPlanService.save(membershipPlanDTO);
        return ResponseEntity.created(new URI("/api/membership-plans/" + membershipPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, membershipPlanDTO.getId().toString()))
            .body(membershipPlanDTO);
    }

    /**
     * {@code PUT  /membership-plans/:id} : Updates an existing membershipPlan.
     *
     * @param id the id of the membershipPlanDTO to save.
     * @param membershipPlanDTO the membershipPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipPlanDTO,
     * or with status {@code 400 (Bad Request)} if the membershipPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MembershipPlanDTO> updateMembershipPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MembershipPlanDTO membershipPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MembershipPlan : {}, {}", id, membershipPlanDTO);
        if (membershipPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        membershipPlanDTO = membershipPlanService.update(membershipPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipPlanDTO.getId().toString()))
            .body(membershipPlanDTO);
    }

    /**
     * {@code PATCH  /membership-plans/:id} : Partial updates given fields of an existing membershipPlan, field will ignore if it is null
     *
     * @param id the id of the membershipPlanDTO to save.
     * @param membershipPlanDTO the membershipPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipPlanDTO,
     * or with status {@code 400 (Bad Request)} if the membershipPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MembershipPlanDTO> partialUpdateMembershipPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MembershipPlanDTO membershipPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MembershipPlan partially : {}, {}", id, membershipPlanDTO);
        if (membershipPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembershipPlanDTO> result = membershipPlanService.partialUpdate(membershipPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-plans} : get all the membershipPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipPlans in body.
     */
    @GetMapping("")
    public List<MembershipPlanDTO> getAllMembershipPlans() {
        LOG.debug("REST request to get all MembershipPlans");
        return membershipPlanService.findAll();
    }

    /**
     * {@code GET  /membership-plans/:id} : get the "id" membershipPlan.
     *
     * @param id the id of the membershipPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MembershipPlanDTO> getMembershipPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MembershipPlan : {}", id);
        Optional<MembershipPlanDTO> membershipPlanDTO = membershipPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipPlanDTO);
    }

    /**
     * {@code DELETE  /membership-plans/:id} : delete the "id" membershipPlan.
     *
     * @param id the id of the membershipPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MembershipPlan : {}", id);
        membershipPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
