package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardSetDAO;
import app.generated.types.CardSetFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardSetRepository
    extends
        JpaRepository<CardSetDAO, Long>,
        JpaSpecificationExecutor<CardSetDAO>,
        EntityRepository<CardSetDAO, CardSetFilter> {
    @Override
    default List<CardSetDAO> findByCriteria(CardSetFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<CardSetDAO> cardSetSpecifications =
            new EntitySpecifications<>();

        if (filter.getName() != null) {
            cardSetSpecifications.hasNameLike(filter.getName());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            cardSetSpecifications.hasBrandId(brandId);
        }

        if (filter.getSeriesId() != null) {
            Long seriesId = fromGraphQLId(filter.getSeriesId());
            cardSetSpecifications.hasSeriesId(seriesId);
        }

        return this.findAll(cardSetSpecifications.getSpecifications());
    }
}
