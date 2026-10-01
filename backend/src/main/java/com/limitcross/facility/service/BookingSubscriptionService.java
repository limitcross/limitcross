package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingSubscription;
import com.limitcross.facility.repository.BookingSubscriptionRepository;
import com.limitcross.facility.service.dto.BookingSubscriptionDTO;
import com.limitcross.facility.service.mapper.BookingSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingSubscription}.
 */
@Service
@Transactional
public class BookingSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingSubscriptionService.class);

    private final BookingSubscriptionRepository bookingSubscriptionRepository;

    private final BookingSubscriptionMapper bookingSubscriptionMapper;

    public BookingSubscriptionService(
        BookingSubscriptionRepository bookingSubscriptionRepository,
        BookingSubscriptionMapper bookingSubscriptionMapper
    ) {
        this.bookingSubscriptionRepository = bookingSubscriptionRepository;
        this.bookingSubscriptionMapper = bookingSubscriptionMapper;
    }

    /**
     * Save a bookingSubscription.
     *
     * @param bookingSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingSubscriptionDTO save(BookingSubscriptionDTO bookingSubscriptionDTO) {
        LOG.debug("Request to save BookingSubscription : {}", bookingSubscriptionDTO);
        BookingSubscription bookingSubscription = bookingSubscriptionMapper.toEntity(bookingSubscriptionDTO);
        bookingSubscription = bookingSubscriptionRepository.save(bookingSubscription);
        return bookingSubscriptionMapper.toDto(bookingSubscription);
    }

    /**
     * Update a bookingSubscription.
     *
     * @param bookingSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingSubscriptionDTO update(BookingSubscriptionDTO bookingSubscriptionDTO) {
        LOG.debug("Request to update BookingSubscription : {}", bookingSubscriptionDTO);
        BookingSubscription bookingSubscription = bookingSubscriptionMapper.toEntity(bookingSubscriptionDTO);
        bookingSubscription = bookingSubscriptionRepository.save(bookingSubscription);
        return bookingSubscriptionMapper.toDto(bookingSubscription);
    }

    /**
     * Partially update a bookingSubscription.
     *
     * @param bookingSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingSubscriptionDTO> partialUpdate(BookingSubscriptionDTO bookingSubscriptionDTO) {
        LOG.debug("Request to partially update BookingSubscription : {}", bookingSubscriptionDTO);

        return bookingSubscriptionRepository
            .findById(bookingSubscriptionDTO.getId())
            .map(existingBookingSubscription -> {
                bookingSubscriptionMapper.partialUpdate(existingBookingSubscription, bookingSubscriptionDTO);

                return existingBookingSubscription;
            })
            .map(bookingSubscriptionRepository::save)
            .map(bookingSubscriptionMapper::toDto);
    }

    /**
     * Get all the bookingSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingSubscriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingSubscriptions");
        return bookingSubscriptionRepository.findAll(pageable).map(bookingSubscriptionMapper::toDto);
    }

    /**
     * Get all the bookingSubscriptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingSubscriptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingSubscriptionRepository.findAllWithEagerRelationships(pageable).map(bookingSubscriptionMapper::toDto);
    }

    /**
     * Get one bookingSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get BookingSubscription : {}", id);
        return bookingSubscriptionRepository.findOneWithEagerRelationships(id).map(bookingSubscriptionMapper::toDto);
    }

    /**
     * Delete the bookingSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingSubscription : {}", id);
        bookingSubscriptionRepository.deleteById(id);
    }
}
