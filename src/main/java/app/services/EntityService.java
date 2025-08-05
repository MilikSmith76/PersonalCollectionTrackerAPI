package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.EntityDAO;
import app.repositories.EntityRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EntityService<
    Entity extends EntityDAO<EntityGQL>,
    EntityGQL,
    Repository extends JpaRepository<Entity, Long>
        & EntityRepository<Entity, Filter>,
    Input,
    Filter
> {

    protected final Repository repository;

    private final String recordNotFoundMessage =
        "Record with ID %d does not exist.";

    @Autowired
    public EntityService(Repository repository) {
        this.repository = repository;
    }

    public Optional<EntityGQL> findById(String id) {
        Long parsedId = fromGraphQLId(id);

        Optional<Entity> entity = this.repository.findById(parsedId);

        if (entity.isEmpty()) {
            throw new IllegalArgumentException(
                recordNotFoundMessage.formatted(parsedId)
            );
        }

        return entity.map(Entity::toGraphQL);
    }

    public Entity validateAndFindById(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    recordNotFoundMessage.formatted(id)
                )
            );
    }

    public List<EntityGQL> findByCriteria(Filter filter) {
        return this.repository.findByCriteria(filter)
            .stream()
            .map(Entity::toGraphQL)
            .collect(Collectors.toList());
    }

    protected abstract Optional<EntityGQL> merge(Input input);

    public Optional<EntityGQL> save(Input input) {
        return this.merge(input);
    }

    private void validateId(Long id) throws IllegalArgumentException {
        if (!this.repository.existsById(id)) {
            throw new IllegalArgumentException(
                recordNotFoundMessage.formatted(id)
            );
        }
    }

    public Optional<EntityGQL> update(Input input, String id) {
        Long parsedId = fromGraphQLId(id);

        this.validateId(parsedId);

        return this.merge(input);
    }

    public Boolean delete(String id) {
        Long parsedId = fromGraphQLId(id);

        Entity entity = this.validateAndFindById(parsedId);
        entity.setDeleted(true);
        entity.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        this.repository.save(entity);
        return true;
    }
}
