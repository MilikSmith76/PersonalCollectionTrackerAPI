package app.repositories;

import app.entities.CollectableTypeDAO;
import app.generated.types.CollectableTypeFilter;
import app.utils.EntityExample;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectableTypeRepository
    extends
        JpaRepository<CollectableTypeDAO, Long>,
        EntityRepository<CollectableTypeDAO, CollectableTypeFilter> {
    @Override
    default List<CollectableTypeDAO> findByCriteria(
        CollectableTypeFilter filter
    ) {
        if (filter == null) {
            return this.findAll();
        }

        EntityExample<CollectableTypeDAO> typeExample = new EntityExample<>(
            CollectableTypeDAO.class
        );

        if (filter.getName() != null) {
            typeExample.setName(filter.getName());
        }

        if (filter.getDeleted() != null) {
            typeExample.setDeleted(filter.getDeleted());
        }

        return this.findAll(typeExample.getExampleEntity());
    }
}
