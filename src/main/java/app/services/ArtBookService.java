package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ArtBookDAO;
import app.generated.types.ArtBook;
import app.generated.types.ArtBookFilter;
import app.generated.types.ArtBookInput;
import app.repositories.ArtBookRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("artBooks")
public class ArtBookService
    extends EntityService<
        ArtBookDAO,
        ArtBook,
        ArtBookRepository,
        ArtBookInput,
        ArtBookFilter
    > {

    private final BrandService brandService;

    private final PublisherService publisherService;

    @Autowired
    public ArtBookService(
        ArtBookRepository artBookRepository,
        BrandService brandService,
        PublisherService publisherService
    ) {
        super(artBookRepository);
        this.brandService = brandService;
        this.publisherService = publisherService;
    }

    @Override
    protected Optional<ArtBook> merge(ArtBookInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long publisherId = fromGraphQLId(input.getPublisherId());

        ArtBookDAO artBookDAO = ArtBookDAO.fromGraphQL(input);
        artBookDAO.setBrand(this.brandService.validateAndFindById(brandId));
        artBookDAO.setPublisher(
            this.publisherService.validateAndFindById(publisherId)
        );

        ArtBookDAO savedArtBook = this.repository.save(artBookDAO);
        return Optional.of(savedArtBook.toGraphQL());
    }
}
