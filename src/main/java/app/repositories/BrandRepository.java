package app.repositories;

import app.entities.BrandDAO;
import app.generated.types.BrandFilter;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository
    extends
        JpaRepository<BrandDAO, Long>, EntityRepository<BrandDAO, BrandFilter> {
    default List<BrandDAO> findByCriteria(BrandFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        BrandDAO example = new BrandDAO();

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
        Example<BrandDAO> exampleQuery = Example.of(example, matcher);
        return this.findAll(exampleQuery);
    }
}
