package org.example.recapprojectspring;

public class InvalidDTOException extends RuntimeException {
    public InvalidDTOException(String message) {
        super(message);
    }
}
