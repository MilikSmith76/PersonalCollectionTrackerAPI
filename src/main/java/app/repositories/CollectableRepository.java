package app.repositories;

import app.entities.CollectableDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectableRepository
    extends JpaRepository<CollectableDAO, Long> {}
