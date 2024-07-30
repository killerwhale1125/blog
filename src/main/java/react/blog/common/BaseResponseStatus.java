package react.blog.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS("SU", "Success"),
    VALIDATION_FAILED("VF", "Validation failed"),
    DUPLICATE_EMAIL("DE", "Duplicate email"),
    DUPLICATE_NICKNAME("DN", "Duplicate nickname"),
    DUPLICATE_TEL_NUMBER("DT", "Duplicate tel number"),
    NOT_EXISTED_USER("NU", "This user does not exist"),
    NOT_EXISTED_BOARD("NB", "This board does not exist"),
    SIGN_IN_FAIL("SF", "Login information mismatch"),
    AUTHORIZATION_FAIL("AF", "Authorization Failed"),
    NO_PERMISSION("NP", "Do not have permission"),
    DATABASE_ERROR("DBE", "Database error");

    private final String code;
    private final String message;

    private BaseResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
