package app.repositories;

import app.entities.VideoGameDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoGameRepository
    extends JpaRepository<VideoGameDAO, Long> {}
