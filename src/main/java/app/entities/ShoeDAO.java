package app.entities;

import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.Shoe;
import app.generated.types.ShoeInput;
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
@Table(name = "shoe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeDAO extends EntityDAO<Shoe> {

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
    @JoinColumn(name = "shoe_model_id", nullable = false)
    private ShoeModelDAO model;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "sku_id", nullable = false)
    private String skuId;

    public ShoeDAO(ShoeInput input) {
        this.id = getIdOrNull(input.getId());
        this.baseCollectable =
        BaseCollectableDAO.fromGraphQL(input.getBaseCollectable());
        this.size = input.getSize();
        this.color = input.getColor();
        this.skuId = input.getSkuId();
    }

    public static ShoeDAO fromGraphQL(ShoeInput input) {
        return new ShoeDAO(input);
    }

    @Override
    public Shoe toGraphQL() {
        return Shoe
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .model(this.model.toGraphQL())
            .size(this.size)
            .color(this.color)
            .skuId(this.skuId)
            .build();
    }
}
