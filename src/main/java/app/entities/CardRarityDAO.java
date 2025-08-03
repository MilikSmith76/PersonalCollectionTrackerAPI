package app.entities;

import app.generated.types.CardRarity;
import app.generated.types.CardRarityInput;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

@Entity
@Table(name = "card_rarity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRarityDAO extends EntityDAO<CardRarity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    public CardRarityDAO(CardRarityInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static CardRarityDAO fromGraphQL(CardRarityInput input) {
        return new CardRarityDAO(input);
    }

    @Override
    public CardRarity toGraphQL() {
        return CardRarity
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .brand(this.brand.toGraphQL())
            .build();
    }
}
