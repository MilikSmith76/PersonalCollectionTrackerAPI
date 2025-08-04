package app.utils;

import app.entities.BaseCollectableDAO;
import app.generated.types.BaseCollectableFilter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecifications<Entity> {

    private final List<Specification<Entity>> specifications;

    public EntitySpecifications() {
        this.specifications = new ArrayList<>();
    }

    private String likePattern(String value) {
        return "%" + value.toLowerCase() + "%";
    }

    public void hasBaseCollectable(BaseCollectableFilter filter) {
        Specification<Entity> baseCollectorSpecification = (
            root,
            query,
            criteriaBuilder
        ) -> {
            Join<Entity, BaseCollectableDAO> baseCollectableJoin = root.join(
                "baseCollectable"
            );

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null) {
                Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(baseCollectableJoin.get("name")),
                    likePattern(filter.getName())
                );

                predicates.add(namePredicate);
            }

            if (filter.getDescription() != null) {
                Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(
                        baseCollectableJoin.get("description")
                    ),
                    likePattern(filter.getDescription())
                );

                predicates.add(descriptionPredicate);
            }

            if (filter.getInitialPrice() != null) {
                Predicate initialPricePredicate = criteriaBuilder.equal(
                    baseCollectableJoin.get("initial_price"),
                    filter.getInitialPrice()
                );

                predicates.add(initialPricePredicate);
            }

            if (filter.getMarketPrice() != null) {
                Predicate marketPricePredicate = criteriaBuilder.equal(
                    baseCollectableJoin.get("market_price"),
                    filter.getMarketPrice()
                );

                predicates.add(marketPricePredicate);
            }

            if (filter.getQuantity() != null) {
                Predicate quantityPredicate = criteriaBuilder.equal(
                    baseCollectableJoin.get("quantity"),
                    filter.getQuantity()
                );

                predicates.add(quantityPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        this.specifications.add(baseCollectorSpecification);
    }

    public void hasBrandId(Long brandId) {
        Specification<Entity> brandIdSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.equal(root.get("brand_id"), brandId);

        this.specifications.add(brandIdSpecification);
    }

    public void hasSeriesId(Long seriesId) {
        Specification<Entity> seriesIdSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.equal(root.get("series_id"), seriesId);

        this.specifications.add(seriesIdSpecification);
    }

    public void hasPublisherId(Long publisherId) {
        Specification<Entity> publisherIdSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.equal(root.get("publisher_id"), publisherId);

        this.specifications.add(publisherIdSpecification);
    }

    public void hasSkuId(String skuId) {
        Specification<Entity> skuIdSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.equal(root.get("sku_id"), skuId);

        this.specifications.add(skuIdSpecification);
    }

    public void hasField(String fieldName, Object value) {
        Specification<Entity> fieldSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.equal(root.get(fieldName), value);

        this.specifications.add(fieldSpecification);
    }

    public void hasFieldLike(String fieldName, String value) {
        Specification<Entity> fieldLikeSpecification = (
                root,
                query,
                criteriaBuilder
            ) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(fieldName)),
                likePattern(value)
            );

        this.specifications.add(fieldLikeSpecification);
    }

    public Specification<Entity> getSpecifications() {
        return Specification.allOf(this.specifications);
    }
}
