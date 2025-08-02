package app.dataFetchers;

import app.generated.types.ArtBook;
import app.generated.types.ArtBookFilter;
import app.generated.types.ArtBookInput;
import app.services.ArtBookService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ArtBookDataFetcher {

    private final ArtBookService artBookService;

    @Autowired
    public ArtBookDataFetcher(ArtBookService artBookService) {
        this.artBookService = artBookService;
    }

    @DgsQuery
    public Optional<ArtBook> artBook(@InputArgument String id) {
        return this.artBookService.findById(id);
    }

    @DgsQuery
    public List<ArtBook> artBooks(@InputArgument ArtBookFilter filter) {
        return this.artBookService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<ArtBook> createArtBook(@InputArgument ArtBookInput input) {
        return this.artBookService.save(input);
    }

    @DgsMutation
    public Optional<ArtBook> updateArtBook(@InputArgument ArtBookInput input) {
        return this.artBookService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteArtBook(@InputArgument String id) {
        return artBookService.delete(id);
    }
}
