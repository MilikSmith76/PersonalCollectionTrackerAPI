package app.services;

import app.entities.PublisherDAO;
import app.generated.types.Publisher;
import app.generated.types.PublisherFilter;
import app.generated.types.PublisherInput;
import app.repositories.PublisherRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Optional<Publisher> findById(Long id) {
        return this.publisherRepository.findById(id)
            .map(PublisherDAO::toGraphQL);
    }

    public List<Publisher> findByCriteria(PublisherFilter filter) {
        return this.publisherRepository.findByCriteria(filter)
            .stream()
            .map(PublisherDAO::toGraphQL)
            .collect(Collectors.toList());
    }

    private Optional<Publisher> merge(PublisherInput input) {
        PublisherDAO publisherDAO = new PublisherDAO(input);
        PublisherDAO savedPublisher =
            this.publisherRepository.save(publisherDAO);
        return Optional.of(savedPublisher.toGraphQL());
    }

    public Optional<Publisher> save(PublisherInput input) {
        return this.merge(input);
    }

    private void validateId(Long id) throws IllegalArgumentException {
        if (!this.publisherRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Publisher with ID " + id + " does not exist."
            );
        }
    }

    public Optional<Publisher> update(PublisherInput input) {
        if (input.getId() == null) {
            throw new IllegalArgumentException(
                "Publisher ID must not be null for update."
            );
        }

        this.validateId(Long.valueOf(input.getId()));

        return this.merge(input);
    }

    public Boolean delete(Long id) {
        this.validateId(id);

        this.publisherRepository.deleteById(id);
        return true;
    }
}
