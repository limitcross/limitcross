package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CustomerWalletRepository;
import com.limitcross.facility.service.CustomerWalletService;
import com.limitcross.facility.service.dto.CustomerWalletDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CustomerWallet}.
 */
@RestController
@RequestMapping("/api/customer-wallets")
public class CustomerWalletResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerWalletResource.class);

    private static final String ENTITY_NAME = "customerWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerWalletService customerWalletService;

    private final CustomerWalletRepository customerWalletRepository;

    public CustomerWalletResource(CustomerWalletService customerWalletService, CustomerWalletRepository customerWalletRepository) {
        this.customerWalletService = customerWalletService;
        this.customerWalletRepository = customerWalletRepository;
    }

    /**
     * {@code POST  /customer-wallets} : Create a new customerWallet.
     *
     * @param customerWalletDTO the customerWalletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerWalletDTO, or with status {@code 400 (Bad Request)} if the customerWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerWalletDTO> createCustomerWallet(@Valid @RequestBody CustomerWalletDTO customerWalletDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerWallet : {}", customerWalletDTO);
        if (customerWalletDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerWalletDTO = customerWalletService.save(customerWalletDTO);
        return ResponseEntity.created(new URI("/api/customer-wallets/" + customerWalletDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerWalletDTO.getId().toString()))
            .body(customerWalletDTO);
    }

    /**
     * {@code PUT  /customer-wallets/:id} : Updates an existing customerWallet.
     *
     * @param id the id of the customerWalletDTO to save.
     * @param customerWalletDTO the customerWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWalletDTO,
     * or with status {@code 400 (Bad Request)} if the customerWalletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerWalletDTO> updateCustomerWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerWalletDTO customerWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerWallet : {}, {}", id, customerWalletDTO);
        if (customerWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerWalletDTO = customerWalletService.update(customerWalletDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerWalletDTO.getId().toString()))
            .body(customerWalletDTO);
    }

    /**
     * {@code PATCH  /customer-wallets/:id} : Partial updates given fields of an existing customerWallet, field will ignore if it is null
     *
     * @param id the id of the customerWalletDTO to save.
     * @param customerWalletDTO the customerWalletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWalletDTO,
     * or with status {@code 400 (Bad Request)} if the customerWalletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerWalletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerWalletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerWalletDTO> partialUpdateCustomerWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerWalletDTO customerWalletDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerWallet partially : {}, {}", id, customerWalletDTO);
        if (customerWalletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWalletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerWalletDTO> result = customerWalletService.partialUpdate(customerWalletDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerWalletDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-wallets} : get all the customerWallets.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerWallets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerWalletDTO>> getAllCustomerWallets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of CustomerWallets");
        Page<CustomerWalletDTO> page;
        if (eagerload) {
            page = customerWalletService.findAllWithEagerRelationships(pageable);
        } else {
            page = customerWalletService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-wallets/:id} : get the "id" customerWallet.
     *
     * @param id the id of the customerWalletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerWalletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerWalletDTO> getCustomerWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerWallet : {}", id);
        Optional<CustomerWalletDTO> customerWalletDTO = customerWalletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerWalletDTO);
    }

    /**
     * {@code DELETE  /customer-wallets/:id} : delete the "id" customerWallet.
     *
     * @param id the id of the customerWalletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerWallet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerWallet : {}", id);
        customerWalletService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
