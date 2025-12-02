package ua.edu.ukma.kataskin.smarthomeproject.aop.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    int maxCallsPerMinute() default 30;
}