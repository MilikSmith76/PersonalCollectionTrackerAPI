package app.repositories;

import app.entities.CardProductTypeDAO;
import app.generated.types.CardProductTypeFilter;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

        CardProductTypeDAO example = new CardProductTypeDAO();

        if (filter.getName() != null) {
            example.setName(filter.getName());
        }

        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<CardProductTypeDAO> exampleQuery = Example.of(example, matcher);
        return this.findAll(exampleQuery);
    }
}
