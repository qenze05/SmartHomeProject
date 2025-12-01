package ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.edu.ukma.kataskin.smarthomeproject.api.controllers.RoomController;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ForbiddenOperationException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.ResourceNotFoundException;
import ua.edu.ukma.kataskin.smarthomeproject.api.exceptionsHandling.exceptions.WeatherApiErrorException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<String> notFound(ResourceNotFoundException ex, HttpServletRequest r) {
        log.warn("404 {} {} – {}", r.getMethod(), r.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    ResponseEntity<String> forbidden(ForbiddenOperationException ex, HttpServletRequest r) {
        log.warn("403 {} {} – {}", r.getMethod(), r.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(WeatherApiErrorException.class)
    ResponseEntity<String> upstream(WeatherApiErrorException ex, HttpServletRequest r) {
        log.error("502 {} {} – {}", r.getMethod(), r.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> validation(MethodArgumentNotValidException ex, HttpServletRequest r) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("400 {} {} – {}", r.getMethod(), r.getRequestURI(), errors);
        return ResponseEntity.badRequest().body("Validation failed: " + errors);
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    ResponseEntity<String> noResource(Exception ex, HttpServletRequest r) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> fallback(Exception ex, HttpServletRequest r) {
        log.error("500 {} {} – {}", r.getMethod(), r.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
