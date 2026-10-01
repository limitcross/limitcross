package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.BookingSubscriptionRepository;
import com.limitcross.facility.service.BookingSubscriptionService;
import com.limitcross.facility.service.dto.BookingSubscriptionDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.BookingSubscription}.
 */
@RestController
@RequestMapping("/api/booking-subscriptions")
public class BookingSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingSubscriptionResource.class);

    private static final String ENTITY_NAME = "bookingSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingSubscriptionService bookingSubscriptionService;

    private final BookingSubscriptionRepository bookingSubscriptionRepository;

    public BookingSubscriptionResource(
        BookingSubscriptionService bookingSubscriptionService,
        BookingSubscriptionRepository bookingSubscriptionRepository
    ) {
        this.bookingSubscriptionService = bookingSubscriptionService;
        this.bookingSubscriptionRepository = bookingSubscriptionRepository;
    }

    /**
     * {@code POST  /booking-subscriptions} : Create a new bookingSubscription.
     *
     * @param bookingSubscriptionDTO the bookingSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingSubscriptionDTO, or with status {@code 400 (Bad Request)} if the bookingSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingSubscriptionDTO> createBookingSubscription(
        @Valid @RequestBody BookingSubscriptionDTO bookingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save BookingSubscription : {}", bookingSubscriptionDTO);
        if (bookingSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingSubscriptionDTO = bookingSubscriptionService.save(bookingSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/booking-subscriptions/" + bookingSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bookingSubscriptionDTO.getId().toString()))
            .body(bookingSubscriptionDTO);
    }

    /**
     * {@code PUT  /booking-subscriptions/:id} : Updates an existing bookingSubscription.
     *
     * @param id the id of the bookingSubscriptionDTO to save.
     * @param bookingSubscriptionDTO the bookingSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the bookingSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingSubscriptionDTO> updateBookingSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingSubscriptionDTO bookingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingSubscription : {}, {}", id, bookingSubscriptionDTO);
        if (bookingSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingSubscriptionDTO = bookingSubscriptionService.update(bookingSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingSubscriptionDTO.getId().toString()))
            .body(bookingSubscriptionDTO);
    }

    /**
     * {@code PATCH  /booking-subscriptions/:id} : Partial updates given fields of an existing bookingSubscription, field will ignore if it is null
     *
     * @param id the id of the bookingSubscriptionDTO to save.
     * @param bookingSubscriptionDTO the bookingSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the bookingSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingSubscriptionDTO> partialUpdateBookingSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingSubscriptionDTO bookingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingSubscription partially : {}, {}", id, bookingSubscriptionDTO);
        if (bookingSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingSubscriptionDTO> result = bookingSubscriptionService.partialUpdate(bookingSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookingSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-subscriptions} : get all the bookingSubscriptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookingSubscriptionDTO>> getAllBookingSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BookingSubscriptions");
        Page<BookingSubscriptionDTO> page;
        if (eagerload) {
            page = bookingSubscriptionService.findAllWithEagerRelationships(pageable);
        } else {
            page = bookingSubscriptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /booking-subscriptions/:id} : get the "id" bookingSubscription.
     *
     * @param id the id of the bookingSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingSubscriptionDTO> getBookingSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingSubscription : {}", id);
        Optional<BookingSubscriptionDTO> bookingSubscriptionDTO = bookingSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingSubscriptionDTO);
    }

    /**
     * {@code DELETE  /booking-subscriptions/:id} : delete the "id" bookingSubscription.
     *
     * @param id the id of the bookingSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingSubscription : {}", id);
        bookingSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
