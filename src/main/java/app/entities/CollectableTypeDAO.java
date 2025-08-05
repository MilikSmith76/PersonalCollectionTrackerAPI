package app.entities;

import static app.utils.Utilities.getIdOrNull;
import static app.utils.Utilities.toGraphQLId;

import app.generated.types.CollectableType;
import app.generated.types.CollectableTypeInput;
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
@Table(name = "collectable_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectableTypeDAO extends EntityDAO<CollectableType> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public CollectableTypeDAO(CollectableTypeInput input) {
        super();
        this.id = getIdOrNull(input.getId());
        this.name = input.getName();
    }

    public static CollectableTypeDAO fromGraphQL(CollectableTypeInput input) {
        return new CollectableTypeDAO(input);
    }

    @Override
    public CollectableType toGraphQL() {
        return CollectableType
            .newBuilder()
            .id(toGraphQLId(this.id))
            .name(this.name)
            .deleted(this.getDeletedOrDefault())
            .createdAt(this.getCreatedAtDate())
            .updatedAt(this.getUpdatedAtDate())
            .deletedAt(this.getDeletedAtDate())
            .build();
    }
}
