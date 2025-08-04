package app.dataFetchers;

import app.generated.types.CardProductType;
import app.generated.types.CardProductTypeFilter;
import app.generated.types.CardProductTypeInput;
import app.services.CardProductTypeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class CardProductTypeDataFetcher {

    private final CardProductTypeService cardProductTypeService;

    @Autowired
    public CardProductTypeDataFetcher(
        CardProductTypeService cardProductTypeService
    ) {
        this.cardProductTypeService = cardProductTypeService;
    }

    @DgsQuery
    public Optional<CardProductType> cardProductType(@InputArgument String id) {
        return this.cardProductTypeService.findById(id);
    }

    @DgsQuery
    public List<CardProductType> cardProductTypes(
        @InputArgument CardProductTypeFilter filter
    ) {
        return this.cardProductTypeService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<CardProductType> createCardProductType(
        @InputArgument CardProductTypeInput input
    ) {
        return this.cardProductTypeService.save(input);
    }

    @DgsMutation
    public Optional<CardProductType> updateCardProductType(
        @InputArgument CardProductTypeInput input
    ) {
        return this.cardProductTypeService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteCardProductType(@InputArgument String id) {
        return cardProductTypeService.delete(id);
    }
}
