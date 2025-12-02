package ua.edu.ukma.kataskin.smarthomeproject.aop.ratelimit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class UserRateLimitExceededException extends RuntimeException {
    public UserRateLimitExceededException(String message) {
        super(message);
    }
}
