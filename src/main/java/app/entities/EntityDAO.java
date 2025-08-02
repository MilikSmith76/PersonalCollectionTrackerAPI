package app.entities;

public abstract class EntityDAO<EntityGQL> {

    public abstract EntityGQL toGraphQL();
}
