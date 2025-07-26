package constants;

public class AppConstants {
    // Asset Status
    public static final String ASSET_AVAILABLE = "AVAILABLE";
    public static final String ASSET_BORROWED = "BORROWED";
    public static final String ASSET_MAINTENANCE = "MAINTENANCE";
    public static final String ASSET_LOST = "LOST";
    public static final String ASSET_DAMAGED = "DAMAGED";

    // Messages
    public static final String ERROR_NOT_FOUND = "Resource not found.";
    public static final String ERROR_VALIDATION = "Validation failed.";
    public static final String ERROR_DATABASE = "Database error occurred.";
    public static final String SUCCESS_CREATE = "Created successfully.";
    public static final String SUCCESS_UPDATE = "Updated successfully.";
    public static final String SUCCESS_DELETE = "Deleted successfully.";

    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_MANAGER = "MANAGER";
}
