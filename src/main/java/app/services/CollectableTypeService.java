package app.services;

import app.entities.CollectableTypeDAO;
import app.generated.types.CollectableType;
import app.generated.types.CollectableTypeFilter;
import app.generated.types.CollectableTypeInput;
import app.repositories.CollectableTypeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("collectableTypes")
public class CollectableTypeService
    extends EntityService<
        CollectableTypeDAO,
        CollectableType,
        CollectableTypeRepository,
        CollectableTypeInput,
        CollectableTypeFilter
    > {

    @Autowired
    public CollectableTypeService(
        CollectableTypeRepository collectableTypeRepository
    ) {
        super(collectableTypeRepository);
    }

    @Override
    protected Optional<CollectableType> merge(CollectableTypeInput input) {
        CollectableTypeDAO collectableTypeDAO = CollectableTypeDAO.fromGraphQL(
            input
        );
        CollectableTypeDAO saved = this.repository.save(collectableTypeDAO);
        return Optional.of(saved.toGraphQL());
    }
}
