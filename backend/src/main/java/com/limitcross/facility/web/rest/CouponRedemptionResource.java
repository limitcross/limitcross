package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CouponRedemptionRepository;
import com.limitcross.facility.service.CouponRedemptionService;
import com.limitcross.facility.service.dto.CouponRedemptionDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CouponRedemption}.
 */
@RestController
@RequestMapping("/api/coupon-redemptions")
public class CouponRedemptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CouponRedemptionResource.class);

    private static final String ENTITY_NAME = "couponRedemption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CouponRedemptionService couponRedemptionService;

    private final CouponRedemptionRepository couponRedemptionRepository;

    public CouponRedemptionResource(
        CouponRedemptionService couponRedemptionService,
        CouponRedemptionRepository couponRedemptionRepository
    ) {
        this.couponRedemptionService = couponRedemptionService;
        this.couponRedemptionRepository = couponRedemptionRepository;
    }

    /**
     * {@code POST  /coupon-redemptions} : Create a new couponRedemption.
     *
     * @param couponRedemptionDTO the couponRedemptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new couponRedemptionDTO, or with status {@code 400 (Bad Request)} if the couponRedemption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CouponRedemptionDTO> createCouponRedemption(@Valid @RequestBody CouponRedemptionDTO couponRedemptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CouponRedemption : {}", couponRedemptionDTO);
        if (couponRedemptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new couponRedemption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        couponRedemptionDTO = couponRedemptionService.save(couponRedemptionDTO);
        return ResponseEntity.created(new URI("/api/coupon-redemptions/" + couponRedemptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, couponRedemptionDTO.getId().toString()))
            .body(couponRedemptionDTO);
    }

    /**
     * {@code PUT  /coupon-redemptions/:id} : Updates an existing couponRedemption.
     *
     * @param id the id of the couponRedemptionDTO to save.
     * @param couponRedemptionDTO the couponRedemptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponRedemptionDTO,
     * or with status {@code 400 (Bad Request)} if the couponRedemptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the couponRedemptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CouponRedemptionDTO> updateCouponRedemption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CouponRedemptionDTO couponRedemptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CouponRedemption : {}, {}", id, couponRedemptionDTO);
        if (couponRedemptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponRedemptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponRedemptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        couponRedemptionDTO = couponRedemptionService.update(couponRedemptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponRedemptionDTO.getId().toString()))
            .body(couponRedemptionDTO);
    }

    /**
     * {@code PATCH  /coupon-redemptions/:id} : Partial updates given fields of an existing couponRedemption, field will ignore if it is null
     *
     * @param id the id of the couponRedemptionDTO to save.
     * @param couponRedemptionDTO the couponRedemptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couponRedemptionDTO,
     * or with status {@code 400 (Bad Request)} if the couponRedemptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the couponRedemptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the couponRedemptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CouponRedemptionDTO> partialUpdateCouponRedemption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CouponRedemptionDTO couponRedemptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CouponRedemption partially : {}, {}", id, couponRedemptionDTO);
        if (couponRedemptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, couponRedemptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!couponRedemptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CouponRedemptionDTO> result = couponRedemptionService.partialUpdate(couponRedemptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couponRedemptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coupon-redemptions} : get all the couponRedemptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couponRedemptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CouponRedemptionDTO>> getAllCouponRedemptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CouponRedemptions");
        Page<CouponRedemptionDTO> page;
        if (eagerload) {
            page = couponRedemptionService.findAllWithEagerRelationships(pageable);
        } else {
            page = couponRedemptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coupon-redemptions/:id} : get the "id" couponRedemption.
     *
     * @param id the id of the couponRedemptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the couponRedemptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CouponRedemptionDTO> getCouponRedemption(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CouponRedemption : {}", id);
        Optional<CouponRedemptionDTO> couponRedemptionDTO = couponRedemptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(couponRedemptionDTO);
    }

    /**
     * {@code DELETE  /coupon-redemptions/:id} : delete the "id" couponRedemption.
     *
     * @param id the id of the couponRedemptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouponRedemption(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CouponRedemption : {}", id);
        couponRedemptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
