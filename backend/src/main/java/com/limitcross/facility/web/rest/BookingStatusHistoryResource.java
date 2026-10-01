package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingStatusHistoryRepository;
import com.limitcross.facility.service.BookingStatusHistoryService;
import com.limitcross.facility.service.dto.BookingStatusHistoryDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingStatusHistory}.
 */
@RestController
@RequestMapping("/api/booking-status-histories")
public class BookingStatusHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingStatusHistoryResource.class);

    private static final String ENTITY_NAME = "bookingStatusHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingStatusHistoryService bookingStatusHistoryService;

    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    public BookingStatusHistoryResource(
        BookingStatusHistoryService bookingStatusHistoryService,
        BookingStatusHistoryRepository bookingStatusHistoryRepository
    ) {
        this.bookingStatusHistoryService = bookingStatusHistoryService;
        this.bookingStatusHistoryRepository = bookingStatusHistoryRepository;
    }

    /**
     * {@code POST  /booking-status-histories} : Create a new bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the bookingStatusHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingStatusHistoryDTO, or with status {@code 400 (Bad Request)} if the bookingStatusHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingStatusHistoryDTO> createBookingStatusHistory(
        @Valid @RequestBody BookingStatusHistoryDTO bookingStatusHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save BookingStatusHistory : {}", bookingStatusHistoryDTO);
        if (bookingStatusHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingStatusHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingStatusHistoryDTO = bookingStatusHistoryService.save(bookingStatusHistoryDTO);
        return ResponseEntity.created(new URI("/api/booking-status-histories/" + bookingStatusHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingStatusHistoryDTO.getId().toString()))
            .body(bookingStatusHistoryDTO);
    }

    /**
     * {@code PUT  /booking-status-histories/:id} : Updates an existing bookingStatusHistory.
     *
     * @param id the id of the bookingStatusHistoryDTO to save.
     * @param bookingStatusHistoryDTO the bookingStatusHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingStatusHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the bookingStatusHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingStatusHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingStatusHistoryDTO> updateBookingStatusHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingStatusHistoryDTO bookingStatusHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingStatusHistory : {}, {}", id, bookingStatusHistoryDTO);
        if (bookingStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingStatusHistoryDTO = bookingStatusHistoryService.update(bookingStatusHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingStatusHistoryDTO.getId().toString()))
            .body(bookingStatusHistoryDTO);
    }

    /**
     * {@code PATCH  /booking-status-histories/:id} : Partial updates given fields of an existing bookingStatusHistory, field will ignore if it is null
     *
     * @param id the id of the bookingStatusHistoryDTO to save.
     * @param bookingStatusHistoryDTO the bookingStatusHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingStatusHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the bookingStatusHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingStatusHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingStatusHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingStatusHistoryDTO> partialUpdateBookingStatusHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingStatusHistoryDTO bookingStatusHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingStatusHistory partially : {}, {}", id, bookingStatusHistoryDTO);
        if (bookingStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingStatusHistoryDTO> result = bookingStatusHistoryService.partialUpdate(bookingStatusHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingStatusHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-status-histories} : get all the bookingStatusHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingStatusHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingStatusHistoryDTO>> getAllBookingStatusHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingStatusHistories");
        Page<BookingStatusHistoryDTO> page;
        if (eagerload) {
            page = bookingStatusHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingStatusHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-status-histories/:id} : get the "id" bookingStatusHistory.
     *
     * @param id the id of the bookingStatusHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingStatusHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingStatusHistoryDTO> getBookingStatusHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingStatusHistory : {}", id);
        Optional<BookingStatusHistoryDTO> bookingStatusHistoryDTO = bookingStatusHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingStatusHistoryDTO);
    }

    /**
     * {@code DELETE  /booking-status-histories/:id} : delete the "id" bookingStatusHistory.
     *
     * @param id the id of the bookingStatusHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingStatusHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingStatusHistory : {}", id);
        bookingStatusHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
