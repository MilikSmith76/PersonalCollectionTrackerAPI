package app.entities;

import static app.utils.Utilities.getEntityGQLOrNull;
import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.Collectable;
import app.generated.types.CollectableInput;
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
@Table(name = "collectable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectableDAO extends EntityDAO<Collectable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bc_id", nullable = false)
    private BaseCollectableDAO baseCollectable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectable_type_id", nullable = false)
    private CollectableTypeDAO type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private SeriesDAO series;

    @Column(name = "sku_id", nullable = false)
    private String skuId;

    public CollectableDAO(CollectableInput input) {
        this.id = getIdOrNull(input.getId());
        this.skuId = input.getSkuId();
    }

    public static CollectableDAO fromGraphQL(CollectableInput input) {
        return new CollectableDAO(input);
    }

    @Override
    public Collectable toGraphQL() {
        return Collectable
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .type(this.type.toGraphQL())
            .brand(this.brand.toGraphQL())
            .series(getEntityGQLOrNull(this.series))
            .skuId(this.skuId)
            .build();
    }
}
