package app.dataFetchers;

import app.generated.types.Series;
import app.generated.types.SeriesFilter;
import app.generated.types.SeriesInput;
import app.services.SeriesService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class SeriesDataFetcher {

    private final SeriesService seriesService;

    @Autowired
    public SeriesDataFetcher(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @DgsQuery
    public Optional<Series> seriesById(@InputArgument String id) {
        return this.seriesService.findById(id);
    }

    @DgsQuery
    public List<Series> series(@InputArgument SeriesFilter filter) {
        return this.seriesService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Series> createSeries(@InputArgument SeriesInput input) {
        return this.seriesService.save(input);
    }

    @DgsMutation
    public Optional<Series> updateSeries(@InputArgument SeriesInput input) {
        return this.seriesService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteSeries(@InputArgument String id) {
        return seriesService.delete(id);
    }
}
