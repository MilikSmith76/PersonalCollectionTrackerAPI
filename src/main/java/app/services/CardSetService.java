package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardSetDAO;
import app.generated.types.CardSet;
import app.generated.types.CardSetFilter;
import app.generated.types.CardSetInput;
import app.repositories.CardSetRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("cardSets")
public class CardSetService
    extends EntityService<
        CardSetDAO,
        CardSet,
        CardSetRepository,
        CardSetInput,
        CardSetFilter
    > {

    private final BrandService brandService;

    private final SeriesService seriesService;

    @Autowired
    public CardSetService(
        CardSetRepository cardSetRepository,
        BrandService brandService,
        SeriesService seriesService
    ) {
        super(cardSetRepository);
        this.brandService = brandService;
        this.seriesService = seriesService;
    }

    @Override
    protected Optional<CardSet> merge(CardSetInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long seriesId = fromGraphQLId(input.getSeriesId());

        CardSetDAO cardSetDAO = CardSetDAO.fromGraphQL(input);
        cardSetDAO.setBrand(this.brandService.validateAndFindById(brandId));
        cardSetDAO.setSeries(this.seriesService.validateAndFindById(seriesId));

        CardSetDAO saved = this.repository.save(cardSetDAO);
        return Optional.of(saved.toGraphQL());
    }
}
