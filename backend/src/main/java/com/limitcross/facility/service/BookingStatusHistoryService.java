package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingStatusHistory;
import com.limitcross.facility.repository.BookingStatusHistoryRepository;
import com.limitcross.facility.service.dto.BookingStatusHistoryDTO;
import com.limitcross.facility.service.mapper.BookingStatusHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingStatusHistory}.
 */
@Service
@Transactional
public class BookingStatusHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingStatusHistoryService.class);

    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    private final BookingStatusHistoryMapper bookingStatusHistoryMapper;

    public BookingStatusHistoryService(
        BookingStatusHistoryRepository bookingStatusHistoryRepository,
        BookingStatusHistoryMapper bookingStatusHistoryMapper
    ) {
        this.bookingStatusHistoryRepository = bookingStatusHistoryRepository;
        this.bookingStatusHistoryMapper = bookingStatusHistoryMapper;
    }

    /**
     * Save a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingStatusHistoryDTO save(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to save BookingStatusHistory : {}", bookingStatusHistoryDTO);
        BookingStatusHistory bookingStatusHistory = bookingStatusHistoryMapper.toEntity(bookingStatusHistoryDTO);
        bookingStatusHistory = bookingStatusHistoryRepository.save(bookingStatusHistory);
        return bookingStatusHistoryMapper.toDto(bookingStatusHistory);
    }

    /**
     * Update a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingStatusHistoryDTO update(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to update BookingStatusHistory : {}", bookingStatusHistoryDTO);
        BookingStatusHistory bookingStatusHistory = bookingStatusHistoryMapper.toEntity(bookingStatusHistoryDTO);
        bookingStatusHistory = bookingStatusHistoryRepository.save(bookingStatusHistory);
        return bookingStatusHistoryMapper.toDto(bookingStatusHistory);
    }

    /**
     * Partially update a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingStatusHistoryDTO> partialUpdate(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to partially update BookingStatusHistory : {}", bookingStatusHistoryDTO);

        return bookingStatusHistoryRepository
            .findById(bookingStatusHistoryDTO.getId())
            .map(existingBookingStatusHistory -> {
                bookingStatusHistoryMapper.partialUpdate(existingBookingStatusHistory, bookingStatusHistoryDTO);

                return existingBookingStatusHistory;
            })
            .map(bookingStatusHistoryRepository::save)
            .map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Get all the bookingStatusHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingStatusHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingStatusHistories");
        return bookingStatusHistoryRepository.findAll(pageable).map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Get all the bookingStatusHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingStatusHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingStatusHistoryRepository.findAllWithEagerRelationships(pageable).map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Get one bookingStatusHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingStatusHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get BookingStatusHistory : {}", id);
        return bookingStatusHistoryRepository.findOneWithEagerRelationships(id).map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Delete the bookingStatusHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingStatusHistory : {}", id);
        bookingStatusHistoryRepository.deleteById(id);
    }
}
