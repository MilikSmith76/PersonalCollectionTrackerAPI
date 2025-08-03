package app.entities;

import static app.utils.Utilities.getEntityGQLOrNull;
import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.VideoGame;
import app.generated.types.VideoGameInput;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoGameDAO extends EntityDAO<VideoGame> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bc_id", nullable = false)
    private BaseCollectableDAO baseCollectable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private SeriesDAO series;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherDAO publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "console_id", nullable = false)
    private ConsoleDAO console;

    @Column(name = "sku_id", nullable = false)
    private String skuId;

    public VideoGameDAO(VideoGameInput input) {
        this.id = getIdOrNull(input.getId());
        this.baseCollectable =
        BaseCollectableDAO.fromGraphQL(input.getBaseCollectable());
        this.skuId = input.getSkuId();
    }

    public static VideoGameDAO fromGraphQL(VideoGameInput input) {
        return new VideoGameDAO(input);
    }

    @Override
    public VideoGame toGraphQL() {
        return VideoGame
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .series(getEntityGQLOrNull(this.series))
            .publisher(this.publisher.toGraphQL())
            .console(this.console.toGraphQL())
            .skuId(this.skuId)
            .build();
    }
}
