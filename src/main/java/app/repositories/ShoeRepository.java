package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ShoeDAO;
import app.generated.types.ShoeFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeRepository
    extends
        JpaRepository<ShoeDAO, Long>,
        JpaSpecificationExecutor<ShoeDAO>,
        EntityRepository<ShoeDAO, ShoeFilter> {
    @Override
    default List<ShoeDAO> findByCriteria(ShoeFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<ShoeDAO> shoeSpecifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            shoeSpecifications.hasBaseCollectable(filter.getBaseCollectable());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            shoeSpecifications.hasField("brand_id", brandId);
        }

        if (filter.getModelId() != null) {
            Long seriesId = fromGraphQLId(filter.getModelId());
            shoeSpecifications.hasField("shoe_model_id", seriesId);
        }

        if (filter.getSize() != null) {
            shoeSpecifications.hasField("size", filter.getSize());
        }

        if (filter.getColor() != null) {
            shoeSpecifications.hasFieldLike("color", filter.getColor());
        }

        if (filter.getSkuId() != null) {
            shoeSpecifications.hasSkuId(filter.getSkuId());
        }

        return this.findAll(shoeSpecifications.getSpecifications());
    }
}
