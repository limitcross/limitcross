package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingMediaRepository;
import com.limitcross.facility.service.BookingMediaService;
import com.limitcross.facility.service.dto.BookingMediaDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingMedia}.
 */
@RestController
@RequestMapping("/api/booking-medias")
public class BookingMediaResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingMediaResource.class);

    private static final String ENTITY_NAME = "bookingMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingMediaService bookingMediaService;

    private final BookingMediaRepository bookingMediaRepository;

    public BookingMediaResource(BookingMediaService bookingMediaService, BookingMediaRepository bookingMediaRepository) {
        this.bookingMediaService = bookingMediaService;
        this.bookingMediaRepository = bookingMediaRepository;
    }

    /**
     * {@code POST  /booking-medias} : Create a new bookingMedia.
     *
     * @param bookingMediaDTO the bookingMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingMediaDTO, or with status {@code 400 (Bad Request)} if the bookingMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingMediaDTO> createBookingMedia(@Valid @RequestBody BookingMediaDTO bookingMediaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingMedia : {}", bookingMediaDTO);
        if (bookingMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingMediaDTO = bookingMediaService.save(bookingMediaDTO);
        return ResponseEntity.created(new URI("/api/booking-medias/" + bookingMediaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingMediaDTO.getId().toString()))
            .body(bookingMediaDTO);
    }

    /**
     * {@code PUT  /booking-medias/:id} : Updates an existing bookingMedia.
     *
     * @param id the id of the bookingMediaDTO to save.
     * @param bookingMediaDTO the bookingMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingMediaDTO,
     * or with status {@code 400 (Bad Request)} if the bookingMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingMediaDTO> updateBookingMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingMediaDTO bookingMediaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingMedia : {}, {}", id, bookingMediaDTO);
        if (bookingMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingMediaDTO = bookingMediaService.update(bookingMediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingMediaDTO.getId().toString()))
            .body(bookingMediaDTO);
    }

    /**
     * {@code PATCH  /booking-medias/:id} : Partial updates given fields of an existing bookingMedia, field will ignore if it is null
     *
     * @param id the id of the bookingMediaDTO to save.
     * @param bookingMediaDTO the bookingMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingMediaDTO,
     * or with status {@code 400 (Bad Request)} if the bookingMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingMediaDTO> partialUpdateBookingMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingMediaDTO bookingMediaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingMedia partially : {}, {}", id, bookingMediaDTO);
        if (bookingMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingMediaDTO> result = bookingMediaService.partialUpdate(bookingMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-medias} : get all the bookingMedias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingMediaDTO>> getAllBookingMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingMedias");
        Page<BookingMediaDTO> page;
        if (eagerload) {
            page = bookingMediaService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingMediaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-medias/:id} : get the "id" bookingMedia.
     *
     * @param id the id of the bookingMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingMediaDTO> getBookingMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingMedia : {}", id);
        Optional<BookingMediaDTO> bookingMediaDTO = bookingMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingMediaDTO);
    }

    /**
     * {@code DELETE  /booking-medias/:id} : delete the "id" bookingMedia.
     *
     * @param id the id of the bookingMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingMedia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingMedia : {}", id);
        bookingMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
