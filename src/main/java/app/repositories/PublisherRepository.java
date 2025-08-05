package app.repositories;

import app.entities.PublisherDAO;
import app.generated.types.PublisherFilter;
import app.utils.EntityExample;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository
    extends
        JpaRepository<PublisherDAO, Long>,
        EntityRepository<PublisherDAO, PublisherFilter> {
    @Override
    default List<PublisherDAO> findByCriteria(PublisherFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntityExample<PublisherDAO> publisherExample = new EntityExample<>(
            PublisherDAO.class
        );

        if (filter.getName() != null) {
            publisherExample.setName(filter.getName());
        }

        if (filter.getDescription() != null) {
            publisherExample.setDescription(filter.getDescription());
        }

        if (filter.getDeleted() != null) {
            publisherExample.setDeleted(filter.getDeleted());
        }

        return this.findAll(publisherExample.getExampleEntity());
    }
}
