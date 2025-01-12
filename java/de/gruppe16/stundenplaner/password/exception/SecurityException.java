package de.gruppe16.stundenplaner.password.exception;

public class SecurityException extends Exception {
    public SecurityException() {
    }
    public SecurityException(String message) {
        super(message);
    }
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
    public SecurityException(Throwable cause) {
        super(cause);
    }
}
