package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardRarityDAO;
import app.generated.types.CardRarity;
import app.generated.types.CardRarityFilter;
import app.generated.types.CardRarityInput;
import app.repositories.CardRarityRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("cardRarities")
public class CardRarityService
    extends EntityService<
        CardRarityDAO,
        CardRarity,
        CardRarityRepository,
        CardRarityInput,
        CardRarityFilter
    > {

    private final BrandService brandService;

    @Autowired
    public CardRarityService(
        CardRarityRepository cardRarityRepository,
        BrandService brandService
    ) {
        super(cardRarityRepository);
        this.brandService = brandService;
    }

    @Override
    protected Optional<CardRarity> merge(CardRarityInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());

        CardRarityDAO cardRarityDAO = CardRarityDAO.fromGraphQL(input);
        cardRarityDAO.setBrand(this.brandService.validateAndFindById(brandId));

        CardRarityDAO saved = this.repository.save(cardRarityDAO);
        return Optional.of(saved.toGraphQL());
    }
}
