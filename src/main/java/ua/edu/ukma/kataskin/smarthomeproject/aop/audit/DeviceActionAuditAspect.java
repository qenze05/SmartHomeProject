package ua.edu.ukma.kataskin.smarthomeproject.aop.audit;

import ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.DeviceActionAudit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class DeviceActionAuditAspect {

    private static final Logger log = LogManager.getLogger(DeviceActionAuditAspect.class);

    @AfterReturning(
            pointcut = "@annotation(ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.DeviceActionAudit)",
            returning = "result"
    )
    public void auditDeviceAction(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DeviceActionAudit annotation = method.getAnnotation(DeviceActionAudit.class);

        String userId = resolveUserId();
        String action = annotation.action();
        String args = Arrays.toString(joinPoint.getArgs());

        log.info("DeviceActionAudit: user={}, action={}, method={}, args={}, result={}",
                userId,
                action,
                signature.toShortString(),
                args,
                result
        );

        // тут можна додатково записувати в окрему audit-таблицю в БД
    }

    private String resolveUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "anonymous";
    }
}
