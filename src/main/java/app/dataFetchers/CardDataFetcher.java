package app.dataFetchers;

import app.generated.types.Card;
import app.generated.types.CardFilter;
import app.generated.types.CardInput;
import app.services.CardService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class CardDataFetcher {

    private final CardService cardService;

    @Autowired
    public CardDataFetcher(CardService cardService) {
        this.cardService = cardService;
    }

    @DgsQuery
    public Optional<Card> card(@InputArgument String id) {
        return this.cardService.findById(id);
    }

    @DgsQuery
    public List<Card> cards(@InputArgument CardFilter filter) {
        return this.cardService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Card> createCard(@InputArgument CardInput input) {
        return this.cardService.save(input);
    }

    @DgsMutation
    public Optional<Card> updateCard(@InputArgument CardInput input) {
        return this.cardService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteCard(@InputArgument String id) {
        return cardService.delete(id);
    }
}
