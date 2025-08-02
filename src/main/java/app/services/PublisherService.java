package app.services;

import app.entities.PublisherDAO;
import app.generated.types.Publisher;
import app.generated.types.PublisherFilter;
import app.generated.types.PublisherInput;
import app.repositories.PublisherRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService
    extends EntityService<
        PublisherDAO,
        Publisher,
        PublisherRepository,
        PublisherInput,
        PublisherFilter
    > {

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        super(publisherRepository);
    }

    @Override
    protected Optional<Publisher> merge(PublisherInput input) {
        PublisherDAO publisherDAO = PublisherDAO.fromGraphQL(input);
        PublisherDAO savedPublisher = this.repository.save(publisherDAO);
        return Optional.of(savedPublisher.toGraphQL());
    }
}
