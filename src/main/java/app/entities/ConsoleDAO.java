package app.entities;

import app.generated.types.Console;
import app.generated.types.ConsoleInput;
import jakarta.persistence.CascadeType;
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
@Table(name = "console")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsoleDAO extends EntityDAO<Console> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandDAO brand;

    public ConsoleDAO(ConsoleInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static ConsoleDAO fromGraphQL(ConsoleInput input) {
        return new ConsoleDAO(input);
    }

    @Override
    public Console toGraphQL() {
        return Console
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .brand(this.brand.toGraphQL())
            .build();
    }
}
