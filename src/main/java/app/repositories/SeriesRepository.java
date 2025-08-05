package app.repositories;

import app.entities.SeriesDAO;
import app.generated.types.SeriesFilter;
import app.utils.EntityExample;
import java.util.List;
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

        EntityExample<SeriesDAO> seriesExample = new EntityExample<>(
            SeriesDAO.class
        );

        if (seriesFilter.getName() != null) {
            seriesExample.setName(seriesFilter.getName());
        }

        if (seriesFilter.getDeleted() != null) {
            seriesExample.setDeleted(seriesFilter.getDeleted());
        }

        return this.findAll(seriesExample.getExampleEntity());
    }
}
