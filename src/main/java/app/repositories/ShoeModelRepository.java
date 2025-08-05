package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ShoeModelDAO;
import app.generated.types.ShoeModelFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeModelRepository
    extends
        JpaRepository<ShoeModelDAO, Long>,
        JpaSpecificationExecutor<ShoeModelDAO>,
        EntityRepository<ShoeModelDAO, ShoeModelFilter> {
    @Override
    default List<ShoeModelDAO> findByCriteria(ShoeModelFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<ShoeModelDAO> shoeModelSpecifications =
            new EntitySpecifications<>();

        if (filter.getName() != null) {
            shoeModelSpecifications.hasNameLike(filter.getName());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            shoeModelSpecifications.hasBrandId(brandId);
        }

        if (filter.getDeleted() != null) {
            shoeModelSpecifications.hasDeleted(filter.getDeleted());
        }

        return this.findAll(shoeModelSpecifications.getSpecifications());
    }
}
