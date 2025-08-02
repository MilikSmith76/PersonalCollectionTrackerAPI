package app.dataFetchers;

import app.generated.types.Publisher;
import app.generated.types.PublisherFilter;
import app.generated.types.PublisherInput;
import app.services.PublisherService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class PublisherDataFetcher {

    private final PublisherService publisherService;

    @Autowired
    public PublisherDataFetcher(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @DgsQuery
    public Optional<Publisher> publisher(@InputArgument String id) {
        return this.publisherService.findById(id);
    }

    @DgsQuery
    public List<Publisher> publishers(@InputArgument PublisherFilter filter) {
        return this.publisherService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Publisher> createPublisher(
        @InputArgument PublisherInput input
    ) {
        return this.publisherService.save(input);
    }

    @DgsMutation
    public Optional<Publisher> updatePublisher(
        @InputArgument PublisherInput input
    ) {
        return this.publisherService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deletePublisher(@InputArgument String id) {
        return publisherService.delete(id);
    }
}
