package fr.sne.market.repository;

import fr.sne.market.domain.Market;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Market entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketRepository extends JpaRepository<Market,Long> {
    
}
