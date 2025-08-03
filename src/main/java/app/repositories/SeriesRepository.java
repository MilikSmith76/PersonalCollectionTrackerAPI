package app.repositories;

import app.entities.SeriesDAO;
import app.generated.types.SeriesFilter;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository
    extends
        JpaRepository<SeriesDAO, Long>,
        EntityRepository<SeriesDAO, SeriesFilter> {
    @Override
    default List<SeriesDAO> findByCriteria(SeriesFilter seriesFilter) {
        if (seriesFilter == null) {
            return this.findAll();
        }

        SeriesDAO example = new SeriesDAO();

        if (seriesFilter.getName() != null) {
            example.setName(seriesFilter.getName());
        }

        return this.findAll(Example.of(example));
    }
}
