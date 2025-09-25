package ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions;

public class WeatherApiErrorException extends RuntimeException {
    public WeatherApiErrorException(String message) {
        super(message);
    }
}