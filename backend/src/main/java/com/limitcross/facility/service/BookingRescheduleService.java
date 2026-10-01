package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingReschedule;
import com.limitcross.facility.repository.BookingRescheduleRepository;
import com.limitcross.facility.service.dto.BookingRescheduleDTO;
import com.limitcross.facility.service.mapper.BookingRescheduleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingReschedule}.
 */
@Service
@Transactional
public class BookingRescheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingRescheduleService.class);

    private final BookingRescheduleRepository bookingRescheduleRepository;

    private final BookingRescheduleMapper bookingRescheduleMapper;

    public BookingRescheduleService(
        BookingRescheduleRepository bookingRescheduleRepository,
        BookingRescheduleMapper bookingRescheduleMapper
    ) {
        this.bookingRescheduleRepository = bookingRescheduleRepository;
        this.bookingRescheduleMapper = bookingRescheduleMapper;
    }

    /**
     * Save a bookingReschedule.
     *
     * @param bookingRescheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingRescheduleDTO save(BookingRescheduleDTO bookingRescheduleDTO) {
        LOG.debug("Request to save BookingReschedule : {}", bookingRescheduleDTO);
        BookingReschedule bookingReschedule = bookingRescheduleMapper.toEntity(bookingRescheduleDTO);
        bookingReschedule = bookingRescheduleRepository.save(bookingReschedule);
        return bookingRescheduleMapper.toDto(bookingReschedule);
    }

    /**
     * Update a bookingReschedule.
     *
     * @param bookingRescheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingRescheduleDTO update(BookingRescheduleDTO bookingRescheduleDTO) {
        LOG.debug("Request to update BookingReschedule : {}", bookingRescheduleDTO);
        BookingReschedule bookingReschedule = bookingRescheduleMapper.toEntity(bookingRescheduleDTO);
        bookingReschedule = bookingRescheduleRepository.save(bookingReschedule);
        return bookingRescheduleMapper.toDto(bookingReschedule);
    }

    /**
     * Partially update a bookingReschedule.
     *
     * @param bookingRescheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingRescheduleDTO> partialUpdate(BookingRescheduleDTO bookingRescheduleDTO) {
        LOG.debug("Request to partially update BookingReschedule : {}", bookingRescheduleDTO);

        return bookingRescheduleRepository
            .findById(bookingRescheduleDTO.getId())
            .map(existingBookingReschedule -> {
                bookingRescheduleMapper.partialUpdate(existingBookingReschedule, bookingRescheduleDTO);

                return existingBookingReschedule;
            })
            .map(bookingRescheduleRepository::save)
            .map(bookingRescheduleMapper::toDto);
    }

    /**
     * Get all the bookingReschedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingRescheduleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingReschedules");
        return bookingRescheduleRepository.findAll(pageable).map(bookingRescheduleMapper::toDto);
    }

    /**
     * Get all the bookingReschedules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingRescheduleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingRescheduleRepository.findAllWithEagerRelationships(pageable).map(bookingRescheduleMapper::toDto);
    }

    /**
     * Get one bookingReschedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingRescheduleDTO> findOne(Long id) {
        LOG.debug("Request to get BookingReschedule : {}", id);
        return bookingRescheduleRepository.findOneWithEagerRelationships(id).map(bookingRescheduleMapper::toDto);
    }

    /**
     * Delete the bookingReschedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingReschedule : {}", id);
        bookingRescheduleRepository.deleteById(id);
    }
}
