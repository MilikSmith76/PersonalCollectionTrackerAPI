package app.repositories;

import app.entities.BrandDAO;
import app.generated.types.BrandFilter;
import app.utils.EntityExample;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository
    extends
        JpaRepository<BrandDAO, Long>, EntityRepository<BrandDAO, BrandFilter> {
    @Override
    default List<BrandDAO> findByCriteria(BrandFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntityExample<BrandDAO> brandExample = new EntityExample<>(
            BrandDAO.class
        );

        if (filter.getName() != null) {
            brandExample.setName(filter.getName());
        }

        if (filter.getDescription() != null) {
            brandExample.setDescription(filter.getDescription());
        }

        if (filter.getDeleted() != null) {
            brandExample.setDeleted(filter.getDeleted());
        }

        return this.findAll(brandExample.getExampleEntity());
    }
}
