package org.trung.tickethub.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN.getReasonPhrase());
    }
}
