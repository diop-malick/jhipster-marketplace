package fr.sne.market.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.sne.market.domain.MarketOrders;

import fr.sne.market.repository.MarketOrdersRepository;
import fr.sne.market.repository.search.MarketOrdersSearchRepository;
import fr.sne.market.web.rest.util.HeaderUtil;
import fr.sne.market.web.rest.util.PaginationUtil;
import fr.sne.market.service.dto.MarketOrdersDTO;
import fr.sne.market.service.mapper.MarketOrdersMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MarketOrders.
 */
@RestController
@RequestMapping("/api")
public class MarketOrdersResource {

    private final Logger log = LoggerFactory.getLogger(MarketOrdersResource.class);

    private static final String ENTITY_NAME = "marketOrders";

    private final MarketOrdersRepository marketOrdersRepository;

    private final MarketOrdersMapper marketOrdersMapper;

    private final MarketOrdersSearchRepository marketOrdersSearchRepository;

    public MarketOrdersResource(MarketOrdersRepository marketOrdersRepository, MarketOrdersMapper marketOrdersMapper, MarketOrdersSearchRepository marketOrdersSearchRepository) {
        this.marketOrdersRepository = marketOrdersRepository;
        this.marketOrdersMapper = marketOrdersMapper;
        this.marketOrdersSearchRepository = marketOrdersSearchRepository;
    }

    /**
     * POST  /market-orders : Create a new marketOrders.
     *
     * @param marketOrdersDTO the marketOrdersDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketOrdersDTO, or with status 400 (Bad Request) if the marketOrders has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/market-orders")
    @Timed
    public ResponseEntity<MarketOrdersDTO> createMarketOrders(@Valid @RequestBody MarketOrdersDTO marketOrdersDTO) throws URISyntaxException {
        log.debug("REST request to save MarketOrders : {}", marketOrdersDTO);
        if (marketOrdersDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new marketOrders cannot already have an ID")).body(null);
        }
        MarketOrders marketOrders = marketOrdersMapper.toEntity(marketOrdersDTO);
        marketOrders = marketOrdersRepository.save(marketOrders);
        MarketOrdersDTO result = marketOrdersMapper.toDto(marketOrders);
        marketOrdersSearchRepository.save(marketOrders);
        return ResponseEntity.created(new URI("/api/market-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /market-orders : Updates an existing marketOrders.
     *
     * @param marketOrdersDTO the marketOrdersDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketOrdersDTO,
     * or with status 400 (Bad Request) if the marketOrdersDTO is not valid,
     * or with status 500 (Internal Server Error) if the marketOrdersDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/market-orders")
    @Timed
    public ResponseEntity<MarketOrdersDTO> updateMarketOrders(@Valid @RequestBody MarketOrdersDTO marketOrdersDTO) throws URISyntaxException {
        log.debug("REST request to update MarketOrders : {}", marketOrdersDTO);
        if (marketOrdersDTO.getId() == null) {
            return createMarketOrders(marketOrdersDTO);
        }
        MarketOrders marketOrders = marketOrdersMapper.toEntity(marketOrdersDTO);
        marketOrders = marketOrdersRepository.save(marketOrders);
        MarketOrdersDTO result = marketOrdersMapper.toDto(marketOrders);
        marketOrdersSearchRepository.save(marketOrders);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marketOrdersDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /market-orders : get all the marketOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marketOrders in body
     */
    @GetMapping("/market-orders")
    @Timed
    public ResponseEntity<List<MarketOrdersDTO>> getAllMarketOrders(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of MarketOrders");
        Page<MarketOrders> page = marketOrdersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-orders");
        return new ResponseEntity<>(marketOrdersMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /market-orders/:id : get the "id" marketOrders.
     *
     * @param id the id of the marketOrdersDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketOrdersDTO, or with status 404 (Not Found)
     */
    @GetMapping("/market-orders/{id}")
    @Timed
    public ResponseEntity<MarketOrdersDTO> getMarketOrders(@PathVariable Long id) {
        log.debug("REST request to get MarketOrders : {}", id);
        MarketOrders marketOrders = marketOrdersRepository.findOne(id);
        MarketOrdersDTO marketOrdersDTO = marketOrdersMapper.toDto(marketOrders);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marketOrdersDTO));
    }

    /**
     * DELETE  /market-orders/:id : delete the "id" marketOrders.
     *
     * @param id the id of the marketOrdersDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/market-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarketOrders(@PathVariable Long id) {
        log.debug("REST request to delete MarketOrders : {}", id);
        marketOrdersRepository.delete(id);
        marketOrdersSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/market-orders?query=:query : search for the marketOrders corresponding
     * to the query.
     *
     * @param query the query of the marketOrders search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/market-orders")
    @Timed
    public ResponseEntity<List<MarketOrdersDTO>> searchMarketOrders(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of MarketOrders for query {}", query);
        Page<MarketOrders> page = marketOrdersSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/market-orders");
        return new ResponseEntity<>(marketOrdersMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
