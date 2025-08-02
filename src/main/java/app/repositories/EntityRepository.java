package app.repositories;

import java.util.List;

public interface EntityRepository<T, Filter> {
    List<T> findByCriteria(Filter filter);
}
