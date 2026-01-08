package org.trung.tickethub.exception;

import java.util.Map;
import java.util.Objects;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import lombok.extern.slf4j.Slf4j;
import org.trung.tickethub.dto.ApiResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException e) {
            log.error("Error occur: ", e);
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = ValidationException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidation(ValidationException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_KEY.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }

    @ExceptionHandler(value = SignatureException.class)
    ResponseEntity<ApiResponse<Void>> handlingSignatureException(SignatureException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ErrorCode.INVALID_TOKEN.getMessage());
        return ResponseEntity.status(401).body(apiResponse);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    ResponseEntity<ApiResponse<Void>> handlingExpiredJwtException(ExpiredJwtException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ErrorCode.EXPIRED_TOKEN.getMessage());
        return ResponseEntity.status(401).body(apiResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Void>> handlingHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ErrorCode.INVALID_DATA.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MalformedJwtException.class)
    ResponseEntity<ApiResponse<Void>> handlingMalformedJwtException(MalformedJwtException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ErrorCode.INVALID_TOKEN.getMessage());

        return ResponseEntity.status(401).body(apiResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse<Void>> handlingIllegalArgumentException(IllegalArgumentException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}