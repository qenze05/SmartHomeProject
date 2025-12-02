package ua.edu.ukma.kataskin.smarthomeproject.aop.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleExceptions {
    // чи перекидати помилку далі після логування
    boolean rethrow() default true;
}