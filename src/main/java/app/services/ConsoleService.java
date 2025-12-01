package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ConsoleDAO;
import app.generated.types.Console;
import app.generated.types.ConsoleFilter;
import app.generated.types.ConsoleInput;
import app.repositories.ConsoleRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("consoles")
public class ConsoleService
    extends EntityService<
        ConsoleDAO,
        Console,
        ConsoleRepository,
        ConsoleInput,
        ConsoleFilter
    > {

    private final BrandService brandService;

    @Autowired
    public ConsoleService(
        ConsoleRepository consoleRepository,
        BrandService brandService
    ) {
        super(consoleRepository);
        this.brandService = brandService;
    }

    @Override
    protected Optional<Console> merge(ConsoleInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());

        ConsoleDAO consoleDAO = ConsoleDAO.fromGraphQL(input);
        consoleDAO.setBrand(this.brandService.validateAndFindById(brandId));

        ConsoleDAO saved = this.repository.save(consoleDAO);
        return java.util.Optional.of(saved.toGraphQL());
    }
}
