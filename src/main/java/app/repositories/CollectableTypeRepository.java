package app.repositories;

import app.entities.CollectableTypeDAO;
import app.generated.types.CollectableTypeFilter;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

        CollectableTypeDAO example = new CollectableTypeDAO();

        if (filter.getName() != null) {
            example.setName(filter.getName());
        }

        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<CollectableTypeDAO> exampleQuery = Example.of(example, matcher);
        return this.findAll(exampleQuery);
    }
}
