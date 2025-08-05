package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CollectableDAO;
import app.generated.types.CollectableFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectableRepository
    extends
        JpaRepository<CollectableDAO, Long>,
        JpaSpecificationExecutor<CollectableDAO>,
        EntityRepository<CollectableDAO, CollectableFilter> {
    @Override
    default List<CollectableDAO> findByCriteria(CollectableFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<CollectableDAO> collectableSpecifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            collectableSpecifications.hasBaseCollectable(
                filter.getBaseCollectable()
            );
        }

        if (filter.getTypeId() != null) {
            Long typeId = fromGraphQLId(filter.getTypeId());
            collectableSpecifications.hasField("collectable_type_id", typeId);
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            collectableSpecifications.hasBrandId(brandId);
        }

        if (filter.getSeriesId() != null) {
            Long seriesId = fromGraphQLId(filter.getSeriesId());
            collectableSpecifications.hasSeriesId(seriesId);
        }

        if (filter.getSkuId() != null) {
            collectableSpecifications.hasSkuId(filter.getSkuId());
        }

        if (filter.getDeleted() != null) {
            collectableSpecifications.hasDeleted(filter.getDeleted());
        }

        return this.findAll(collectableSpecifications.getSpecifications());
    }
}
