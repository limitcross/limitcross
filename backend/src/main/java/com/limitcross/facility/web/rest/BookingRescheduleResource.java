package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingRescheduleRepository;
import com.limitcross.facility.service.BookingRescheduleService;
import com.limitcross.facility.service.dto.BookingRescheduleDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingReschedule}.
 */
@RestController
@RequestMapping("/api/booking-reschedules")
public class BookingRescheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingRescheduleResource.class);

    private static final String ENTITY_NAME = "bookingReschedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingRescheduleService bookingRescheduleService;

    private final BookingRescheduleRepository bookingRescheduleRepository;

    public BookingRescheduleResource(
        BookingRescheduleService bookingRescheduleService,
        BookingRescheduleRepository bookingRescheduleRepository
    ) {
        this.bookingRescheduleService = bookingRescheduleService;
        this.bookingRescheduleRepository = bookingRescheduleRepository;
    }

    /**
     * {@code POST  /booking-reschedules} : Create a new bookingReschedule.
     *
     * @param bookingRescheduleDTO the bookingRescheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingRescheduleDTO, or with status {@code 400 (Bad Request)} if the bookingReschedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingRescheduleDTO> createBookingReschedule(@Valid @RequestBody BookingRescheduleDTO bookingRescheduleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingReschedule : {}", bookingRescheduleDTO);
        if (bookingRescheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingReschedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingRescheduleDTO = bookingRescheduleService.save(bookingRescheduleDTO);
        return ResponseEntity.created(new URI("/api/booking-reschedules/" + bookingRescheduleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingRescheduleDTO.getId().toString()))
            .body(bookingRescheduleDTO);
    }

    /**
     * {@code PUT  /booking-reschedules/:id} : Updates an existing bookingReschedule.
     *
     * @param id the id of the bookingRescheduleDTO to save.
     * @param bookingRescheduleDTO the bookingRescheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingRescheduleDTO,
     * or with status {@code 400 (Bad Request)} if the bookingRescheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingRescheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingRescheduleDTO> updateBookingReschedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingRescheduleDTO bookingRescheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingReschedule : {}, {}", id, bookingRescheduleDTO);
        if (bookingRescheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingRescheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingRescheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingRescheduleDTO = bookingRescheduleService.update(bookingRescheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingRescheduleDTO.getId().toString()))
            .body(bookingRescheduleDTO);
    }

    /**
     * {@code PATCH  /booking-reschedules/:id} : Partial updates given fields of an existing bookingReschedule, field will ignore if it is null
     *
     * @param id the id of the bookingRescheduleDTO to save.
     * @param bookingRescheduleDTO the bookingRescheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingRescheduleDTO,
     * or with status {@code 400 (Bad Request)} if the bookingRescheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingRescheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingRescheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingRescheduleDTO> partialUpdateBookingReschedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingRescheduleDTO bookingRescheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingReschedule partially : {}, {}", id, bookingRescheduleDTO);
        if (bookingRescheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingRescheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingRescheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingRescheduleDTO> result = bookingRescheduleService.partialUpdate(bookingRescheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingRescheduleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-reschedules} : get all the bookingReschedules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingReschedules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingRescheduleDTO>> getAllBookingReschedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingReschedules");
        Page<BookingRescheduleDTO> page;
        if (eagerload) {
            page = bookingRescheduleService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingRescheduleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-reschedules/:id} : get the "id" bookingReschedule.
     *
     * @param id the id of the bookingRescheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingRescheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingRescheduleDTO> getBookingReschedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingReschedule : {}", id);
        Optional<BookingRescheduleDTO> bookingRescheduleDTO = bookingRescheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingRescheduleDTO);
    }

    /**
     * {@code DELETE  /booking-reschedules/:id} : delete the "id" bookingReschedule.
     *
     * @param id the id of the bookingRescheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingReschedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingReschedule : {}", id);
        bookingRescheduleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
