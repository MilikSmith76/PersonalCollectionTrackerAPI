package app.entities;

import app.generated.types.CardProductType;
import app.generated.types.CardProductTypeInput;
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

import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

@Entity
@Table(name = "card_product_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardProductTypeDAO extends EntityDAO<CardProductType> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public CardProductTypeDAO(CardProductTypeInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static CardProductTypeDAO fromGraphQL(CardProductTypeInput input) {
        return new CardProductTypeDAO(input);
    }

    @Override
    public CardProductType toGraphQL() {
        return CardProductType
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .build();
    }
}
