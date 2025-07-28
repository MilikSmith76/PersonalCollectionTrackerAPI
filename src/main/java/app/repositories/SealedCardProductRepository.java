package app.repositories;

import app.entities.SealedCardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SealedCardProductRepository
    extends JpaRepository<SealedCardProduct, Long> {}
