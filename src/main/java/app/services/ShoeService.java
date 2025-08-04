package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ShoeDAO;
import app.generated.types.Shoe;
import app.generated.types.ShoeFilter;
import app.generated.types.ShoeInput;
import app.repositories.ShoeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoeService
    extends EntityService<
        ShoeDAO,
        Shoe,
        ShoeRepository,
        ShoeInput,
        ShoeFilter
    > {

    private final BrandService brandService;

    @Autowired
    public ShoeService(
        ShoeRepository shoeRepository,
        BrandService brandService
    ) {
        super(shoeRepository);
        this.brandService = brandService;
    }

    @Override
    protected Optional<Shoe> merge(ShoeInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long modelId = fromGraphQLId(input.getModelId());

        ShoeDAO shoeDAO = ShoeDAO.fromGraphQL(input);
        shoeDAO.setBrand(this.brandService.validateAndFindById(brandId));

        ShoeDAO savedShoe = this.repository.save(shoeDAO);
        return Optional.of(savedShoe.toGraphQL());
    }
}
