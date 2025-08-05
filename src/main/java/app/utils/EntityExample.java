package app.utils;

import app.entities.EntityDAO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class EntityExample<Entity extends EntityDAO> {

    private final Entity exampleEntity;
    private final Class<Entity> entityClass;
    private final ExampleMatcher matcher;

    public EntityExample(Class<Entity> entityClass) {
        this.entityClass = entityClass;
        this.matcher =
        ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        try {
            this.exampleEntity =
            this.entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create example entity", e);
        }
    }

    public void setName(String name) {
        try {
            this.entityClass.getMethod("setName", String.class)
                .invoke(this.exampleEntity, name);
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to set name on example entity",
                e
            );
        }
    }

    public void setDescription(String description) {
        try {
            this.entityClass.getMethod("setDescription", String.class)
                .invoke(this.exampleEntity, description);
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to set description on example entity",
                e
            );
        }
    }

    public void setDeleted(boolean deleted) {
        this.exampleEntity.setDeleted(deleted);
    }

    public Example<Entity> getExampleEntity() {
        return Example.of(this.exampleEntity, matcher);
    }
}
