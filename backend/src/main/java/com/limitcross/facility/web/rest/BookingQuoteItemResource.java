package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingQuoteItemRepository;
import com.limitcross.facility.service.BookingQuoteItemService;
import com.limitcross.facility.service.dto.BookingQuoteItemDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingQuoteItem}.
 */
@RestController
@RequestMapping("/api/booking-quote-items")
public class BookingQuoteItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQuoteItemResource.class);

    private static final String ENTITY_NAME = "bookingQuoteItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingQuoteItemService bookingQuoteItemService;

    private final BookingQuoteItemRepository bookingQuoteItemRepository;

    public BookingQuoteItemResource(
        BookingQuoteItemService bookingQuoteItemService,
        BookingQuoteItemRepository bookingQuoteItemRepository
    ) {
        this.bookingQuoteItemService = bookingQuoteItemService;
        this.bookingQuoteItemRepository = bookingQuoteItemRepository;
    }

    /**
     * {@code POST  /booking-quote-items} : Create a new bookingQuoteItem.
     *
     * @param bookingQuoteItemDTO the bookingQuoteItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingQuoteItemDTO, or with status {@code 400 (Bad Request)} if the bookingQuoteItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingQuoteItemDTO> createBookingQuoteItem(@Valid @RequestBody BookingQuoteItemDTO bookingQuoteItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingQuoteItem : {}", bookingQuoteItemDTO);
        if (bookingQuoteItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingQuoteItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingQuoteItemDTO = bookingQuoteItemService.save(bookingQuoteItemDTO);
        return ResponseEntity.created(new URI("/api/booking-quote-items/" + bookingQuoteItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingQuoteItemDTO.getId().toString()))
            .body(bookingQuoteItemDTO);
    }

    /**
     * {@code PUT  /booking-quote-items/:id} : Updates an existing bookingQuoteItem.
     *
     * @param id the id of the bookingQuoteItemDTO to save.
     * @param bookingQuoteItemDTO the bookingQuoteItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingQuoteItemDTO,
     * or with status {@code 400 (Bad Request)} if the bookingQuoteItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingQuoteItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingQuoteItemDTO> updateBookingQuoteItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingQuoteItemDTO bookingQuoteItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingQuoteItem : {}, {}", id, bookingQuoteItemDTO);
        if (bookingQuoteItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingQuoteItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingQuoteItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingQuoteItemDTO = bookingQuoteItemService.update(bookingQuoteItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingQuoteItemDTO.getId().toString()))
            .body(bookingQuoteItemDTO);
    }

    /**
     * {@code PATCH  /booking-quote-items/:id} : Partial updates given fields of an existing bookingQuoteItem, field will ignore if it is null
     *
     * @param id the id of the bookingQuoteItemDTO to save.
     * @param bookingQuoteItemDTO the bookingQuoteItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingQuoteItemDTO,
     * or with status {@code 400 (Bad Request)} if the bookingQuoteItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingQuoteItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingQuoteItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingQuoteItemDTO> partialUpdateBookingQuoteItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingQuoteItemDTO bookingQuoteItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingQuoteItem partially : {}, {}", id, bookingQuoteItemDTO);
        if (bookingQuoteItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingQuoteItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingQuoteItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingQuoteItemDTO> result = bookingQuoteItemService.partialUpdate(bookingQuoteItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingQuoteItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-quote-items} : get all the bookingQuoteItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingQuoteItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingQuoteItemDTO>> getAllBookingQuoteItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of BookingQuoteItems");
        Page<BookingQuoteItemDTO> page = bookingQuoteItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-quote-items/:id} : get the "id" bookingQuoteItem.
     *
     * @param id the id of the bookingQuoteItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingQuoteItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingQuoteItemDTO> getBookingQuoteItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingQuoteItem : {}", id);
        Optional<BookingQuoteItemDTO> bookingQuoteItemDTO = bookingQuoteItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingQuoteItemDTO);
    }

    /**
     * {@code DELETE  /booking-quote-items/:id} : delete the "id" bookingQuoteItem.
     *
     * @param id the id of the bookingQuoteItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingQuoteItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingQuoteItem : {}", id);
        bookingQuoteItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
