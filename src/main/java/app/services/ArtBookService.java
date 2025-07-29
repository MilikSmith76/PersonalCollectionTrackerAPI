package app.services;

import app.entities.ArtBookDAO;
import app.generated.types.ArtBook;
import app.generated.types.ArtBookFilter;
import app.repositories.ArtBookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtBookService {

    private final ArtBookRepository artBookRepository;

    @Autowired
    public ArtBookService(ArtBookRepository artBookRepository) {
        this.artBookRepository = artBookRepository;
    }

    public Optional<ArtBook> findById(Long id) {
        return this.artBookRepository.findById(id).map(ArtBookDAO::toGraphQL);
    }

    public List<ArtBook> findAll(ArtBookFilter filter) {
        return this.artBookRepository.findAll()
            .stream()
            .map(ArtBookDAO::toGraphQL)
            .collect(Collectors.toList());
    }
}
