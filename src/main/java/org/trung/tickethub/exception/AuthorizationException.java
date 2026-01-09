package org.trung.tickethub.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException() {
        super(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
