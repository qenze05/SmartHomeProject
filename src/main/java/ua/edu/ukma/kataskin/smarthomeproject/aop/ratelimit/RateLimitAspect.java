package ua.edu.ukma.kataskin.smarthomeproject.aop.ratelimit;

import ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.RateLimited;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LogManager.getLogger(RateLimitAspect.class);

    private static final long WINDOW_MS = 60_000L;

    private final ConcurrentMap<String, UserRateInfo> userStats = new ConcurrentHashMap<>();

    @Around("@annotation(ua.edu.ukma.kataskin.smarthomeproject.aop.annotations.RateLimited)")
    public Object rateLimit(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RateLimited annotation = method.getAnnotation(RateLimited.class);

        int maxCalls = annotation.maxCallsPerMinute();
        String userId = resolveUserId();

        // якщо юзера не можемо визначити, можна:
        // а) пропускати (return pjp.proceed())
        // б) вважати усі виклики одним "анонімним" юзером
        if (userId == null) {
            // варіант: не лімітуємо анонімів
            return pjp.proceed();
        }

        long now = System.currentTimeMillis();
        UserRateInfo info = userStats.computeIfAbsent(userId, id -> new UserRateInfo(now));

        synchronized (info) {
            if (now - info.windowStart >= WINDOW_MS) {
                // нове вікно
                info.windowStart = now;
                info.calls = 0;
            }

            if (info.calls >= maxCalls) {
                log.warn("Rate limit exceeded for user {} on method {}", userId, signature.toShortString());
                throw new UserRateLimitExceededException(
                        "Rate limit exceeded for user " + userId + " on method " + signature.toShortString()
                );
            }

            info.calls++;
        }

        return pjp.proceed();
    }

    private String resolveUserId() {
        // якщо в тебе вже є Spring Security:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName(); // username як userId
        }
        return null;
    }

    private static class UserRateInfo {
        long windowStart;
        int calls;

        UserRateInfo(long windowStart) {
            this.windowStart = windowStart;
            this.calls = 0;
        }
    }
}
