package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.CollectableDAO;
import app.generated.types.Collectable;
import app.generated.types.CollectableFilter;
import app.generated.types.CollectableInput;
import app.repositories.CollectableRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectableService
    extends EntityService<
        CollectableDAO,
        Collectable,
        CollectableRepository,
        CollectableInput,
        CollectableFilter
    > {

    private final BrandService brandService;

    private final SeriesService seriesService;

    private final CollectableTypeService typeService;

    @Autowired
    public CollectableService(
        CollectableRepository collectableRepository,
        BrandService brandService,
        SeriesService seriesService,
        CollectableTypeService typeService
    ) {
        super(collectableRepository);
        this.brandService = brandService;
        this.seriesService = seriesService;
        this.typeService = typeService;
    }

    @Override
    protected Optional<Collectable> merge(CollectableInput input) {
        Long typeId = fromGraphQLId(input.getTypeId());
        Long brandId = fromGraphQLId(input.getBrandId());
        Long seriesId = fromGraphQLId(input.getSeriesId());

        CollectableDAO collectableDAO = CollectableDAO.fromGraphQL(input);
        collectableDAO.setBrand(this.brandService.validateAndFindById(brandId));
        collectableDAO.setSeries(
            this.seriesService.validateAndFindById(seriesId)
        );
        collectableDAO.setType(this.typeService.validateAndFindById(typeId));

        CollectableDAO savedCollectable = this.repository.save(collectableDAO);
        return Optional.of(savedCollectable.toGraphQL());
    }
}
