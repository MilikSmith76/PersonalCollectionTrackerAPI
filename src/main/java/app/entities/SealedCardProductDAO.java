package app.entities;

import app.generated.types.SealedCardProduct;
import app.generated.types.SealedCardProductInput;
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

import static app.utils.Utilities.getEntityGQLOrNull;
import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

@Entity
@Table(name = "sealed_card_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SealedCardProductDAO extends EntityDAO<SealedCardProduct> {

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
    @JoinColumn(name = "card_set_id", nullable = false)
    private CardSetDAO set;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpt_id", nullable = false)
    private CardProductTypeDAO type;

    @Column(name = "sku_id", nullable = false)
    private String skuId;

    public SealedCardProductDAO(SealedCardProductInput input) {
        this.id = getIdOrNull(input.getId());
        this.baseCollectable = BaseCollectableDAO.fromGraphQL(input.getBaseCollectable());
        this.skuId = input.getSkuId();
    }

    public static SealedCardProductDAO fromGraphQL(SealedCardProductInput input) {
        return new SealedCardProductDAO(input);
    }

    @Override
    public SealedCardProduct toGraphQL() {
        return SealedCardProduct
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .series(getEntityGQLOrNull(this.series))
            .set(this.set.toGraphQL())
            .type(this.type.toGraphQL())
            .skuId(this.skuId)
            .build();
    }
}
