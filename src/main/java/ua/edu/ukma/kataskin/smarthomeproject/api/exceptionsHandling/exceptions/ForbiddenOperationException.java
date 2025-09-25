package ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}


