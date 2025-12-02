package ua.edu.ukma.kataskin.smarthomeproject.aop.exception;


import ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.HandleExceptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class ExceptionHandlingAspect {

    private static final Logger log = LogManager.getLogger(ExceptionHandlingAspect.class);

    @Around("@annotation(ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.HandleExceptions)")
    public Object handleExceptions(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        HandleExceptions annotation = method.getAnnotation(HandleExceptions.class);

        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            log.error("Exception in {} with args {}: {}",
                    signature.toShortString(),
                    Arrays.toString(pjp.getArgs()),
                    ex.getMessage(),
                    ex
            );


            if (annotation == null || annotation.rethrow()) {
                throw ex;
            } else {
                return null;
            }
        }
    }
}
