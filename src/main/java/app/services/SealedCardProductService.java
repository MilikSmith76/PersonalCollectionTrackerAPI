package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.SealedCardProductDAO;
import app.generated.types.SealedCardProduct;
import app.generated.types.SealedCardProductFilter;
import app.generated.types.SealedCardProductInput;
import app.repositories.SealedCardProductRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SealedCardProductService
    extends EntityService<
        SealedCardProductDAO,
        SealedCardProduct,
        SealedCardProductRepository,
        SealedCardProductInput,
        SealedCardProductFilter
    > {

    private final BrandService brandService;

    private final SeriesService seriesService;

    @Autowired
    public SealedCardProductService(
        SealedCardProductRepository sealedCardProductRepository,
        BrandService brandService,
        SeriesService seriesService
    ) {
        super(sealedCardProductRepository);
        this.brandService = brandService;
        this.seriesService = seriesService;
    }

    @Override
    protected Optional<SealedCardProduct> merge(SealedCardProductInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long seriesId = fromGraphQLId(input.getSeriesId());
        Long setId = fromGraphQLId(input.getSetId());
        Long typeId = fromGraphQLId(input.getTypeId());

        SealedCardProductDAO sealedCardProductDAO =
            SealedCardProductDAO.fromGraphQL(input);
        sealedCardProductDAO.setBrand(
            this.brandService.validateAndFindById(brandId)
        );
        sealedCardProductDAO.setSeries(
            this.seriesService.validateAndFindById(seriesId)
        );

        SealedCardProductDAO savedSealedCardProduct =
            this.repository.save(sealedCardProductDAO);
        return Optional.of(savedSealedCardProduct.toGraphQL());
    }
}
