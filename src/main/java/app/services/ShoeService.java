package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ShoeDAO;
import app.generated.types.Shoe;
import app.generated.types.ShoeFilter;
import app.generated.types.ShoeInput;
import app.repositories.ShoeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("shoes")
public class ShoeService
    extends EntityService<
        ShoeDAO,
        Shoe,
        ShoeRepository,
        ShoeInput,
        ShoeFilter
    > {

    private final BrandService brandService;

    private final ShoeModelService modelService;

    @Autowired
    public ShoeService(
        ShoeRepository shoeRepository,
        BrandService brandService,
        ShoeModelService modelService
    ) {
        super(shoeRepository);
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @Override
    protected Optional<Shoe> merge(ShoeInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long modelId = fromGraphQLId(input.getModelId());

        ShoeDAO shoeDAO = ShoeDAO.fromGraphQL(input);
        shoeDAO.setBrand(this.brandService.validateAndFindById(brandId));
        shoeDAO.setModel(this.modelService.validateAndFindById(modelId));

        ShoeDAO savedShoe = this.repository.save(shoeDAO);
        return Optional.of(savedShoe.toGraphQL());
    }
}
