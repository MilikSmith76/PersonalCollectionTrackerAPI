package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ArtBookDAO;
import app.generated.types.ArtBookFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtBookRepository
    extends
        JpaRepository<ArtBookDAO, Long>,
        JpaSpecificationExecutor<ArtBookDAO>,
        EntityRepository<ArtBookDAO, ArtBookFilter> {
    @Override
    default List<ArtBookDAO> findByCriteria(ArtBookFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<ArtBookDAO> artBookSpecifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            artBookSpecifications.hasBaseCollectable(
                filter.getBaseCollectable()
            );
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            artBookSpecifications.hasBrandId(brandId);
        }

        if (filter.getPublisherId() != null) {
            Long publisherId = fromGraphQLId(filter.getPublisherId());
            artBookSpecifications.hasPublisherId(publisherId);
        }

        if (filter.getIsbn() != null) {
            artBookSpecifications.hasFieldLike("isbn", filter.getIsbn());
        }

        return this.findAll(artBookSpecifications.getSpecifications());
    }
}
