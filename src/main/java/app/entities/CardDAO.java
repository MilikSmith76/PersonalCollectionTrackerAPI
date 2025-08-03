package app.entities;

import static app.utils.Utilities.getEntityGQLOrNull;
import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.Card;
import app.generated.types.CardInput;
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
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDAO extends EntityDAO<Card> {

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
    @JoinColumn(name = "card_rarity_id", nullable = false)
    private CardRarityDAO rarity;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    public CardDAO(CardInput input) {
        this.id = getIdOrNull(input.getId());
        this.baseCollectable =
        BaseCollectableDAO.fromGraphQL(input.getBaseCollectable());
        this.cardNumber = input.getCardNumber();
    }

    public static CardDAO fromGraphQL(CardInput input) {
        return new CardDAO(input);
    }

    @Override
    public Card toGraphQL() {
        return Card
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .series(getEntityGQLOrNull(this.series))
            .set(this.set.toGraphQL())
            .rarity(this.rarity.toGraphQL())
            .cardNumber(this.cardNumber)
            .build();
    }
}
