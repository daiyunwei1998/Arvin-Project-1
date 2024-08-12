package com.stylish.stylish.aspect;

import com.stylish.stylish.annotation.RateLimited;
import com.stylish.stylish.exception.RateLimitExceededException;
import com.stylish.stylish.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Log4j2
@Aspect
@Component
public class GlobalRateLimitingAspect {

    private final RateLimitingService rateLimitingService;

    public GlobalRateLimitingAspect(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }
    // Pointcut expression to apply to all controller methods
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods() && @annotation(rateLimited)")
    public Object rateLimitAdvice(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();

        String key = "rate_limit:" + ipAddress;
        int limit = rateLimited.limit();
        long windowMillis = rateLimited.windowMillis();

        if (rateLimitingService.isAllowed(key, limit, windowMillis)) {
            rateLimitingService.addRequestTimestamp(key, System.currentTimeMillis(), windowMillis);
            return joinPoint.proceed();  // Proceed with the method execution
        } else {
            throw new RateLimitExceededException("Rate limit exceeded.");
        }
    }
}