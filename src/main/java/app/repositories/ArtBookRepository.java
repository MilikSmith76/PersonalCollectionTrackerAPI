package app.repositories;

import app.entities.ArtBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtBookRepository extends JpaRepository<ArtBook, Long> {}
