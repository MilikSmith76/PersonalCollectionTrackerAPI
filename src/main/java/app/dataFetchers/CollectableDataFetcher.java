package app.dataFetchers;

import app.generated.types.Collectable;
import app.generated.types.CollectableFilter;
import app.generated.types.CollectableInput;
import app.services.CollectableService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.Optional;

@DgsComponent
public class CollectableDataFetcher {

    private final CollectableService collectableService;

    public CollectableDataFetcher(CollectableService collectableService) {
        this.collectableService = collectableService;
    }

    @DgsQuery
    public Optional<Collectable> collectable(@InputArgument String id) {
        return this.collectableService.findById(id);
    }

    @DgsQuery
    public Iterable<Collectable> collectables(
        @InputArgument CollectableFilter filter
    ) {
        return this.collectableService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Collectable> createCollectable(
        @InputArgument CollectableInput input
    ) {
        return this.collectableService.save(input);
    }

    @DgsMutation
    public Optional<Collectable> updateCollectable(
        @InputArgument CollectableInput input
    ) {
        return this.collectableService.update(input, input.getId());
    }

    @DgsMutation
    public boolean deleteCollectable(@InputArgument String id) {
        return this.collectableService.delete(id);
    }
}
