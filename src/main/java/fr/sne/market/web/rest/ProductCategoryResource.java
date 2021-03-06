package fr.sne.market.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.sne.market.service.ProductCategoryService;
import fr.sne.market.web.rest.util.HeaderUtil;
import fr.sne.market.service.dto.ProductCategoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProductCategory.
 */
@RestController
@RequestMapping("/api")
public class ProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

    private static final String ENTITY_NAME = "productCategory";

    private final ProductCategoryService productCategoryService;

    public ProductCategoryResource(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * POST  /product-categories : Create a new productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productCategoryDTO, or with status 400 (Bad Request) if the productCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@Valid @RequestBody ProductCategoryDTO productCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save ProductCategory : {}", productCategoryDTO);
        if (productCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new productCategory cannot already have an ID")).body(null);
        }
        ProductCategoryDTO result = productCategoryService.save(productCategoryDTO);
        return ResponseEntity.created(new URI("/api/product-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-categories : Updates an existing productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productCategoryDTO,
     * or with status 400 (Bad Request) if the productCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the productCategoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-categories")
    @Timed
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(@Valid @RequestBody ProductCategoryDTO productCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update ProductCategory : {}", productCategoryDTO);
        if (productCategoryDTO.getId() == null) {
            return createProductCategory(productCategoryDTO);
        }
        ProductCategoryDTO result = productCategoryService.save(productCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-categories : get all the productCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productCategories in body
     */
    @GetMapping("/product-categories")
    @Timed
    public List<ProductCategoryDTO> getAllProductCategories() {
        log.debug("REST request to get all ProductCategories");
        return productCategoryService.findAll();
    }

    /**
     * GET  /product-categories/:id : get the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productCategoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<ProductCategoryDTO> getProductCategory(@PathVariable Long id) {
        log.debug("REST request to get ProductCategory : {}", id);
        ProductCategoryDTO productCategoryDTO = productCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(productCategoryDTO));
    }

    /**
     * DELETE  /product-categories/:id : delete the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) {
        log.debug("REST request to delete ProductCategory : {}", id);
        productCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-categories?query=:query : search for the productCategory corresponding
     * to the query.
     *
     * @param query the query of the productCategory search
     * @return the result of the search
     */
    @GetMapping("/_search/product-categories")
    @Timed
    public List<ProductCategoryDTO> searchProductCategories(@RequestParam String query) {
        log.debug("REST request to search ProductCategories for query {}", query);
        return productCategoryService.search(query);
    }

}
