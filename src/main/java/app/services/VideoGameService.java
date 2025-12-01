package app.services;

import static app.utils.Utilities.fromGraphQLId;

import app.entities.VideoGameDAO;
import app.generated.types.VideoGame;
import app.generated.types.VideoGameFilter;
import app.generated.types.VideoGameInput;
import app.repositories.VideoGameRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("videoGames")
public class VideoGameService
    extends EntityService<
        VideoGameDAO,
        VideoGame,
        VideoGameRepository,
        VideoGameInput,
        VideoGameFilter
    > {

    private final BrandService brandService;

    private final SeriesService seriesService;

    private final PublisherService publisherService;

    private final ConsoleService consoleService;

    @Autowired
    public VideoGameService(
        VideoGameRepository videoGameRepository,
        BrandService brandService,
        SeriesService seriesService,
        PublisherService publisherService,
        ConsoleService consoleService
    ) {
        super(videoGameRepository);
        this.brandService = brandService;
        this.seriesService = seriesService;
        this.publisherService = publisherService;
        this.consoleService = consoleService;
    }

    @Override
    protected Optional<VideoGame> merge(VideoGameInput input) {
        Long brandId = fromGraphQLId(input.getBrandId());
        Long seriesId = fromGraphQLId(input.getSeriesId());
        Long publisherId = fromGraphQLId(input.getPublisherId());
        Long consoleId = fromGraphQLId(input.getConsoleId());

        VideoGameDAO videoGameDAO = VideoGameDAO.fromGraphQL(input);
        videoGameDAO.setBrand(this.brandService.validateAndFindById(brandId));
        videoGameDAO.setSeries(
            this.seriesService.validateAndFindById(seriesId)
        );
        videoGameDAO.setPublisher(
            this.publisherService.validateAndFindById(publisherId)
        );
        videoGameDAO.setConsole(
            this.consoleService.validateAndFindById(consoleId)
        );

        VideoGameDAO savedVideoGame = this.repository.save(videoGameDAO);
        return Optional.of(savedVideoGame.toGraphQL());
    }
}
