package org.trung.tickethub.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
