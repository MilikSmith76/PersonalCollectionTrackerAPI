package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CardDAO;
import app.generated.types.CardFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository
    extends
        JpaRepository<CardDAO, Long>,
        JpaSpecificationExecutor<CardDAO>,
        EntityRepository<CardDAO, CardFilter> {
    @Override
    default List<CardDAO> findByCriteria(CardFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<CardDAO> cardSpecifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            cardSpecifications.hasBaseCollectable(filter.getBaseCollectable());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            cardSpecifications.hasBrandId(brandId);
        }

        if (filter.getSeriesId() != null) {
            Long seriesId = fromGraphQLId(filter.getSeriesId());
            cardSpecifications.hasSeriesId(seriesId);
        }

        if (filter.getSetId() != null) {
            Long cardSetId = fromGraphQLId(filter.getSetId());
            cardSpecifications.hasField("card_set_id", cardSetId);
        }

        if (filter.getRarityId() != null) {
            Long rarityId = fromGraphQLId(filter.getRarityId());
            cardSpecifications.hasField("card_rarity_id", rarityId);
        }

        if (filter.getCardNumber() != null) {
            cardSpecifications.hasFieldLike(
                "card_number",
                filter.getCardNumber()
            );
        }

        if (filter.getDeleted() != null) {
            cardSpecifications.hasDeleted(filter.getDeleted());
        }

        return this.findAll(cardSpecifications.getSpecifications());
    }
}
