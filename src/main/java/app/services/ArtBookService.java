package app.services;

import app.entities.ArtBook;
import app.repositories.ArtBookRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class ArtBookService {

    ArtBookRepository artBookRepository;

    @Autowired
    public ArtBookService(ArtBookRepository artBookRepository) {
        this.artBookRepository = artBookRepository;
    }

    public Optional<ArtBook> findById(Long id) {
        return this.artBookRepository.findById(id);
    }
}
