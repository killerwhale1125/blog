package react.blog.common;

public interface ResponseMessage {
    public static final String SUCCESS = "Success.";
    public static final String VALIDATION_FAILED = "Validation failed.";
    public static final String DUPLICATE_EMAIL = "Duplicate email";
    public static final String DUPLICATE_NICKNAME = "Duplicate nickname";
    public static final String DUPLICATE_TEL_NUMBER = "Duplicate tel number";
    public static final String NOT_EXISTED_USER = "This user does not exist";
    public static final String NOT_EXISTED_BOARD = "This board does not exist";

    // HTTP Status 401
    public static final String SIGN_IN_FAIL = "Login information mismatch";
    public static final String AUTHORIZATION_FAIL = "Authorization Failed";

    // HTTP Status 403
    public static final String NO_PERMISSION = "Do not have permission";

    // HTTP Status 500
    public static final String DATABASE_ERROR = "Database error.";
}
