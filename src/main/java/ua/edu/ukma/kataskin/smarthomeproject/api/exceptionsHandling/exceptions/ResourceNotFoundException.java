package ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

