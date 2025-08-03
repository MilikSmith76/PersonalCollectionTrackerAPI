package app.entities;

import app.generated.types.Series;
import app.generated.types.SeriesInput;
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
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDAO extends EntityDAO<Series> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public SeriesDAO(SeriesInput input) {
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static SeriesDAO fromGraphQL(SeriesInput input) {
        return new SeriesDAO(input);
    }

    @Override
    public Series toGraphQL() {
        return Series
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .build();
    }
}
