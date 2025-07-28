package app.entities;

import app.generated.types.ArtBook;
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
@Table(name = "art_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtBookDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bc_id", nullable = false)
    private BaseCollectableDAO baseCollectable;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private PublisherDAO publisher;

    @Column(name = "isbn")
    private String isbn;

    public ArtBook toGraphQL() {
        return ArtBook
            .newBuilder()
            .id(String.valueOf(this.id))
            .baseCollectable(this.baseCollectable.toGraphQL())
            .brand(this.brand.toGraphQL())
            .publisher(this.publisher.toGraphQL())
            .isbn(this.isbn)
            .build();
    }
}
