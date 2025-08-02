package app.utils;

public class Utilities {

    public static boolean isNumber(String str) {
        return str != null && str.matches(Constants.numberPattern);
    }

    public static String toGraphQLId(Long id) {
        return id != null ? String.valueOf(id) : null;
    }

    public static Long fromGraphQLId(String id)
        throws IllegalArgumentException {
        Long parsedId = isNumber(id) ? Long.valueOf(id) : null;

        if (parsedId == null) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        }

        return parsedId;
    }
}
