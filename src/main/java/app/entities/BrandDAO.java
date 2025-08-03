package app.entities;

import app.generated.types.Brand;
import app.generated.types.BrandInput;
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
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDAO extends EntityDAO<Brand> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    public BrandDAO(BrandInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
        this.description = input.getDescription();
        this.logoUrl = input.getLogoUrl();
    }

    public static BrandDAO fromGraphQL(BrandInput input) {
        return new BrandDAO(input);
    }

    @Override
    public Brand toGraphQL() {
        return Brand
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .description(this.description)
            .logoUrl(this.logoUrl)
            .build();
    }
}
