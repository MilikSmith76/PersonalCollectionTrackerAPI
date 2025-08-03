package app.entities;

import app.generated.types.ShoeModel;
import app.generated.types.ShoeModelInput;
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
@Table(name = "shoe_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeModelDAO extends EntityDAO<ShoeModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    public ShoeModelDAO(ShoeModelInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static ShoeModelDAO fromGraphQL(ShoeModelInput input) {
        return new ShoeModelDAO(input);
    }

    @Override
    public ShoeModel toGraphQL() {
        return ShoeModel
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .brand(this.brand.toGraphQL())
            .build();
    }
}
