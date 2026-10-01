package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingQuote;
import com.limitcross.facility.repository.BookingQuoteRepository;
import com.limitcross.facility.service.dto.BookingQuoteDTO;
import com.limitcross.facility.service.mapper.BookingQuoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingQuote}.
 */
@Service
@Transactional
public class BookingQuoteService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQuoteService.class);

    private final BookingQuoteRepository bookingQuoteRepository;

    private final BookingQuoteMapper bookingQuoteMapper;

    public BookingQuoteService(BookingQuoteRepository bookingQuoteRepository, BookingQuoteMapper bookingQuoteMapper) {
        this.bookingQuoteRepository = bookingQuoteRepository;
        this.bookingQuoteMapper = bookingQuoteMapper;
    }

    /**
     * Save a bookingQuote.
     *
     * @param bookingQuoteDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingQuoteDTO save(BookingQuoteDTO bookingQuoteDTO) {
        LOG.debug("Request to save BookingQuote : {}", bookingQuoteDTO);
        BookingQuote bookingQuote = bookingQuoteMapper.toEntity(bookingQuoteDTO);
        bookingQuote = bookingQuoteRepository.save(bookingQuote);
        return bookingQuoteMapper.toDto(bookingQuote);
    }

    /**
     * Update a bookingQuote.
     *
     * @param bookingQuoteDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingQuoteDTO update(BookingQuoteDTO bookingQuoteDTO) {
        LOG.debug("Request to update BookingQuote : {}", bookingQuoteDTO);
        BookingQuote bookingQuote = bookingQuoteMapper.toEntity(bookingQuoteDTO);
        bookingQuote = bookingQuoteRepository.save(bookingQuote);
        return bookingQuoteMapper.toDto(bookingQuote);
    }

    /**
     * Partially update a bookingQuote.
     *
     * @param bookingQuoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingQuoteDTO> partialUpdate(BookingQuoteDTO bookingQuoteDTO) {
        LOG.debug("Request to partially update BookingQuote : {}", bookingQuoteDTO);

        return bookingQuoteRepository
            .findById(bookingQuoteDTO.getId())
            .map(existingBookingQuote -> {
                bookingQuoteMapper.partialUpdate(existingBookingQuote, bookingQuoteDTO);

                return existingBookingQuote;
            })
            .map(bookingQuoteRepository::save)
            .map(bookingQuoteMapper::toDto);
    }

    /**
     * Get all the bookingQuotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingQuoteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingQuotes");
        return bookingQuoteRepository.findAll(pageable).map(bookingQuoteMapper::toDto);
    }

    /**
     * Get all the bookingQuotes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingQuoteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingQuoteRepository.findAllWithEagerRelationships(pageable).map(bookingQuoteMapper::toDto);
    }

    /**
     * Get one bookingQuote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingQuoteDTO> findOne(Long id) {
        LOG.debug("Request to get BookingQuote : {}", id);
        return bookingQuoteRepository.findOneWithEagerRelationships(id).map(bookingQuoteMapper::toDto);
    }

    /**
     * Delete the bookingQuote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingQuote : {}", id);
        bookingQuoteRepository.deleteById(id);
    }
}
