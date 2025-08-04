package app.dataFetchers;

import app.generated.types.VideoGame;
import app.generated.types.VideoGameFilter;
import app.generated.types.VideoGameInput;
import app.services.VideoGameService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class VideoGameDataFetcher {

    private final VideoGameService videoGameService;

    @Autowired
    public VideoGameDataFetcher(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @DgsQuery
    public Optional<VideoGame> videoGame(@InputArgument String id) {
        return this.videoGameService.findById(id);
    }

    @DgsQuery
    public List<VideoGame> videoGames(@InputArgument VideoGameFilter filter) {
        return this.videoGameService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<VideoGame> createVideoGame(
        @InputArgument VideoGameInput input
    ) {
        return this.videoGameService.save(input);
    }

    @DgsMutation
    public Optional<VideoGame> updateVideoGame(
        @InputArgument VideoGameInput input
    ) {
        return this.videoGameService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteVideoGame(@InputArgument String id) {
        return videoGameService.delete(id);
    }
}
