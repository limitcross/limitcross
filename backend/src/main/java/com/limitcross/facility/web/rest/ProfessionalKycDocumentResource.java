package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalKycDocumentRepository;
import com.limitcross.facility.service.ProfessionalKycDocumentService;
import com.limitcross.facility.service.dto.ProfessionalKycDocumentDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalKycDocument}.
 */
@RestController
@RequestMapping("/api/professional-kyc-documents")
public class ProfessionalKycDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalKycDocumentResource.class);

    private static final String ENTITY_NAME = "professionalKycDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalKycDocumentService professionalKycDocumentService;

    private final ProfessionalKycDocumentRepository professionalKycDocumentRepository;

    public ProfessionalKycDocumentResource(
        ProfessionalKycDocumentService professionalKycDocumentService,
        ProfessionalKycDocumentRepository professionalKycDocumentRepository
    ) {
        this.professionalKycDocumentService = professionalKycDocumentService;
        this.professionalKycDocumentRepository = professionalKycDocumentRepository;
    }

    /**
     * {@code POST  /professional-kyc-documents} : Create a new professionalKycDocument.
     *
     * @param professionalKycDocumentDTO the professionalKycDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalKycDocumentDTO, or with status {@code 400 (Bad Request)} if the professionalKycDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalKycDocumentDTO> createProfessionalKycDocument(
        @Valid @RequestBody ProfessionalKycDocumentDTO professionalKycDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalKycDocument : {}", professionalKycDocumentDTO);
        if (professionalKycDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalKycDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalKycDocumentDTO = professionalKycDocumentService.save(professionalKycDocumentDTO);
        return ResponseEntity.created(new URI("/api/professional-kyc-documents/" + professionalKycDocumentDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalKycDocumentDTO.getId().toString())
            )
            .body(professionalKycDocumentDTO);
    }

    /**
     * {@code PUT  /professional-kyc-documents/:id} : Updates an existing professionalKycDocument.
     *
     * @param id the id of the professionalKycDocumentDTO to save.
     * @param professionalKycDocumentDTO the professionalKycDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalKycDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the professionalKycDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalKycDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalKycDocumentDTO> updateProfessionalKycDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalKycDocumentDTO professionalKycDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalKycDocument : {}, {}", id, professionalKycDocumentDTO);
        if (professionalKycDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalKycDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalKycDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalKycDocumentDTO = professionalKycDocumentService.update(professionalKycDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalKycDocumentDTO.getId().toString()))
            .body(professionalKycDocumentDTO);
    }

    /**
     * {@code PATCH  /professional-kyc-documents/:id} : Partial updates given fields of an existing professionalKycDocument, field will ignore if it is null
     *
     * @param id the id of the professionalKycDocumentDTO to save.
     * @param professionalKycDocumentDTO the professionalKycDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalKycDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the professionalKycDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalKycDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalKycDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalKycDocumentDTO> partialUpdateProfessionalKycDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalKycDocumentDTO professionalKycDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalKycDocument partially : {}, {}", id, professionalKycDocumentDTO);
        if (professionalKycDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalKycDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalKycDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalKycDocumentDTO> result = professionalKycDocumentService.partialUpdate(professionalKycDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalKycDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-kyc-documents} : get all the professionalKycDocuments.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalKycDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalKycDocumentDTO>> getAllProfessionalKycDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalKycDocuments");
        Page<ProfessionalKycDocumentDTO> page;
        if (eagerload) {
            page = professionalKycDocumentService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalKycDocumentService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-kyc-documents/:id} : get the "id" professionalKycDocument.
     *
     * @param id the id of the professionalKycDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalKycDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalKycDocumentDTO> getProfessionalKycDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalKycDocument : {}", id);
        Optional<ProfessionalKycDocumentDTO> professionalKycDocumentDTO = professionalKycDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalKycDocumentDTO);
    }

    /**
     * {@code DELETE  /professional-kyc-documents/:id} : delete the "id" professionalKycDocument.
     *
     * @param id the id of the professionalKycDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalKycDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalKycDocument : {}", id);
        professionalKycDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
