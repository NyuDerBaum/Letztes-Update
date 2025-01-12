package de.gruppe16.stundenplaner.password.exception;

public class AlgorithmException extends RuntimeException {
    public AlgorithmException() {
    }
    public AlgorithmException(String message) {
        super(message);
    }
    public AlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
    public AlgorithmException(Throwable cause) {
        super(cause);
    }
}
