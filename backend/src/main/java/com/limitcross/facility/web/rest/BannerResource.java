package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BannerRepository;
import com.limitcross.facility.service.BannerService;
import com.limitcross.facility.service.dto.BannerDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.Banner}.
 */
@RestController
@RequestMapping("/api/banners")
public class BannerResource {

    private static final Logger LOG = LoggerFactory.getLogger(BannerResource.class);

    private static final String ENTITY_NAME = "banner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BannerService bannerService;

    private final BannerRepository bannerRepository;

    public BannerResource(BannerService bannerService, BannerRepository bannerRepository) {
        this.bannerService = bannerService;
        this.bannerRepository = bannerRepository;
    }

    /**
     * {@code POST  /banners} : Create a new banner.
     *
     * @param bannerDTO the bannerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bannerDTO, or with status {@code 400 (Bad Request)} if the banner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BannerDTO> createBanner(@Valid @RequestBody BannerDTO bannerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Banner : {}", bannerDTO);
        if (bannerDTO.getId() != null) {
            throw new BadRequestAlertException("A new banner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bannerDTO = bannerService.save(bannerDTO);
        return ResponseEntity.created(new URI("/api/banners/" + bannerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bannerDTO.getId().toString()))
            .body(bannerDTO);
    }

    /**
     * {@code PUT  /banners/:id} : Updates an existing banner.
     *
     * @param id the id of the bannerDTO to save.
     * @param bannerDTO the bannerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bannerDTO,
     * or with status {@code 400 (Bad Request)} if the bannerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bannerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BannerDTO bannerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Banner : {}, {}", id, bannerDTO);
        if (bannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bannerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bannerDTO = bannerService.update(bannerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bannerDTO.getId().toString()))
            .body(bannerDTO);
    }

    /**
     * {@code PATCH  /banners/:id} : Partial updates given fields of an existing banner, field will ignore if it is null
     *
     * @param id the id of the bannerDTO to save.
     * @param bannerDTO the bannerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bannerDTO,
     * or with status {@code 400 (Bad Request)} if the bannerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bannerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bannerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BannerDTO> partialUpdateBanner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BannerDTO bannerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Banner partially : {}, {}", id, bannerDTO);
        if (bannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bannerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BannerDTO> result = bannerService.partialUpdate(bannerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bannerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /banners} : get all the banners.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BannerDTO>> getAllBanners(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Banners");
        Page<BannerDTO> page;
        if (eagerload) {
            page = bannerService.findAllWithEagerRelationships(pageable);
        } else {
            page = bannerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banners/:id} : get the "id" banner.
     *
     * @param id the id of the bannerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bannerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BannerDTO> getBanner(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Banner : {}", id);
        Optional<BannerDTO> bannerDTO = bannerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bannerDTO);
    }

    /**
     * {@code DELETE  /banners/:id} : delete the "id" banner.
     *
     * @param id the id of the bannerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Banner : {}", id);
        bannerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
