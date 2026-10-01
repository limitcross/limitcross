package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CityPackagePriceRepository;
import com.limitcross.facility.service.CityPackagePriceService;
import com.limitcross.facility.service.dto.CityPackagePriceDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CityPackagePrice}.
 */
@RestController
@RequestMapping("/api/city-package-prices")
public class CityPackagePriceResource {

    private static final Logger LOG = LoggerFactory.getLogger(CityPackagePriceResource.class);

    private static final String ENTITY_NAME = "cityPackagePrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityPackagePriceService cityPackagePriceService;

    private final CityPackagePriceRepository cityPackagePriceRepository;

    public CityPackagePriceResource(
        CityPackagePriceService cityPackagePriceService,
        CityPackagePriceRepository cityPackagePriceRepository
    ) {
        this.cityPackagePriceService = cityPackagePriceService;
        this.cityPackagePriceRepository = cityPackagePriceRepository;
    }

    /**
     * {@code POST  /city-package-prices} : Create a new cityPackagePrice.
     *
     * @param cityPackagePriceDTO the cityPackagePriceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cityPackagePriceDTO, or with status {@code 400 (Bad Request)} if the cityPackagePrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CityPackagePriceDTO> createCityPackagePrice(@Valid @RequestBody CityPackagePriceDTO cityPackagePriceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CityPackagePrice : {}", cityPackagePriceDTO);
        if (cityPackagePriceDTO.getId() != null) {
            throw new BadRequestAlertException("A new cityPackagePrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cityPackagePriceDTO = cityPackagePriceService.save(cityPackagePriceDTO);
        return ResponseEntity.created(new URI("/api/city-package-prices/" + cityPackagePriceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cityPackagePriceDTO.getId().toString()))
            .body(cityPackagePriceDTO);
    }

    /**
     * {@code PUT  /city-package-prices/:id} : Updates an existing cityPackagePrice.
     *
     * @param id the id of the cityPackagePriceDTO to save.
     * @param cityPackagePriceDTO the cityPackagePriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityPackagePriceDTO,
     * or with status {@code 400 (Bad Request)} if the cityPackagePriceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cityPackagePriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityPackagePriceDTO> updateCityPackagePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CityPackagePriceDTO cityPackagePriceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CityPackagePrice : {}, {}", id, cityPackagePriceDTO);
        if (cityPackagePriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityPackagePriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityPackagePriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cityPackagePriceDTO = cityPackagePriceService.update(cityPackagePriceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cityPackagePriceDTO.getId().toString()))
            .body(cityPackagePriceDTO);
    }

    /**
     * {@code PATCH  /city-package-prices/:id} : Partial updates given fields of an existing cityPackagePrice, field will ignore if it is null
     *
     * @param id the id of the cityPackagePriceDTO to save.
     * @param cityPackagePriceDTO the cityPackagePriceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityPackagePriceDTO,
     * or with status {@code 400 (Bad Request)} if the cityPackagePriceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cityPackagePriceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cityPackagePriceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CityPackagePriceDTO> partialUpdateCityPackagePrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CityPackagePriceDTO cityPackagePriceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CityPackagePrice partially : {}, {}", id, cityPackagePriceDTO);
        if (cityPackagePriceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityPackagePriceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityPackagePriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CityPackagePriceDTO> result = cityPackagePriceService.partialUpdate(cityPackagePriceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cityPackagePriceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /city-package-prices} : get all the cityPackagePrices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cityPackagePrices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CityPackagePriceDTO>> getAllCityPackagePrices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CityPackagePrices");
        Page<CityPackagePriceDTO> page;
        if (eagerload) {
            page = cityPackagePriceService.findAllWithEagerRelationships(pageable);
        } else {
            page = cityPackagePriceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /city-package-prices/:id} : get the "id" cityPackagePrice.
     *
     * @param id the id of the cityPackagePriceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cityPackagePriceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityPackagePriceDTO> getCityPackagePrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CityPackagePrice : {}", id);
        Optional<CityPackagePriceDTO> cityPackagePriceDTO = cityPackagePriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cityPackagePriceDTO);
    }

    /**
     * {@code DELETE  /city-package-prices/:id} : delete the "id" cityPackagePrice.
     *
     * @param id the id of the cityPackagePriceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityPackagePrice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CityPackagePrice : {}", id);
        cityPackagePriceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
