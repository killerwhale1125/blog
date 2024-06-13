package react.blog.common;

public interface ResponseCode {

    public static final String SUCCESS = "SU";
    public static final String VALIDATION_FAILED = "VF";
    public static final String DUPLICATE_EMAIL = "DE";
    public static final String DUPLICATE_NICKNAME = "DN";
    public static final String DUPLICATE_TEL_NUMBER = "DT";
    public static final String NOT_EXISTED_USER = "NU";
    public static final String NOT_EXISTED_BOARD = "NB";

    // HTTP Status 401
    public static final String SIGN_IN_FAIL = "SF";
    public static final String AUTHORIZATION_FAIL = "AF";

    // HTTP Status 403
    public static final String NO_PERMISSION = "NP";

    // HTTP Status 500
    public static final String DATABASE_ERROR = "DBE";
}
