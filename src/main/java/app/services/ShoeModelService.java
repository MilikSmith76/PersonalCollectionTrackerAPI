package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.ShoeModelDAO;
import app.generated.types.ShoeModel;
import app.generated.types.ShoeModelFilter;
import app.generated.types.ShoeModelInput;
import app.repositories.ShoeModelRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("shoeModels")
public class ShoeModelService
    extends EntityService<
        ShoeModelDAO,
        ShoeModel,
        ShoeModelRepository,
        ShoeModelInput,
        ShoeModelFilter
    > {

    private final BrandService brandService;

    @Autowired
    public ShoeModelService(
        ShoeModelRepository shoeModelRepository,
        BrandService brandService
    ) {
        super(shoeModelRepository);
        this.brandService = brandService;
    }

    @Override
    protected Optional<ShoeModel> merge(ShoeModelInput input) {
        long brandId = fromGraphQLId(input.getBrandId());

        ShoeModelDAO shoeModelDAO = ShoeModelDAO.fromGraphQL(input);
        shoeModelDAO.setBrand(this.brandService.validateAndFindById(brandId));

        ShoeModelDAO saved = this.repository.save(shoeModelDAO);
        return Optional.of(saved.toGraphQL());
    }
}
