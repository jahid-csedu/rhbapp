package com.example.rhbapp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logRequestDetails(JoinPoint joinPoint) {
        log.info("Request: {} with arguments {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "controllerMethods()", returning = "result")
    public void logResponseDetails(JoinPoint joinPoint, Object result) {
        log.info("Response from method {}: {}", joinPoint.getSignature(), result);
    }

    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution time of method {}: {} ms", joinPoint.getSignature(), (endTime - startTime));
        return result;
    }
}
