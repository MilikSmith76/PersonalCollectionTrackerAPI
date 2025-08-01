package app.entities;

import app.generated.types.BaseCollectable;
import app.generated.types.BaseCollectableInput;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "base_collectable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseCollectableDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "initial_price", nullable = false)
    private String initialPrice;

    @Column(name = "market_price", nullable = false)
    private String marketPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public BaseCollectableDAO(BaseCollectableInput input) {
        this.id = input.getId() != null ? Long.valueOf(input.getId()) : null;
        this.name = input.getName();
        this.description = input.getDescription();
        this.imageUrl = input.getImageUrl();
        this.initialPrice = input.getInitialPrice();
        this.marketPrice = input.getMarketPrice();
        this.quantity = input.getQuantity();
    }

    public BaseCollectable toGraphQL() {
        return BaseCollectable
            .newBuilder()
            .id(String.valueOf(this.id))
            .name(this.name)
            .description(this.description)
            .imageUrl(this.imageUrl)
            .initialPrice(this.initialPrice)
            .marketPrice(this.marketPrice)
            .quantity(this.quantity)
            .build();
    }
}
