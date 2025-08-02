package app.repositories;

import java.util.List;

public interface EntityRepository<Entity, Filter> {
    List<Entity> findByCriteria(Filter filter);
}
