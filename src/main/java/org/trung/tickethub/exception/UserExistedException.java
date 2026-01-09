package org.trung.tickethub.exception;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);
    }

    public UserExistedException() {
        super("User already exists");
    }
}
