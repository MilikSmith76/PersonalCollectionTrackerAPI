package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ArtBookDAO;
import app.generated.types.ArtBook;
import app.generated.types.ArtBookFilter;
import app.generated.types.ArtBookInput;
import app.repositories.ArtBookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtBookService {

    private final ArtBookRepository artBookRepository;

    private final BrandService brandService;

    private final PublisherService publisherService;

    @Autowired
    public ArtBookService(
        ArtBookRepository artBookRepository,
        BrandService brandService,
        PublisherService publisherService
    ) {
        this.artBookRepository = artBookRepository;
        this.brandService = brandService;
        this.publisherService = publisherService;
    }

    public Optional<ArtBook> findById(Long id) {
        return this.artBookRepository.findById(id).map(ArtBookDAO::toGraphQL);
    }

    public List<ArtBook> findByCriteria(ArtBookFilter filter) {
        return this.artBookRepository.findByCriteria(filter)
            .stream()
            .map(ArtBookDAO::toGraphQL)
            .collect(Collectors.toList());
    }

    private Optional<ArtBook> merge(ArtBookInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long publisherId = fromGraphQLId(input.getPublisherId());

        ArtBookDAO artBookDAO = new ArtBookDAO(input);
        artBookDAO.setBrand(this.brandService.validateAndFindById(brandId));
        artBookDAO.setPublisher(
            this.publisherService.validateAndFindById(publisherId)
        );

        ArtBookDAO savedArtBook = this.artBookRepository.save(artBookDAO);
        return Optional.of(savedArtBook.toGraphQL());
    }

    public Optional<ArtBook> save(ArtBookInput input) {
        return this.merge(input);
    }

    private void validateId(Long id) throws IllegalArgumentException {
        if (!artBookRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Art Book with ID " + id + " does not exist."
            );
        }
    }

    public Optional<ArtBook> update(ArtBookInput input) {
        Long id = fromGraphQLId(input.getId());

        this.validateId(id);

        return this.merge(input);
    }

    public Boolean delete(Long id) {
        this.validateId(id);

        this.artBookRepository.deleteById(id);
        return true;
    }
}
