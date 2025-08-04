package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardRarityDAO;
import app.generated.types.CardRarityFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRarityRepository
    extends
        JpaRepository<CardRarityDAO, Long>,
        JpaSpecificationExecutor<CardRarityDAO>,
        EntityRepository<CardRarityDAO, CardRarityFilter> {
    @Override
    default List<CardRarityDAO> findByCriteria(CardRarityFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<CardRarityDAO> cardRaritySpecifications =
            new EntitySpecifications<>();

        if (filter.getName() != null) {
            cardRaritySpecifications.hasNameLike(filter.getName());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            cardRaritySpecifications.hasBrandId(brandId);
        }

        return this.findAll(cardRaritySpecifications.getSpecifications());
    }
}
