package app.entities;

import static app.utils.Utilities.getEntityGQLOrNull;
import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.CardSet;
import app.generated.types.CardSetInput;
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

@Entity
@Table(name = "card_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardSetDAO extends EntityDAO<CardSet> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private SeriesDAO series;

    public CardSetDAO(CardSetInput input) {
        super();
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static CardSetDAO fromGraphQL(CardSetInput input) {
        return new CardSetDAO(input);
    }

    @Override
    public CardSet toGraphQL() {
        return CardSet
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .brand(this.brand.toGraphQL())
            .series(getEntityGQLOrNull(this.series))
            .deleted(this.getDeletedOrDefault())
            .createdAt(this.getCreatedAtDate())
            .updatedAt(this.getUpdatedAtDate())
            .deletedAt(this.getDeletedAtDate())
            .build();
    }
}
