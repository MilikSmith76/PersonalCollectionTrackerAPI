package app.repositories;

import app.entities.SealedCardProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SealedCardProductRepository
    extends JpaRepository<SealedCardProductDAO, Long> {}
