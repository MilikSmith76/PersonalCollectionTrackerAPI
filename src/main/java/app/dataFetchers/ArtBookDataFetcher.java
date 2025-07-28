package app.dataFetchers;

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
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }

        if (!id.matches("\\d+")) {
            throw new IllegalArgumentException(
                "Invalid ID format. ID must be a numeric string."
            );
        }

        return this.artBookService.findById(Long.valueOf(id));
    }

    @DgsQuery
    public List<ArtBook> artBooks(@InputArgument ArtBookFilter filter) {
        return this.artBookService.findAll(filter);
    }
}
