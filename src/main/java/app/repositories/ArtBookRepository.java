package app.repositories;

import app.entities.ArtBookDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtBookRepository extends JpaRepository<ArtBookDAO, Long> {}
