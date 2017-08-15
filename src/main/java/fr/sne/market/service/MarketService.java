package fr.sne.market.service;

import fr.sne.market.domain.Market;
import fr.sne.market.repository.MarketRepository;
import fr.sne.market.repository.search.MarketSearchRepository;
import fr.sne.market.service.dto.MarketDTO;
import fr.sne.market.service.mapper.MarketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Market.
 */
@Service
@Transactional
public class MarketService {

    private final Logger log = LoggerFactory.getLogger(MarketService.class);

    private final MarketRepository marketRepository;

    private final MarketMapper marketMapper;

    private final MarketSearchRepository marketSearchRepository;

    public MarketService(MarketRepository marketRepository, MarketMapper marketMapper, MarketSearchRepository marketSearchRepository) {
        this.marketRepository = marketRepository;
        this.marketMapper = marketMapper;
        this.marketSearchRepository = marketSearchRepository;
    }

    /**
     * Save a market.
     *
     * @param marketDTO the entity to save
     * @return the persisted entity
     */
    public MarketDTO save(MarketDTO marketDTO) {
        log.debug("Request to save Market : {}", marketDTO);
        Market market = marketMapper.toEntity(marketDTO);
        market = marketRepository.save(market);
        MarketDTO result = marketMapper.toDto(market);
        marketSearchRepository.save(market);
        return result;
    }

    /**
     *  Get all the markets.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MarketDTO> findAll() {
        log.debug("Request to get all Markets");
        return marketRepository.findAll().stream()
            .map(marketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one market by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MarketDTO findOne(Long id) {
        log.debug("Request to get Market : {}", id);
        Market market = marketRepository.findOne(id);
        return marketMapper.toDto(market);
    }

    /**
     *  Delete the  market by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Market : {}", id);
        marketRepository.delete(id);
        marketSearchRepository.delete(id);
    }

    /**
     * Search for the market corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MarketDTO> search(String query) {
        log.debug("Request to search Markets for query {}", query);
        return StreamSupport
            .stream(marketSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(marketMapper::toDto)
            .collect(Collectors.toList());
    }
}
