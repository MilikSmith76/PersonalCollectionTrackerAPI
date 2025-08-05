package app.repositories;

import app.entities.CardProductTypeDAO;
import app.generated.types.CardProductTypeFilter;
import app.utils.EntityExample;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductTypeRepository
    extends
        JpaRepository<CardProductTypeDAO, Long>,
        EntityRepository<CardProductTypeDAO, CardProductTypeFilter> {
    @Override
    default List<CardProductTypeDAO> findByCriteria(
        CardProductTypeFilter filter
    ) {
        if (filter == null) {
            return this.findAll();
        }

        EntityExample<CardProductTypeDAO> productTypeExample =
            new EntityExample<>(CardProductTypeDAO.class);

        if (filter.getName() != null) {
            productTypeExample.setName(filter.getName());
        }

        if (filter.getDeleted() != null) {
            productTypeExample.setDeleted(filter.getDeleted());
        }

        return this.findAll(productTypeExample.getExampleEntity());
    }
}
