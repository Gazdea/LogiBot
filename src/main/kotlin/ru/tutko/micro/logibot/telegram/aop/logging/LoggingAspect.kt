package ru.tutko.micro.logibot.telegram.aop.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Pointcut("within(ru.tutko.micro.logibot.telegram.service..*)")
    fun serviceLayer() {}

    @Before("serviceLayer()")
    fun logBefore(joinPoint: JoinPoint) {
        log.info("Вызов метода: ${joinPoint.signature} с аргументами: ${joinPoint.args.joinToString()}")
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        log.info("Метод ${joinPoint.signature} вернул: $result")
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    fun logAfterThrowing(joinPoint: JoinPoint, exception: Throwable) {
        log.error("Метод ${joinPoint.signature} выбросил исключение: ${exception.message}", exception)
    }
}