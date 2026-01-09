package org.trung.tickethub.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException() {
        super(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
    }
}
