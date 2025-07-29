package app.dataFetchers;

import static app.utils.Utilities.fromGraphQLId;

import app.generated.types.Brand;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.services.BrandService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class BrandDataFetcher {

    private final BrandService brandService;

    @Autowired
    public BrandDataFetcher(BrandService brandService) {
        this.brandService = brandService;
    }

    @DgsQuery
    public Optional<Brand> brand(@InputArgument String id) {
        Long parsedId = fromGraphQLId(id);
        return this.brandService.findById(parsedId);
    }

    @DgsQuery
    public List<Brand> brands(@InputArgument BrandFilter filter) {
        return this.brandService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Brand> saveBrand(@InputArgument BrandInput input) {
        return this.brandService.save(input);
    }

    @DgsMutation
    public Optional<Brand> updateBrand(@InputArgument BrandInput input) {
        return this.brandService.update(input);
    }

    @DgsMutation
    public Boolean deleteBrand(@InputArgument String id) {
        Long parsedId = fromGraphQLId(id);
        return brandService.delete(parsedId);
    }
}
