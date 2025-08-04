package app.dataFetchers;

import app.generated.types.SealedCardProduct;
import app.generated.types.SealedCardProductFilter;
import app.generated.types.SealedCardProductInput;
import app.services.SealedCardProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;

@DgsComponent
public class SealedCardProductDataFetcher {

    private final SealedCardProductService sealedCardProductService;

    public SealedCardProductDataFetcher(
        SealedCardProductService sealedCardProductService
    ) {
        this.sealedCardProductService = sealedCardProductService;
    }

    @DgsQuery
    public Optional<SealedCardProduct> sealedCardProduct(
        @InputArgument String id
    ) {
        return this.sealedCardProductService.findById(id);
    }

    @DgsQuery
    public List<SealedCardProduct> sealedCardProducts(
        @InputArgument SealedCardProductFilter filter
    ) {
        return this.sealedCardProductService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<SealedCardProduct> createSealedCardProduct(
        @InputArgument SealedCardProductInput input
    ) {
        return this.sealedCardProductService.save(input);
    }

    @DgsMutation
    public Optional<SealedCardProduct> updateSealedCardProduct(
        @InputArgument SealedCardProductInput input
    ) {
        return this.sealedCardProductService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteSealedCardProduct(@InputArgument String id) {
        return this.sealedCardProductService.delete(id);
    }
}
