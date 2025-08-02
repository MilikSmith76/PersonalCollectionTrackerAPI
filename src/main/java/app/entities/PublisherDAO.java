package app.entities;

import app.generated.types.Publisher;
import app.generated.types.PublisherInput;
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
@Table(name = "publisher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDAO extends EntityDAO<Publisher> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    public PublisherDAO(PublisherInput input) {
        this.id = input.getId() != null ? Long.valueOf(input.getId()) : null;
        this.name = input.getName();
        this.description = input.getDescription();
        this.logoUrl = input.getLogoUrl();
    }

    public static PublisherDAO fromGraphQL(PublisherInput input) {
        return new PublisherDAO(input);
    }

    public Publisher toGraphQL() {
        return Publisher
            .newBuilder()
            .id(String.valueOf(id))
            .name(name)
            .description(description)
            .logoUrl(logoUrl)
            .build();
    }
}
