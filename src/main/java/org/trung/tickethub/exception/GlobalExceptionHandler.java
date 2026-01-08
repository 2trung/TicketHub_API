package org.trung.tickethub.exception;

import java.util.Map;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidation(MethodArgumentNotValidException exception) {
        var fieldError = exception.getFieldError();
        if (fieldError == null) {
            return buildErrorResponse(ErrorCode.INVALID_ARGUMENT);
        }

        String message = fieldError.getDefaultMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(ErrorCode.INVALID_ARGUMENT.getCode())
                .message(message)
                .build();

        return ResponseEntity.status(ErrorCode.INVALID_ARGUMENT.getStatusCode()).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(ErrorCode errorCode) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = ValidationException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidation(ValidationException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_ARGUMENT.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_ARGUMENT.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
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