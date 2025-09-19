package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityDAO<EntityGQL> {

    public EntityDAO() {
        this.deleted = false;
        this.deletedAt = null;
    }

    public abstract EntityGQL toGraphQL();

    private String getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    String getCreatedAtDate() {
        return this.createdAt != null
            ? this.createdAt.toString()
            : this.getCurrentTimestamp();
    }

    String getUpdatedAtDate() {
        return this.updatedAt != null
            ? this.updatedAt.toString()
            : this.getCurrentTimestamp();
    }

    Boolean getDeletedOrDefault() {
        return this.deleted != null ? this.deleted : false;
    }

    String getDeletedAtDate() {
        return this.deletedAt != null ? this.deletedAt.toString() : null;
    }

    @Column(
        name = "created_at",
        nullable = false,
        insertable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL"
    )
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @Column(
        name = "updated_at",
        nullable = false,
        insertable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP"
    )
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    @Column(
        name = "deleted",
        nullable = false,
        insertable = false,
        columnDefinition = "BOOLEAN DEFAULT FALSE NOT NULL"
    )
    protected Boolean deleted;

    @Column(
        name = "deleted_at",
        insertable = false,
        columnDefinition = "TIMESTAMP NULL"
    )
    @Temporal(TemporalType.TIMESTAMP)
    protected Date deletedAt;
}
