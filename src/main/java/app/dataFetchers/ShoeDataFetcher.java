package app.dataFetchers;

import app.generated.types.Shoe;
import app.generated.types.ShoeFilter;
import app.generated.types.ShoeInput;
import app.services.ShoeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class ShoeDataFetcher {

    private final ShoeService shoeService;

    @Autowired
    public ShoeDataFetcher(ShoeService shoeService) {
        this.shoeService = shoeService;
    }

    @DgsQuery
    public Optional<Shoe> shoe(@InputArgument String id) {
        return this.shoeService.findById(id);
    }

    @DgsQuery
    public List<Shoe> shoes(@InputArgument ShoeFilter filter) {
        return this.shoeService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Shoe> createShoe(@InputArgument ShoeInput input) {
        return this.shoeService.save(input);
    }

    @DgsMutation
    public Optional<Shoe> updateShoe(@InputArgument ShoeInput input) {
        return this.shoeService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteShoe(@InputArgument String id) {
        return shoeService.delete(id);
    }
}
