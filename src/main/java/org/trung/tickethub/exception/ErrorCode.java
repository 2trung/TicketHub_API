package org.trung.tickethub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1001, "Invalid API key", HttpStatus.BAD_REQUEST),

    INVALID_PHONE_NUMBER(1002, "Invalid phone number", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1003, "Authentication required", HttpStatus.UNAUTHORIZED),

    USER_ALREADY_EXISTS(1004, "User already exists", HttpStatus.BAD_REQUEST),

    EMAIL_ALREADY_EXISTS(1005, "Email already exists", HttpStatus.BAD_REQUEST),

    INVALID_OTP(1006, "Invalid or expired OTP", HttpStatus.BAD_REQUEST),

    INVALID_CREDENTIALS(1007, "Invalid username or password", HttpStatus.BAD_REQUEST),

    INVALID_TOKEN(1008, "Invalid access token", HttpStatus.UNAUTHORIZED),

    EXPIRED_TOKEN(1009, "Access token has expired", HttpStatus.UNAUTHORIZED),

    INVALID_DATA(1010, "Invalid request data", HttpStatus.BAD_REQUEST),

    TOO_MANY_REQUESTS(1011, "Too many requests", HttpStatus.TOO_MANY_REQUESTS),

    USER_NOT_FOUND(1012, "User not found", HttpStatus.NOT_FOUND),

    USER_BLOCKED(1013, "User blocked", HttpStatus.BAD_REQUEST),

    EXPIRED_KEY(1014, "Expried reset password key", HttpStatus.BAD_REQUEST);



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
