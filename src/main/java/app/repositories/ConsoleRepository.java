package app.repositories;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ConsoleDAO;
import app.generated.types.ConsoleFilter;
import app.utils.EntitySpecifications;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsoleRepository
    extends
        JpaRepository<ConsoleDAO, Long>,
        JpaSpecificationExecutor<ConsoleDAO>,
        EntityRepository<ConsoleDAO, ConsoleFilter> {
    @Override
    default List<ConsoleDAO> findByCriteria(ConsoleFilter filter) {
        if (filter == null) {
            return this.findAll();
        }

        EntitySpecifications<ConsoleDAO> consoleSpecifications =
            new EntitySpecifications<>();

        if (filter.getName() != null) {
            consoleSpecifications.hasNameLike(filter.getName());
        }

        if (filter.getBrandId() != null) {
            Long brandId = fromGraphQLId(filter.getBrandId());
            consoleSpecifications.hasBrandId(brandId);
        }

        if (filter.getDeleted() != null) {
            consoleSpecifications.hasDeleted(filter.getDeleted());
        }

        return this.findAll(consoleSpecifications.getSpecifications());
    }
}
