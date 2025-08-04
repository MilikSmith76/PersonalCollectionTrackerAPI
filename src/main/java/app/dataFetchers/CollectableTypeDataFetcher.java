package app.dataFetchers;

import app.generated.types.CollectableType;
import app.generated.types.CollectableTypeFilter;
import app.generated.types.CollectableTypeInput;
import app.services.CollectableTypeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class CollectableTypeDataFetcher {

    private final CollectableTypeService collectableTypeService;

    @Autowired
    public CollectableTypeDataFetcher(
        CollectableTypeService collectableTypeService
    ) {
        this.collectableTypeService = collectableTypeService;
    }

    @DgsQuery
    public Optional<CollectableType> collectableType(@InputArgument String id) {
        return this.collectableTypeService.findById(id);
    }

    @DgsQuery
    public List<CollectableType> collectableTypes(
        @InputArgument CollectableTypeFilter filter
    ) {
        return this.collectableTypeService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<CollectableType> createCollectableType(
        @InputArgument CollectableTypeInput input
    ) {
        return this.collectableTypeService.save(input);
    }

    @DgsMutation
    public Optional<CollectableType> updateCollectableType(
        @InputArgument CollectableTypeInput input
    ) {
        return this.collectableTypeService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteCollectableType(@InputArgument String id) {
        return collectableTypeService.delete(id);
    }
}
