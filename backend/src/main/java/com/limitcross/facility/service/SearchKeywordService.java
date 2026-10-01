package com.limitcross.facility.service;

import com.limitcross.facility.domain.SearchKeyword;
import com.limitcross.facility.repository.SearchKeywordRepository;
import com.limitcross.facility.service.dto.SearchKeywordDTO;
import com.limitcross.facility.service.mapper.SearchKeywordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.SearchKeyword}.
 */
@Service
@Transactional
public class SearchKeywordService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchKeywordService.class);

    private final SearchKeywordRepository searchKeywordRepository;

    private final SearchKeywordMapper searchKeywordMapper;

    public SearchKeywordService(SearchKeywordRepository searchKeywordRepository, SearchKeywordMapper searchKeywordMapper) {
        this.searchKeywordRepository = searchKeywordRepository;
        this.searchKeywordMapper = searchKeywordMapper;
    }

    /**
     * Save a searchKeyword.
     *
     * @param searchKeywordDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchKeywordDTO save(SearchKeywordDTO searchKeywordDTO) {
        LOG.debug("Request to save SearchKeyword : {}", searchKeywordDTO);
        SearchKeyword searchKeyword = searchKeywordMapper.toEntity(searchKeywordDTO);
        searchKeyword = searchKeywordRepository.save(searchKeyword);
        return searchKeywordMapper.toDto(searchKeyword);
    }

    /**
     * Update a searchKeyword.
     *
     * @param searchKeywordDTO the entity to save.
     * @return the persisted entity.
     */
    public SearchKeywordDTO update(SearchKeywordDTO searchKeywordDTO) {
        LOG.debug("Request to update SearchKeyword : {}", searchKeywordDTO);
        SearchKeyword searchKeyword = searchKeywordMapper.toEntity(searchKeywordDTO);
        searchKeyword = searchKeywordRepository.save(searchKeyword);
        return searchKeywordMapper.toDto(searchKeyword);
    }

    /**
     * Partially update a searchKeyword.
     *
     * @param searchKeywordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SearchKeywordDTO> partialUpdate(SearchKeywordDTO searchKeywordDTO) {
        LOG.debug("Request to partially update SearchKeyword : {}", searchKeywordDTO);

        return searchKeywordRepository
            .findById(searchKeywordDTO.getId())
            .map(existingSearchKeyword -> {
                searchKeywordMapper.partialUpdate(existingSearchKeyword, searchKeywordDTO);

                return existingSearchKeyword;
            })
            .map(searchKeywordRepository::save)
            .map(searchKeywordMapper::toDto);
    }

    /**
     * Get all the searchKeywords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SearchKeywordDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SearchKeywords");
        return searchKeywordRepository.findAll(pageable).map(searchKeywordMapper::toDto);
    }

    /**
     * Get all the searchKeywords with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SearchKeywordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return searchKeywordRepository.findAllWithEagerRelationships(pageable).map(searchKeywordMapper::toDto);
    }

    /**
     * Get one searchKeyword by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SearchKeywordDTO> findOne(Long id) {
        LOG.debug("Request to get SearchKeyword : {}", id);
        return searchKeywordRepository.findOneWithEagerRelationships(id).map(searchKeywordMapper::toDto);
    }

    /**
     * Delete the searchKeyword by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SearchKeyword : {}", id);
        searchKeywordRepository.deleteById(id);
    }
}
