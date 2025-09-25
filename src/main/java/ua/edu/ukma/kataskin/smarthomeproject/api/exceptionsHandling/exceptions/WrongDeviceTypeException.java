package ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions;

public class WrongDeviceTypeException extends RuntimeException {
    public WrongDeviceTypeException(String message) {
        super(message);
    }
}