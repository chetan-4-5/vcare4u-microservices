package com.vcare4u.labservice.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.vcare4u.labservice.controller..(..)) || execution( com.vcare4u.labservice.service..*(..))")
    public void applicationPackagePointcut() {}

    @Before("applicationPackagePointcut()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info("➡ Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("⬅ Exiting method: {} with result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logMethodException(JoinPoint joinPoint, Throwable e) {
        log.error("💥 Exception in method: {} with cause: {}", joinPoint.getSignature(), e.getMessage());
    }
}