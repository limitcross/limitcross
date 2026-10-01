package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.SearchKeywordRepository;
import com.limitcross.facility.service.SearchKeywordService;
import com.limitcross.facility.service.dto.SearchKeywordDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.SearchKeyword}.
 */
@RestController
@RequestMapping("/api/search-keywords")
public class SearchKeywordResource {

    private static final Logger LOG = LoggerFactory.getLogger(SearchKeywordResource.class);

    private static final String ENTITY_NAME = "searchKeyword";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchKeywordService searchKeywordService;

    private final SearchKeywordRepository searchKeywordRepository;

    public SearchKeywordResource(SearchKeywordService searchKeywordService, SearchKeywordRepository searchKeywordRepository) {
        this.searchKeywordService = searchKeywordService;
        this.searchKeywordRepository = searchKeywordRepository;
    }

    /**
     * {@code POST  /search-keywords} : Create a new searchKeyword.
     *
     * @param searchKeywordDTO the searchKeywordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchKeywordDTO, or with status {@code 400 (Bad Request)} if the searchKeyword has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SearchKeywordDTO> createSearchKeyword(@Valid @RequestBody SearchKeywordDTO searchKeywordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SearchKeyword : {}", searchKeywordDTO);
        if (searchKeywordDTO.getId() != null) {
            throw new BadRequestAlertException("A new searchKeyword cannot already have an ID", ENTITY_NAME, "idexists");
        }
        searchKeywordDTO = searchKeywordService.save(searchKeywordDTO);
        return ResponseEntity.created(new URI("/api/search-keywords/" + searchKeywordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, searchKeywordDTO.getId().toString()))
            .body(searchKeywordDTO);
    }

    /**
     * {@code PUT  /search-keywords/:id} : Updates an existing searchKeyword.
     *
     * @param id the id of the searchKeywordDTO to save.
     * @param searchKeywordDTO the searchKeywordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchKeywordDTO,
     * or with status {@code 400 (Bad Request)} if the searchKeywordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchKeywordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SearchKeywordDTO> updateSearchKeyword(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SearchKeywordDTO searchKeywordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SearchKeyword : {}, {}", id, searchKeywordDTO);
        if (searchKeywordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchKeywordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchKeywordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        searchKeywordDTO = searchKeywordService.update(searchKeywordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchKeywordDTO.getId().toString()))
            .body(searchKeywordDTO);
    }

    /**
     * {@code PATCH  /search-keywords/:id} : Partial updates given fields of an existing searchKeyword, field will ignore if it is null
     *
     * @param id the id of the searchKeywordDTO to save.
     * @param searchKeywordDTO the searchKeywordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchKeywordDTO,
     * or with status {@code 400 (Bad Request)} if the searchKeywordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the searchKeywordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the searchKeywordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SearchKeywordDTO> partialUpdateSearchKeyword(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SearchKeywordDTO searchKeywordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SearchKeyword partially : {}, {}", id, searchKeywordDTO);
        if (searchKeywordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, searchKeywordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!searchKeywordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SearchKeywordDTO> result = searchKeywordService.partialUpdate(searchKeywordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, searchKeywordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /search-keywords} : get all the searchKeywords.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchKeywords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SearchKeywordDTO>> getAllSearchKeywords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SearchKeywords");
        Page<SearchKeywordDTO> page;
        if (eagerload) {
            page = searchKeywordService.findAllWithEagerRelationships(pageable);
        } else {
            page = searchKeywordService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-keywords/:id} : get the "id" searchKeyword.
     *
     * @param id the id of the searchKeywordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchKeywordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SearchKeywordDTO> getSearchKeyword(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SearchKeyword : {}", id);
        Optional<SearchKeywordDTO> searchKeywordDTO = searchKeywordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchKeywordDTO);
    }

    /**
     * {@code DELETE  /search-keywords/:id} : delete the "id" searchKeyword.
     *
     * @param id the id of the searchKeywordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchKeyword(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SearchKeyword : {}", id);
        searchKeywordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
