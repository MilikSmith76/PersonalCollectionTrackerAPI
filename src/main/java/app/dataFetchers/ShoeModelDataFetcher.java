package app.dataFetchers;

import app.generated.types.ShoeModel;
import app.generated.types.ShoeModelFilter;
import app.generated.types.ShoeModelInput;
import app.services.ShoeModelService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ShoeModelDataFetcher {

    private final ShoeModelService shoeModelService;

    @Autowired
    public ShoeModelDataFetcher(ShoeModelService shoeModelService) {
        this.shoeModelService = shoeModelService;
    }

    @DgsQuery
    public Optional<ShoeModel> shoeModel(@InputArgument String id) {
        return this.shoeModelService.findById(id);
    }

    @DgsQuery
    public List<ShoeModel> shoeModels(@InputArgument ShoeModelFilter filter) {
        return this.shoeModelService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<ShoeModel> createShoeModel(
        @InputArgument ShoeModelInput input
    ) {
        return this.shoeModelService.save(input);
    }

    @DgsMutation
    public Optional<ShoeModel> updateShoeModel(
        @InputArgument ShoeModelInput input
    ) {
        return this.shoeModelService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteShoeModel(@InputArgument String id) {
        return shoeModelService.delete(id);
    }
}
