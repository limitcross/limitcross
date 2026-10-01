package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalWalletRepository;
import com.limitcross.facility.service.ProfessionalWalletService;
import com.limitcross.facility.service.dto.ProfessionalWalletDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalWallet}.
 */
@RestController
@RequestMapping("/api/professional-wallets")
public class ProfessionalWalletResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalWalletResource.class);

    private static final String ENTITY_NAME = "professionalWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalWalletService professionalWalletService;

    private final ProfessionalWalletRepository professionalWalletRepository;

    public ProfessionalWalletResource(
        ProfessionalWalletService professionalWalletService,
        ProfessionalWalletRepository professionalWalletRepository
    ) {
        this.professionalWalletService = professionalWalletService;
        this.professionalWalletRepository = professionalWalletRepository;
    }

    /**
     * {@code POST  /professional-wallets} : Create a new professionalWallet.
     *
     * @param professionalWalletDTO the professionalWalletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalWalletDTO, or with status {@code 400 (Bad Request)} if the professionalWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalWalletDTO> createProfessionalWallet(@Valid @RequestBody ProfessionalWalletDTO professionalWalletDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalWallet : {}", professionalWalletDTO);
        if (professionalWalletDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalWalletDTO = professionalWalletService.save(professionalWalletDTO);
        return ResponseEntity.created(new URI("/api/professional-wallets/" + professionalWalletDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalWalletDTO.getId().toString()))
            .body(professionalWalletDTO);
    }

    /**
     * {@code PUT  /professional-wallets/:id} : Updates an existing professionalWallet.
     *
     * @param id the id of the professionalWalletDTO to save.
     * @param professionalWalletDTO the professionalWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalWalletDTO,
     * or with status {@code 400 (Bad Request)} if the professionalWalletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalWalletDTO> updateProfessionalWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalWalletDTO professionalWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalWallet : {}, {}", id, professionalWalletDTO);
        if (professionalWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalWalletDTO = professionalWalletService.update(professionalWalletDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalWalletDTO.getId().toString()))
            .body(professionalWalletDTO);
    }

    /**
     * {@code PATCH  /professional-wallets/:id} : Partial updates given fields of an existing professionalWallet, field will ignore if it is null
     *
     * @param id the id of the professionalWalletDTO to save.
     * @param professionalWalletDTO the professionalWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalWalletDTO,
     * or with status {@code 400 (Bad Request)} if the professionalWalletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalWalletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalWalletDTO> partialUpdateProfessionalWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalWalletDTO professionalWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalWallet partially : {}, {}", id, professionalWalletDTO);
        if (professionalWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalWalletDTO> result = professionalWalletService.partialUpdate(professionalWalletDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalWalletDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-wallets} : get all the professionalWallets.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalWallets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalWalletDTO>> getAllProfessionalWallets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalWallets");
        Page<ProfessionalWalletDTO> page;
        if (eagerload) {
            page = professionalWalletService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalWalletService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-wallets/:id} : get the "id" professionalWallet.
     *
     * @param id the id of the professionalWalletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalWalletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalWalletDTO> getProfessionalWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalWallet : {}", id);
        Optional<ProfessionalWalletDTO> professionalWalletDTO = professionalWalletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalWalletDTO);
    }

    /**
     * {@code DELETE  /professional-wallets/:id} : delete the "id" professionalWallet.
     *
     * @param id the id of the professionalWalletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalWallet : {}", id);
        professionalWalletService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
