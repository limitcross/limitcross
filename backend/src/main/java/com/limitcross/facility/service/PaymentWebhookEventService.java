package com.limitcross.facility.service;

import com.limitcross.facility.domain.PaymentWebhookEvent;
import com.limitcross.facility.repository.PaymentWebhookEventRepository;
import com.limitcross.facility.service.dto.PaymentWebhookEventDTO;
import com.limitcross.facility.service.mapper.PaymentWebhookEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.PaymentWebhookEvent}.
 */
@Service
@Transactional
public class PaymentWebhookEventService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentWebhookEventService.class);

    private final PaymentWebhookEventRepository paymentWebhookEventRepository;

    private final PaymentWebhookEventMapper paymentWebhookEventMapper;

    public PaymentWebhookEventService(
        PaymentWebhookEventRepository paymentWebhookEventRepository,
        PaymentWebhookEventMapper paymentWebhookEventMapper
    ) {
        this.paymentWebhookEventRepository = paymentWebhookEventRepository;
        this.paymentWebhookEventMapper = paymentWebhookEventMapper;
    }

    /**
     * Save a paymentWebhookEvent.
     *
     * @param paymentWebhookEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentWebhookEventDTO save(PaymentWebhookEventDTO paymentWebhookEventDTO) {
        LOG.debug("Request to save PaymentWebhookEvent : {}", paymentWebhookEventDTO);
        PaymentWebhookEvent paymentWebhookEvent = paymentWebhookEventMapper.toEntity(paymentWebhookEventDTO);
        paymentWebhookEvent = paymentWebhookEventRepository.save(paymentWebhookEvent);
        return paymentWebhookEventMapper.toDto(paymentWebhookEvent);
    }

    /**
     * Update a paymentWebhookEvent.
     *
     * @param paymentWebhookEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentWebhookEventDTO update(PaymentWebhookEventDTO paymentWebhookEventDTO) {
        LOG.debug("Request to update PaymentWebhookEvent : {}", paymentWebhookEventDTO);
        PaymentWebhookEvent paymentWebhookEvent = paymentWebhookEventMapper.toEntity(paymentWebhookEventDTO);
        paymentWebhookEvent = paymentWebhookEventRepository.save(paymentWebhookEvent);
        return paymentWebhookEventMapper.toDto(paymentWebhookEvent);
    }

    /**
     * Partially update a paymentWebhookEvent.
     *
     * @param paymentWebhookEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentWebhookEventDTO> partialUpdate(PaymentWebhookEventDTO paymentWebhookEventDTO) {
        LOG.debug("Request to partially update PaymentWebhookEvent : {}", paymentWebhookEventDTO);

        return paymentWebhookEventRepository
            .findById(paymentWebhookEventDTO.getId())
            .map(existingPaymentWebhookEvent -> {
                paymentWebhookEventMapper.partialUpdate(existingPaymentWebhookEvent, paymentWebhookEventDTO);

                return existingPaymentWebhookEvent;
            })
            .map(paymentWebhookEventRepository::save)
            .map(paymentWebhookEventMapper::toDto);
    }

    /**
     * Get all the paymentWebhookEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentWebhookEventDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PaymentWebhookEvents");
        return paymentWebhookEventRepository.findAll(pageable).map(paymentWebhookEventMapper::toDto);
    }

    /**
     * Get one paymentWebhookEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentWebhookEventDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentWebhookEvent : {}", id);
        return paymentWebhookEventRepository.findById(id).map(paymentWebhookEventMapper::toDto);
    }

    /**
     * Delete the paymentWebhookEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentWebhookEvent : {}", id);
        paymentWebhookEventRepository.deleteById(id);
    }
}
