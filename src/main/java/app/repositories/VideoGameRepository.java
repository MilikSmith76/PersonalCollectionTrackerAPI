package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.VideoGameDAO;
import app.generated.types.VideoGameFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoGameRepository
    extends
        JpaRepository<VideoGameDAO, Long>,
        JpaSpecificationExecutor<VideoGameDAO>,
        EntityRepository<VideoGameDAO, VideoGameFilter> {
    @Override
    default List<VideoGameDAO> findByCriteria(VideoGameFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<VideoGameDAO> videoGameSpecifications =
            new EntitySpecifications<>();

        if (filter.getBaseCollectable() != null) {
            videoGameSpecifications.hasBaseCollectable(
                filter.getBaseCollectable()
            );
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            videoGameSpecifications.hasBrandId(brandId);
        }

        if (filter.getSeriesId() != null) {
            Long seriesId = fromGraphQLId(filter.getSeriesId());
            videoGameSpecifications.hasSeriesId(seriesId);
        }

        if (filter.getPublisherId() != null) {
            Long publisherId = fromGraphQLId(filter.getPublisherId());
            videoGameSpecifications.hasPublisherId(publisherId);
        }

        if (filter.getConsoleId() != null) {
            Long consoleId = fromGraphQLId(filter.getConsoleId());
            videoGameSpecifications.hasField("console_id", consoleId);
        }

        if (filter.getSkuId() != null) {
            videoGameSpecifications.hasSkuId(filter.getSkuId());
        }

        return this.findAll(videoGameSpecifications.getSpecifications());
    }
}
