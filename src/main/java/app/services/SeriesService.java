package app.services;

import app.entities.SeriesDAO;
import app.generated.types.Series;
import app.generated.types.SeriesFilter;
import app.generated.types.SeriesInput;
import app.repositories.SeriesRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("series")
public class SeriesService
    extends EntityService<
        SeriesDAO,
        Series,
        SeriesRepository,
        SeriesInput,
        SeriesFilter
    > {

    @Autowired
    public SeriesService(SeriesRepository seriesRepository) {
        super(seriesRepository);
    }

    @Override
    protected Optional<Series> merge(SeriesInput input) {
        SeriesDAO seriesDAO = SeriesDAO.fromGraphQL(input);
        SeriesDAO savedSeries = this.repository.save(seriesDAO);
        return Optional.of(savedSeries.toGraphQL());
    }
}
