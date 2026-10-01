package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.PaymentWebhookEventRepository;
import com.limitcross.facility.service.PaymentWebhookEventService;
import com.limitcross.facility.service.dto.PaymentWebhookEventDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.PaymentWebhookEvent}.
 */
@RestController
@RequestMapping("/api/payment-webhook-events")
public class PaymentWebhookEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentWebhookEventResource.class);

    private static final String ENTITY_NAME = "paymentWebhookEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentWebhookEventService paymentWebhookEventService;

    private final PaymentWebhookEventRepository paymentWebhookEventRepository;

    public PaymentWebhookEventResource(
        PaymentWebhookEventService paymentWebhookEventService,
        PaymentWebhookEventRepository paymentWebhookEventRepository
    ) {
        this.paymentWebhookEventService = paymentWebhookEventService;
        this.paymentWebhookEventRepository = paymentWebhookEventRepository;
    }

    /**
     * {@code POST  /payment-webhook-events} : Create a new paymentWebhookEvent.
     *
     * @param paymentWebhookEventDTO the paymentWebhookEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentWebhookEventDTO, or with status {@code 400 (Bad Request)} if the paymentWebhookEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentWebhookEventDTO> createPaymentWebhookEvent(
        @Valid @RequestBody PaymentWebhookEventDTO paymentWebhookEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PaymentWebhookEvent : {}", paymentWebhookEventDTO);
        if (paymentWebhookEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentWebhookEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentWebhookEventDTO = paymentWebhookEventService.save(paymentWebhookEventDTO);
        return ResponseEntity.created(new URI("/api/payment-webhook-events/" + paymentWebhookEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, paymentWebhookEventDTO.getId().toString()))
            .body(paymentWebhookEventDTO);
    }

    /**
     * {@code PUT  /payment-webhook-events/:id} : Updates an existing paymentWebhookEvent.
     *
     * @param id the id of the paymentWebhookEventDTO to save.
     * @param paymentWebhookEventDTO the paymentWebhookEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentWebhookEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentWebhookEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentWebhookEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentWebhookEventDTO> updatePaymentWebhookEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentWebhookEventDTO paymentWebhookEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentWebhookEvent : {}, {}", id, paymentWebhookEventDTO);
        if (paymentWebhookEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentWebhookEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentWebhookEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentWebhookEventDTO = paymentWebhookEventService.update(paymentWebhookEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentWebhookEventDTO.getId().toString()))
            .body(paymentWebhookEventDTO);
    }

    /**
     * {@code PATCH  /payment-webhook-events/:id} : Partial updates given fields of an existing paymentWebhookEvent, field will ignore if it is null
     *
     * @param id the id of the paymentWebhookEventDTO to save.
     * @param paymentWebhookEventDTO the paymentWebhookEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentWebhookEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentWebhookEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentWebhookEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentWebhookEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentWebhookEventDTO> partialUpdatePaymentWebhookEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentWebhookEventDTO paymentWebhookEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentWebhookEvent partially : {}, {}", id, paymentWebhookEventDTO);
        if (paymentWebhookEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentWebhookEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentWebhookEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentWebhookEventDTO> result = paymentWebhookEventService.partialUpdate(paymentWebhookEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentWebhookEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-webhook-events} : get all the paymentWebhookEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentWebhookEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentWebhookEventDTO>> getAllPaymentWebhookEvents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PaymentWebhookEvents");
        Page<PaymentWebhookEventDTO> page = paymentWebhookEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-webhook-events/:id} : get the "id" paymentWebhookEvent.
     *
     * @param id the id of the paymentWebhookEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentWebhookEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentWebhookEventDTO> getPaymentWebhookEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentWebhookEvent : {}", id);
        Optional<PaymentWebhookEventDTO> paymentWebhookEventDTO = paymentWebhookEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentWebhookEventDTO);
    }

    /**
     * {@code DELETE  /payment-webhook-events/:id} : delete the "id" paymentWebhookEvent.
     *
     * @param id the id of the paymentWebhookEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentWebhookEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentWebhookEvent : {}", id);
        paymentWebhookEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
