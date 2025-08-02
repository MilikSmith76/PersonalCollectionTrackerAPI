package app.dataFetchers;

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
        return this.brandService.findById(id);
    }

    @DgsQuery
    public List<Brand> brands(@InputArgument BrandFilter filter) {
        return this.brandService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Brand> createBrand(@InputArgument BrandInput input) {
        return this.brandService.save(input);
    }

    @DgsMutation
    public Optional<Brand> updateBrand(@InputArgument BrandInput input) {
        return this.brandService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteBrand(@InputArgument String id) {
        return brandService.delete(id);
    }
}
