package constants;

public class AppConstants {
    // Asset Status (THEO CHUẨN TRONG DB TRIGGER)
    public static final String ASSET_AVAILABLE = "available";
    public static final String ASSET_BORROWED = "borrow";

    // Request Status (THEO CHUẨN TRONG DB TRIGGER)
    public static final String REQUEST_PENDING = "Pending";
    public static final String REQUEST_APPROVED = "Approved";
    public static final String REQUEST_REJECTED = "Rejected";
    public static final String REQUEST_COMPLETED = "Completed";

    // Request Types
    public static final String REQUEST_TYPE_BORROW = "borrow";
    public static final String REQUEST_TYPE_RETURN = "return";

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
