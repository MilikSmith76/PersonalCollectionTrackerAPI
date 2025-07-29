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

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    public BrandDAO(BrandInput input) {
        this.id = input.getId() != null ? Long.valueOf(input.getId()) : null;
        this.name = input.getName();
        this.description = input.getDescription();
        this.logoUrl = input.getLogoUrl();
    }

    public Brand toGraphQL() {
        return Brand
            .newBuilder()
            .id(String.valueOf(id))
            .name(name)
            .description(description)
            .logoUrl(logoUrl)
            .build();
    }
}
