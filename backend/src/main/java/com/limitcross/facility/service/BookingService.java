package com.limitcross.facility.service;

import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.repository.BookingRepository;
import com.limitcross.facility.service.dto.BookingDTO;
import com.limitcross.facility.service.mapper.BookingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.Booking}.
 */
@Service
@Transactional
public class BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingDTO save(BookingDTO bookingDTO) {
        LOG.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    /**
     * Update a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingDTO update(BookingDTO bookingDTO) {
        LOG.debug("Request to update Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    /**
     * Partially update a booking.
     *
     * @param bookingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        LOG.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    /**
     * Get all the bookings with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingRepository.findAllWithEagerRelationships(pageable).map(bookingMapper::toDto);
    }

    /**
     *  Get all the bookings where CouponRedemption is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findAllWhereCouponRedemptionIsNull() {
        LOG.debug("Request to get all bookings where CouponRedemption is null");
        return StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
            .filter(booking -> booking.getCouponRedemption() == null)
            .map(bookingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the bookings where Review is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findAllWhereReviewIsNull() {
        LOG.debug("Request to get all bookings where Review is null");
        return StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
            .filter(booking -> booking.getReview() == null)
            .map(bookingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the bookings where ChatThread is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findAllWhereChatThreadIsNull() {
        LOG.debug("Request to get all bookings where ChatThread is null");
        return StreamSupport.stream(bookingRepository.findAll().spliterator(), false)
            .filter(booking -> booking.getChatThread() == null)
            .map(bookingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one booking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        LOG.debug("Request to get Booking : {}", id);
        return bookingRepository.findOneWithEagerRelationships(id).map(bookingMapper::toDto);
    }

    /**
     * Delete the booking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
