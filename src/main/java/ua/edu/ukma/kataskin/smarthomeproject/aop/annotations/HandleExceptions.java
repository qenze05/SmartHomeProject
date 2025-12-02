package ua.edu.ukma.kataskin.smarthomeproject.aop.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleExceptions {
    boolean rethrow() default true;
}