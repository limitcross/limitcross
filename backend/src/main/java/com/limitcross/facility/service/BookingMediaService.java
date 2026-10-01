package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingMedia;
import com.limitcross.facility.repository.BookingMediaRepository;
import com.limitcross.facility.service.dto.BookingMediaDTO;
import com.limitcross.facility.service.mapper.BookingMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingMedia}.
 */
@Service
@Transactional
public class BookingMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingMediaService.class);

    private final BookingMediaRepository bookingMediaRepository;

    private final BookingMediaMapper bookingMediaMapper;

    public BookingMediaService(BookingMediaRepository bookingMediaRepository, BookingMediaMapper bookingMediaMapper) {
        this.bookingMediaRepository = bookingMediaRepository;
        this.bookingMediaMapper = bookingMediaMapper;
    }

    /**
     * Save a bookingMedia.
     *
     * @param bookingMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingMediaDTO save(BookingMediaDTO bookingMediaDTO) {
        LOG.debug("Request to save BookingMedia : {}", bookingMediaDTO);
        BookingMedia bookingMedia = bookingMediaMapper.toEntity(bookingMediaDTO);
        bookingMedia = bookingMediaRepository.save(bookingMedia);
        return bookingMediaMapper.toDto(bookingMedia);
    }

    /**
     * Update a bookingMedia.
     *
     * @param bookingMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingMediaDTO update(BookingMediaDTO bookingMediaDTO) {
        LOG.debug("Request to update BookingMedia : {}", bookingMediaDTO);
        BookingMedia bookingMedia = bookingMediaMapper.toEntity(bookingMediaDTO);
        bookingMedia = bookingMediaRepository.save(bookingMedia);
        return bookingMediaMapper.toDto(bookingMedia);
    }

    /**
     * Partially update a bookingMedia.
     *
     * @param bookingMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingMediaDTO> partialUpdate(BookingMediaDTO bookingMediaDTO) {
        LOG.debug("Request to partially update BookingMedia : {}", bookingMediaDTO);

        return bookingMediaRepository
            .findById(bookingMediaDTO.getId())
            .map(existingBookingMedia -> {
                bookingMediaMapper.partialUpdate(existingBookingMedia, bookingMediaDTO);

                return existingBookingMedia;
            })
            .map(bookingMediaRepository::save)
            .map(bookingMediaMapper::toDto);
    }

    /**
     * Get all the bookingMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingMediaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingMedias");
        return bookingMediaRepository.findAll(pageable).map(bookingMediaMapper::toDto);
    }

    /**
     * Get all the bookingMedias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingMediaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingMediaRepository.findAllWithEagerRelationships(pageable).map(bookingMediaMapper::toDto);
    }

    /**
     * Get one bookingMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingMediaDTO> findOne(Long id) {
        LOG.debug("Request to get BookingMedia : {}", id);
        return bookingMediaRepository.findOneWithEagerRelationships(id).map(bookingMediaMapper::toDto);
    }

    /**
     * Delete the bookingMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingMedia : {}", id);
        bookingMediaRepository.deleteById(id);
    }
}
