package app.services;

import app.entities.SeriesDAO;
import app.generated.types.Series;
import app.generated.types.SeriesFilter;
import app.generated.types.SeriesInput;
import app.repositories.SeriesRepository;
import java.util.Optional;

public class SeriesService
    extends EntityService<
        SeriesDAO,
        Series,
        SeriesRepository,
        SeriesInput,
        SeriesFilter
    > {

    public SeriesService(SeriesRepository repository) {
        super(repository);
    }

    @Override
    protected Optional<Series> merge(SeriesInput input) {
        SeriesDAO seriesDAO = SeriesDAO.fromGraphQL(input);
        SeriesDAO savedSeries = this.repository.save(seriesDAO);
        return Optional.of(savedSeries.toGraphQL());
    }
}
