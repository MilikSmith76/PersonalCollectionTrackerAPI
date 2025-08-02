package app.repositories;

import app.entities.PublisherDAO;
import app.generated.types.PublisherFilter;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository
    extends
        JpaRepository<PublisherDAO, Long>,
        EntityRepository<PublisherDAO, PublisherFilter> {
    @Override
    default List<PublisherDAO> findByCriteria(PublisherFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        PublisherDAO example = new PublisherDAO();

        if (filter.getName() != null) {
            example.setName(filter.getName());
        }

        if (filter.getDescription() != null) {
            example.setDescription(filter.getDescription());
        }

        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<PublisherDAO> exampleQuery = Example.of(example, matcher);
        return this.findAll(exampleQuery);
    }
}
