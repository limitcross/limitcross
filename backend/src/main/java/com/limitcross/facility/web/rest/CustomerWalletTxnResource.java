package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CustomerWalletTxnRepository;
import com.limitcross.facility.service.CustomerWalletTxnService;
import com.limitcross.facility.service.dto.CustomerWalletTxnDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CustomerWalletTxn}.
 */
@RestController
@RequestMapping("/api/customer-wallet-txns")
public class CustomerWalletTxnResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerWalletTxnResource.class);

    private static final String ENTITY_NAME = "customerWalletTxn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerWalletTxnService customerWalletTxnService;

    private final CustomerWalletTxnRepository customerWalletTxnRepository;

    public CustomerWalletTxnResource(
        CustomerWalletTxnService customerWalletTxnService,
        CustomerWalletTxnRepository customerWalletTxnRepository
    ) {
        this.customerWalletTxnService = customerWalletTxnService;
        this.customerWalletTxnRepository = customerWalletTxnRepository;
    }

    /**
     * {@code POST  /customer-wallet-txns} : Create a new customerWalletTxn.
     *
     * @param customerWalletTxnDTO the customerWalletTxnDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerWalletTxnDTO, or with status {@code 400 (Bad Request)} if the customerWalletTxn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerWalletTxnDTO> createCustomerWalletTxn(@Valid @RequestBody CustomerWalletTxnDTO customerWalletTxnDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerWalletTxn : {}", customerWalletTxnDTO);
        if (customerWalletTxnDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerWalletTxn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerWalletTxnDTO = customerWalletTxnService.save(customerWalletTxnDTO);
        return ResponseEntity.created(new URI("/api/customer-wallet-txns/" + customerWalletTxnDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerWalletTxnDTO.getId().toString()))
            .body(customerWalletTxnDTO);
    }

    /**
     * {@code PUT  /customer-wallet-txns/:id} : Updates an existing customerWalletTxn.
     *
     * @param id the id of the customerWalletTxnDTO to save.
     * @param customerWalletTxnDTO the customerWalletTxnDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWalletTxnDTO,
     * or with status {@code 400 (Bad Request)} if the customerWalletTxnDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerWalletTxnDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerWalletTxnDTO> updateCustomerWalletTxn(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerWalletTxnDTO customerWalletTxnDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerWalletTxn : {}, {}", id, customerWalletTxnDTO);
        if (customerWalletTxnDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWalletTxnDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWalletTxnRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerWalletTxnDTO = customerWalletTxnService.update(customerWalletTxnDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerWalletTxnDTO.getId().toString()))
            .body(customerWalletTxnDTO);
    }

    /**
     * {@code PATCH  /customer-wallet-txns/:id} : Partial updates given fields of an existing customerWalletTxn, field will ignore if it is null
     *
     * @param id the id of the customerWalletTxnDTO to save.
     * @param customerWalletTxnDTO the customerWalletTxnDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWalletTxnDTO,
     * or with status {@code 400 (Bad Request)} if the customerWalletTxnDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerWalletTxnDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerWalletTxnDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerWalletTxnDTO> partialUpdateCustomerWalletTxn(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerWalletTxnDTO customerWalletTxnDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerWalletTxn partially : {}, {}", id, customerWalletTxnDTO);
        if (customerWalletTxnDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWalletTxnDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWalletTxnRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerWalletTxnDTO> result = customerWalletTxnService.partialUpdate(customerWalletTxnDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerWalletTxnDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-wallet-txns} : get all the customerWalletTxns.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerWalletTxns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerWalletTxnDTO>> getAllCustomerWalletTxns(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CustomerWalletTxns");
        Page<CustomerWalletTxnDTO> page;
        if (eagerload) {
            page = customerWalletTxnService.findAllWithEagerRelationships(pageable);
        } else {
            page = customerWalletTxnService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-wallet-txns/:id} : get the "id" customerWalletTxn.
     *
     * @param id the id of the customerWalletTxnDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerWalletTxnDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerWalletTxnDTO> getCustomerWalletTxn(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerWalletTxn : {}", id);
        Optional<CustomerWalletTxnDTO> customerWalletTxnDTO = customerWalletTxnService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerWalletTxnDTO);
    }

    /**
     * {@code DELETE  /customer-wallet-txns/:id} : delete the "id" customerWalletTxn.
     *
     * @param id the id of the customerWalletTxnDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerWalletTxn(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerWalletTxn : {}", id);
        customerWalletTxnService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
