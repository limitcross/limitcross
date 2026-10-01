package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingAssignmentRepository;
import com.limitcross.facility.service.BookingAssignmentService;
import com.limitcross.facility.service.dto.BookingAssignmentDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingAssignment}.
 */
@RestController
@RequestMapping("/api/booking-assignments")
public class BookingAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingAssignmentResource.class);

    private static final String ENTITY_NAME = "bookingAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingAssignmentService bookingAssignmentService;

    private final BookingAssignmentRepository bookingAssignmentRepository;

    public BookingAssignmentResource(
        BookingAssignmentService bookingAssignmentService,
        BookingAssignmentRepository bookingAssignmentRepository
    ) {
        this.bookingAssignmentService = bookingAssignmentService;
        this.bookingAssignmentRepository = bookingAssignmentRepository;
    }

    /**
     * {@code POST  /booking-assignments} : Create a new bookingAssignment.
     *
     * @param bookingAssignmentDTO the bookingAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingAssignmentDTO, or with status {@code 400 (Bad Request)} if the bookingAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingAssignmentDTO> createBookingAssignment(@Valid @RequestBody BookingAssignmentDTO bookingAssignmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingAssignment : {}", bookingAssignmentDTO);
        if (bookingAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingAssignmentDTO = bookingAssignmentService.save(bookingAssignmentDTO);
        return ResponseEntity.created(new URI("/api/booking-assignments/" + bookingAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingAssignmentDTO.getId().toString()))
            .body(bookingAssignmentDTO);
    }

    /**
     * {@code PUT  /booking-assignments/:id} : Updates an existing bookingAssignment.
     *
     * @param id the id of the bookingAssignmentDTO to save.
     * @param bookingAssignmentDTO the bookingAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the bookingAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingAssignmentDTO> updateBookingAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingAssignmentDTO bookingAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingAssignment : {}, {}", id, bookingAssignmentDTO);
        if (bookingAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingAssignmentDTO = bookingAssignmentService.update(bookingAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingAssignmentDTO.getId().toString()))
            .body(bookingAssignmentDTO);
    }

    /**
     * {@code PATCH  /booking-assignments/:id} : Partial updates given fields of an existing bookingAssignment, field will ignore if it is null
     *
     * @param id the id of the bookingAssignmentDTO to save.
     * @param bookingAssignmentDTO the bookingAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the bookingAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingAssignmentDTO> partialUpdateBookingAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingAssignmentDTO bookingAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingAssignment partially : {}, {}", id, bookingAssignmentDTO);
        if (bookingAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingAssignmentDTO> result = bookingAssignmentService.partialUpdate(bookingAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-assignments} : get all the bookingAssignments.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingAssignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingAssignmentDTO>> getAllBookingAssignments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingAssignments");
        Page<BookingAssignmentDTO> page;
        if (eagerload) {
            page = bookingAssignmentService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingAssignmentService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-assignments/:id} : get the "id" bookingAssignment.
     *
     * @param id the id of the bookingAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingAssignmentDTO> getBookingAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingAssignment : {}", id);
        Optional<BookingAssignmentDTO> bookingAssignmentDTO = bookingAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingAssignmentDTO);
    }

    /**
     * {@code DELETE  /booking-assignments/:id} : delete the "id" bookingAssignment.
     *
     * @param id the id of the bookingAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingAssignment : {}", id);
        bookingAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
