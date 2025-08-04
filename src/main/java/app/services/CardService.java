package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardDAO;
import app.generated.types.Card;
import app.generated.types.CardFilter;
import app.generated.types.CardInput;
import app.repositories.CardRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService
    extends EntityService<
        CardDAO,
        Card,
        CardRepository,
        CardInput,
        CardFilter
    > {

    private final BrandService brandService;

    private final SeriesService seriesService;

    // TODO: Add CardSetService and CardRarityService when implemented

    @Autowired
    public CardService(
        CardRepository cardRepository,
        BrandService brandService,
        SeriesService seriesService
    ) {
        super(cardRepository);
        this.brandService = brandService;
        this.seriesService = seriesService;
    }

    @Override
    protected Optional<Card> merge(CardInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long seriesId = fromGraphQLId(input.getSeriesId());
        Long setId = fromGraphQLId(input.getSetId());
        Long rarityId = fromGraphQLId(input.getRarityId());

        CardDAO cardDAO = CardDAO.fromGraphQL(input);
        cardDAO.setBrand(this.brandService.validateAndFindById(brandId));
        cardDAO.setSeries(this.seriesService.validateAndFindById(seriesId));

        CardDAO savedCard = this.repository.save(cardDAO);
        return Optional.of(savedCard.toGraphQL());
    }
}
