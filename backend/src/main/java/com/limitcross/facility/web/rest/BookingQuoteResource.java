package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingQuoteRepository;
import com.limitcross.facility.service.BookingQuoteService;
import com.limitcross.facility.service.dto.BookingQuoteDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingQuote}.
 */
@RestController
@RequestMapping("/api/booking-quotes")
public class BookingQuoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQuoteResource.class);

    private static final String ENTITY_NAME = "bookingQuote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingQuoteService bookingQuoteService;

    private final BookingQuoteRepository bookingQuoteRepository;

    public BookingQuoteResource(BookingQuoteService bookingQuoteService, BookingQuoteRepository bookingQuoteRepository) {
        this.bookingQuoteService = bookingQuoteService;
        this.bookingQuoteRepository = bookingQuoteRepository;
    }

    /**
     * {@code POST  /booking-quotes} : Create a new bookingQuote.
     *
     * @param bookingQuoteDTO the bookingQuoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingQuoteDTO, or with status {@code 400 (Bad Request)} if the bookingQuote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingQuoteDTO> createBookingQuote(@Valid @RequestBody BookingQuoteDTO bookingQuoteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingQuote : {}", bookingQuoteDTO);
        if (bookingQuoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingQuote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingQuoteDTO = bookingQuoteService.save(bookingQuoteDTO);
        return ResponseEntity.created(new URI("/api/booking-quotes/" + bookingQuoteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingQuoteDTO.getId().toString()))
            .body(bookingQuoteDTO);
    }

    /**
     * {@code PUT  /booking-quotes/:id} : Updates an existing bookingQuote.
     *
     * @param id the id of the bookingQuoteDTO to save.
     * @param bookingQuoteDTO the bookingQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the bookingQuoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingQuoteDTO> updateBookingQuote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingQuoteDTO bookingQuoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingQuote : {}, {}", id, bookingQuoteDTO);
        if (bookingQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingQuoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingQuoteDTO = bookingQuoteService.update(bookingQuoteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingQuoteDTO.getId().toString()))
            .body(bookingQuoteDTO);
    }

    /**
     * {@code PATCH  /booking-quotes/:id} : Partial updates given fields of an existing bookingQuote, field will ignore if it is null
     *
     * @param id the id of the bookingQuoteDTO to save.
     * @param bookingQuoteDTO the bookingQuoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingQuoteDTO,
     * or with status {@code 400 (Bad Request)} if the bookingQuoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingQuoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingQuoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingQuoteDTO> partialUpdateBookingQuote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingQuoteDTO bookingQuoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingQuote partially : {}, {}", id, bookingQuoteDTO);
        if (bookingQuoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingQuoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingQuoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingQuoteDTO> result = bookingQuoteService.partialUpdate(bookingQuoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingQuoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-quotes} : get all the bookingQuotes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingQuotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingQuoteDTO>> getAllBookingQuotes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingQuotes");
        Page<BookingQuoteDTO> page;
        if (eagerload) {
            page = bookingQuoteService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingQuoteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-quotes/:id} : get the "id" bookingQuote.
     *
     * @param id the id of the bookingQuoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingQuoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingQuoteDTO> getBookingQuote(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingQuote : {}", id);
        Optional<BookingQuoteDTO> bookingQuoteDTO = bookingQuoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingQuoteDTO);
    }

    /**
     * {@code DELETE  /booking-quotes/:id} : delete the "id" bookingQuote.
     *
     * @param id the id of the bookingQuoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingQuote(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingQuote : {}", id);
        bookingQuoteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
