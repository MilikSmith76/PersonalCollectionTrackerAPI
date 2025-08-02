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

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDAO extends EntityDAO<Series> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public SeriesDAO(SeriesInput input) {
        this.id = input.getId() != null ? Long.valueOf(input.getId()) : null;
        this.name = input.getName();
    }

    public static SeriesDAO fromGraphQL(SeriesInput input) {
        return new SeriesDAO(input);
    }

    @Override
    public Series toGraphQL() {
        return null;
    }
}
