package app.dataFetchers;

import app.generated.types.CardRarity;
import app.generated.types.CardRarityFilter;
import app.generated.types.CardRarityInput;
import app.services.CardRarityService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class CardRarityDataFetcher {

    private final CardRarityService cardRarityService;

    @Autowired
    public CardRarityDataFetcher(CardRarityService cardRarityService) {
        this.cardRarityService = cardRarityService;
    }

    @DgsQuery
    public Optional<CardRarity> cardRarity(@InputArgument String id) {
        return this.cardRarityService.findById(id);
    }

    @DgsQuery
    public List<CardRarity> cardRarities(
        @InputArgument CardRarityFilter filter
    ) {
        return this.cardRarityService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<CardRarity> createCardRarity(
        @InputArgument CardRarityInput input
    ) {
        return this.cardRarityService.save(input);
    }

    @DgsMutation
    public Optional<CardRarity> updateCardRarity(
        @InputArgument CardRarityInput input
    ) {
        return this.cardRarityService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteCardRarity(@InputArgument String id) {
        return cardRarityService.delete(id);
    }
}
