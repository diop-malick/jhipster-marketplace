package fr.sne.market.service.mapper;

import fr.sne.market.domain.*;
import fr.sne.market.service.dto.MarketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Market and its DTO MarketDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MarketMapper extends EntityMapper <MarketDTO, Market> {
    
    @Mapping(target = "infos", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Market toEntity(MarketDTO marketDTO); 
    default Market fromId(Long id) {
        if (id == null) {
            return null;
        }
        Market market = new Market();
        market.setId(id);
        return market;
    }
}
