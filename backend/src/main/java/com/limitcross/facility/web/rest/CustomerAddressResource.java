package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.CustomerAddressRepository;
import com.limitcross.facility.service.CustomerAddressQueryService;
import com.limitcross.facility.service.CustomerAddressService;
import com.limitcross.facility.service.criteria.CustomerAddressCriteria;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
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
 * REST controller for managing {@link com.limitcross.facility.domain.CustomerAddress}.
 */
@RestController
@RequestMapping("/api/customer-addresses")
public class CustomerAddressResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAddressResource.class);

    private static final String ENTITY_NAME = "customerAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAddressService customerAddressService;

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerAddressQueryService customerAddressQueryService;

    public CustomerAddressResource(
        CustomerAddressService customerAddressService,
        CustomerAddressRepository customerAddressRepository,
        CustomerAddressQueryService customerAddressQueryService
    ) {
        this.customerAddressService = customerAddressService;
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressQueryService = customerAddressQueryService;
    }

    /**
     * {@code POST  /customer-addresses} : Create a new customerAddress.
     *
     * @param customerAddressDTO the customerAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAddressDTO, or with status {@code 400 (Bad Request)} if the customerAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerAddressDTO> createCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerAddressDTO = customerAddressService.save(customerAddressDTO);
        return ResponseEntity.created(new URI("/api/customer-addresses/" + customerAddressDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerAddressDTO.getId().toString()))
            .body(customerAddressDTO);
    }

    /**
     * {@code PUT  /customer-addresses/:id} : Updates an existing customerAddress.
     *
     * @param id the id of the customerAddressDTO to save.
     * @param customerAddressDTO the customerAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAddressDTO,
     * or with status {@code 400 (Bad Request)} if the customerAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerAddressDTO> updateCustomerAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerAddressDTO customerAddressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerAddress : {}, {}", id, customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerAddressDTO = customerAddressService.update(customerAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAddressDTO.getId().toString()))
            .body(customerAddressDTO);
    }

    /**
     * {@code PATCH  /customer-addresses/:id} : Partial updates given fields of an existing customerAddress, field will ignore if it is null
     *
     * @param id the id of the customerAddressDTO to save.
     * @param customerAddressDTO the customerAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAddressDTO,
     * or with status {@code 400 (Bad Request)} if the customerAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerAddressDTO> partialUpdateCustomerAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerAddressDTO customerAddressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerAddress partially : {}, {}", id, customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerAddressDTO> result = customerAddressService.partialUpdate(customerAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-addresses} : get all the customerAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAddresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerAddressDTO>> getAllCustomerAddresses(
        CustomerAddressCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerAddresses by criteria: {}", criteria);

        Page<CustomerAddressDTO> page = customerAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-addresses/count} : count all the customerAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerAddresses(CustomerAddressCriteria criteria) {
        LOG.debug("REST request to count CustomerAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-addresses/:id} : get the "id" customerAddress.
     *
     * @param id the id of the customerAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerAddressDTO> getCustomerAddress(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerAddress : {}", id);
        Optional<CustomerAddressDTO> customerAddressDTO = customerAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAddressDTO);
    }

    /**
     * {@code DELETE  /customer-addresses/:id} : delete the "id" customerAddress.
     *
     * @param id the id of the customerAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerAddress : {}", id);
        customerAddressService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
