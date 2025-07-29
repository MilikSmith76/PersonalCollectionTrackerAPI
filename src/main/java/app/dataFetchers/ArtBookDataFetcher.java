package app.dataFetchers;

import static app.utils.Utilities.fromGraphQLId;

import app.generated.types.ArtBook;
import app.generated.types.ArtBookFilter;
import app.services.ArtBookService;
import com.netflix.graphql.dgs.DgsComponent;
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
        Long parsedId = fromGraphQLId(id);
        return this.artBookService.findById(parsedId);
    }

    @DgsQuery
    public List<ArtBook> artBooks(@InputArgument ArtBookFilter filter) {
        return this.artBookService.findAll(filter);
    }
}
