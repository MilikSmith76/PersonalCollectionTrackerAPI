package app.test.utils;

public class Constants {

    public static final String MYSQL_IMAGE = "mysql:9.4.0";

    // Error values
    public static final String GRAPH_QL_INTERNAL_ERROR = "INTERNAL";
    public static final String GRAPH_QL_INVALID_ARGUMENT_ERROR =
        "INVALID_ARGUMENT";
    public static final String NON_EXISTENT_ID_ERROR_MESSAGE =
        "Record with ID 0 does not exist.";
    public static final String FULL_NON_EXISTENT_ID_ERROR_MESSAGE =
        "java.lang.IllegalArgumentException: Record with ID 0 does not exist.";
    public static final String INVALID_BRAND_ERROR_MESSAGE =
        "not-null property references a null or transient value: app.entities.BrandDAO.logoUrl";

    // Default id values
    public static final long DEFAULT_ID = 1L;
    public static final String DEFAULT_ID_STRING = "1";
    public static final long NON_EXISTENT_ID = 0L;
    public static final String NON_EXISTENT_ID_STRING = "0";

    // Brand Entity values
    public static final String DEFAULT_BRAND_NAME = "Brand";
    public static final String DEFAULT_FIRST_BRAND_NAME =
        DEFAULT_BRAND_NAME + " 1";
    public static final String DEFAULT_SECOND_BRAND_NAME =
        DEFAULT_BRAND_NAME + " 2";
    public static final String DEFAULT_NON_EXISTENT_BRAND_NAME =
        "Non Existent Brand";
    public static final String DEFAULT_BRAND_LOGO_URL = "brand_logo_url";
    public static final String DEFAULT_FIRST_BRAND_LOGO_URL =
        "brand_1_logo_url";
    public static final String DEFAULT_SECOND_BRAND_LOGO_URL =
        "brand_2_logo_url";
    public static final String DEFAULT_DESCRIPTION =
        "This is a test description.";
}
