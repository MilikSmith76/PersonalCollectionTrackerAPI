package app.entities;

import app.generated.types.ArtBook;
import app.generated.types.ArtBookInput;
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

import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

@Entity
@Table(name = "art_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtBookDAO extends EntityDAO<ArtBook> {

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
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherDAO publisher;

    @Column(name = "isbn")
    private String isbn;

    public ArtBookDAO(ArtBookInput input) {
        this.id = getIdOrNull(input.getId());
        this.baseCollectable =
        new BaseCollectableDAO(input.getBaseCollectable());
        this.isbn = input.getIsbn();
    }

    public static ArtBookDAO fromGraphQL(ArtBookInput input) {
        return new ArtBookDAO(input);
    }

    @Override
    public ArtBook toGraphQL() {
        return ArtBook
            .newBuilder()
            .id(toGraphQLId(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .publisher(this.publisher.toGraphQL())
            .isbn(this.isbn)
            .build();
    }
}
