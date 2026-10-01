package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ProfessionalSkillRepository;
import com.limitcross.facility.service.ProfessionalSkillService;
import com.limitcross.facility.service.dto.ProfessionalSkillDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.ProfessionalSkill}.
 */
@RestController
@RequestMapping("/api/professional-skills")
public class ProfessionalSkillResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalSkillResource.class);

    private static final String ENTITY_NAME = "professionalSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalSkillService professionalSkillService;

    private final ProfessionalSkillRepository professionalSkillRepository;

    public ProfessionalSkillResource(
        ProfessionalSkillService professionalSkillService,
        ProfessionalSkillRepository professionalSkillRepository
    ) {
        this.professionalSkillService = professionalSkillService;
        this.professionalSkillRepository = professionalSkillRepository;
    }

    /**
     * {@code POST  /professional-skills} : Create a new professionalSkill.
     *
     * @param professionalSkillDTO the professionalSkillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalSkillDTO, or with status {@code 400 (Bad Request)} if the professionalSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalSkillDTO> createProfessionalSkill(@Valid @RequestBody ProfessionalSkillDTO professionalSkillDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalSkill : {}", professionalSkillDTO);
        if (professionalSkillDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalSkillDTO = professionalSkillService.save(professionalSkillDTO);
        return ResponseEntity.created(new URI("/api/professional-skills/" + professionalSkillDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, professionalSkillDTO.getId().toString()))
            .body(professionalSkillDTO);
    }

    /**
     * {@code PUT  /professional-skills/:id} : Updates an existing professionalSkill.
     *
     * @param id the id of the professionalSkillDTO to save.
     * @param professionalSkillDTO the professionalSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalSkillDTO,
     * or with status {@code 400 (Bad Request)} if the professionalSkillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalSkillDTO> updateProfessionalSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalSkillDTO professionalSkillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalSkill : {}, {}", id, professionalSkillDTO);
        if (professionalSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalSkillDTO = professionalSkillService.update(professionalSkillDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalSkillDTO.getId().toString()))
            .body(professionalSkillDTO);
    }

    /**
     * {@code PATCH  /professional-skills/:id} : Partial updates given fields of an existing professionalSkill, field will ignore if it is null
     *
     * @param id the id of the professionalSkillDTO to save.
     * @param professionalSkillDTO the professionalSkillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalSkillDTO,
     * or with status {@code 400 (Bad Request)} if the professionalSkillDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalSkillDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalSkillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalSkillDTO> partialUpdateProfessionalSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalSkillDTO professionalSkillDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalSkill partially : {}, {}", id, professionalSkillDTO);
        if (professionalSkillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalSkillDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalSkillDTO> result = professionalSkillService.partialUpdate(professionalSkillDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professionalSkillDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-skills} : get all the professionalSkills.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalSkills in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalSkillDTO>> getAllProfessionalSkills(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ProfessionalSkills");
        Page<ProfessionalSkillDTO> page;
        if (eagerload) {
            page = professionalSkillService.findAllWithEagerRelationships(pageable);
        } else {
            page = professionalSkillService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-skills/:id} : get the "id" professionalSkill.
     *
     * @param id the id of the professionalSkillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalSkillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalSkillDTO> getProfessionalSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalSkill : {}", id);
        Optional<ProfessionalSkillDTO> professionalSkillDTO = professionalSkillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalSkillDTO);
    }

    /**
     * {@code DELETE  /professional-skills/:id} : delete the "id" professionalSkill.
     *
     * @param id the id of the professionalSkillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalSkill(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalSkill : {}", id);
        professionalSkillService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
