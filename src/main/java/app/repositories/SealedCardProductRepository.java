package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.SealedCardProductDAO;
import app.generated.types.SealedCardProductFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SealedCardProductRepository
    extends
        JpaRepository<SealedCardProductDAO, Long>,
        JpaSpecificationExecutor<SealedCardProductDAO>,
        EntityRepository<SealedCardProductDAO, SealedCardProductFilter> {
    @Override
    default List<SealedCardProductDAO> findByCriteria(
        SealedCardProductFilter filter
    ) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<SealedCardProductDAO> specifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            specifications.hasBaseCollectable(filter.getBaseCollectable());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            specifications.hasBrandId(brandId);
        }

        if (filter.getSeriesId() != null) {
            Long seriesId = fromGraphQLId(filter.getSeriesId());
            specifications.hasSeriesId(seriesId);
        }

        if (filter.getSetId() != null) {
            Long setId = fromGraphQLId(filter.getSetId());
            specifications.hasField("set_id", setId);
        }

        if (filter.getTypeId() != null) {
            Long typeId = fromGraphQLId(filter.getTypeId());
            specifications.hasField("cpt_id", typeId);
        }

        if (filter.getSkuId() != null) {
            specifications.hasSkuId(filter.getSkuId());
        }

        if (filter.getDeleted() != null) {
            specifications.hasDeleted(filter.getDeleted());
        }

        return this.findAll(specifications.getSpecifications());
    }
}
