package app.test.utils;

public class Constants {

    public static final String MYSQL_IMAGE = "mysql:9.4.0";
    public static final String GRAPH_QL_INTERNAL_ERROR = "INTERNAL";
    public static final String GRAPH_QL_INVALID_ARGUMENT_ERROR =
        "INVALID_ARGUMENT";
    public static final String nonExistentIdErrorMessage =
        "Record with ID 0 does not exist.";
    public static final String fullNonExistentIdErrorMessage =
        "java.lang.IllegalArgumentException: Record with ID 0 does not exist.";
    public static final String invalidBrandErrorMessage =
        "not-null property references a null or transient value: app.entities.BrandDAO.logoUrl";
    public static final long testId = 1L;
    public static final String testIdString = "1";
    public static final long testInvalidId = 0L;
    public static final String testInvalidIdString = "0";
    public static final String defaultBrandName = "Brand";
    public static final String defaultFirstBrandName = "Brand 1";
    public static final String defaultSecondBrandName = "Brand 2";
    public static final String defaultNonExistentBrandName =
        "Non Existent Brand";
    public static final String defaultBrandLogoUrl = "brand_logo_url";
    public static final String defaultFirstBrandLogoUrl = "brand_1_logo_url";
    public static final String defaultSecondBrandLogoUrl = "brand_2_logo_url";
    public static final String defaultDescription =
        "This is a test description.";
}
