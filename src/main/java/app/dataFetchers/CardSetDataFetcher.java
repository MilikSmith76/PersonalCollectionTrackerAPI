package app.dataFetchers;

import app.generated.types.CardSet;
import app.generated.types.CardSetFilter;
import app.generated.types.CardSetInput;
import app.services.CardSetService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class CardSetDataFetcher {

    private final CardSetService cardSetService;

    @Autowired
    public CardSetDataFetcher(CardSetService cardSetService) {
        this.cardSetService = cardSetService;
    }

    @DgsQuery
    public Optional<CardSet> cardSet(@InputArgument String id) {
        return this.cardSetService.findById(id);
    }

    @DgsQuery
    public List<CardSet> cardSets(@InputArgument CardSetFilter filter) {
        return this.cardSetService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<CardSet> createCardSet(@InputArgument CardSetInput input) {
        return this.cardSetService.save(input);
    }

    @DgsMutation
    public Optional<CardSet> updateCardSet(@InputArgument CardSetInput input) {
        return this.cardSetService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteCardSet(@InputArgument String id) {
        return cardSetService.delete(id);
    }
}
