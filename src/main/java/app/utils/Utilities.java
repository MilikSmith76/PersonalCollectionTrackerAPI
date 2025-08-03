package app.utils;

import app.entities.EntityDAO;

public class Utilities {

    public static boolean isNumber(String str) {
        return str != null && str.matches(Constants.numberPattern);
    }

    public static Long getIdOrNull(String id) {
        return isNumber(id) ? Long.valueOf(id) : null;
    }

    public static <EntityGQL> EntityGQL getEntityGQLOrNull(EntityDAO<EntityGQL> entity) {
        return entity != null ? entity.toGraphQL() : null;
    }

    public static String toGraphQLId(Long id) {
        return id != null ? String.valueOf(id) : null;
    }

    public static Long fromGraphQLId(String id)
        throws IllegalArgumentException {
        Long parsedId = getIdOrNull(id);

        if (parsedId == null) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        }

        return parsedId;
    }
}
